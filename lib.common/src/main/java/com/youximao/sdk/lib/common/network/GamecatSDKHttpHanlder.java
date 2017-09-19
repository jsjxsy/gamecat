package com.youximao.sdk.lib.common.network;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;
import com.alibaba.sdk.android.man.network.MANNetworkErrorCodeBuilder;
import com.alibaba.sdk.android.man.network.MANNetworkErrorInfo;
import com.alibaba.sdk.android.man.network.MANNetworkPerformanceHitBuilder;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * Created by root on 17-2-28.
 */
public class GamecatSDKHttpHanlder extends StringCallback{
    private GameCatSDKListener listener;
    private Context mContext;
    private MANNetworkPerformanceHitBuilder networkPerformanceHitBuilder;
    // 获取MAN服务
    private MANService manService;
    private String url;

    public GamecatSDKHttpHanlder(GameCatSDKListener listener, Context context, String url, String requestMethod) {
        this.listener = listener;
        this.mContext = context;
        this.url = url;
        this.networkPerformanceHitBuilder = new MANNetworkPerformanceHitBuilder(url, requestMethod);
        this.manService = MANServiceProvider.getService();

        /* 打点记录网络请求开始 */
        networkPerformanceHitBuilder.hitRequestStart();
        // 打点记录建连时间
        networkPerformanceHitBuilder.hitConnectFinished();
        Log.e("xsy", "startNetworkRequests");
        Log.e("xsy", "url " + url);

        // 打点记录首包时间
        networkPerformanceHitBuilder.hitRecievedFirstByte();
    }

    @Override
    public void onError(Call call, Exception e, int i) {
        MANNetworkErrorInfo errorInfo = MANNetworkErrorCodeBuilder.buildIOException()
                .withExtraInfo("error_url", url);
        // 打点，记录出错情况
        networkPerformanceHitBuilder.hitRequestEndWithError(errorInfo);
        // 上报网络性能事件打点数据
        manService.getMANAnalytics().getDefaultTracker().send(networkPerformanceHitBuilder.build());
        e.printStackTrace();
        if (null != listener){
            listener.onFail("网络有误");
        }
    }

    @Override
    public void onResponse(String response, int i) {
        try {
            Log.e("xsy","response:"+response);
            networkPerformanceHitBuilder.hitRequestEndWithLoadBytes(response.getBytes().length);
            JSONObject jsonObject = new JSONObject(response);
            if ("002".equals(jsonObject.get("code"))){
                CancelUtil.cancelLogin(mContext);
            }
            if (null != listener){
                listener.onSuccess(new JSONObject(response));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFail("数据解析出错");
        }
        // 上报网络性能事件打点数据
        manService.getMANAnalytics().getDefaultTracker().send(networkPerformanceHitBuilder.build());
    }
}
