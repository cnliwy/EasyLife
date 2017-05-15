package com.liwy.music.service;

import android.app.Activity;

import com.liwy.music.model.Music;
import com.liwy.music.utils.Preferences;


/**
 * 播放本地音乐
 */
public abstract class PlayMusic implements IExecutor<Music> {
    private Activity mActivity;
    protected Music music;
    private int mTotalStep;
    protected int mCounter = 0;

    public PlayMusic(Activity activity, int totalStep) {
        mActivity = activity;
        mTotalStep = totalStep;
    }

    @Override
    public void execute() {
        checkNetwork();
    }

    private void checkNetwork() {
        boolean mobileNetworkPlay = Preferences.enableMobileNetworkPlay();
//        if (NetworkUtils.isActiveNetworkMobile(mActivity) && !mobileNetworkPlay) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//            builder.setTitle(R.string.tips);
//            builder.setMessage(R.string.play_tips);
//            builder.setPositiveButton(R.string.play_tips_sure, new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Preferences.saveMobileNetworkPlay(true);
//                    getPlayInfoWrapper();
//                }
//            });
//            builder.setNegativeButton(R.string.cancel, null);
//            Dialog dialog = builder.create();
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
//        } else {
            getPlayInfoWrapper();
//        }
    }

    private void getPlayInfoWrapper() {
        onPrepare();
        getPlayInfo();
    }

    protected abstract void getPlayInfo();

    protected void checkCounter() {
        mCounter++;
        if (mCounter == mTotalStep) {
            onExecuteSuccess(music);
        }
    }
}
