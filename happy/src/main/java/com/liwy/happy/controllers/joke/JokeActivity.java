package com.liwy.happy.controllers.joke;

import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;


import com.github.mzule.activityrouter.annotation.Router;
import com.liwy.easylibrary.adapter.FragmentAdapter;
import com.liwy.easylibrary.base.BaseActivity;
import com.liwy.easylibrary.base.BaseSlideActivity;
import com.liwy.easylibrary.views.tabindicator.EasyIndicator;
import com.liwy.easylibrary.views.tabindicator.TabBean;
import com.liwy.easylibrary.views.tabindicator.TabConfig;
import com.liwy.happy.R;
import com.liwy.happy.R2;
import com.liwy.happy.controllers.joke.gifjoke.GifJokeFragment;
import com.liwy.happy.controllers.joke.imgjoke.ImgJokeFragment;
import com.liwy.happy.controllers.joke.textjoke.TextJokeFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


@Router("joke")
public class JokeActivity extends BaseSlideActivity<JokePresenter> implements JokeView {

    @BindView(R2.id.mhappy_viewpager)
    ViewPager viewPager;

    @BindView(R2.id.mhappy_indictor)
    EasyIndicator indicator;

    private TextJokeFragment textJokeFragment;
    private ImgJokeFragment imgJokeFragment;
    private GifJokeFragment gifJokeFragment;

    @Override
    public void initView() {
        initToolbarWithBack(TOOLBAR_MODE_CENTER, "开心一刻", 0, new OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                manageLayout.openDrawer(Gravity.LEFT);
            }
        });
        initSlideMenu();
        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    public void initViewPager(){
        textJokeFragment = new TextJokeFragment();
        imgJokeFragment = new ImgJokeFragment();
        gifJokeFragment = new GifJokeFragment();
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(textJokeFragment);
        fragmentAdapter.addFragment(imgJokeFragment);
        fragmentAdapter.addFragment(gifJokeFragment);
        viewPager.setAdapter(fragmentAdapter);
        initIndictor();
    }

    /**
     * 初始化导航栏
     */
    public void initIndictor(){
        TabConfig config = new TabConfig.Builder()
                .setDistance(5)                            // 设置文字和图片的距离
                .setTextColorNor(R.color.text_gray_6)    // 设置默认的文字颜色
                .setTextColorSel(R.color.colorAccent)    // 设置选中后的文字颜色
                .setTextSize(18)                           // 设置文字的大小
                .setBgColorNor(R.color.white)             // 设置默认的背景色
//              .setBgColorSel(R.color.bg_sel)             // 设置选中后的背景色
                .setImgWidth(30)                           // 设置图片宽度
                .setImgHeight(30)                          // 设置图片高度
                .setLineColor(R.color.colorAccent)       // 设置下划线的颜色
                .setShowLine(true)                        // 设置是否显示下划线
                .builder();
        indicator.setConfig(config);
        List<TabBean> list = new ArrayList<TabBean>() ;
        list.add(new TabBean("笑话"));
        list.add(new TabBean("趣图"));
        list.add(new TabBean("动图"));
        indicator.setTabAndViewPager(list,viewPager);
    }

    // init presenter
    @Override
    protected void initPresenter() {
        mPresenter = new JokePresenter();
        mPresenter.init(this,this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.mhappy_activity_jokes;
    }


}
