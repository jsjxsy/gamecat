package com.youximao.sdk.app.splash.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.app.splash.R;

/**
 * Created by Administrator on 2017/2/9.
 */

public class SplashFragment extends BaseFragment {

    public static SplashFragment getInstance() {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_start;
    }

    @Override
    public void init(View view) {
    }

    @Override
    public void onResume() {
        super.onResume();
        switchFragment();
    }

    public void switchFragment() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_ACTIVE).equals("1")) {
                    FragmentManagerUtil.getInstance().openFragment(getActivity(), AdvertFragment.getInstance());
                } else {
                    if (isAvailableActivity()) {
                        getActivity().finish();
                    }
                }
            }
        }, 2000);

    }
}

