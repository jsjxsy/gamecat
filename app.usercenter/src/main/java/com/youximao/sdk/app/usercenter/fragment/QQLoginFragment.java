package com.youximao.sdk.app.usercenter.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.activity.UserActivity;
import com.youximao.sdk.app.usercenter.database.AccountTypeDao;
import com.youximao.sdk.app.usercenter.model.AccessToken;
import com.youximao.sdk.app.usercenter.model.User;
import com.youximao.sdk.app.usercenter.model.UserCallback;
import com.youximao.sdk.app.usercenter.model.UserInfo;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.app.usercenter.service.UserUrl;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseWebViewClient;
import com.youximao.sdk.lib.common.base.BaseWebViewFragment;
import com.youximao.sdk.lib.common.common.callback.SDKCallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

/**
 * Created by admin on 2016/11/9.
 */

public class QQLoginFragment extends BaseWebViewFragment {
    private String mAccount;

    public static QQLoginFragment getInstance() {
        QQLoginFragment fragment = new QQLoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
        mWebView.setBackgroundColor(0);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_browser_qq;
    }

    @Override
    public String getUrl() {
        return UserUrl.getQQLoginUrl();
    }

    @Override
    public BaseWebViewClient getBaseWebViewClient(WebView webView) {
        return new MyWebViewClient(webView);
    }

    private void switchAccount() {
        Intent intent = new Intent(getActivity(), UserActivity.class);
        getActivity().startActivity(intent);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), QQLoginFragment.this, true);
    }

    private void saveLocalUserInformation(User user) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_TOKEN, user.getToken());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID, user.getOpenId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_USER_ID, user.getUserId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_BINDING_CHANNEL_ID, user.getUserGameBindingChannelId());
        getUserInfo();
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
                        saveUserType(userInfo);
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
                    FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance());
                }
            }
        }, null);
    }

    private void saveUserType(UserInfo userInfo) {
        if (userInfo != null) {
            AccountTypeDao.getInstance().saveAccountType(userInfo, LoginFragment.QQ_TYPE, Config.getToken(), System.currentTimeMillis());
        }
    }

    class MyWebViewClient extends BaseWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView);
            registerHandler("js_switchAccount", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    switchAccount();
                }
            });

            registerHandler("js_accessToken", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    AccessToken accessToken = JSON.parseObject(data.toString(), AccessToken.class);
                    if (accessToken == null) {
                        ToastUtil.makeText(getActivity(), "请求参数accessToken为空", false);
                        return;
                    }
                    UserApi.getInstance().getQQOpenId(accessToken.getAccessToken(), new GameCatSDKListener() {
                        @Override
                        public void onSuccess(JSONObject message) {
                            if (isAvailableActivity()) {
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
                                        ToastUtil.makeText(getActivity(), "登录成功", true);
                                        Collect.getInstance().custom(CustomId.id_251000);
                                        FragmentManagerUtil.getInstance().closeFragment(getActivity(), QQLoginFragment.this, true);
                                    } else {
                                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                                        FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance(), false);
                                    }
                                } catch (Exception e) {
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
                                FragmentManagerUtil.getInstance().openFragment(getActivity(), LoginFragment.getInstance());
                            }
                        }
                    }, null);
                }
            });

        }
    }
}
