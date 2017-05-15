package com.liwy.music.controllers.local;


import com.liwy.easylibrary.base.BaseFragment;
import com.liwy.music.R;


public class LocalMusicFragment extends BaseFragment<LocalMusicPresenter> implements LocalMusicView {

    @Override
    public void initView() {

    }

    // init presenter
    @Override
    protected void initPresenter() {
        mPresenter = new LocalMusicPresenter();
        mPresenter.init(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.music_fragment_local_music;

    }
}
