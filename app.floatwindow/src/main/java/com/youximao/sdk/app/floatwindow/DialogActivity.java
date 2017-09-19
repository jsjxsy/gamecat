package com.youximao.sdk.app.floatwindow;

import android.app.Fragment;

import com.youximao.sdk.lib.common.base.BaseActivity;

/**
 * Created by admin on 2017/3/20.
 */

public class DialogActivity extends BaseActivity {

    @Override
    public Fragment getFragment() {
        return FloatWindowDialog.newInstance();
    }
}
