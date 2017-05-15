package com.liwy.music.controllers.online;


import com.liwy.easylibrary.base.view.IView;
import com.liwy.music.adapter.MusicAdapter;

public interface OnlineMusicView extends IView {
    public void finishRefresh();
    public void finishLoad();
    public void setAdapter(MusicAdapter adapter);

}
