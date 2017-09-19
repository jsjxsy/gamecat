package com.youximao.sdk.app.peckgifts.activity;

import android.app.Fragment;

import com.youximao.sdk.app.peckgifts.fragment.GiftsFragment;
import com.youximao.sdk.lib.common.base.BaseActivity;

/**
 * Created by admin on 2016/10/17.
 */

public class PeckGiftsActivity extends BaseActivity {

    @Override
    public Fragment getFragment() {
        return GiftsFragment.getInstance();
    }
}
