package com.youximao.sdk.app.pay.activity;

import android.app.Application;

import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by root on 17-2-13.
 */
public class PayApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCacheSharedPreferences.init(this);
    }
}
