package com.liwy.music.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.liwy.easylibrary.common.utils.ImgUtils;
import com.liwy.easylibrary.views.easyrecycler.EasyHolder;
import com.liwy.easylibrary.views.easyrecycler.EasyRecyclerAdapter;
import com.liwy.music.R;
import com.liwy.music.model.OnlineMusic;

import java.util.List;



/**
 * Created by liwy on 2017/3/28.
 */

public class MusicAdapter extends EasyRecyclerAdapter<OnlineMusic> {

    public MusicAdapter(Context context) {
        super(context);
    }

    public MusicAdapter(Context context, List<OnlineMusic> list) {
        super(context, list);
    }

    @Override
    public void convert(EasyHolder holder, final OnlineMusic item) {
        TextView nameTv = (TextView) holder.getView(R.id.tv_music_name);
        TextView artTv = (TextView) holder.getView(R.id.tv_music_art);
        ImageView imageIv = (ImageView)holder.getView(R.id.iv_music);

        ImgUtils.getInstance().display(mContext,item.getPic_small(),imageIv);
        if(!TextUtils.isEmpty(item.getTitle())) {
            nameTv.setText(item.getTitle());
        }
        if(item.getAlbum_title()!=null && !"".equals(item.getAlbum_title())) {
            artTv.setText(item.getArtist_name() + "-" + item.getAlbum_title());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.music_item_music;
    }

}
