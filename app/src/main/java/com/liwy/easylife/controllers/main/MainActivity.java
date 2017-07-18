package com.liwy.easylife.controllers.main;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.liwy.easylibrary.base.BaseActivity;
import com.liwy.easylife.R;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainView {
    public static boolean isForeground = false;

    @BindView(R.id.btn_send)
    Button sendBtn;

    @Override
    public void initView() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFlagAutoCancelNotification();
            }
        });
    }

    @Override
    protected void initPresenter() {
        mPresenter = new MainPresenter();
        mPresenter.init(this,this);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    /**
     * 设置FLAG_AUTO_CANCEL
     * 该 flag 表示用户单击通知后自动消失
     */
    private void sendFlagAutoCancelNotification() {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        //设置一个Intent,不然点击通知不会自动消失
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Send Notification Use FLAG_AUTO_CLEAR")
                .setContentText("Hi,My id is 1,i can be clear.")
                .setContentIntent(resultPendingIntent);
        Notification notification = builder.build();
        //设置 Notification 的 flags = FLAG_NO_CLEAR
        //FLAG_AUTO_CANCEL 表示该通知能被状态栏的清除按钮给清除掉
        //等价于 builder.setAutoCancel(true);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(1, notification);
    }

}
