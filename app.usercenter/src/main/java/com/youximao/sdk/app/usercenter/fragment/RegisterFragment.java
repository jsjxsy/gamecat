package com.youximao.sdk.app.usercenter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.model.User;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.CheckPhone;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.AES.CryptoException;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.SimpleTextWatcher;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 注册界面
 * Created by davy on 16/6/15.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener {
    Bundle args;
    String gameId = "";
    Fragment registerFragment;
    EditText mRegisterPhoneEditText;
    EditText mVerificationEditText;
    EditText mPasswordEditText;
    TextView mGetVerificationCode;
    TextView mVerificationWrongTip;
    TextView mPhoneWrongTip;
    Button mLoginButton;
    TextView mPasswordTip;
    String mPhone = "";
    String mPassword = "";
    String mVCode = "";
    private boolean stopCountdown = true;
    private ImageView mClear;
    //手机号码输入监听
    private final TextWatcher mTextWatcher = new SimpleTextWatcher() {
        String regex = "[0-9a-zA-Z]{6,18}";

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            mPhone = mRegisterPhoneEditText.getEditableText().toString();
            mPassword = mPasswordEditText.getEditableText().toString();
            mVCode = mVerificationEditText.getEditableText().toString();
            if (mPhone.length() == 11 && stopCountdown) {
                mGetVerificationCode.setEnabled(true);
                mVerificationWrongTip.setVisibility(View.GONE);
            } else {
                mGetVerificationCode.setEnabled(false);
            }


            if (mPhone.length() == 11 && mPassword.length() > 5 && mVCode.length() == 4) {
                if (mPassword.matches(regex)) {
                    mPasswordTip.setVisibility(View.GONE);
                    mLoginButton.setEnabled(true);
                } else {
                    mPasswordTip.setVisibility(View.VISIBLE);
                    mPasswordEditText.getEditableText().clear();
                    mLoginButton.setEnabled(false);
                }
            } else {
                mLoginButton.setEnabled(false);
            }

            if (mPhone.length() > 0) {
                mClear.setVisibility(View.VISIBLE);
            } else {
                mClear.setVisibility(View.GONE);
            }
        }
    };
    private Task mTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        registerFragment = this;
        args = getArguments();
        if (args != null) {
            gameId = args.getString("gameId");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_phone_register;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTask != null) {
            mGetVerificationCode.removeCallbacks(mTask);
            mTask = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //倒计时关闭
        setCheckCodeEnabled(true);
    }

    public void init(View view) {
        mRegisterPhoneEditText = (EditText) view.findViewById(R.id.id_edit_text_input_phone);
        mRegisterPhoneEditText.addTextChangedListener(mTextWatcher);
        mPhoneWrongTip = (TextView) view.findViewById(R.id.id_text_view_wrong_phone);
        mPhoneWrongTip.setVisibility(View.GONE);
        mClear = (ImageView) view.findViewById(R.id.id_image_view_clear);
        mClear.setOnClickListener(this);
        mClear.setVisibility(View.GONE);

        mVerificationEditText = (EditText) view.findViewById(R.id.id_edit_text_verification_code_phone_register);
        mVerificationEditText.addTextChangedListener(mTextWatcher);
        mVerificationWrongTip = (TextView) view.findViewById(R.id.id_text_view_verification_code_phone_register);
        mVerificationWrongTip.setVisibility(View.GONE);
        mGetVerificationCode = (TextView) view.findViewById(R.id.id_text_view_get_verification_phone_register);
        mGetVerificationCode.setOnClickListener(this);
        mGetVerificationCode.setEnabled(false);

        mPasswordEditText = (EditText) view.findViewById(R.id.id_password_edit_phone_register);
        mPasswordEditText.addTextChangedListener(mTextWatcher);
        mPasswordTip = (TextView) view.findViewById(R.id.id_text_view_password_tip);
        mPasswordTip.setVisibility(View.GONE);

        mLoginButton = (Button) view.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(this);
        mLoginButton.setEnabled(false);
    }

    /**
     * 设置校验码是否处于激活状态
     *
     * @param isAction
     */
    private void setCheckCodeEnabled(boolean isAction) {
        if (isAction) {
            stopCountdown = true;
            mGetVerificationCode.setText("重新获取");
            if (mPhone.length() == 11) {
                mGetVerificationCode.setEnabled(true);
                mVerificationWrongTip.setVisibility(View.GONE);
            }
        } else {
            stopCountdown = false;
            mGetVerificationCode.setEnabled(false);
        }
    }

    /**
     * 倒计时
     *
     * @param icount
     */
    private void Countdown(final int icount) {
        mGetVerificationCode.setEnabled(false);
        // 倒计时
        mTask = new Task(icount);
        mGetVerificationCode.postDelayed(new Task(icount), 1000);
    }

    @Override
    public void close(Fragment fragment) {
        super.close(fragment);
        FragmentManagerUtil.getInstance().openFragment(getActivity(), AutoLoginDialogFragment.getInstance(LoginFragment.Mobile_TYPE));
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), fragment, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_button) {
            registerByPhone();
        } else if (v.getId() == R.id.id_text_view_get_verification_phone_register) {
            if (CheckPhone.isPhone(mPhone)) {
                getVerificationCode();
            } else {
                ToastUtil.makeText(getActivity(), "请输入正确的手机号", false);
            }

        } else if (v.getId() == R.id.id_image_view_clear) {
            mRegisterPhoneEditText.getEditableText().clear();
        }
    }

    private void registerByPhone() {
        mLoginButton.setEnabled(false);
        showWaitingFragment("注册中...");
        UserApi.getInstance().normalRegister(mPhone, mPassword, mVCode, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                closeWaitingFragment();
                mLoginButton.setEnabled(true);
                try {
                    String code = message.getString("code");
                    switch (code) {
                        case "000":
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            User user = JSON.parseObject(content, User.class);
                            saveLocalUserInformation(mPhone, user);
                            Collect.getInstance().register(user.getOpenId(), mPhone);
                            Collect.getInstance().custom(CustomId.id_294000);
                            close(registerFragment);
                            break;
                        case "2101":
                            mPhoneWrongTip.setText("手机已注册");
                            mPhoneWrongTip.setVisibility(View.VISIBLE);
                            break;
                        case "2102":
                            mVerificationWrongTip.setText("验证码验证失败");
                            mVerificationWrongTip.setVisibility(View.VISIBLE);
                            break;
                        case "02102":
                            mVerificationWrongTip.setText("验证码不能为空");
                            mVerificationWrongTip.setVisibility(View.VISIBLE);
                            break;
                        case "02103":
                            ToastUtil.makeText(getActivity(), "密码必须大于6位", false);
                            break;
                        default:
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (CryptoException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String message) {
                if (mRegisterPhoneEditText.getText().length() == 11) {
                    mLoginButton.setEnabled(true);
                }
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    ToastUtil.makeText(getActivity(), "网络有误", true);
                }
            }
        },mContext);
    }

    private void saveLocalUserInformation(String account, User user) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT, account);
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_TOKEN, user.getToken());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_OPEN_ID, user.getOpenId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_USER_ID, user.getUserId());
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_BINDING_CHANNEL_ID, user.getUserGameBindingChannelId());
    }

    private void getVerificationCode() {
        if (mGetVerificationCode.getText().toString().equals("重新获取")) {
            Collect.getInstance().custom(CustomId.id_292000);
        } else {
            Collect.getInstance().custom(CustomId.id_291000);
        }

        showWaitingFragment("获取验证码中...");
        UserApi.getInstance().getVerificationCode(String.valueOf(1), mPhone, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    closeWaitingFragment();
                    String code = message.getString("code");
                    switch (code) {
                        case "000":
                            mGetVerificationCode.setEnabled(false);
                            mGetVerificationCode.setText("60s");
                            Countdown(60);
                            break;
                        case "2302":
                            mVerificationWrongTip.setText("*获取失败，已超获取次数");
                            mVerificationWrongTip.setVisibility(View.VISIBLE);
                            break;
                        case "2303":
                            mVerificationWrongTip.setText("*手机号已注册游戏猫");
                            mVerificationWrongTip.setVisibility(View.VISIBLE);
                            break;
                        default:
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            break;
                    }
                } catch (JSONException e) {
                    if (isAvailableActivity()) {
                        ToastUtil.makeText(getActivity(), "数据格式有误", false);
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
        },mContext);
    }

    class Task implements Runnable {
        private int icount;

        public Task(int icount) {
            this.icount = icount;
        }

        @Override
        public void run() {
            if (icount > 0) {
                mGetVerificationCode.setText(icount + "s");
                Countdown(icount - 1);
            } else {
                setCheckCodeEnabled(true);
            }
        }
    }
}
