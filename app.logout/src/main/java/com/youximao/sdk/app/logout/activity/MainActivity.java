package com.youximao.sdk.app.logout.activity;


import android.app.Fragment;

import com.youximao.sdk.app.logout.fragment.LogoutFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;

public class MainActivity extends BaseActivity {


    @Override
    public Fragment getFragment() {
        return LogoutFragment.getInstance();
    }
}
