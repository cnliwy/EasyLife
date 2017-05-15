package com.liwy.easylibrary.base;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.mzule.activityrouter.router.Routers;
import com.liwy.easylibrary.R;
import com.liwy.easylibrary.adapter.SlideItemAdapter;
import com.liwy.easylibrary.base.presenter.BasePresenter;
import com.liwy.easylibrary.base.view.IView;
import com.liwy.easylibrary.entity.SlideItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * 抽象基类
 * Created by liwy on 2016/11/16.
 */

public abstract class BaseSlideActivity<T extends BasePresenter> extends EasyActivity implements IView {
    public T mPresenter;



    /**
     * 需要子类来实现，获取子类的IPresenter，一个activity有可能有多个IPresenter
     */
    protected  BasePresenter getPresenter(){
        return mPresenter;
    }

    /**
     * 初始化presenters
     */
    protected abstract void  initPresenter();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化presenter
        initPresenter();
        mPresenter.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    /**=========================侧滑菜单功能=======================**/
    // 左侧滑动菜单控件
    @Nullable
//    @BindView(R2.id.id_lv_left_menu)
    public ListView mLvLeftMenu;

    @Nullable
//    @BindView(R2.id.fd)
    public DrawerLayout manageLayout;

    public SlideItemAdapter adapter;

    // 侧滑菜单数据源
    public List<SlideItem> mItems = new ArrayList<SlideItem>(
            Arrays.asList(
                    new SlideItem(R.drawable.ic_slide_music, "听雨楼"),
                    new SlideItem(R.drawable.ic_slide_happy, "欢乐谷"),
                    new SlideItem(R.drawable.ic_slide_happy, "微博"),
                    new SlideItem(R.drawable.ic_slide_time, "定时关闭音乐"),
                    new SlideItem(R.drawable.ic_slide_exit, "退出")

            ));

    // 初始化侧滑菜单列表
    public void initSlideMenu() {
        mLvLeftMenu = (ListView)findViewById(R.id.id_lv_left_menu);
        manageLayout = (DrawerLayout)findViewById(R.id.fd);
        mLvLeftMenu.addHeaderView(initSlideMenuTop());
        adapter = new SlideItemAdapter(this,mItems);
        mLvLeftMenu.setAdapter(adapter);
        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                manageLayout.closeDrawers();
                switch (position) {
                    case 1:
                        Routers.open(mContext,Uri.parse("easy://music"));
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);// 如果本活动已经存在则不重新加载
//                        intent.setClass(mContext, MusicActivity.class);
                        break;
                    case 2:
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        intent.setClass(mContext, JokeActivity.class);
                        Routers.open(mContext,Uri.parse("easy://joke"));
                        break;
                    case 3:
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        intent.setClass(mContext, WeiboActivity.class);
                        Routers.open(mContext,Uri.parse("easy://weibo"));
                        break;
                }
            }
        });
    }

    /**
     * 初始化滑动菜单的顶部view
     * @return
     */
    private View initSlideMenuTop(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.nav_header_main, mLvLeftMenu, false);
        return view;
    }


}
