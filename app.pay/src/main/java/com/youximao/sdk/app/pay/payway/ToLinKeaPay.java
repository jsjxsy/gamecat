package com.youximao.sdk.app.pay.payway;

import android.content.Context;
import android.util.Log;

import com.linkea.pay.LinkeaPay;
import com.linkea.pay.LinkeaPayInterface;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davy on 16/7/29.
 */
public class ToLinKeaPay {

    String orderNo;
    String detail;
    String callback;
    String title;
    String price;

    public void actionAliPay(final Context context, String jsonString, final GameCatSDKListener listener) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            orderNo = jsonObject.getString("orderNo");
            detail = jsonObject.getString("detail");
            callback = jsonObject.getString("callback");
            title = jsonObject.getString("title");
            price = jsonObject.getString("price");

            LinkeaPay.getInstance(context).linkeaSDKPay(context, orderNo, price, LinkeaPayInterface.PayType.ALIPAY, title, price, callback, new LinkeaPayInterface() {

                @Override
                public void onWaitting() {
                    Log.i("Tag", "支付结果确认中");
                }

                @Override
                public void onSuccess() {
                    CallBackUtil.onSuccess();
                    Collect.getInstance().custom(CustomId.id_332000);
                    Log.i("Tag", "支付成功");
                }

                @Override
                public void onFailure(String msg) {
                    CallBackUtil.onFail();
                    Collect.getInstance().custom(CustomId.id_333000);
                    Log.i("Tag", "支付失败：" + msg);
                }

                @Override
                public void onError(String errorMsg) {
                    Log.i("Tag", errorMsg);
                }

                @Override
                public void onCreatOrder() {

                    Log.i("Tag", "订单创建");
                }

                @Override
                public void onCreatOrderSuccess() {
                    JSONObject successJson = new JSONObject();
                    try {
                        successJson.put("message", "success");
                        listener.onSuccess(successJson);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    Log.i("Tag", "订单创建成功");

                }

                @Override
                public void onCreatOrderFailure(String msg) {
                    listener.onFail(msg);
                    Log.i("Tag", "订单创建失败:" + msg);

                }

                @Override
                public void onQueryPayInfo() {
                    Log.i("Tag", "交易结果查询");
                }
            });


        } catch (JSONException e) {
            listener.onFail("支付初始化失败请重试");
            e.printStackTrace();
        }

    }


    public void actionWeChatPay(final Context context, String jsonString, final GameCatSDKListener listener) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            orderNo = jsonObject.getString("orderNo");
            detail = jsonObject.getString("detail");
            callback = jsonObject.getString("callback");
            title = jsonObject.getString("title");
            price = jsonObject.getString("price");

            LinkeaPay.getInstance(context).linkeaSDKPay(context, orderNo, price, LinkeaPayInterface.PayType.WECHAT_PAY, title, price, callback, new LinkeaPayInterface() {

                @Override
                public void onWaitting() {
                    Log.i("Tag", "支付结果确认中");
                }

                @Override
                public void onSuccess() {
                    CallBackUtil.onSuccess();
                    Log.i("Tag", "支付成功");
                }

                @Override
                public void onFailure(String msg) {
                    CallBackUtil.onFail();
                    Log.i("Tag", "支付失败：" + msg);
                }

                @Override
                public void onError(String errorMsg) {
                    Log.i("Tag", errorMsg);
                }

                @Override
                public void onCreatOrder() {

                    Log.i("Tag", "订单创建");
                }

                @Override
                public void onCreatOrderSuccess() {
                    JSONObject successJson = new JSONObject();
                    try {
                        successJson.put("message", "success");
                        listener.onSuccess(successJson);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                    Log.i("Tag", "订单创建成功");

                }

                @Override
                public void onCreatOrderFailure(String msg) {
                    listener.onFail(msg);
                    Log.i("Tag", "订单创建失败:" + msg);

                }

                @Override
                public void onQueryPayInfo() {
                    Log.i("Tag", "交易结果查询");
                }
            });


        } catch (JSONException e) {
            listener.onFail("支付初始化失败请重试");
            e.printStackTrace();
        }

    }
}
