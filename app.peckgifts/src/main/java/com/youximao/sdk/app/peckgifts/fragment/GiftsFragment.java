package com.youximao.sdk.app.peckgifts.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.youximao.sdk.app.peckgifts.R;
import com.youximao.sdk.app.peckgifts.service.PeckGiftsUrl;
import com.youximao.sdk.lib.common.base.BaseWebViewClient;
import com.youximao.sdk.lib.common.base.BaseWebViewFragment;
import com.youximao.sdk.lib.common.common.ClipboardManagerUtil;
import com.youximao.sdk.lib.common.common.hybird.WVJBWebViewClient;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;

import net.wequick.small.Small;

import java.util.List;

/**
 * Created by admin on 2016/10/31.
 */

public class GiftsFragment extends BaseWebViewFragment implements View.OnClickListener {
    public static GiftsFragment getInstance() {
        GiftsFragment fragment = new GiftsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        super.init(view);
        view.findViewById(R.id.id_text_view_invisible).setOnClickListener(this);
    }

    @Override
    public String getUrl() {
        return PeckGiftsUrl.getPeckGiftsUrl();
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_gifts_browser;
    }

    @Override
    public BaseWebViewClient getBaseWebViewClient(WebView webView) {
        return new MyWebViewClient(webView);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_text_view_invisible) {
            FragmentManagerUtil.getInstance().closeFragment(getActivity(),GiftsFragment.this,true);
        }
    }


    class MyWebViewClient extends BaseWebViewClient {
        public MyWebViewClient(WebView webView) {
            super(webView);
            registerHandler("js_closeWindow", new WVJBWebViewClient.WVJBHandler() {

                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    FragmentManagerUtil.getInstance().closeFragment(getActivity(), GiftsFragment.this, true);
                }
            });
            //TODO
            registerHandler("js_copy", new WVJBWebViewClient.WVJBHandler() {

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

            registerHandler("js_openApp", new WVJBHandler() {
                @Override
                public void request(Object data, WVJBResponseCallback callback) {
                    if (openApp()) {
                        callback.callback("{\"result\":true}");
                    } else {
                        callback.callback("{\"result\":false}");
                    }
                }
            });
        }
    }

    private void switchAccount() {
        Small.openUri("usercenter?fromPage=UserSettingFragment",getActivity());
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), GiftsFragment.this, true);
    }

    private boolean openApp() {
        String packageName = "com.youximao.app";
        String className = "com.youximao.app.AppStart";

        if (isAvilible(getActivity(), packageName)) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断相对应的APP是否存在
     *
     * @param context
     * @param packageName(包名)(若想判断QQ，则改为com.tencent.mobileqq，若想判断微信，则改为com.tencent.mm)
     * @return
     */
    public boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        //获取手机系统的所有APP包名，然后进行一一比较
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pInfo.size(); i++) {
            if ((pInfo.get(i)).packageName
                    .equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }
}
