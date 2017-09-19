package com.youximao.sdk.app.personal.service;

import android.content.Context;

import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by davy on 16/6/18.
 */
public class GameCatPayApi {

    /**
     * 请求代金券
     */
    public static void queryAvailableCoupon(String price, String goodsType, final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("price", price);
        paramsMap.put("goodsType", goodsType);
        Map<String, String> headersMap = new HashMap<>();

        String queryAvailableCouponUrl = GameCatPayUrl.initEvnUrl() + GameCatPayUrl.QUERY_AVAILABLE_COUPON_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(queryAvailableCouponUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
//                        checkIsLoginOut(message,context);
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }

    /**
     * 创建订单
     */
    public static void createOrder(Map<String, String> params, final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.putAll(params);
        Map<String, String> headersMap = new HashMap<>();

        String createOrderUrl = GameCatPayUrl.initSDKPayUrl() + GameCatPayUrl.CREATE_ORDER_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(createOrderUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
//                        checkIsLoginOut(message,context);
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);

    }

    /**
     * 请求支付方式
     *
     * @Param platform(7 sdk-安卓购买道具 8-sdk-ios喵点充值 9-sdk-安卓喵点充值 10-sdk-ios购买游戏道具)
     */
    public static void queryPayWay(String price, String platform, final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("platform", platform);
        paramsMap.put("price", price);
        Map<String, String> headersMap = new HashMap<>();

        String queryPayWayUrl = GameCatPayUrl.initEvnUrl() + GameCatPayUrl.QUERY_PAY_WAY_URL;
        NetworkRequestsUtil.getInstance().startAsynchronousRequests(queryPayWayUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
//                        checkIsLoginOut(message,context);
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {
                        listener.onFail(message);
                    }
                },context);
    }

    /**
     * 发起支付
     */
    public static void sdkPay(String interfaceId, String orderId, final GameCatSDKListener listener, final Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("interfaceId", interfaceId);
        paramsMap.put("orderId", orderId);
        Map<String, String> headersMap = new HashMap<>();

        String sdkPayUrl = GameCatPayUrl.initEvnUrl() + GameCatPayUrl.SDK_PAY_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(sdkPayUrl, headersMap, paramsMap,
                new GameCatSDKListener() {
                    @Override
                    public void onSuccess(JSONObject message) {
//                        checkIsLoginOut(message,context);
                        listener.onSuccess(message);
                    }

                    @Override
                    public void onFail(String message) {

                        listener.onFail(message);
                    }
                },context);

    }

    /**
     * 开关查询接口
     *
     * @param
     */
    public static void switchList(final GameCatSDKListener listener,Context context) {
        Map<String, String> paramsMap = new HashMap<>();
        Map<String, String> headersMap = new HashMap<>();
        String swtch = GameCatPayUrl.initSDKPayUrl() + GameCatPayUrl.SWITCH_LIST_URL;
        NetworkRequestsUtil.getInstance().startNetworkRequests(swtch, headersMap, paramsMap,
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

    public static void checkIsLoginOut(JSONObject json,Context context) {
        try {
            String code = json.getString("code");
            String message = json.getString("message");
            if (code.equals("002") || code.equals("009")) {
                CancelUtil.cancelLogin(context);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
