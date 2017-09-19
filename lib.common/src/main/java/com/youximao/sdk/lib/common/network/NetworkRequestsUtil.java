package com.youximao.sdk.lib.common.network;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.config.DefaultParams;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.DeviceNetworkUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class NetworkRequestsUtil implements INetworkRequests {
    private static final String TAG = "post";
    private static final long CONNECT_TIME_OUT = 100 * 1000L;
    private static final long READ_TIME_OUT = 100 * 1000L;
    public static OkHttpClient okHttpClient;
    private static NetworkRequestsUtil networkRequestsUtil;

    public static NetworkRequestsUtil getInstance() {
        if (networkRequestsUtil == null) {

            networkRequestsUtil = new NetworkRequestsUtil();
            HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
                    .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                    .build();
            OkHttpUtils.initClient(okHttpClient);
        }
        return networkRequestsUtil;
    }


    public void defaultHeaderParams(Map<String, String> headerMap) {
        if (headerMap != null) {
            if (!headerMap.isEmpty()) {
                headerMap.putAll(headerMap);
            }
            headerMap.put("token", Config.getToken());
            headerMap.put("channelId", Config.getChannelId());
            headerMap.put("devicesId", DeviceNetworkUtil.getDeviceId(SDKManager.getContext()));
            headerMap.put("terminalName", Build.MANUFACTURER);
            headerMap.put("terminalType", "2");
            headerMap.put("version", Config.getVersion());
            Log.e("xsy", "headerMap" + headerMap);
        }

    }

    public void defaultBodyParams(Map<String, String> paramsMap) {
        if (paramsMap != null) {
            try {
                com.alibaba.fastjson.JSONObject dataJson = DefaultParams.getInstance(SDKManager.getContext()).getDataParams();
                String json = dataJson.toJSONString();
                Map<String, String> map = JSON.parseObject(json, Map.class);
                paramsMap.putAll(map);
                String data = JSON.toJSONString(paramsMap);
                Log.e("xsy", "paramsMap data : " + data);
                paramsMap.clear();
                String AESKey = Config.getAESKey();
                if (!TextUtils.isEmpty(AESKey)) {
                    paramsMap.put("data", AESUtil.encryptAES(data, Config.getAESKey()));
                }
            } catch (CryptoException e) {
                e.printStackTrace();
            }
            paramsMap.put("devicStatue", DefaultParams.getInstance(SDKManager.getContext()).getDeviceStatusParams().toJSONString());
            paramsMap.put("appKey", Config.getAppKey());
            paramsMap.put("requestId", String.valueOf(System.currentTimeMillis()));
            Log.e("xsy", "paramsMap data : " + paramsMap.toString());

        }
    }

    public void defaultParams(Map<String, String> headerMap, Map<String, String> paramsMap) {
        defaultHeaderParams(headerMap);
        defaultBodyParams(paramsMap);
    }


    @Override
    public void stopNetworkRequests() {
        OkHttpUtils.getInstance().cancelTag(TAG);
        Log.e("xsy", "stopNetworkRequests");
    }

    @Override
    public void startNetworkRequests(final String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context) {
        defaultParams(headersMap, paramsMap);
        OkHttpUtils.post().url(url).params(paramsMap).headers(headersMap).tag(TAG).build().execute(new GamecatSDKHttpHanlder(listener, context, url, "POST"));
    }

    /**
     * get 请求
     *
     * @param url
     */
    public void startNetworkRequests(final String url, final GameCatSDKListener listener, Context context) {

        Log.e("xsy", "startNetworkRequests");
        Log.e("xsy", "url " + url);

        OkHttpUtils.get().url(url).tag("get").build().execute(new GamecatSDKHttpHanlder(listener, context, url, "GET"));
    }


    @Override
    public void startAsynchronousRequests(final String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context) {
        defaultParams(headersMap, paramsMap);
        Log.e("xsy", "startAsynchronousRequests: url " + url);
        OkHttpUtils.post()
                .url(url)
                .params(paramsMap)
                .headers(headersMap)
                .build()
                .execute(new GamecatSDKHttpHanlder(listener, context, url, "POST"));
    }

    @Override
    public void startAsynchronousRequestNotEncryptAES(final String url, final Map<String, String> headersMap, final Map<String, String> paramsMap, final GameCatSDKListener listener, Context context) {
        Log.e("xsy", "startAsynchronousRequests: url " + url);
        OkHttpUtils.post()
                .url(url)
                .params(paramsMap)
                .headers(headersMap)
                .build()
                .execute(new GamecatSDKHttpHanlder(listener, context, url, "POST"));
    }
}
