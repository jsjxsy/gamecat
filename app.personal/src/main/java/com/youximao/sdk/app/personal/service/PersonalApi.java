package com.youximao.sdk.app.personal.service;

import android.content.Context;

import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yulinsheng on 16-11-9.
 */
public class PersonalApi {

    /**
     * 喵点充值－初始化数据
     *
     * @param
     */
    public static void catPointRechargeInit(final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        String catPointRechargeInit = PersonalUrl.initWechat() + PersonalUrl.WECHAT_POINT_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(catPointRechargeInit, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }

    /**
     * 喵点充值－创建订单
     *
     * @param
     */
    public static void createGoodsOrder(String goodsId, String point, String price, final GameCatSDKListener listener, Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goodsId", goodsId);
        paramsMap.put("point", point);
        paramsMap.put("price", price);

        Map<String, String> headersMap = new HashMap<>();
        String createGoodsOrder = PersonalUrl.initWechat() + PersonalUrl.WECHAT_CREATE_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(createGoodsOrder, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }
}
