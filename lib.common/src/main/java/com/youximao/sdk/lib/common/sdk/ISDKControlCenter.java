package com.youximao.sdk.lib.common.sdk;

import android.app.Activity;

/**
 * Created by davy on 16/6/14.
 */
public interface ISDKControlCenter {
    void goToLogin(Activity activity, boolean switchAccount, final GameCatSDKListener listener);

    void goToOrder(Activity activity, double amount, String description, String codeNo,
                   String notifyUrl, String extend, final GameCatSDKListener listener);

    void goToSplashPage(Activity activity);


    GameCatSDKListener getActionSDKListener();

    void setActionSDKListener(final GameCatSDKListener listener);

}
