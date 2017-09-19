package com.youximao.sdk.lib.common.base;

import android.graphics.Bitmap;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;

import com.youximao.sdk.lib.common.common.hybird.WVJBWebViewClient;


/**
 * Created by admin on 2016/11/18.
 */

public class BaseWebViewClient extends WVJBWebViewClient {

    public BaseWebViewClient(WebView webView) {
        // support js send
        super(webView, new WVJBHandler() {
            @Override
            public void request(Object data, WVJBResponseCallback callback) {
                Log.e("xsy", "request data:" + data);
            }
        });
        enableLogging();
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (!view.getSettings().getLoadsImagesAutomatically()) {
            view.getSettings().setLoadsImagesAutomatically(true);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.e("xsy", " shouldOverrideUrlLoading BaseWebViewClient url " + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    //当网页面加载失败时，会调用 这个方法，所以我们在这个方法中处理
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        view.loadUrl("http://sdkstatic.dm.com/error.html");//添加显示本地文件
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); // 接受所有证书
    }
}
