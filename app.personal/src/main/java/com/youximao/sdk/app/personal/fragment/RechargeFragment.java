package com.youximao.sdk.app.personal.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.adapter.RechargeAdapter;
import com.youximao.sdk.app.personal.model.CustomItem;
import com.youximao.sdk.app.personal.model.GoodsItem;
import com.youximao.sdk.app.personal.model.PointRecharge;
import com.youximao.sdk.app.personal.service.GameCatPayApi;
import com.youximao.sdk.app.personal.service.PersonalApi;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.model.MeowOrder;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.util.InputMethodUtil;
import com.youximao.sdk.lib.common.common.widget.GridViewInScrollView;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import net.wequick.small.Small;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by yulinsheng on 16-10-25.
 * 　充值页面
 */
public class RechargeFragment extends BaseFragment implements View.OnClickListener {
    private static final String ORDER_PLATFORM = "9";
    private static final String POINT_RECHARGE = "goods";
    ImageView mImageViewBack;
    ImageView mImageViewClose;
    GridView mGridViewPayWay;
    Button mButtonConfirmPay;
    RechargeAdapter mRechargeAdapter;
    ArrayList<GoodsItem> mRechargeList = new ArrayList<>();
    CustomItem mCustom;
    int mAmount;
    private String mPoint = "";
    private String mGoodsId = "";
    private String mPrice = "";

    public static RechargeFragment getInstance(PointRecharge goods) {
        RechargeFragment fragment = new RechargeFragment();
        Bundle args = new Bundle();
        args.putSerializable(POINT_RECHARGE, goods);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            PointRecharge goods = (PointRecharge) args.getSerializable(POINT_RECHARGE);
            if (goods != null) {
                mRechargeList = goods.getGoods();
                mCustom = goods.getCustom();
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_recharge_money;
    }

    @Override
    public void init(View view) {
        mImageViewBack = (ImageView) view.findViewById(R.id.iv_back);
        mImageViewBack.setOnClickListener(this);

        mImageViewClose = (ImageView) view.findViewById(R.id.iv_close);
        mImageViewClose.setOnClickListener(this);

        mButtonConfirmPay = (Button) view.findViewById(R.id.confirm_pay_button);
        mButtonConfirmPay.setOnClickListener(this);

        mGridViewPayWay = (GridViewInScrollView) view.findViewById(R.id.gv_pay_way);
        mRechargeAdapter = new RechargeAdapter(getActivity(), mButtonConfirmPay);
        mRechargeAdapter.setArrayList(mRechargeList);
        mGridViewPayWay.setAdapter(mRechargeAdapter);
        mButtonConfirmPay.setText("立即支付￥"+mRechargeList.get(0).getPrice());


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            FragmentManagerUtil.getInstance().closeDialogFragment(getActivity(), RechargeFragment.this);
        } else if (v.getId() == R.id.iv_close) {
            getActivity().finish();
        } else if (v.getId() == R.id.confirm_pay_button) {
            createGoodsOrder();
        }
    }

    private void createGoodsOrder() {
        mButtonConfirmPay.setEnabled(false);
        if (mRechargeAdapter.getPotion() == mRechargeList.size()) {
            mPoint = mRechargeAdapter.getMoney().getText().toString();
            mGoodsId = mCustom.getGoodsId();
            mPrice = mPoint;

            try {
                mAmount = Integer.valueOf(mPrice).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            if (mAmount > 5000000) {
                ToastUtil.makeText(getActivity(), "超过金额限制", false);
                mButtonConfirmPay.setEnabled(true);
                return;
            }
        } else {
            mPoint = mRechargeList.get(mRechargeAdapter.getPotion()).getPoint();
            mPrice = mRechargeList.get(mRechargeAdapter.getPotion()).getPrice() + "";
            mGoodsId = mRechargeList.get(mRechargeAdapter.getPotion()).getGoodsId();
        }

        //创建订单接口
        showCircleWaitingFragment("游戏猫支付");
        PersonalApi.createGoodsOrder(mGoodsId, mPoint, mPrice, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                closeWaitingFragment();
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        MeowOrder meowOrder = JSON.parseObject(content, MeowOrder.class);
                        openPayWayAction(meowOrder);
                    } else {
                        String data = message.getString("message");
                        ToastUtil.makeText(getActivity(), data, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "数据解析错误", false);
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络错误", false);
                }
            }
        },mContext);
    }

    private void openPayWayAction(final MeowOrder meowOrder) {
        if (meowOrder == null) {
            return;
        }
        GameCatPayApi.queryPayWay(meowOrder.getPayPrice(), ORDER_PLATFORM, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (TextUtils.equals(code, "000")) {
                        closeWaitingFragment();
                        String data = message.getString("data");
                        String payWayList = AESUtil.decryptAES(data, Config.getAESKey());
                        openPayAction(meowOrder, payWayList);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        getActivity().finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getActivity().finish();
                } catch (CryptoException e) {
                    e.printStackTrace();
                    getActivity().finish();
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络错误", false);
                    closeWaitingFragment();
                    getActivity().finish();
                }
            }
        },mContext);

    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodUtil.closeInputMethod(getActivity());
    }

    private void openPayAction(MeowOrder meowOrder, String payWayList) {
        StringBuilder description = new StringBuilder();
        description.append("喵点充值: ");
        double amount = 0;
        double catPoint = 0;
        try {
            amount = Double.parseDouble(mPrice);
            catPoint = Double.parseDouble(meowOrder.getPoint());
        } catch (Exception e) {
            e.printStackTrace();
        }
        description.append(Double.valueOf(catPoint).intValue());
        description.append(" 喵点");

//        ConfirmPayFragment fragment = ConfirmPayFragment.getInstance(amount, description.toString(), null, payWayList, meowOrder);
        mButtonConfirmPay.setEnabled(true);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), RechargeFragment.this, false);
//        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment);

        Intent intent = Small.getIntentOfUri("pay",getActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("type",1);
        intent.putExtra("amount",amount);
        intent.putExtra("description",description.toString());
        intent.putExtra("payWayList",payWayList);
        intent.putExtra("meowOrder",meowOrder);
        getActivity().startActivity(intent);
        getActivity().finish();
    }
}
