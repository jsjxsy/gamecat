package com.youximao.sdk.lib.common.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.youximao.sdk.lib.common.R;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.common.Event;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.config.DefaultParams;
import com.youximao.sdk.lib.common.common.hybird.WVJBWebViewClient;
import com.youximao.sdk.lib.common.common.util.DeviceNetworkUtil;

/**
 * Created by admin on 2016/11/18.
 */

public class BaseWebViewFragment extends BaseFragment {
    public View mView;
    protected WebView mWebView;
    protected WVJBWebViewClient mWebViewClient;
    protected LinearLayout mUnknown;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mView = inflater.inflate(getLocalLayoutId(), container, false);
        init(mView);
        if (savedInstanceState != null) {
            Log.e("xsy", "savedInstanceState");
            mWebView.restoreState(savedInstanceState);
        } else {
            Log.e("xsy", "not savedInstanceState");
            mWebView.loadUrl(getUrl());
        }
        return mView;
    }

    @Override
    public void init(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        mUnknown = (LinearLayout) view.findViewById(R.id.ll_unknown);
        initSetting(mWebView.getSettings());
        mWebViewClient = getBaseWebViewClient(mWebView);
        mWebView.setWebViewClient(mWebViewClient);
        if (DeviceNetworkUtil.isNetworkAvailable(getActivity())) {
            mUnknown.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);
        } else {
            mUnknown.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
        mUnknown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DeviceNetworkUtil.isNetworkAvailable(getActivity())) {
                    mUnknown.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);
                } else {
                    mUnknown.setVisibility(View.VISIBLE);
                    mWebView.setVisibility(View.GONE);
                    initCommonParameter();
                    mWebView.loadUrl(getUrl());
                }
            }
        });
        initCommonParameter();
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_browser;
    }

    public void initSetting(WebSettings settings) {
        //优化显示
        if (Build.VERSION.SDK_INT >= 19) {
            mWebView.getSettings().setLoadsImagesAutomatically(true);
        } else {
            mWebView.getSettings().setLoadsImagesAutomatically(false);
        }
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        if (DeviceNetworkUtil.isNetworkAvailable(getActivity())) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    /**
     * 初始化WebView参数，让WebView获取用户信息
     */
    private void initCommonParameter() {
        mWebViewClient.registerHandler("js_commentParameter", new WVJBWebViewClient.WVJBHandler() {

            @Override
            public void request(Object data, WVJBWebViewClient.WVJBResponseCallback callback) {
                JSONObject json = new JSONObject();
                json.clear();
                json.put("token", Config.getToken());
                json.put("devicesId", Config.getVersion());
                json.put("terminalName", Build.MANUFACTURER);
                json.put("terminalType", "2");
                json.put("version", Config.getVersion());
                json.put("openId", Config.getOpenId());
                json.put("appKey", Config.getAppKey());
                json.put("sdkVersion", Config.getVersion());
                json.put("devicStatue", DefaultParams.getInstance(getActivity()).getDeviceStatusParams());
                json.put("data", DefaultParams.getInstance(getActivity()).getDataParams());
                json.put("requestId", String.valueOf(System.currentTimeMillis()));
                Log.e("xsy", "json:" + json.toJSONString());
                callback.callback(json.toJSONString());
            }
        });

        mWebViewClient.registerHandler("js_analytics", new WVJBWebViewClient.WVJBHandler() {

            @Override
            public void request(Object data, WVJBWebViewClient.WVJBResponseCallback callback) {
                String json = data.toString();
                Event event = JSON.parseObject(json, Event.class);
                Log.e("xsy", "json:" + json);
                if (event != null) {
                    Collect.getInstance().custom(event.getEventId());
                    callback.callback("success");
                } else {
                    callback.callback("fail");
                }
            }
        });

    }

    public String getUrl() {
        return "";
    }

    public BaseWebViewClient getBaseWebViewClient(WebView webView) {

        return new MyWebViewClient(webView);


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mWebView.destroy();
        super.onDestroy();
    }

    class MyWebViewClient extends BaseWebViewClient {

        public MyWebViewClient(WebView webView) {
            super(webView);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        //当网页面加载失败时，会调用 这个方法，所以我们在这个方法中处理
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mUnknown.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
        }
    }


}
