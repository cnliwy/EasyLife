package com.liwy.music.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.util.Log;


import com.liwy.easylibrary.common.ToastUtils;
import com.liwy.music.R;
import com.liwy.music.constants.Actions;
import com.liwy.music.enums.PlayModeEnum;
import com.liwy.music.model.Music;
import com.liwy.music.model.OnlineMusic;
import com.liwy.music.receiver.NoisyAudioStreamReceiver;
import com.liwy.music.utils.MusicUtils;
import com.liwy.music.utils.Preferences;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import static com.liwy.music.service.AppCache.getPlayService;


/**
 * 音乐播放后台服务
 */
public class PlayService extends Service implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {
    private static final String TAG = "Service";
    private static final long TIME_UPDATE = 100L;
    private List<Music> mMusicList;
    private List<OnlineMusic> mOnlineMusicList;
    private MediaPlayer mPlayer = new MediaPlayer();
    private IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private Handler mHandler = new Handler();
    private AudioManager mAudioManager;
    private OnPlayerEventListener mListener;
    // 网络播放模式（在线播放还是本地播放）false 本地播放  true 网络播放
    private boolean playNetStyle = false;
    // 正在播放的歌曲[本地|网络]
    private Music mPlayingMusic;
    // 正在播放的本地歌曲的序号
    private int mPlayingPosition;
    private boolean isPausing;
    private boolean isPreparing;
    private long quitTimerRemain;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        mMusicList = AppCache.getMusicList();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mPlayer.setOnCompletionListener(this);
        Notifier.init(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public static void startCommand(Context context, String action) {
        Intent intent = new Intent(context, PlayService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case Actions.ACTION_MEDIA_PLAY_PAUSE:
                    playPause();
                    break;
                case Actions.ACTION_MEDIA_NEXT:
                    next();
                    break;
                case Actions.ACTION_MEDIA_PREVIOUS:
                    prev();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * 每次启动时扫描音乐
     */
    public void updateMusicList() {
        MusicUtils.scanMusic(this, mMusicList);
        if (!mMusicList.isEmpty()) {
            updatePlayingPosition();
            mPlayingMusic = (mPlayingMusic == null) ? mMusicList.get(mPlayingPosition) : mPlayingMusic;
        }
    }

    /**
     * 更新在线缓存列表
     * @param list
     */
    public void updateOnlineMusicList(List<OnlineMusic> list){
        this.mOnlineMusicList = list;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        next();
    }

    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        mListener = listener;
    }

    public void play(int position) {
        if (mMusicList.isEmpty()) {
            return;
        }

        if (position < 0) {
            position = mMusicList.size() - 1;
        } else if (position >= mMusicList.size()) {
            position = 0;
        }

        mPlayingPosition = position;
        Music music = mMusicList.get(mPlayingPosition);
        Preferences.saveCurrentSongId(music.getId());
        play(music);
    }

    public void play(Music music) {
        mPlayingMusic = music;
        try {
            mPlayer.reset();
            mPlayer.setDataSource(music.getPath());
            mPlayer.prepareAsync();
            isPreparing = true;
            mPlayer.setOnPreparedListener(mPreparedListener);
            if (mListener != null) {
                mListener.onChange(music);
            }
            Notifier.showPlay(music);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 播放在线音乐
     * @param mPlayingPosition
     */
    public void playOnlieMusic(int mPlayingPosition) {
        this.mPlayingPosition = mPlayingPosition;
        OnlineMusic onlineMusic = mOnlineMusicList.get(mPlayingPosition);
        playOnlineMusic(onlineMusic);
    }

    /**
     * 播放在线音乐
     * @param onlineMusic
     */
    public void playOnlineMusic(OnlineMusic onlineMusic){
        new PlayOnlineMusic(null, onlineMusic) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onExecuteSuccess(Music music) {
                getPlayService().play(music);
            }

            @Override
            public void onExecuteFail(Exception e) {
//                    mProgressDialog.cancel();
                ToastUtils.show(R.string.music_unable_to_play);
            }
        }.execute();
    }

    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            isPreparing = false;
            start();
        }
    };

    public void playPause() {
        if (isPreparing()) {
            return;
        }

        if (isPlaying()) {
            pause();
        } else if (isPausing()) {
            resume();
        } else {
            play(getPlayingPosition());
        }
    }

    private void start() {
        mPlayer.start();
        isPausing = false;
        mHandler.post(mBackgroundRunnable);
        Notifier.showPlay(mPlayingMusic);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        registerReceiver(mNoisyReceiver, mNoisyFilter);
    }

    private void pause() {
        if (!isPlaying()) {
            return;
        }

        mPlayer.pause();
        isPausing = true;
        mHandler.removeCallbacks(mBackgroundRunnable);
        Notifier.showPause(mPlayingMusic);
        mAudioManager.abandonAudioFocus(this);
        unregisterReceiver(mNoisyReceiver);
        if (mListener != null) {
            mListener.onPlayerPause();
        }
    }

    private void resume() {
        if (!isPausing()) {
            return;
        }

        start();
        if (mListener != null) {
            mListener.onPlayerResume();
        }
    }

    /**
     * 下一首
     */
    public void next() {
        if (playNetStyle){
            netNext();
        }else {
            localNext();
        }

    }

    /**
     * 本地列表的自动播放
     */
    private void localNext(){
        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition + 1);
                break;
        }
    }

    /**
     * 网络播放的自动播放
     */
    private void netNext(){
        if (mOnlineMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mOnlineMusicList.size());
                playOnlieMusic(mPlayingPosition);
                break;
            case SINGLE:
                playOnlieMusic(mPlayingPosition);
                break;
            case LOOP:
            default:
                playOnlieMusic(mPlayingPosition + 1);
                break;
        }
    }

    public void prev() {
        if (mMusicList.isEmpty()) {
            return;
        }

        PlayModeEnum mode = PlayModeEnum.valueOf(Preferences.getPlayMode());
        switch (mode) {
            case SHUFFLE:
                mPlayingPosition = new Random().nextInt(mMusicList.size());
                play(mPlayingPosition);
                break;
            case SINGLE:
                play(mPlayingPosition);
                break;
            case LOOP:
            default:
                play(mPlayingPosition - 1);
                break;
        }
    }

    /**
     * 跳转到指定的时间位置
     *
     * @param msec 时间
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPausing()) {
            mPlayer.seekTo(msec);
            if (mListener != null) {
                mListener.onPublish(msec);
            }
        }
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public boolean isPausing() {
        return mPlayer != null && isPausing;
    }

    public boolean isPreparing() {
        return mPlayer != null && isPreparing;
    }

    /**
     * 获取正在播放的本地歌曲的序号
     */
    public int getPlayingPosition() {
        return mPlayingPosition;
    }

    /**
     * 获取正在播放的歌曲[本地|网络]
     */
    public Music getPlayingMusic() {
        return mPlayingMusic;
    }

    /**
     * 删除或下载歌曲后刷新正在播放的本地歌曲的序号
     */
    public void updatePlayingPosition() {
        int position = 0;
        long id = Preferences.getCurrentSongId();
        for (int i = 0; i < mMusicList.size(); i++) {
            if (mMusicList.get(i).getId() == id) {
                position = i;
                break;
            }
        }
        mPlayingPosition = position;
        Preferences.saveCurrentSongId(mMusicList.get(mPlayingPosition).getId());
    }

    private Runnable mBackgroundRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null) {
                mListener.onPublish(mPlayer.getCurrentPosition());
            }
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_LOSS:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                pause();
                break;
        }
    }

    public void startQuitTimer(long milli) {
        stopQuitTimer();
        if (milli > 0) {
            quitTimerRemain = milli + DateUtils.SECOND_IN_MILLIS;
            mHandler.post(mQuitRunnable);
        } else {
            quitTimerRemain = 0;
            if (mListener != null) {
                mListener.onTimer(quitTimerRemain);
            }
        }
    }

    private void stopQuitTimer() {
        mHandler.removeCallbacks(mQuitRunnable);
    }

    private Runnable mQuitRunnable = new Runnable() {
        @Override
        public void run() {
            quitTimerRemain -= DateUtils.SECOND_IN_MILLIS;
            if (quitTimerRemain > 0) {
                if (mListener != null) {
                    mListener.onTimer(quitTimerRemain);
                }
                mHandler.postDelayed(this, DateUtils.SECOND_IN_MILLIS);
            } else {
                AppCache.clearStack();
                stop();
            }
        }
    };

    @Override
    public void onDestroy() {
        AppCache.setPlayService(null);
        super.onDestroy();
        Log.i(TAG, "onDestroy: " + getClass().getSimpleName());
    }

    public void stop() {
        pause();
        stopQuitTimer();
        mPlayer.reset();
        mPlayer.release();
        mPlayer = null;
        Notifier.cancelAll();
        AppCache.setPlayService(null);
        stopSelf();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }

    public boolean isPlayNetStyle() {
        return playNetStyle;
    }

    public void setPlayNetStyle(boolean playNetStyle) {
        this.playNetStyle = playNetStyle;
    }

    public List<OnlineMusic> getOnlineMusicList() {
        return mOnlineMusicList;
    }

    public void setOnlineMusicList(List<OnlineMusic> mOnlineMusicList) {
        this.mOnlineMusicList = mOnlineMusicList;
    }
}
