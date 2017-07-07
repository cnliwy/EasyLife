package com.liwy.easylibrary.base.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by liwy on 2016/11/16.
 */

public interface IPresenter {
    public void onStart();
    public void onResume();
    public void onPause();
    public void onStop();
    public void onDestroy();
    public void onCreate(@Nullable Bundle savedInstanceState);
}
