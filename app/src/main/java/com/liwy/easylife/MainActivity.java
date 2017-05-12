package com.liwy.easylife;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mzule.activityrouter.router.Routers;
import com.liwy.easylibrary.base.EasyActivity;
import com.liwy.easylibrary.common.ToastUtils;

import butterknife.BindView;

public class MainActivity extends EasyActivity {

    @BindView(R.id.btn_joke)
    Button jokeBtn;

    @BindView(R.id.btn_weibo)
    Button weiboBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initToolbarWithBack(TOOLBAR_MODE_CENTER, "主页", 0, new OnLeftClickListener() {
            @Override
            public void onLeftClick() {
                finish();

            }
        });
        jokeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Routers.open(MainActivity.this, Uri.parse("easy://joke"));
            }
        });
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("正在开发");
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
