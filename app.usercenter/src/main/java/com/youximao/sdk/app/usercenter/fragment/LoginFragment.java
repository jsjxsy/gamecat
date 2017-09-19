package com.youximao.sdk.app.usercenter.fragment;

import android.app.Fragment;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.adapter.AccountAdapter;
import com.youximao.sdk.app.usercenter.callback.DeleteDialogListener;
import com.youximao.sdk.app.usercenter.callback.SelectAccountListener;
import com.youximao.sdk.app.usercenter.database.AccountTypeDao;
import com.youximao.sdk.app.usercenter.dialog.AccountsPopWindow;
import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.app.usercenter.model.User;
import com.youximao.sdk.app.usercenter.model.UserCallback;
import com.youximao.sdk.app.usercenter.model.UserInfo;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.callback.SDKCallBackUtil;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.SimpleTextWatcher;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 用户登录界面
 * Created by davy on 16/6/15.
 */
public class LoginFragment extends BaseFragment implements View.OnClickListener, DeleteDialogListener, SelectAccountListener {
    public final static int QQ_TYPE = 1;
    public final static int QUICK_REGISTER_TYPE = 2;
    public final static int Mobile_TYPE = 3;
    public final static int CAT_POINT_TYPE = 4;

    private final static int DISPLAY_VERSION_TIME = 5;
    private int mCount = 0;
    EditText mPhoneEditText;
    EditText mPasswordEditText;
    Button mLoginButton;
    TextView mPhoneRegister;
    TextView mMissedPassword;
    TextView mPhonePrompt;
    TextView mPasswordPrompt;
    TextView mPasswordWaring;
    private ImageView mAccountMore;
    //手机号码输入监听
    private final TextWatcher mEditTextWatcher = new SimpleTextWatcher() {


        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (mPasswordEditText == null || mPhoneEditText == null) {
                return;
            }

            String account = mPhoneEditText.getEditableText().toString();
            String password = mPasswordEditText.getEditableText().toString();
            mPhonePrompt.setVisibility(View.GONE);
            mPasswordPrompt.setVisibility(View.GONE);

            if (password.length() > 0 && password.length() < 6) {
                mPasswordWaring.setVisibility(View.VISIBLE);
            } else {
                mPasswordWaring.setVisibility(View.GONE);
            }

            /**
             * 手机号11 喵号7-10位
             * 密码6位数字或字母
             */
            if (account.length() > 6 && password.length() > 5) {
                mLoginButton.setEnabled(true);
            } else {
                mLoginButton.setEnabled(false);
            }
        }
    };
    private String mInitPassword;
    private View mInputPasswordLayout;
    private AccountAdapter mAccountAdapter;

    public static LoginFragment getInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_login;
    }

    public void init(View view) {
        view.findViewById(R.id.id_login_title).setOnClickListener(this);
        mMissedPassword = (TextView) view.findViewById(R.id.id_text_view_miss_password);
        mMissedPassword.setOnClickListener(this);

        mPhoneEditText = (EditText) view.findViewById(R.id.id_edit_text_input_phone);
        mPhoneEditText.addTextChangedListener(mEditTextWatcher);

        mPhonePrompt = (TextView) view.findViewById(R.id.id_text_view_input_tip);
        mPasswordPrompt = (TextView) view.findViewById(R.id.id_text_view_password_tip);
        mPhonePrompt.setVisibility(View.GONE);
        mPasswordPrompt.setVisibility(View.GONE);
        mPasswordWaring = (TextView) view.findViewById(R.id.id_text_view_password_wroing);
        mPasswordWaring.setVisibility(View.GONE);

        mAccountMore = (ImageView) view.findViewById(R.id.id_image_view_up_arrow);
        mAccountMore.setOnClickListener(this);
        TextView mAccountWrongTip = (TextView) view.findViewById(R.id.id_text_view_wrong_phone);
        mAccountWrongTip.setVisibility(View.GONE);

        mPasswordEditText = (EditText) view.findViewById(R.id.id_password_edit);
        mPasswordEditText.addTextChangedListener(mEditTextWatcher);
        TextView mPasswordWrongTip = (TextView) view.findViewById(R.id.id_text_view_wrong_password);
        mPasswordWrongTip.setVisibility(View.GONE);

        mLoginButton = (Button) view.findViewById(R.id.id_login_button);
        mLoginButton.setOnClickListener(this);
        mLoginButton.setEnabled(false);

        TextView mQuickRegister = (TextView) view.findViewById(R.id.id_text_view_quick_register);
        mQuickRegister.setOnClickListener(this);

        TextView mQQLogin = (TextView) view.findViewById(R.id.id_text_view_qq_login);
        mQQLogin.setOnClickListener(this);
        if (AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_QQ_LOGIN).equals("1")) {
            mQQLogin.setVisibility(View.VISIBLE);
        } else {
            mQQLogin.setVisibility(View.GONE);
        }

        mPhoneRegister = (TextView) view.findViewById(R.id.id_text_view_phone_register);
        mPhoneRegister.setOnClickListener(this);

        mInputPasswordLayout = view.findViewById(R.id.id_edit_text_input_password_layout);
        initViewByData();
    }


    private void initViewByData() {
        String account = AppCacheSharedPreferences.getCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT);
        ArrayList<AccountType> accountTypes = AccountTypeDao.getInstance().listAccounts();
        //SharePreference is null but account ArrayList is not empty
        if (TextUtils.isEmpty(account) && !accountTypes.isEmpty()) {
            if (accountTypes.get(0).getType() == LoginFragment.Mobile_TYPE) {
                account = accountTypes.get(0).getSafeMobile();
            } else {
                account = accountTypes.get(0).getAccount();
            }
            setInputPhoneIcon(accountTypes.get(0));
        }


        if (!TextUtils.isEmpty(account)) {

            mPhoneEditText.setText(account);
            mPhoneEditText.setSelection(account.length());

            String token = Config.getToken();
            if (TextUtils.isEmpty(token) && !accountTypes.isEmpty()) {
                token = accountTypes.get(0).getToken();
            }
            if (!TextUtils.isEmpty(token)) {
                mPasswordEditText.setText(token);
                mInitPassword = mPasswordEditText.getText().toString();
            }

            Bundle args = getArguments();
            if (args != null && args.containsKey(FindPasswordFragmentThree.GAME_CAT_PASSWORD)) {
                String password = args.getString(FindPasswordFragmentThree.GAME_CAT_PASSWORD);
                mPasswordEditText.setText(password);
            }
        }
        setAccountMore(accountTypes);
    }


    private void setAccountMore(ArrayList<AccountType> accountTypes) {
        if (accountTypes.isEmpty()) {
            mAccountMore.setVisibility(View.GONE);
        } else {
            mAccountMore.setVisibility(View.VISIBLE);
        }
    }


    private void setInputPhoneIcon(AccountType account) {
        int resId = -1;
        switch (account.getType()) {
            case LoginFragment.QQ_TYPE:
                resId = R.drawable.game_cat_account_qq;
                break;
            case LoginFragment.QUICK_REGISTER_TYPE:
                resId = R.drawable.game_cat_account_one_key_login;
                break;
            case LoginFragment.Mobile_TYPE:
                resId = R.drawable.game_cat_account_mobile;
                break;
            case LoginFragment.CAT_POINT_TYPE:
                resId = R.drawable.game_cat_account_cat_point;
                break;
            default:
                resId = R.drawable.game_cat_account_cat_point;
                break;
        }

        Drawable leftDrawable = getResources().getDrawable(resId);
        // 取 drawable 的长宽
        int w = leftDrawable.getIntrinsicWidth();
        int h = leftDrawable.getIntrinsicHeight();
        Rect rect = new Rect(0, 0, w, h);
        leftDrawable.setBounds(rect);
        mPhoneEditText.setCompoundDrawables(leftDrawable, null, null, null);
    }


    @Override
    public void close(Fragment fragment) {
        super.close(fragment);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), fragment, true);
    }

    AccountsPopWindow accountsPopWindow;

    private void displayMore() {
        if (!isAvailableActivity()) {
            return;
        }

        if (accountsPopWindow == null) {
            accountsPopWindow = new AccountsPopWindow(getActivity(), this);
            mAccountAdapter = accountsPopWindow.getAccountAdapter();
            mAccountAdapter.setSelectAccountListener(this);
        }

        if (accountsPopWindow.isShowing()) {
            accountsPopWindow.dismiss();
        } else {
            accountsPopWindow.showAsDropDown(mInputPasswordLayout);
        }

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_login_button) {
            openLoginAction();
        } else if (v.getId() == R.id.id_text_view_miss_password) {
            Collect.getInstance().custom(CustomId.id_224000);
            openFindPasswordAction();
        } else if (v.getId() == R.id.id_image_view_clear) {
            clearAccount();
        } else if (v.getId() == R.id.id_image_view_up_arrow) {
            displayMore();
        } else if (v.getId() == R.id.id_text_view_quick_register) {
            Collect.getInstance().custom(CustomId.id_280000);
            quickRegister();
        } else if (v.getId() == R.id.id_text_view_qq_login) {
            Collect.getInstance().custom(CustomId.id_250000);
            openOtherLoginAction();
        } else if (v.getId() == R.id.id_text_view_phone_register) {
            Collect.getInstance().custom(CustomId.id_290000);
            openRegisterAction();
        } else if (v.getId() == R.id.id_login_title) {
            mCount++;
            if (mCount == DISPLAY_VERSION_TIME) {
                openVersionAction();
                mCount = 0;
            }

        }

    }

    private void openLoginAction() {
        String password = mPasswordEditText.getEditableText().toString();
        String account = mPhoneEditText.getEditableText().toString();
        int type = findTypeByAccount(account);
        String token = findTokenByAccount(account, type);
        AppCacheSharedPreferences.putCacheInteger(SharePreferenceConstant.GAME_CAT_LOGIN_TYPE, type);
        if (!TextUtils.isEmpty(token) && TextUtils.equals(password, mInitPassword)) {
            Log.e("xsy", "openLoginAction ===> autoLogin");
            AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_TOKEN, token);
            autoLogin();
        } else {
            Log.e("xsy", "openLoginAction ===> login");
            login();
        }

    }

    private void openFindPasswordAction() {
        Collect.getInstance().custom(CustomId.id_224000);
        clearAccountAndPassword();
        Fragment fragment = FindPasswordFragmentOne.getInstance();
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment, true);
    }

    private void openRegisterAction() {
        clearAccountAndPassword();
        RegisterFragment fragment = new RegisterFragment();
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment, true);
    }

    private void openVersionAction() {
        VersionFragment fragment = new VersionFragment();
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment, true);
    }

    private void clearAccount() {
        mPhoneEditText.getText().clear();
        mPasswordEditText.getText().clear();
    }


    private void clearAccountAndPassword() {
        mPhoneEditText.setText("");
        mPasswordEditText.setText("");
    }

    private void openOtherLoginAction() {
        Fragment fragment = QQLoginFragment.getInstance();
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment, true);
    }

    private void quickRegister() {
        Fragment fragment = QuickRegisterFragment.getInstance();
        FragmentManagerUtil.getInstance().openFragment(getActivity(), fragment, true);
//        quickRegisterAction();
    }


    private void quickRegisterAction() {
        showWaitingFragment("注册中...");
        UserApi.getInstance().quickRegister(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    try {
                        String code = message.getString("code");
                        switch (code) {
                            case "000":
                                String data = message.getString("data");
                                if (!TextUtils.isEmpty(data)) {
                                    String content = AESUtil.decryptAES(data, Config.getAESKey());
                                    User user = JSON.parseObject(content, User.class);
                                    Collect.getInstance().custom(CustomId.id_281000);
                                    saveLocalUserInformation(user);
                                }
                                FragmentManagerUtil.getInstance().openFragment(getActivity(), AutoLoginDialogFragment.getInstance(QUICK_REGISTER_TYPE));
                                break;
                            default:
                                ToastUtil.makeText(getActivity(), message.getString("message"), false);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                }
            }
        }, mContext);
    }

    private void login() {

        final String password = mPasswordEditText.getText().toString();
        final String account = mPhoneEditText.getText().toString();
        showWaitingFragment("登录中...");
        UserApi.getInstance().normalLogin(account, password, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    try {
                        String code = message.getString("code");
                        switch (code) {
                            case "000":
                                String data = message.getString("data");
                                String content = AESUtil.decryptAES(data, Config.getAESKey());
                                User user = JSON.parseObject(content, User.class);
                                //返回信息去掉userId，防止对接方迷惑采用openId还是userId
                                UserCallback userCallback = JSON.parseObject(content, UserCallback.class);
                                String jsonUser = JSON.toJSONString(userCallback);
                                SDKCallBackUtil.onSuccess(new JSONObject(jsonUser));
                                Collect.getInstance().login(account, user.getOpenId());
                                Collect.getInstance().custom(CustomId.id_293000);
                                saveLocalUserInformation(account, user);
                                getUserInfo();
                                break;
                            case "103":
                                mPhonePrompt.setVisibility(View.VISIBLE);
                                break;
                            case "2002":
                                mPasswordPrompt.setVisibility(View.VISIBLE);
                                break;
                            default:
                                ToastUtil.makeText(getActivity(), message.getString("message"), false);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    SDKCallBackUtil.onFail(message);
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                }
            }
        }, mContext);

    }


    private void saveLocalUserInformation(String account, User user) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT, account);
        saveLocalUserInformation(user);
    }


    private void saveLocalUserInformation(User user) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_TOKEN, user.getToken());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID, user.getOpenId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_USER_ID, user.getUserId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_BINDING_CHANNEL_ID, user.getUserGameBindingChannelId());
    }


    private int findTypeByAccount(String account) {

        int accountType = AccountTypeDao.getInstance().findTypeByAccount(account);
        if (accountType != -1 && accountType != LoginFragment.Mobile_TYPE
                && accountType != LoginFragment.CAT_POINT_TYPE) {
            return accountType;
        }

        if (!TextUtils.isEmpty(account) && account.length() == 11) {
            return LoginFragment.Mobile_TYPE;
        } else {
            return LoginFragment.CAT_POINT_TYPE;
        }
    }


    private void saveUserType(UserInfo userInfo) {
        String account = mPhoneEditText.getText().toString();
        int type = findTypeByAccount(account);
        if (userInfo != null) {
            AccountTypeDao.getInstance().saveAccountType(userInfo, type, Config.getToken(), System.currentTimeMillis());
        }
    }


    @Override
    public void deleteAccount(AccountType account) {
        AccountTypeDao.getInstance().deleteAccount(account.getAccount());
        String accountDeleted;
        if (account.getType() == LoginFragment.Mobile_TYPE) {
            accountDeleted = account.getSafeMobile();
        } else {
            accountDeleted = account.getAccount();
        }

        //账号清空
        String token = Config.getToken();
        String tokenDeleted = account.getToken();
        if (TextUtils.equals(token, tokenDeleted)) {
            AppCacheSharedPreferences.removeByKey(SharePreferenceConstant.GAME_CAT_TOKEN);
            AppCacheSharedPreferences.removeByKey(SharePreferenceConstant.GAME_CAT_ACCOUNT);
            mPhoneEditText.getEditableText().clear();
        }
        String accountLocal = mPhoneEditText.getText().toString();
        if (TextUtils.equals(accountDeleted, accountLocal)) {
            mPhoneEditText.getEditableText().clear();
        }
        //密码清空
        mPasswordEditText.getEditableText().clear();

        ArrayList<AccountType> accountTypes = AccountTypeDao.getInstance().listAccounts();
        setAccountMore(accountTypes);
    }

    @Override
    public void selectAccount(AccountType account) {
        if (account != null) {
            setInputPhoneIcon(account);
            String accountSelected;
            switch (account.getType()) {
                case LoginFragment.Mobile_TYPE:
                    accountSelected = account.getSafeMobile();
                    break;
                default:
                    accountSelected = account.getAccount();
                    break;
            }
            mPhoneEditText.setText(accountSelected);
            mPhoneEditText.setSelection(accountSelected.length());
            String token = account.getToken();
            if (!TextUtils.isEmpty(token)) {
                mPasswordEditText.setText(token);
                mInitPassword = mPasswordEditText.getText().toString();
            }
        }
    }


    public AccountsPopWindow getAccountsPopWindow() {
        return accountsPopWindow;
    }

    private String findTokenByAccount(String account, int type) {
        if (TextUtils.isEmpty(account)) {
            return "";
        }
        ArrayList<AccountType> accountTypes = AccountTypeDao.getInstance().listAccounts();
        if (accountTypes != null && accountTypes.isEmpty()) {
            if (type == LoginFragment.Mobile_TYPE) {

                for (AccountType accountType : accountTypes) {
                    if (accountType != null && accountType.getSafeMobile().equals(account)) {
                        return accountType.getToken();
                    }
                }

            } else {
                return AccountTypeDao.getInstance().findTokenByAccount(account);
            }

        }
        return "";
    }

    private void autoLogin() {

        UserApi.getInstance().autoLogin(new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                if (isAvailableActivity()) {
                    try {
                        String code = message.getString("code");
                        if (code.equals("000")) {
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            User user = JSON.parseObject(content, User.class);
                            String account = mPhoneEditText.getEditableText().toString();
                            AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT, account);
                            saveLocalUserInformation(user);
                            getUserInfo();
                            SDKCallBackUtil.onSuccess(new JSONObject(content));

                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            mPasswordEditText.getEditableText().clear();
                        }
                    } catch (Exception e) {
                        if (isAvailableActivity()) {
                            ToastUtil.makeText(getActivity(), "返回数据不正确", false);
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                    SDKCallBackUtil.onFail(message);
                }
            }
        }, mContext);
    }


    private void getUserInfo() {
        UserApi.getInstance().getUserInfoByToken(new GameCatSDKListener() {

            @Override
            public void onSuccess(JSONObject message) {
                try {
                    if (isAvailableActivity()) {
                        String code = message.getString("code");
                        if (code.equals("000")) {
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            UserInfo userInfo = JSON.parseObject(content, UserInfo.class);
                            saveUserType(userInfo);
                            ToastUtil.makeText(getActivity(), "登录成功", true);
                            close(LoginFragment.this);
                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        }
                    }

                } catch (Exception e) {
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "返回数据不正确", false);
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                }
            }
        }, mContext);
    }
}
