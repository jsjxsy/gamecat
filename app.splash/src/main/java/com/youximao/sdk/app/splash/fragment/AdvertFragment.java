package com.youximao.sdk.app.splash.fragment;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.youximao.sdk.app.splash.R;
import com.youximao.sdk.app.splash.service.AdvertUrl;
import com.youximao.sdk.lib.common.base.BaseFragment;

/**
 * Created by Administrator on 2017/2/9.
 */

public class AdvertFragment extends BaseFragment implements View.OnClickListener {
    private ImageView mImageViewClose;
    private WebView mWebView;

    public static AdvertFragment getInstance() {
        AdvertFragment fragment = new AdvertFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_fragment_advert;
    }

    @Override
    public void init(View view) {
        mImageViewClose = (ImageView) view.findViewById(R.id.iv_close);
        mImageViewClose.setOnClickListener(this);
        mWebView = (WebView) view.findViewById(R.id.wb_activity);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.loadUrl(getUrl());
    }


    private String getUrl() {
        return AdvertUrl.getAdvertUrl();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            getActivity().finish();
        }
    }
}

