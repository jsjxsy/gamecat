package com.youximao.sdk.app.usercenter.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.database.AccountTypeDao;
import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.app.usercenter.model.User;
import com.youximao.sdk.app.usercenter.model.UserCallback;
import com.youximao.sdk.app.usercenter.model.UserInfo;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.callback.SDKCallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by admin on 2016/10/24.
 */

public class AutoLoginDialogFragment extends BaseFragment implements View.OnClickListener {
    private static final String LOGIN_TYPE = "loginType";
    private final int DELAY_SEND_REQUEST = 2 * 1000;
    private TextView mSwitchAccount;
    private String mPhone;
    private TextView mAccountTextView;
    private int mType;
    //    private String mAccount;
    private ImageView mProgressBarImageView;

    public static AutoLoginDialogFragment getInstance(int type) {
        AutoLoginDialogFragment fragment = new AutoLoginDialogFragment();
        Bundle args = new Bundle();
        args.putInt(LOGIN_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_auto_login_dialog;
    }

    @Override
    public void init(View view) {
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt(LOGIN_TYPE);
        }
        mAccountTextView = (TextView) view.findViewById(R.id.id_text_view_account);
        mSwitchAccount = (TextView) view.findViewById(R.id.id_layout_switch_account);
        mProgressBarImageView = (ImageView)
                view.findViewById(R.id.id_image_view_progress_bar);
        RotateAnimation mRotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateAnimation.setDuration(1000 * 1);
        mRotateAnimation.setRepeatCount(-1);
        mRotateAnimation.setInterpolator(new LinearInterpolator());
        mProgressBarImageView.startAnimation(mRotateAnimation);

        mSwitchAccount.setEnabled(true);
        mSwitchAccount.setOnClickListener(this);
        getUserInfo();

    }


    private void autoLogin() {
        UserApi.getInstance().autoLogin(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                if (isAvailableActivity()) {
                    mSwitchAccount.setEnabled(true);
                    try {
                        String code = message.getString("code");
                        if (code.equals("000")) {
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            User user = JSON.parseObject(content, User.class);
                            saveLocalUserInformation(user);
                            //返回信息去掉userId，防止对接方迷惑采用openId还是userId
                            UserCallback userCallback = JSON.parseObject(content, UserCallback.class);
                            String jsonUser = JSON.toJSONString(userCallback);
                            SDKCallBackUtil.onSuccess(new JSONObject(jsonUser));
                            Collect.getInstance().login(mPhone, user.getOpenId());
                            ToastUtil.makeText(getActivity(), "登录成功", true);
                            FragmentManagerUtil.getInstance().closeFragment(getActivity(), AutoLoginDialogFragment.this, true);
                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance(), false);
                        }
                    } catch (Exception e) {
                        if (isAvailableActivity()) {
                            ToastUtil.makeText(getActivity(), "返回数据不正确", false);
                            e.printStackTrace();
                            FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance());
                        }
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                    SDKCallBackUtil.onFail(message);
                    FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance());
                }
            }
        },null);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_layout_switch_account) {
            FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance(), false);
        }
    }

    private void saveLocalUserInformation(User user) {
        Log.e("xsy", "autoLoginFragment saveLocalUserInformation user token :" + user.getToken());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_TOKEN, user.getToken());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID, user.getOpenId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_USER_ID, user.getUserId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_BINDING_CHANNEL_ID, user.getUserGameBindingChannelId());

    }

    private void getUserInfo() {
        UserApi.getInstance().getUserInfoByToken(new GameCatSDKListener() {

            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        UserInfo userInfo = JSON.parseObject(content, UserInfo.class);
                        displayName(userInfo);
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance(), false);
                    }
                } catch (Exception e) {
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "返回数据不正确", false);
                        e.printStackTrace();
                        FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance());
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                    FragmentManagerUtil.getInstance().openFragment(getActivity(),LoginFragment.getInstance());
                }
            }
        },null);
    }


    private void displayName(UserInfo userInfo) {
        StringBuilder mContent = new StringBuilder();
        mPhone = userInfo.getSafeMobile();
        String account = "";
        switch (mType) {
            case LoginFragment.Mobile_TYPE:
                account = userInfo.getSafeMobile();
                break;
            default:
                account = userInfo.getAccount();
                break;
        }
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT, account);
        mContent.append(account);
        mContent.append("登录中");
        mAccountTextView.setText(mContent);
        openAutoLoginAction();
        saveUserType(userInfo);
    }

    private void openAutoLoginAction() {
        mSwitchAccount.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collect.getInstance().custom(CustomId.id_260000);
                Log.e("CustomId", "id_260000");
                mSwitchAccount.setEnabled(false);
                autoLogin();
            }
        }, DELAY_SEND_REQUEST);

    }

    private void saveUserType(UserInfo userInfo) {
        if (userInfo != null) {
            AccountTypeDao.getInstance().saveAccountType(userInfo, mType, Config.getToken(), System.currentTimeMillis());
        }
    }

}

