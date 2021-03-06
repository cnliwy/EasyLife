package com.liwy.happy.adapter;

import android.content.Context;


import com.liwy.easylibrary.views.easyrecycler.EasyHolder;
import com.liwy.easylibrary.views.easyrecycler.EasyRecyclerAdapter;
import com.liwy.happy.R;
import com.liwy.happy.entity.Joke;

import java.util.List;



/**
 * Created by liwy on 2017/4/20.
 */

public class TextJokeAdapter extends EasyRecyclerAdapter<Joke> {

    public TextJokeAdapter(Context context) {
        super(context);
    }

    public TextJokeAdapter(Context context, List<Joke> list) {
        super(context, list);
    }

    @Override
    public void convert(EasyHolder holder, Joke item) {
        String time = item.getTime();
        time = time.substring(0,time.length()-4);
        holder.setText(R.id.tv_time,time);
        holder.setText(R.id.tv_content,item.getContent());
    }

    @Override
    public int getLayoutId() {
        return R.layout.mhappy_item_textjoke;
    }

    public static int getMyOrder(char value){
        return ((int)value)%5;
    }
}
