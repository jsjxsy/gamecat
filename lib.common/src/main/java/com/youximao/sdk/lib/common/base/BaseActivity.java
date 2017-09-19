package com.youximao.sdk.lib.common.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.youximao.sdk.lib.common.R;
import com.youximao.sdk.lib.common.common.SDKManager;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.network.NetworkRequestsUtil;


/**
 * Created by admin on 2016/11/17.
 */

public abstract class BaseActivity extends Activity {
    private final static String SCREEN_ROTATE = "screenRotate";
    private final boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        Uri uri = Small.getUri(this);
        boolean isLandscape = Config.getIsLandscape();
//        if(null != uri){
//            isLandscape = uri.getBooleanQueryParameter(IntentConstant.IS_LANDSCAP,true);
//        }
        if(isLandscape) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        SDKManager.init(this);
        //全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutId(""));
        //初始化
        FragmentManagerUtil.getInstance().getFragmentList().clear();
        //防止横竖屏切换重新创建fragment
        if (savedInstanceState == null || !savedInstanceState.getBoolean(SCREEN_ROTATE)) {
            if (!isOwnmanagementFragment()){
                switchFragment();
            }
        }
    }

    protected boolean isOwnmanagementFragment(){
        return false;
    }

    public int getLayoutId(String layoutName) {
        return R.layout.gamecat_activity;
    }


    public void switchFragment() {
        FragmentManagerUtil.getInstance().openFragment(this, getFragment());
    }

    public abstract Fragment getFragment();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment fragment = getFragment();
        if (BaseFragment.isShoWDialog) {
            if (fragment != null && fragment instanceof BaseFragment) {
                ((BaseFragment) fragment).closeWaitingFragment();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SCREEN_ROTATE, flag);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkRequestsUtil.getInstance().stopNetworkRequests();
    }
}
