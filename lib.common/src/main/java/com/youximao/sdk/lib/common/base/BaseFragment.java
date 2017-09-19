package com.youximao.sdk.lib.common.base;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youximao.sdk.lib.common.common.MResource;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * 资源获取
 * Created by davy on 16/6/15.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    public static boolean isShoWDialog = false;
    public View mView;
    private Fragment mDialogFragment;
    protected Activity mContext;
    protected Map<String, Fragment> fragmentList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        Activity activity1 = Objects.requireNonNull(activity);
        this.mContext = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mView = inflater.inflate(getLocalLayoutId(), container, false);
        init(mView);
        return mView;
    }

    public abstract void init(View view);

    public abstract int getLocalLayoutId();

    public void close(Fragment fragment) {

    }

    public void closeWaitingFragment() {
        if (isShoWDialog) {
            isShoWDialog = false;
            if (isAvailableActivity()) {
                FragmentManagerUtil.getInstance().closeDialogFragment(getActivity(), mDialogFragment);
            }
        }
    }

    public void showWaitingFragment(String message) {
        if (!isShoWDialog) {
            isShoWDialog = true;
            if (isAvailableActivity()) {
                mDialogFragment = ProgressDialogFragment.getInstance(message);
                FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), mDialogFragment);
            }
        }
    }

    public void showCircleWaitingFragment(String message) {
        if (!isShoWDialog) {
            isShoWDialog = true;
            if (isAvailableActivity()) {
                mDialogFragment = CircleProgressDialogFragment.getInstance(message);
                FragmentManagerUtil.getInstance().openDialogFragment(getActivity(), mDialogFragment);
            }
        }
    }

    public boolean isAvailableActivity() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        NetworkRequestsUtil.getInstance().stopNetworkRequests();
    }

    /**
     * 获取id
     *
     * @param layoutName id的名字
     * @return
     */
    public int getLayoutId(String layoutName) {
        return MResource.getResource("layout", layoutName);
    }

    /**
     * 获取id
     *
     * @param name id的名字
     * @return
     */
    public int getIdId(String name) {
        int id = MResource.getResource("id", name);
        return id;
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 当前页面打开fragment保存到当前页面容器中
     *
     * @param fragment
     */
    protected void addFragment(Fragment fragment) {
        if (fragmentList == null) {
            fragmentList = new HashMap<String, Fragment>();
        }
        fragmentList.put(fragment.getClass().toString(), fragment);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (fragmentList == null || fragmentList.size() == 0) {
            return;
        }
        for (Fragment fragment : fragmentList.values()) {
            if (hidden) {
                hideFragment(fragment);
            } else {
                showFragment(fragment);
            }
        }
    }

    protected void hideFragment(Fragment fragment) {
        if (fragment != null && getActivity() != null) {
            if (fragment.isAdded()) {
                FragmentTransaction beginTransaction = getActivity().getFragmentManager().beginTransaction();
                beginTransaction.hide(fragment);
                beginTransaction.commitAllowingStateLoss();
            }
        }
    }

    protected void showFragment(Fragment fragment) {
        if (fragment != null && getActivity() != null) {
            if (fragment.isAdded()) {
                FragmentTransaction beginTransaction = getActivity().getFragmentManager().beginTransaction();
                beginTransaction.show(fragment);
                beginTransaction.commitAllowingStateLoss();
            }
        }
    }
}
