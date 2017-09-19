package com.youximao.sdk.app.peckgifts.activity;

import android.app.Application;

import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by root on 17-2-10.
 */
public class PackageApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppCacheSharedPreferences.init(this);
    }
}
