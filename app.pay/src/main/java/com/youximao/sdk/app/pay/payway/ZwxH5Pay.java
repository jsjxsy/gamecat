package com.youximao.sdk.app.pay.payway;

import android.content.Context;

import com.zwxpay.android.h5_library.manager.WebViewManager;

/**
 * Created by davy on 16/9/19.
 */
public class ZwxH5Pay {
    public static void toPay(Context activity, String prepay_url) {
        new WebViewManager(activity, true).showWeiXinView(prepay_url);
    }
}
