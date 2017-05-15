package com.liwy.music.controllers;

import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.github.mzule.activityrouter.annotation.Router;
import com.liwy.easylibrary.adapter.FragmentAdapter;
import com.liwy.easylibrary.base.BaseSlideActivity;
import com.liwy.easylibrary.common.ToastUtils;
import com.liwy.music.R;
import com.liwy.music.R2;
import com.liwy.music.controllers.local.LocalMusicFragment;
import com.liwy.music.controllers.online.OnlineMusicFragment;
import com.liwy.music.model.Music;
import com.liwy.music.service.OnPlayerEventListener;
import com.liwy.music.utils.CoverLoader;

import butterknife.BindView;

import static com.liwy.music.service.AppCache.getPlayService;


/**
 * Created by liwy on 2017/5/15.
 */

@Router("music")
public class MusicActivity extends BaseSlideActivity<MusicPresenter> implements MusicView,OnPlayerEventListener,View.OnClickListener {
    @BindView(R2.id.viewpager)
    ViewPager viewPager;

    @BindView(R2.id.fl_play_bar)
    FrameLayout flPlayBar;

    @BindView(R2.id.iv_play_bar_cover)
    ImageView ivPlayBarCover;

    @BindView(R2.id.tv_play_bar_title)
    TextView tvPlayBarTitle;

    @BindView(R2.id.tv_play_bar_artist)
    TextView tvPlayBarArtist;

    @BindView(R2.id.iv_play_bar_play)
    ImageView ivPlayBarPlay;

    @BindView(R2.id.iv_play_bar_next)
    ImageView ivPlayBarNext;

    @BindView(R2.id.pb_play_bar)
    ProgressBar mProgressBar;

    OnlineMusicFragment onlineMusicFragment;
    LocalMusicFragment localMusicFragment;
    @Override
    public void initView() {
        initToolbarWithBack(TOOLBAR_MODE_CENTER, "听雨楼", R.drawable.ic_menu, new OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                manageLayout.openDrawer(Gravity.LEFT);
            }
        });
        initSlideMenu();
        initViewPager();
        flPlayBar.setOnClickListener(this);
        ivPlayBarPlay.setOnClickListener(this);
        ivPlayBarNext.setOnClickListener(this);
        //开启音乐播放事件监听，更新页面
        getPlayService().setOnPlayEventListener(this);
        onChange(getPlayService().getPlayingMusic());
    }



    /**
     * 初始化ViewPager
     */
    public void initViewPager(){
        onlineMusicFragment = new OnlineMusicFragment();
        localMusicFragment = new LocalMusicFragment();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(onlineMusicFragment);
        fragmentAdapter.addFragment(localMusicFragment);
        viewPager.setAdapter(fragmentAdapter);
    }


    @Override
    protected void initPresenter() {
        mPresenter = new MusicPresenter();
        mPresenter.init(this,this,this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.music_activity_music;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.fl_play_bar:
                ToastUtils.show("播放页面正在开发");
                break;
            case R2.id.iv_play_bar_play:
                mPresenter.play();
                break;
            case R2.id.iv_play_bar_next:
                mPresenter.next();
                break;
        }
    }

    /**
     * 更新播放进度
     */
    @Override
    public void onPublish(int progress) {
        mProgressBar.setProgress(progress);
//        if (mPlayFragment != null && mPlayFragment.isInitialized()) {
//            mPlayFragment.onPublish(progress);
//        }
    }

    @Override
    public void onChange(Music music) {
        onPlay(music);
//        if (mPlayFragment != null && mPlayFragment.isInitialized()) {
//            mPlayFragment.onChange(music);
//        }
    }

    @Override
    public void onPlayerPause() {
        ivPlayBarPlay.setSelected(false);
//        if (mPlayFragment != null && mPlayFragment.isInitialized()) {
//            mPlayFragment.onPlayerPause();
//        }
    }

    @Override
    public void onPlayerResume() {
        ivPlayBarPlay.setSelected(true);
//        if (mPlayFragment != null && mPlayFragment.isInitialized()) {
//            mPlayFragment.onPlayerResume();
//        }
    }

    @Override
    public void onTimer(long remain) {
//        if (timerItem == null) {
//            timerItem = navigationView.getMenu().findItem(R.id.action_timer);
//        }
//        String title = getString(R.string.menu_timer);
//        timerItem.setTitle(remain == 0 ? title : SystemUtils.formatTime(title + "(mm:ss)", remain));
    }

    public void onPlay(Music music) {
        if (music == null) {
            return;
        }

        Bitmap cover = CoverLoader.getInstance().loadThumbnail(music);
        ivPlayBarCover.setImageBitmap(cover);
        tvPlayBarTitle.setText(music.getTitle());
        tvPlayBarArtist.setText(music.getArtist());
        if (getPlayService().isPlaying() || getPlayService().isPreparing()) {
            ivPlayBarPlay.setSelected(true);
        } else {
            ivPlayBarPlay.setSelected(false);
        }
        mProgressBar.setMax((int) music.getDuration());
        mProgressBar.setProgress(0);

//        if (mLocalMusicFragment != null && mLocalMusicFragment.isInitialized()) {
//            mLocalMusicFragment.onItemPlay();
//        }
    }


}
