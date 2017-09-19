package com.youximao.sdk.lib.common.common.util;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.youximao.sdk.lib.common.common.MResource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/2/6.
 */

public class FragmentManagerUtil {
    List<Fragment> fragmentsList = new ArrayList();

    private static FragmentManagerUtil INSTANCE;

    private FragmentManagerUtil() {

    }

    public static FragmentManagerUtil getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new FragmentManagerUtil();
        }
        return INSTANCE;
    }

    public void openFragment(Activity activity, Fragment fragment) {
        if (activity == null) {
            return;
        }
        setFragmentList(fragment);
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(MResource.getResource("id", "fragment"), fragment);
        transaction.commitAllowingStateLoss();
    }


    public void openFragment(Activity activity, Fragment fragment, boolean isAddToBackStack) {
        if (activity == null) {
            return;
        }
        setFragmentList(fragment);
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(MResource.getResource("id", "fragment"), fragment);
        if (isAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
    }

    public void closeFragment(Activity activity, Fragment fragment, boolean isCloseActivity, boolean isPopBackStack) {
        if (activity == null) {
            return;
        }
        if (isPopBackStack) {
            activity.getFragmentManager().popBackStack();
        }
        closeFragment(activity, fragment, isCloseActivity);
    }


    public void closeFragment(Activity activity, Fragment fragment, boolean isCloseActivity) {
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
        if (isCloseActivity) {
            getFragmentList().clear();
            activity.finish();
        } else {
            if (getFragmentList().size() > 0) {
                getFragmentList().remove(getFragmentList().size() - 1);
            }
        }
    }

    public void openDialogFragment(Activity activity, Fragment fragment) {
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(MResource.getResource("id", "fragment"), fragment);
        transaction.commitAllowingStateLoss();
    }

    public void closeDialogFragment(Activity activity, Fragment fragment) {
        if (activity == null) {
            return;
        }
        FragmentManager fm = activity.getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

    public List<Fragment> getFragmentList() {
        return fragmentsList;
    }

    public void setFragmentList(Fragment fragment) {
        fragmentsList.add(fragment);
    }
}
