package com.youximao.sdk.app.personal.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.model.GoodsItem;
import com.youximao.sdk.app.personal.model.PointRecharge;
import com.youximao.sdk.app.personal.service.PersonalApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by zhan on 17-3-13.
 * <p/>
 * 个人中心
 */
public class PersonalFragment extends BaseFragment implements ModifyInfoFragment.SynchronizeInfor {
    private TextView mSwitchAccount;
    private TextView mNickName;
    private View mBtnNick;
    private TextView mAccount;
    private TextView mLevel;
    private TextView mCashCoupon;
    private View mBtnCashCoupon;
    private TextView mMiaodian;
    private Button mBtnToRecharge;
    private TextView mBindPhone;
    private View mBtnBindPhone;
    private View mBtnModifyPwd;
    private View mBtnTransactionRecord;
    private TextView mVersion;
    private TextView mAssociatedAccount;

    private boolean isJump;

    public static PersonalFragment getInstance(boolean isJump) {
        PersonalFragment personalFragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putBoolean("isjump",isJump);
        personalFragment.setArguments(args);
        return personalFragment;
    }

    @Override
    public void init(View view) {
        mAssociatedAccount = (TextView) view.findViewById(R.id.tv_associated_account);
        mSwitchAccount = (TextView) view.findViewById(R.id.tv_switch_account);
        mSwitchAccount.setOnClickListener(this);
        mNickName = (TextView) view.findViewById(R.id.tv_nick_name);
        mBtnNick = view.findViewById(R.id.btn_nick);
        mBtnNick.setOnClickListener(this);
        mAccount = (TextView) view.findViewById(R.id.tv_account);
        mLevel = (TextView) view.findViewById(R.id.tv_level);
        mCashCoupon = (TextView) view.findViewById(R.id.tv_cash_coupon);
        mBtnCashCoupon = view.findViewById(R.id.btn_cash_coupon);
        mBtnCashCoupon.setOnClickListener(this);
        mMiaodian = (TextView) view.findViewById(R.id.tv_miaodian);
        mBtnToRecharge = (Button) view.findViewById(R.id.btn_to_recharge);
        mBtnToRecharge.setOnClickListener(this);
        mBindPhone = (TextView) view.findViewById(R.id.tv_bind_phone);
        mBtnBindPhone = view.findViewById(R.id.btn_bind_phone);
        mBtnBindPhone.setOnClickListener(this);
        mBtnModifyPwd = view.findViewById(R.id.btn_modify_pwd);
        mBtnModifyPwd.setOnClickListener(this);
        mBtnTransactionRecord = view.findViewById(R.id.btn_transaction_record);
        mBtnTransactionRecord.setOnClickListener(this);
        mVersion = (TextView) view.findViewById(R.id.tv_version);
        mVersion.setText(Config.getVersion());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            isJump = bundle.getBoolean("isjump",false);
        }
        if (isJump){
            // 自动跳转到绑定安全手机
            openSynchInfor(ModifyInfoFragment.BIND_PHONE, "https://www.baidu.com/", this);
        }
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_personal;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_switch_account://切换账号
                switchAccount();
                break;
            case R.id.btn_nick://修改昵称
                openSynchInfor(ModifyInfoFragment.MODIFY_NAME, "https://www.baidu.com/", this);
                break;
            case R.id.btn_cash_coupon://代金券
                openSynchInfor(ModifyInfoFragment.CASH_COUPON, "https://www.baidu.com/", this);
                break;
            case R.id.btn_to_recharge://充值
                Collect.getInstance().custom(CustomId.id_210000);
                catPointRechargeInit();
                break;
            case R.id.btn_bind_phone://绑定安全手机
                openSynchInfor(ModifyInfoFragment.BIND_PHONE, "https://www.baidu.com/", this);
                break;
            case R.id.btn_modify_pwd://修改登陆密码
                openSynchInfor(ModifyInfoFragment.MODIFY_PWD, "https://www.baidu.com/", this);
                break;
            case R.id.btn_transaction_record://交易记录
                openSynchInfor(ModifyInfoFragment.TRANSACTION_RECORD, "https://www.baidu.com/", this);
                break;
        }
    }

    private void catPointRechargeInit() {
        PersonalApi.catPointRechargeInit(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        PointRecharge goods = JSON.parseObject(content, PointRecharge.class);
                        //添加自定义金额
                        List<GoodsItem> mRechargeList = goods.getGoods();
                        GoodsItem goodsItem = new GoodsItem();
                        goodsItem.setGoodsId("");
                        goodsItem.setPoint("");
                        goodsItem.getPrice();
                        goodsItem.setIsEnable("");
                        mRechargeList.add(goodsItem);
                        openPayAction(goods);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
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
        }, mContext);
    }

    private void openPayAction(PointRecharge goods) {
        RechargeFragment fragment = RechargeFragment.getInstance(goods);
        addFragment(fragment);
        FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), fragment);
    }

    private void openSynchInfor(int type, String url, ModifyInfoFragment.SynchronizeInfor synchronizeInfor) {
        ModifyInfoFragment modifyInfoFragment = ModifyInfoFragment.getInstance(type, url, synchronizeInfor);
        addFragment(modifyInfoFragment);
        FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), modifyInfoFragment);
    }

    private void switchAccount() {
        CancelUtil.cancelLogin(mContext);
        getActivity().finish();
    }

    @Override
    public void toSynchInfor(String info) {

    }
}
