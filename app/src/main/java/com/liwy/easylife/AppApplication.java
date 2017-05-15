package com.liwy.easylife;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.mzule.activityrouter.annotation.Modules;
import com.github.mzule.activityrouter.router.RouterCallback;
import com.github.mzule.activityrouter.router.RouterCallbackProvider;
import com.github.mzule.activityrouter.router.SimpleRouterCallback;
import com.liwy.easylibrary.base.EasyApplication;

/**
 * Created by liwy on 2017/5/12.
 */
@Modules({"app","happy","music"})
public class AppApplication extends EasyApplication implements RouterCallbackProvider {
    // 跳转回调，可自定义路径错误、跳转前后拦截等事件
    @Override
    public RouterCallback provideRouterCallback() {
        return new SimpleRouterCallback(){
            @Override
            public void notFound(Context context, Uri uri) {
                context.startActivity(new Intent(context, NotFoundActivity.class));
            }

            @Override
            public void error(Context context, Uri uri, Throwable e) {
                super.error(context, uri, e);
            }
        };
    }
}
