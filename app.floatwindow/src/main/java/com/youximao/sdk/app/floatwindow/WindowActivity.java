package com.youximao.sdk.app.floatwindow;

import android.app.Activity;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.gamecat.sdk.proxy.HostInterface;
import com.gamecat.sdk.proxy.HostInterfaceManager;
import com.gamecat.sdk.proxy.ServiceInterface;
import com.gamecat.sdk.proxy.ServiceInterfaceManager;

import net.wequick.small.Small;

public class WindowActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fw_activity_main);
        action();
    }

    private void action() {
        Uri uri = Small.getUri(this);
        if (uri != null) {
            String method = uri.getQueryParameter("method");
            switch (method) {
                case "start":
                    start(uri);
                    break;
            }
        }
    }


    private void start(Uri uri) {
        boolean isShowNavigationBar = Boolean.parseBoolean(uri.getQueryParameter("isShowNavigationBar"));
        HostInterface mInterface = HostInterfaceManager.getInstance().getHostInterface();
        if (null != mInterface) {
            Activity mContext = mInterface.getActivity();
            FloatWindowService.startService(mContext, isShowNavigationBar);
            ServiceInterfaceManager.setServiceInterface(new ServiceInterface() {
                @Override
                public void showFloatWindow() {
                    if (FloatWindowService.getInstance() != null) {
                        FloatWindowService.getInstance().showSmallWindow();
                    }
                }

                @Override
                public void hideFloatWindow() {
                    if (FloatWindowService.getInstance() != null) {
                        FloatWindowService.getInstance().hideSmallWindow();
                    }
                }

                @Override
                public void stopFloatWindow() {
                    HostInterface mInterface = HostInterfaceManager.getInstance().getHostInterface();
                    if (null != mInterface) {
                        Context mContext = mInterface.getActivity();
                        if (FloatWindowService.getInstance() != null) {
                            FloatWindowService.stopService(mContext);
                        }
                    } else {
                        FloatWindowService.stopService(WindowActivity.this);
                    }
                }
            });
            finish();
        } else {
            FloatWindowService.startService(WindowActivity.this, isShowNavigationBar);
        }
    }


}
