package com.youximao.sdk.lib.common.sdk;

import android.app.Activity;
import android.content.Intent;

import com.youximao.sdk.lib.common.common.network.AppConfig;

import net.wequick.small.Small;

import org.json.JSONObject;


/**
 * Created by davy on 16/6/14.
 */
public class SDKControlCenter implements ISDKControlCenter {
    private static ISDKControlCenter mInstance;
    GameCatSDKListener actionListener;


    private SDKControlCenter() {
    }

    public static ISDKControlCenter getControlCenter() {
        if (null == mInstance) {
            mInstance = new SDKControlCenter();
        }
        return mInstance;
    }


    @Override
    public void goToLogin(final Activity activity, final boolean switchAccount, final GameCatSDKListener listener) {
        if (AppConfig.getAppConfig(activity).isLocalMobileSId()) {
            openLoginAction(activity, switchAccount, listener);
        } else {
            AppConfig.getAppConfig(activity).getMobileSid(new GameCatSDKListener() {

                @Override
                public void onSuccess(JSONObject message) {
                    openLoginAction(activity, switchAccount, listener);
                }

                @Override
                public void onFail(String message) {
                    listener.onFail(message);
                }
            },activity);

        }

    }

    private void openLoginAction(Activity activity, boolean switchAccount, GameCatSDKListener listener) {
        setActionSDKListener(listener);
        Small.openUri("usercenter?switchAccount=" + switchAccount, activity);
    }

    @Override
    public void goToOrder(Activity activity, double amount, String description, String codeNo,
                          String notifyUrl, String extend, final GameCatSDKListener listener) {
        Intent intent = Small.getIntentOfUri("pay", activity);
        intent.putExtra("amount", amount);
        intent.putExtra("description", description);
        intent.putExtra("codeNo", codeNo);
        intent.putExtra("notifyUrl", notifyUrl);
        intent.putExtra("extend", extend);

        activity.startActivity(intent);

    }


    @Override
    public void goToSplashPage(Activity activity) {
        Small.openUri("splash", activity);
    }


    @Override
    public GameCatSDKListener getActionSDKListener() {
        return actionListener;
    }

    @Override
    public void setActionSDKListener(GameCatSDKListener listener) {
        actionListener = listener;
    }


}
