package com.youximao.sdk.lib.common.common.callback;

//import com.gamecat.pay.service.daoImpl.PayControlCenter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davy on 16/8/8.
 */
public class CallBackUtil {

    //支付成功回调出去
    public static void onSuccess() {
        JSONObject callBack = new JSONObject();
        try {
            callBack.put("codeNo", "");
            callBack.put("message", "支付成功");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (PayControlCenter.GameCatSDKLister() != null) {
//            PayControlCenter.GameCatSDKLister().onSuccess(callBack);
//        }
    }

    //支付失败回调出去
    public static void onFail() {
        JSONObject callBack = new JSONObject();
        try {
            callBack.put("codeNo", "");
            callBack.put("message", "支付失败");
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        if (PayControlCenter.GameCatSDKLister() != null) {
//            PayControlCenter.GameCatSDKLister().onFail(callBack.toString());
//        }

    }
}
