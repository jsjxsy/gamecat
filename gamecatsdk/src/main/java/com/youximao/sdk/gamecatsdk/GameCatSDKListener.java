package com.youximao.sdk.gamecatsdk;

import org.json.JSONObject;

/**
 * SDK监听接口
 */
public interface GameCatSDKListener {

    /**
     * 成功回调
     **/
    void onSuccess(JSONObject message);

    /**
     * 失败回调
     **/
    void onFail(String message);

}
