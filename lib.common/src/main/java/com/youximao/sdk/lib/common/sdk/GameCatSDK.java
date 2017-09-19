package com.youximao.sdk.lib.common.sdk;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;

import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

/**
 * Created by davy on 16/6/14.
 */
public class GameCatSDK {

    /**
     * 设置环境
     *
     * @param environment
     */
    @Deprecated
    public static void setEnvironment(Activity activity, String gameId, int environment, String aesKey) {
        setEnvironment(activity, gameId, environment, aesKey, true);
    }

    public static void setEnvironment(Activity activity, String gameId, int environment, String aesKey, boolean isLandscape) {
        //去掉空格
        if (!TextUtils.isEmpty(aesKey)) {
            aesKey = aesKey.trim();
        }
        if (!TextUtils.isEmpty(gameId)) {
            gameId = gameId.trim();
        }
        Config.setEnvironment(activity, gameId, environment, aesKey, isLandscape);
    }

    /**
     * 登录
     *
     * @param listener 登录监听器，用作登录成功或者失败的回调
     */
    public static void Login(Activity activity, boolean switchAccount, final GameCatSDKListener listener) {
        SDKControlCenter.getControlCenter().goToLogin(activity, switchAccount, listener);
    }


    /**
     * 注销登录接口
     */
    public static void Logout(Context context) {
        CancelUtil.cancelLogin(context);
    }

    /**
     * 调用订单方法
     *
     * @param amount      订单金额
     * @param description 产品介绍
     * @param codeNo      订单编号
     * @param notifyUrl   发货回调地址
     * @param extend      扩展参数
     * @param listener    订单回调
     */
    public static void Order(Activity activity, double amount, String description, String codeNo,
                             String notifyUrl, String extend, final GameCatSDKListener listener) {
        SDKManager.setContext(activity);
        SDKControlCenter.getControlCenter().goToOrder(activity, amount, description, codeNo, notifyUrl, extend, listener);
    }


    public static void Order(Activity activity, double amount, String description, String codeNo,
                             String notifyUrl, String extend) {
        SDKManager.setContext(activity);
        SDKControlCenter.getControlCenter().goToOrder(activity, amount, description, codeNo, notifyUrl, extend, null);
    }


    /**
     * 闪屏
     */
    public static void splashPage(Activity activity) {
        SDKManager.setContext(activity);
        SDKControlCenter.getControlCenter().goToSplashPage(activity);
    }

}
