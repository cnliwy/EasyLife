package com.liwy.easylife.controllers.splash;


import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.liwy.easylibrary.base.presenter.BasePresenter;
import com.liwy.easylibrary.common.ToastUtils;
import com.liwy.music.controllers.MusicActivity;
import com.liwy.music.service.AppCache;
import com.liwy.music.service.PlayService;
import com.yanzhenjie.permission.AndPermission;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by liwy on 2017/3/21.
 */

public class SplashPresenter extends BasePresenter<SplashView> {
    private ServiceConnection mPlayServiceConnection;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCache.init(mContext);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermisssion();
    }


    public void toMain(){
       Observable.timer(2000, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Long>() {
           @Override
           public void call(Long aLong) {
               mView.turnToActivityWithFinish(MusicActivity.class);
           }
       });
   }

    private void checkPermisssion(){
        if (!AndPermission.hasPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            AndPermission.with(mActivity).permission(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).requestCode(100).send();
        }else{
            checkService();
        }
    }

    /**
     * 检查服务是否开启
     */
    public void checkService() {
        if (AppCache.getPlayService() == null) {
            bindService();
        }else {
            toMain();
        }
    }


    public void bindService(){
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Intent intent = new Intent();
                intent.setClass(mContext, PlayService.class);
                mPlayServiceConnection = new PlayServiceConnection();
                boolean flag = mContext.bindService(intent, mPlayServiceConnection, Context.BIND_AUTO_CREATE);
                if (flag){
                    subscriber.onNext("1");
                }else{
                    subscriber.onNext("0");
                }

            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if ("0".equals(s)){
                    ToastUtils.show("无法启动");
                }else{
                    toMain();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        if (mPlayServiceConnection != null) {
            mContext.unbindService(mPlayServiceConnection);
        }
        super.onDestroy();
    }

    private class PlayServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            final PlayService playService = ((PlayService.PlayBinder) service).getService();
            AppCache.setPlayService(playService);
            playService.updateMusicList();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }
}
