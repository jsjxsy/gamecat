package com.youximao.sdk.app.pay.activity;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

import com.youximao.sdk.app.pay.R;
import com.youximao.sdk.app.pay.fragment.ConfirmPayFragment;
import com.youximao.sdk.app.pay.fragment.WaitingFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;
import com.youximao.sdk.lib.common.common.callback.CallBackUtil;
import com.youximao.sdk.lib.common.common.model.MeowOrder;


public class PayActivity extends BaseActivity {
    private double mAmount;
    private String mDescription;
    private String mCodeNo;
    private String mNotifyUrl;
    private String mExtend;
    private String mGoodsType = "1";
    private int type;//１：代表从个人跳到支付确认界面　其他值走原来的正常逻辑
    private double amount;
    private String description;
    private String payWayList;
    private MeowOrder meowOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initRuntime();
        super.onCreate(savedInstanceState);
    }

    private void initRuntime() {
        Intent intent = getIntent();
        mAmount = intent.getDoubleExtra("amount", 1);
        mDescription = intent.getStringExtra("description");
        mCodeNo = intent.getStringExtra("codeNo");
        mNotifyUrl = intent.getStringExtra("notifyUrl");
        mExtend = intent.getStringExtra("extend");
        type = intent.getIntExtra("type",0);
        amount = intent.getDoubleExtra("amount",0);
        description = intent.getStringExtra("description");
        payWayList = intent.getStringExtra("payWayList");
        meowOrder = (MeowOrder) intent.getSerializableExtra("meowOrder");
    }

    @Override
    public int getLayoutId(String layoutName) {
        return R.layout.gamecat_pay_activity;
    }

    @Override
    public Fragment getFragment() {
        if (type == 1){
            return ConfirmPayFragment.getInstance(amount, description, null, payWayList, meowOrder,0,"");
        }else {
            return WaitingFragment.getInstance("游戏猫支付", mAmount, mGoodsType);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CallBackUtil.onFail();
    }
}
