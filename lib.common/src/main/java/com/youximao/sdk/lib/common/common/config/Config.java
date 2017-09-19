package com.youximao.sdk.lib.common.common.config;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.youximao.sdk.lib.common.alianalytics.GameCatAnalytics;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.network.DomainApi;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;


/**
 * Created by davy on 16/6/14.
 */
public class Config {

    public final static String VERSION = "2.0.0";

    public static void setEnvironment(Activity activity, String gameId, int environment, String aesKey, boolean isLandscape) {
        SDKManager.init(activity);
        initGameId(gameId);
        initEnvironment(environment);
        initAppKey(activity);
        initChlId(activity);
        initAESKey(aesKey);
        initIsLanscape(isLandscape);
        DomainApi.initDomainList(activity);
        GameCatAnalytics.getInstance().init(activity);
    }

    private static void initIsLanscape(boolean isLandscape) {
        AppCacheSharedPreferences.putCacheBoolean(SharePreferenceConstant.GAME_CAT_LANDSCAPE, isLandscape);
    }

    public static boolean getIsLandscape() {
        return AppCacheSharedPreferences.getCacheBoolean(SharePreferenceConstant.GAME_CAT_LANDSCAPE, false);
    }

    private static void initEnvironment(int environment) {
        AppCacheSharedPreferences.putCacheInteger(SharePreferenceConstant.GAME_CAT_ENVIRONMENT, environment);
    }

    public static int getEnvironment() {
        return AppCacheSharedPreferences.getCacheInteger(SharePreferenceConstant.GAME_CAT_ENVIRONMENT, 1);
    }

    public static String getOpenId() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID);
    }


    private static void initGameId(String gameId) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_GAME_ID, gameId);
    }

    public static String getGameId() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_GAME_ID);
    }

    public static String getUserId() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_USER_ID);
    }

    public static String getPhone() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT);
    }

    public static String getVersion() {
        String version = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_VERSION);
        if (TextUtils.isEmpty(version)) {
            return VERSION;
        }
        return version;
    }

    public static void setVersion(String version) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_VERSION, version);
    }

    private static void initAppKey(Activity activity) {
        if (activity != null) {
            String appKey = getStringMetaData(activity, "GameCatAppKey");
            AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_APP_KEY, appKey);
        }
    }


    public static String getAppKey() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_APP_KEY);
    }

    private static void initChlId(Activity activity) {
        if (activity != null) {
            String chlId = getStringMetaData(activity, "GameCatChlId");
            AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_CHANNEL_ID, chlId);
        }

    }

    public static String getChannelId() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_CHANNEL_ID);
    }

    private static void initAESKey(String AESKey) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_AES_KEY, AESKey);

    }

    public static String getAESKey() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_AES_KEY);
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_TOKEN));
    }


    public static String getGameBindingChannelId() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_BINDING_CHANNEL_ID);
    }

    public static String getToken() {
        return AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_TOKEN);
    }

    private static String getStringMetaData(Context context, String key) {
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

}
