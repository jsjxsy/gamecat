package com.youximao.sdk.lib.common.common.network;

import android.content.Context;

import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/2/27.
 */

public class UpdateApi {
    public static void update(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();

//        String updateUrl = "http://192.168.18.112/web/update/bundle.json";
//
//        OkHttpUtils.get()
//                .url(updateUrl)
//                .build()
//                .execute(new StringCallback() {
//                             @Override
//                             public void onError(Call call, Exception e, int i) {
//                                 e.printStackTrace();
//                                 listener.onFail("网络有误");
//                             }
//
//                             @Override
//                             public void onResponse(String response, int i) {
//                                 if (response != null) {
//                                     try {
//
//                                         Log.e("xsy", "response:" + response);
//                                         listener.onSuccess(new JSONObject(response));
//                                     } catch (Exception e) {
//                                         listener.onFail("数据解析出错");
//                                         e.printStackTrace();
//                                     }
//                                 } else {
//                                     listener.onFail("数据解析出错");
//                                 }
//                             }
//                         }
//
//                );
        String updateUrl = UpdateUrl.getUpdateUrl();
        NetworkRequestsUtil.getInstance().startAsynchronousRequests(updateUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },null);
    }
}
