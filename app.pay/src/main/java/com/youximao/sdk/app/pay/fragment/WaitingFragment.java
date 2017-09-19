package com.youximao.sdk.app.pay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.pay.model.Order;
import com.youximao.sdk.app.pay.service.GameCatPayApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.CircleProgressDialogFragment;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.util.MD5Util;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * 请求代金劵
 * Created by davy on 16/6/15.
 */
public class WaitingFragment extends CircleProgressDialogFragment {

    private static final String ORDER_AMOUNT = "orderAmount";
    private static final String ORDER_PRODUCT_TYPE = "productType";
    private double mAmount;
    private String mGoodsType;
    private String description;
    private int mCashCouponNum;
    private static final String ORDER_PLATFORM = "11";

    public static WaitingFragment getInstance(String waitingText, double amount, String goodsType) {
        WaitingFragment fragment = new WaitingFragment();
        Bundle args = new Bundle();
        args.putString(WAITING_MESSAGE, waitingText);
        args.putDouble(ORDER_AMOUNT, amount);
        args.putString(ORDER_PRODUCT_TYPE, goodsType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
        if (null != getActivity() && getActivity().getIntent() != null) {
            description = getActivity().getIntent().getStringExtra("description");
        }
        Bundle args = getArguments();
        if (args != null) {
            mAmount = args.getDouble(ORDER_AMOUNT);
            mGoodsType = args.getString(ORDER_PRODUCT_TYPE);
        }
        Collect.getInstance().custom(CustomId.id_300001);
        queryAvailableCoupon();
    }

    //查询可用代金券
    public void queryAvailableCoupon() {
        GameCatPayApi.queryAvailableCoupon(String.valueOf(mAmount), mGoodsType, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
//                        openConfirmOrderAction(content);
                        analyCashCoupon(content);
                        confirmOrder(content);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        CallBackUtil.onFail();
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "数据解析出错", false);
                        CallBackUtil.onFail();
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFail(String message) {
                CallBackUtil.onFail();
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "请求失败,请重试", false);
                    getActivity().finish();
                }
            }
        }, mContext);
    }

    private void analyCashCoupon(String content){
        try {
            JSONObject message = new JSONObject(content);
            String size = message.getString("totalSize");
            if (size.equals("0")) {
                mCashCouponNum = 0;
            } else {
                JSONArray list = message.getJSONArray("list");
                mCashCouponNum = list.length();
            }

        } catch (JSONException e) {
            e.printStackTrace();
            if (isAvailableActivity()) {
                ToastUtil.makeText(getActivity(), "数据解析出错", false);
                getActivity().finish();
            }
        }
    }

    public void confirmOrder(final String cashCouponList) {
        try {
            Map<String, String> params = initParams();
            GameCatPayApi.createOrder(params, new GameCatSDKListener() {
                @Override
                public void onSuccess(JSONObject message) {
                    try {
                        String code = message.getString("code");
                        if (TextUtils.equals(code, "000")) {
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            Order order = JSON.parseObject(content, Order.class);
                            openPayWayAction(order,cashCouponList);
                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            getActivity().finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(String message) {
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "网络错误", false);
                        getActivity().finish();
                    }
                }
            }, mContext);
        } catch (Exception e) {
            e.printStackTrace();
            if (isAvailableActivity()) {
                ToastUtil.makeText(getActivity(), "数据解析出错", false);
                getActivity().finish();
            }
        }
    }


    private void openPayWayAction(final Order order, final String cashCouponList) {
        if (order == null) {
            return;
        }
        GameCatPayApi.queryPayWay(order.getPayAmountRMB(), ORDER_PLATFORM, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        String data = message.getString("data");
                        String payWayList = AESUtil.decryptAES(data, Config.getAESKey());
                        openPayAction(order, payWayList,cashCouponList);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "数据解析出错", false);
                        getActivity().finish();
                    }
                } catch (CryptoException e) {
                    e.printStackTrace();
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "数据解析出错", false);
                        getActivity().finish();
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络错误", false);
                    getActivity().finish();
                }
            }
        },mContext);

    }

    private void openPayAction(Order order, String payWayList,String cashCouponList) {
        double payAmount = 0;
        try {
            payAmount = Double.parseDouble(order.getPayAmountRMB());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfirmPayFragment fragment = ConfirmPayFragment.getInstance(payAmount, description, order, payWayList, null,mCashCouponNum,cashCouponList);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), WaitingFragment.this, false);
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment);
    }

    private Map<String, String> initParams() {
        Intent intent = getActivity().getIntent();
        String codeNo = "";
        if (intent.hasExtra("codeNo")) {
            codeNo = intent.getStringExtra("codeNo");
        }
        String notifyUrl = "";
        if (intent.hasExtra("notifyUrl")) {
            try {
                notifyUrl = URLDecoder.decode(intent.getStringExtra("notifyUrl"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        String extend = "";
        if (intent.hasExtra("extend")) {
            extend = intent.getStringExtra("extend");
        }

        Map<String, String> params = new HashMap<>();
        try {
            params.put("codeNo", codeNo);
            params.put("gameId", Config.getGameId());
            params.put("cpDiscript", description);
            params.put("amount", String.valueOf(mAmount));
            params.put("notifyUrl", notifyUrl);
            params.put("extend", extend);
            params.put("sdkVersion", Config.getVersion());
            params.put("openId", Config.getOpenId());
            params.put("cid", Config.getChannelId());
            //多传代金券id
            params.put("cc_no", ""+mCashCouponNum);
            String sign = MD5Util.MD5(Config.getChannelId() + Config.getGameId() + codeNo + Config.getOpenId() + mAmount + notifyUrl + mCashCouponNum);
            params.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
            if (isAvailableActivity()) {
                ToastUtil.makeText(getActivity(), "数据解析出错", false);
                getActivity().finish();
            }
        }
        return params;
    }
}
