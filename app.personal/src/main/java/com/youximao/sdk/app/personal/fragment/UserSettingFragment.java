package com.youximao.sdk.app.personal.fragment;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.model.GoodsItem;
import com.youximao.sdk.app.personal.model.PointRecharge;
import com.youximao.sdk.app.personal.service.PersonalApi;
import com.youximao.sdk.app.personal.service.PersonalUrl;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseWebViewClient;
import com.youximao.sdk.lib.common.base.BaseWebViewFragment;
import com.youximao.sdk.lib.common.common.callback.CancelUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.List;

/**
 * 主要是处理网页上的东西，webView加载器
 * Created by davy on 16/6/14.
 */
public class UserSettingFragment extends BaseWebViewFragment implements View.OnClickListener {
    private ViewStub mProgressBarView;

    public static UserSettingFragment getInstance() {
        UserSettingFragment fragment = new UserSettingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
//        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view.findViewById(R.id.id_text_view_invisible).setOnClickListener(this);
        mProgressBarView = (ViewStub) view.findViewById(R.id.view_stub_progress_bar);
        mProgressBarView.inflate();
    }

    @Override
    public String getUrl() {
        return PersonalUrl.getWebUserCenterUrl();
    }


    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_person_browser;
    }

    @Override
    public BaseWebViewClient getBaseWebViewClient(WebView webView) {
        return new MyWebViewClient(webView);
    }

    private void switchAccount() {
        CancelUtil.cancelLogin(mContext);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), UserSettingFragment.this, true);
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
        },mContext);
    }

    @Override
    public void onDestroy() {
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onDestroy();
    }

    private void openPayAction(PointRecharge goods) {
        RechargeFragment fragment = RechargeFragment.getInstance(goods);
        FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), fragment);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_text_view_invisible) {
            FragmentManagerUtil.getInstance().closeFragment(getActivity(), UserSettingFragment.this, true);
        }
    }

    class MyWebViewClient extends BaseWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView);
            enableLogging();
            registerHandler("js_switchAccount", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    switchAccount();
                }
            });

            registerHandler("js_noToken", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    switchAccount();
                }
            });


            registerHandler("js_closeWindow", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    FragmentManagerUtil.getInstance().closeFragment(getActivity(), UserSettingFragment.this, true);
                    Collect.getInstance().custom(CustomId.id_200000);
                }
            });

            registerHandler("js_catPointRecharge", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    Collect.getInstance().custom(CustomId.id_210000);
                    catPointRechargeInit();
                }
            });

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mProgressBarView != null) {
                mProgressBarView.setVisibility(View.GONE);
            }
        }
    }


}
