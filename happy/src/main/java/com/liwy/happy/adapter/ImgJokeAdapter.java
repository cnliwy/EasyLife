package com.liwy.happy.adapter;

import android.content.Context;
import android.widget.ImageView;


import com.liwy.easylibrary.views.easyrecycler.EasyHolder;
import com.liwy.easylibrary.views.easyrecycler.EasyRecyclerAdapter;
import com.liwy.happy.R;
import com.liwy.happy.entity.Joke;

import java.util.List;



/**
 * Created by liwy on 2017/4/20.
 */

public class ImgJokeAdapter extends EasyRecyclerAdapter<Joke> {

    public ImgJokeAdapter(Context context) {
        super(context);
    }

    public ImgJokeAdapter(Context context, List<Joke> list) {
        super(context, list);
    }

    @Override
    public void convert(EasyHolder holder, Joke item) {
        ImageView imageView = (ImageView)holder.getView(R.id.img_joke);

        String time = item.getTime();
        time = time.substring(0,time.length()-4);
        holder.setText(R.id.tv_time,time);
        holder.setText(R.id.tv_title,item.getTitle());
//        ImgUtils.getInstance().display(mContext,item.getImg(),imageView);

    }

    @Override
    public int getLayoutId() {
        return R.layout.mhappy_item_imgjoke;
    }

}
