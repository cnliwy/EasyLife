package com.liwy.easylife;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mzule.activityrouter.router.Routers;
import com.liwy.easylibrary.base.EasyActivity;

import butterknife.BindView;


public class MainActivity extends EasyActivity {

    @BindView(R.id.btn_joke)
    Button jokeBtn;

    @BindView(R.id.btn_weibo)
    Button weiboBtn;

    @BindView(R.id.btn_music)
    Button musicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbarWithBack(TOOLBAR_MODE_CENTER, "主页", 0, new OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();

            }
        });

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routers.open(mContext,Uri.parse("easy://music"));
            }
        });
        jokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routers.open(mContext, Uri.parse("easy://joke"));
            }
        });
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routers.open(mContext, Uri.parse("easy://weibo"));
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
