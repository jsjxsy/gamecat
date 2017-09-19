/*
package com.youximao.sdk.app.pay.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.pay.R;
import com.youximao.sdk.app.pay.adapter.VoucherAdapter;
import com.youximao.sdk.app.pay.model.Order;
import com.youximao.sdk.app.pay.service.GameCatPayApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.util.MD5Util;
import com.youximao.sdk.lib.common.common.widget.DialogUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

*/
/**
 * Created by davy on 16/6/15.
 * 创建订单页面
 *//*

public class ConfirmOrderFragment extends BaseFragment implements View.OnClickListener {
    private static final String ORDER_AMOUNT = "orderAmount";
    private static final String ORDER_DESCRIPTION = "orderDescription";
    private static final String ORDER_COUPON_LIST = "couponList";
    private static final String ORDER_PLATFORM = "11";
    public String mCcno = "0";
    //关闭按钮
    ImageView mClose;
    //确定订单按钮
    Button mConfirmOrderButton;
    //代金券列表
    RelativeLayout mVouchersList;
    //产品介绍
    TextView mDescriptionTextView;
    //可用代金券张数
    //产品价格
    TextView mAmountTextView;
    TextView mVouchersSize;
    //右箭头
    ImageView mComeImage;
    ListView mListView;
    ImageView mCancel;
    TextView mSelectVouchers;
    TextView mHadNoMore;
    DialogUtil mDialogUtil;
    private ArrayList<String> mGroups = new ArrayList<>();
    private double mAmount;
    private String mDescription;
    private String mCouponList;

    public static ConfirmOrderFragment getInstance(double amount, String description, String Content) {
        ConfirmOrderFragment fragment = new ConfirmOrderFragment();
        Bundle args = new Bundle();
        args.putDouble(ORDER_AMOUNT, amount);
        args.putString(ORDER_DESCRIPTION, description);
        args.putSerializable(ORDER_COUPON_LIST, Content);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_pay_confirm_order;
    }

    @Override
    public void init(View view) {
        mDescriptionTextView = (TextView) view.findViewById(R.id.description);
        mAmountTextView = (TextView) view.findViewById(R.id.amount);

        mVouchersSize = (TextView) view.findViewById(R.id.vouchers_size);
        mComeImage = (ImageView) view.findViewById(R.id.come_image);

        mVouchersList = (RelativeLayout) view.findViewById(R.id.vouchers_list);
        mVouchersList.setOnClickListener(this);

        mConfirmOrderButton = (Button) view.findViewById(R.id.confirm_order_button);
        mConfirmOrderButton.setOnClickListener(this);
        mConfirmOrderButton.setEnabled(true);

        mClose = (ImageView) view.findViewById(R.id.close);
        mClose.setOnClickListener(this);
        initData();
    }


    private void initData() {
        Bundle args = getArguments();
        if (args != null) {
            mAmount = args.getDouble(ORDER_AMOUNT);
            mDescription = args.getString(ORDER_DESCRIPTION);
            mCouponList = args.getString(ORDER_COUPON_LIST);
        }

        mDescriptionTextView.setText(mDescription);
        //去掉科学计数法
        DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance();
        df.setGroupingUsed(false);
        mAmountTextView.setText("￥" + df.format(mAmount));
        mAmountTextView.getPaint().setFakeBoldText(true);//字体加粗
        mGroups.add("不使用代金券");

        try {
            JSONObject message = new JSONObject(mCouponList);
            String size = message.getString("totalSize");
            if (size.equals("0")) {
                mVouchersSize.setText("无可用代金券");
                mComeImage.setVisibility(View.INVISIBLE);
                mCcno = "0";
                mVouchersList.setClickable(false);
            } else {
                JSONArray list = message.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    mGroups.add(list.get(i).toString());
                }
                mVouchersList.setClickable(true);
                mVouchersSize.setText(size + "张可用");
            }

        } catch (JSONException e) {
            getActivity().finish();
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.vouchers_list) {
            openDialog();
        } else if (v.getId() == R.id.confirm_order_button) {
            mConfirmOrderButton.setEnabled(false);
            ConfirmOrder();
        } else if (v.getId() == R.id.close) {
            Collect.getInstance().custom(CustomId.id_312000);
            CallBackUtil.onFail();
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDialogUtil != null) {
            mDialogUtil.cancel();
        }
    }

    public void openDialog() {
        Collect.getInstance().custom(CustomId.id_313000);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.gamecat_pay_confirm_order_dialog, null);
        mDialogUtil = new DialogUtil(getActivity(), view);

        mDialogUtil.show();
        mListView = (ListView) view.findViewById(R.id.listview);
        mHadNoMore = (TextView) view.findViewById(R.id.had_no_more);
        mHadNoMore.setVisibility(View.INVISIBLE);
        mCancel = (ImageView) view.findViewById(R.id.img_cancel);
        mSelectVouchers = (TextView) view.findViewById(R.id.select_vouchers);
        final VoucherAdapter voucherAdapter = new VoucherAdapter(getActivity());
        mListView.setAdapter(voucherAdapter);
        mListView.setOnItemClickListener(voucherAdapter);
        voucherAdapter.setArrayList(mGroups);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setDivider(null);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collect.getInstance().custom(CustomId.id_314000);
                mVouchersSize.setText("不使用代金券");
                mDialogUtil.cancel();
            }
        });
        mSelectVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogUtil.cancel();
                Collect.getInstance().custom(CustomId.id_315000);
                int position = voucherAdapter.getSelectedItem();
                if (ListView.INVALID_POSITION != position) {
                    if (position == 0) {
                        mVouchersSize.setText("不使用代金券");
                        mCcno = "0";
                    } else {
                        try {
                            mVouchersSize.setText(new JSONObject(mGroups.get(position)).getString("cc_pname"));
                            mCcno = new JSONObject(mGroups.get(position)).getString("cc_no");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        // 监听listview滚到最底部
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    // 当不滚动时
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        // 判断滚动到底部
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            mHadNoMore.setVisibility(View.VISIBLE);
                        } else {
                            mHadNoMore.setVisibility(View.INVISIBLE);
                        }
                        break;
                    //开始滚动
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        mHadNoMore.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }
        });

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
                notifyUrl = URLDecoder.decode(intent.getStringExtra("notifyUrl"),"UTF-8");
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
            params.put("cpDiscript", mDescription);
            params.put("amount", String.valueOf(mAmount));
            params.put("notifyUrl", notifyUrl);
            params.put("extend", extend);
            params.put("sdkVersion", Config.getVersion());
            params.put("openId", Config.getOpenId());
            params.put("cid", Config.getChannelId());
            //多传代金券id
            params.put("cc_no", mCcno);
            String sign = MD5Util.MD5(Config.getChannelId() + Config.getGameId() + codeNo + Config.getOpenId() + mAmount + notifyUrl + mCcno);
            params.put("sign", sign);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }


    public void ConfirmOrder() {
        Collect.getInstance().custom(CustomId.id_311000);
        try {
            showWaitingFragment("订单创建中...");
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
                            openPayWayAction(order);
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
                        closeWaitingFragment();
                    }
                }
            },mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openPayWayAction(final Order order) {
        if (order == null) {
            return;
        }
        GameCatPayApi.queryPayWay(order.getPayAmountRMB(), ORDER_PLATFORM, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        closeWaitingFragment();
                        String data = message.getString("data");
                        String payWayList = AESUtil.decryptAES(data, Config.getAESKey());
                        openPayAction(order, payWayList);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().finish();
                } catch (CryptoException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络错误", false);
                    closeWaitingFragment();
                }
            }
        },mContext);

    }

    private void openPayAction(Order order, String payWayList) {
        double payAmount = 0;
        try {
            payAmount = Double.parseDouble(order.getPayAmountRMB());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ConfirmPayFragment fragment = ConfirmPayFragment.getInstance(payAmount, mDescription, order, payWayList, null,0,"");
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), ConfirmOrderFragment.this, false);
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment);
    }
}
*/
