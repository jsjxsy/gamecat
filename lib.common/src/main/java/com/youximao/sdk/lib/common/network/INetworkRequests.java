package com.youximao.sdk.lib.common.network;

import android.content.Context;

import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import java.util.Map;

/**
 * Created by davy on 16/6/14.
 */
public interface INetworkRequests {

    void stopNetworkRequests();

    void startNetworkRequests(String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context);

    void startAsynchronousRequests(String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context);

    void startAsynchronousRequestNotEncryptAES(String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context);

}
