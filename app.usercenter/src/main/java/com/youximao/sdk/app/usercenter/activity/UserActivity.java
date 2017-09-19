package com.youximao.sdk.app.usercenter.activity;


import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.youximao.sdk.app.usercenter.fragment.AutoLoginDialogFragment;
import com.youximao.sdk.app.usercenter.fragment.LoginFragment;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.base.BaseActivity;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.IntentConstant;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.database.DatabaseCache;

import net.wequick.small.Small;


public class UserActivity extends BaseActivity {
    private String pageName = "";
    private boolean switchAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Fragment getFragment() {
        Uri uri = Small.getUri(this);
        if (uri != null) {
            pageName = uri.getQueryParameter(IntentConstant.FROM_PAGE);
            switchAccount = uri.getBooleanQueryParameter(IntentConstant.SWITCH_ACCOUNT, false);
        }
        AppCacheSharedPreferences.putCacheBoolean(IntentConstant.SWITCH_ACCOUNT, switchAccount);
        if (!Config.isLogin() || TextUtils.equals(pageName, IntentConstant.PAGE_NAME)
                || switchAccount) {
            return LoginFragment.getInstance();
        } else {
            int type = AppCacheSharedPreferences.getCacheInteger(SharePreferenceConstant.GAME_CAT_LOGIN_TYPE);
            Log.e("xsy", "getFragment() type:" + type);
            return AutoLoginDialogFragment.getInstance(type);
        }
    }


    private boolean isCreateDatabase() {
        Uri uri = Small.getUri(this);
        if (uri != null) {
            String method = uri.getQueryParameter("method");
            if (TextUtils.equals("database", method)) {
                return true;
            }
        }

        return false;
    }


    @Override
    protected boolean isOwnmanagementFragment() {
        if (isCreateDatabase()) {
            createDatabaseAction();
            return true;
        }
        return super.isOwnmanagementFragment();
    }

    private void createDatabaseAction() {
        DatabaseCache.getInstance().initContext(UserActivity.this);
        UserApi.getInstance();
        finish();
    }

//    class createDatabaseAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            DatabaseCache.getInstance().initContext(UserActivity.this);
//            UserApi.getInstance();
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void result) {
//            finish();
//        }
//
//    }
}
