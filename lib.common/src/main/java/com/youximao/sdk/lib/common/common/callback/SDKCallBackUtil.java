package com.youximao.sdk.lib.common.common.callback;


import com.youximao.sdk.lib.common.sdk.SDKControlCenter;

import org.json.JSONObject;

/**
 * Created by davy on 16/9/18.
 */
public class SDKCallBackUtil {

    //登录成功,回调出去
    public static void onSuccess(JSONObject data) {
        if (SDKControlCenter.getControlCenter().getActionSDKListener() != null) {
            SDKControlCenter.getControlCenter().getActionSDKListener().onSuccess(data);
        }
    }

    //登录失败，回调出去
    public static void onFail(String message) {
        if (SDKControlCenter.getControlCenter().getActionSDKListener() != null) {
            SDKControlCenter.getControlCenter().getActionSDKListener().onFail(message);
        }
    }

}
