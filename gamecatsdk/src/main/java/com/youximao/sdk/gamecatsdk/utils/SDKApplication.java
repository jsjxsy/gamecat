package com.youximao.sdk.gamecatsdk.utils;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.lzy.okgo.OkGo;
import com.lzy.utils.AppCacheSharedPreferences;
import com.youximao.sdk.gamecatsdk.CrashHandler;
import com.youximao.sdk.gamecatsdk.UpgradeManager;

import net.wequick.small.BuildConfig;
import net.wequick.small.Small;


/**
 * Created by admin on 2017/2/6.
 */

public class SDKApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Small.preSetUp(this);
        Small.setBaseUri("http://sdkstatic.dm.com/error.html");// 浏览器跳转url
        Small.setUp(this, null);
        OkGo.init(this);
        init();
        if (BuildConfig.DEBUG) {
            new UpgradeManager(this).checkUpgrade();
        }
    }

    private void init() {
        AppCacheSharedPreferences.init(this);
        initAppKey();
        initCrashHandler();
    }

    private void initAppKey() {
        String appKey = getStringMetaData(this, "GameCatAppKey");
        AppCacheSharedPreferences.putCacheString(AppCacheSharedPreferences.GAME_CAT_APP_KEY, appKey);
    }

    private String getStringMetaData(Context context, String key) {
        Object r = null;
        try {
            PackageManager manager = context.getPackageManager();
            ApplicationInfo info = manager.getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null) {
                r = info.metaData.get(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r == null ? "" : r.toString();
    }


    private void initCrashHandler() {
        int env = AppCacheSharedPreferences.getCacheInteger("Environment", 1);
        //当不为联调试环境的时候，记录crash日志
        if (env != 1 && env != 0) {
            CrashHandler.getInstance().init(this);
        }
    }


}
