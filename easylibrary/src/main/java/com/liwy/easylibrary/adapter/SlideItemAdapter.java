package com.liwy.easylibrary.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;


import com.liwy.easylibrary.R;
import com.liwy.easylibrary.entity.SlideItem;
import com.liwy.easylibrary.views.easyrecycler.EasyListAdapter;

import java.util.List;



/**
 * Created by liwy on 2017/4/11.
 */

public class SlideItemAdapter extends EasyListAdapter<SlideItem> {

    public SlideItemAdapter(Context context, List<SlideItem> mDatas) {
        super(context, mDatas);
    }

    @Override
    public ViewHolder initView(ViewHolder viewHolder, int position) {
        SlideItem item = mDatas.get(position);
        TextView itemView = (TextView) viewHolder.getView(R.id.tv_menu_title);
        itemView.setText(item.name);
        ImageView imageView = viewHolder.getView(R.id.iv_menu_icon);
        imageView.setImageResource(item.icon);
        return viewHolder;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_slide_menu;
    }
}
