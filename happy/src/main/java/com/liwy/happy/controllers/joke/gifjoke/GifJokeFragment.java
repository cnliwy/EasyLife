package com.liwy.happy.controllers.joke.gifjoke;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.liwy.easylibrary.base.BaseFragment;
import com.liwy.happy.R;
import com.liwy.happy.R2;
import com.liwy.happy.adapter.ImgJokeAdapter;
import com.liwy.happy.entity.Joke;

import butterknife.BindView;



public class GifJokeFragment extends BaseFragment<GifJokePresenter> implements GifJokeView {

//    @BindView(R2.id.rv_list)
//    public EasyRecyclerView listView;

//    @BindView(R2.id.id_swiperefreshlayout)
//    SwipeRefreshLayout swipeRefreshLayout;

//    @BindView(R2.id.empty_view)
//    public View emptyView;

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
//        listView.setLayoutManager(new LinearLayoutManager(mContext));
//        listView.setFooterResource(R.layout.item_footer);
//        listView.setEmptyView(emptyView);
//        listView.setLoadMoreEnable(true);
//        listView.setOnLoadMoreListener(new EasyRecyclerView.OnLoadMoreListener() {
//            @Override
//            public void loadMoreListener() {
//                mPresenter.loadMore();
//            }
//        });
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.initData();
//            }
//        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.next();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new GifJokePresenter();
        mPresenter.init(this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.mhappy_fragment_gif_joke;

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
