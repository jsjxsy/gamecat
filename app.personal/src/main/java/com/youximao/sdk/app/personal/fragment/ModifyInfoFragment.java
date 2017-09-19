package com.youximao.sdk.app.personal.fragment;

import android.webkit.WebView;

import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.lib.common.base.BaseWebViewClient;
import com.youximao.sdk.lib.common.base.BaseWebViewFragment;

/**
 * Created by zhan on 17-3-14.
 * <p/>
 * 修改信息（昵称　密码　安全手机　代金券　交易记录）
 */
public class ModifyInfoFragment extends BaseWebViewFragment {
    private SynchronizeInfor synchronizeInfor;//同步h5修改信息
    private int type;//1:修改昵称　2:代金券 　3:绑定安全手机　 4:修改登陆密码　 5:交易记录
    public static final int MODIFY_NAME = 1;
    public static final int CASH_COUPON = 2;
    public static final int BIND_PHONE = 3;
    public static final int MODIFY_PWD = 4;
    public static final int TRANSACTION_RECORD = 5;
    private String loadURL;

    public void setLoadURL(String loadURL) {
        this.loadURL = loadURL;
    }

    public void setSynchronizeInfor(SynchronizeInfor synchronizeInfor) {
        this.synchronizeInfor = synchronizeInfor;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static ModifyInfoFragment getInstance(int type, String url, SynchronizeInfor synchronizeInfor) {
        ModifyInfoFragment modifyInfoFragment = new ModifyInfoFragment();
        modifyInfoFragment.setType(type);
        modifyInfoFragment.setLoadURL(url);
        modifyInfoFragment.setSynchronizeInfor(synchronizeInfor);
        return modifyInfoFragment;
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_person_browser;
    }

    @Override
    public String getUrl() {
        return loadURL;
    }

    public interface SynchronizeInfor {
        void toSynchInfor(String info);
    }

    @Override
    public BaseWebViewClient getBaseWebViewClient(WebView webView) {
        return new MyWebViewClient(webView);
    }

    class MyWebViewClient extends BaseWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView);
            enableLogging();
            registerHandler("info", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    switch (type) {
                        case MODIFY_NAME:
                            // TODO: 17-3-14 同步信息
                            break;
                        case CASH_COUPON:
                            break;
                        case BIND_PHONE:
                            break;
                        case MODIFY_PWD:
                            break;
                        case TRANSACTION_RECORD:
                            break;
                    }
                }
            });
        }
    }
}
