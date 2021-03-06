package com.liwy.happy.controllers.joke.imgjoke;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.liwy.easylibrary.base.BaseFragment;
import com.liwy.easylibrary.views.easyrecycler.EasyRecyclerView;
import com.liwy.happy.R;
import com.liwy.happy.R2;
import com.liwy.happy.adapter.ImgJokeAdapter;
import com.liwy.happy.entity.Joke;

import butterknife.BindView;


public class ImgJokeFragment extends BaseFragment<ImgJokePresenter> implements ImgJokeView {



    @BindView(R2.id.tv_time)
    TextView timeTv;

    @BindView(R2.id.tv_title)
    TextView titleTv;

    @BindView(R2.id.img_joke)
    ImageView imgIv;

    @BindView(R2.id.btn_next)
    Button nextBtn;

    @Override
    public void initView() {
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.next();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ImgJokePresenter();
        mPresenter.init(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.mhappy_fragment_img_joke;

    }

    @Override
    public void updateNext(Joke joke) {
        String time = joke.getTime();
        time = time.substring(0,time.length()-4);
        timeTv.setText(time);
        titleTv.setText(joke.getTitle());
        Glide.with(this).load(joke.getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .crossFade().centerCrop()
                .placeholder(R.drawable.ic_slide_time)
                .into(imgIv);
    }

}
