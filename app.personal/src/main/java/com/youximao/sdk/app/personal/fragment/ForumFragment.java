package com.youximao.sdk.app.personal.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.model.ForumItem;
import com.youximao.sdk.app.personal.model.FroumGameId;
import com.youximao.sdk.app.personal.service.ForumApi;
import com.youximao.sdk.app.personal.service.ForumUrl;
import com.youximao.sdk.lib.common.base.BaseWebViewClient;
import com.youximao.sdk.lib.common.base.BaseWebViewFragment;
import com.youximao.sdk.lib.common.common.ClipboardManagerUtil;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * 论坛初始化界面
 * Created by admin on 2016/12/6.
 */

public class ForumFragment extends BaseWebViewFragment implements View.OnClickListener {
    public static ForumFragment newInstance() {
        Bundle args = new Bundle();
        ForumFragment fragment = new ForumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        boolean isInto = AppCacheSharedPreferences.getCacheBoolean(IntentConstant.SWITCH_ACCOUNT, true);
        if (AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_BBS).equals("1")) {
            catForumGameId();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mView = inflater.inflate(getLocalLayoutId(), container, false);
        init(mView);
        if (savedInstanceState != null) {
            Log.e("xsy", "savedInstanceState");
            mWebView.restoreState(savedInstanceState);
        } else {
            mWebView.loadUrl(getUrl());
        }
        return mView;
    }

    @Override
    public String getUrl() {
        return ForumUrl.getForumUrl();
    }


    @Override
    public BaseWebViewClient getBaseWebViewClient(WebView webView) {
        return new MyWebViewClient(webView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_text_view_invisible) {
            FragmentManagerUtil.getInstance().closeFragment(getActivity(), ForumFragment.this, true);
        }
    }

    class MyWebViewClient extends BaseWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView);
            registerHandler("js_closeWindow", new WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {

                    FragmentManagerUtil.getInstance().closeFragment(getActivity(), ForumFragment.this, true);
                }
            });
            registerHandler("js_copy", new WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    String content = data.toString();
                    ClipboardManagerUtil.copy(content, getActivity());
                }
            });
            registerHandler("js_switchAccount", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    switchAccount();
                }
            });
        }
    }

    private void switchAccount() {
        CancelUtil.cancelLogin(mContext);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), ForumFragment.this, true);
    }

    private void catForumGameId() {
        ForumApi.getGameInfo(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        FroumGameId gameId = JSON.parseObject(content, FroumGameId.class);
                        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_FROUM_GAME_ID, gameId.getMid());
                        catForumInit();
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.makeText(getActivity(), "数据解析错误", false);
                }
            }

            @Override
            public void onFail(String message) {
                ToastUtil.makeText(getActivity(), "网络错误", false);
            }
        }, mContext);
    }


    private void catForumInit() {
        ForumApi.getDiscuzToken(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    String code = message.getString("code");
                    if (code.equals("000")) {
                        String data = message.getString("data");
                        Log.e("data", data);
                        String content = AESUtil.decryptAES(data, Config.getAESKey());
                        ForumItem forumItem = JSON.parseObject(content, ForumItem.class);
                        saveForumInfo(forumItem);
                        if (mWebView != null) {
                            mWebView.loadUrl(getUrl());
                        }
                    } else {
                        ToastUtil.makeText(getActivity(), message.getString("message"), false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ToastUtil.makeText(getActivity(), "数据解析错误", false);

                }
            }

            @Override
            public void onFail(String message) {
                ToastUtil.makeText(getActivity(), "网络错误", false);
            }
        }, mContext);
    }

    private void saveForumInfo(ForumItem forumItem) {
        //授权信息拼凑
        String auth = forumItem.getCookiepre() + "auth=" + forumItem.getAuth();
        String saltkey = forumItem.getCookiepre() + "saltkey=" + forumItem.getSaltkey();
        LoadCookie(auth, saltkey);
    }

    /**
     * 论坛cookie授权信息
     */
    protected void LoadCookie(String auth, String saltkey) {
        String forumUrl = ForumUrl.getForumUrl();
        Log.e("forumUrl", forumUrl);
        CookieManager cookie = CookieManager.getInstance();
        CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(getActivity());
        cookieSyncManager.startSync();
        String cookieJar = cookie.getCookie(forumUrl);
        cookie.removeSessionCookie();
        cookie.setAcceptCookie(true);

        String webHost = forumUrl;
        try {
            URL url = new URL(forumUrl);
            webHost = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        cookie.setCookie(webHost, cookieJar);
        cookie.setCookie(webHost, Config.getToken());
        cookie.setCookie(webHost, auth);
        cookie.setCookie(webHost, saltkey);
//        AppCacheSharedPreferences.putCacheBoolean(IntentConstant.SWITCH_ACCOUNT, false);
    }
}
