package com.youximao.sdk.app.splash.activity;

import android.app.Fragment;

import com.youximao.sdk.app.splash.fragment.SplashFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;

public class SplashActivity extends BaseActivity {
    @Override
    public Fragment getFragment() {
        return SplashFragment.getInstance();
    }
}
