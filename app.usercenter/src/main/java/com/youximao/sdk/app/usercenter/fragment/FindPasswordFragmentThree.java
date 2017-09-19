package com.youximao.sdk.app.usercenter.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.constant.SharePreferenceConstant;
import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.SimpleTextWatcher;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/10/22.
 */

public class FindPasswordFragmentThree extends BaseFragment implements View.OnClickListener {
    public static final String GAME_CAT_PASSWORD = "password";
    private String mPhone;
    private String mVCode;
    private EditText mPasswordEditText;
    private TextView mPasswordWrongTip;
    private EditText mPasswordAgainEditText;
    private TextView mPasswordAgainWrongTip;
    private Button mConfirmButton;
    private String mNewPassword;
    private String mNewPasswordAgain;
    private final TextWatcher mTextEditWatcher = new SimpleTextWatcher() {
        String regex = "[0-9a-zA-Z]{6,18}";

        @Override
        public void afterTextChanged(Editable s) {
            if (!isAvailableActivity()) {
                return;
            }

            mNewPassword = mPasswordEditText.getEditableText().toString();
            mNewPasswordAgain = mPasswordAgainEditText.getEditableText().toString();
            if (mNewPassword.length() > 5 && mNewPasswordAgain.length() > 5) {
                if (mNewPassword.matches(regex)) {
                    mPasswordWrongTip.setVisibility(View.GONE);
                    mConfirmButton.setEnabled(true);
                } else {
                    mPasswordWrongTip.setVisibility(View.VISIBLE);
                    mPasswordEditText.getEditableText().clear();
                    mConfirmButton.setEnabled(false);
                }
            } else {
                mConfirmButton.setEnabled(false);
            }

        }
    };

    public static FindPasswordFragmentThree getInstance(String phone, String vcode) {
        FindPasswordFragmentThree fragment = new FindPasswordFragmentThree();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        args.putString("vcode", vcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mPhone = args.getString("phone");
            mVCode = args.getString("vcode");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_find_password_input_password;
    }

    @Override
    public void init(View view) {
        mPasswordEditText = (EditText) view.findViewById(R.id.id_edit_text_input_password);
        mPasswordEditText.addTextChangedListener(mTextEditWatcher);
        mPasswordWrongTip = (TextView) view.findViewById(R.id.id_text_view_password_tip);
        mPasswordWrongTip.setVisibility(View.GONE);

        mPasswordAgainEditText = (EditText) view.findViewById(R.id.id_edit_text_input_password_again);
        mPasswordAgainEditText.addTextChangedListener(mTextEditWatcher);
        mPasswordAgainWrongTip = (TextView) view.findViewById(R.id.id_text_view_input_password_again);
        mPasswordAgainWrongTip.setVisibility(View.GONE);


        mConfirmButton = (Button) view.findViewById(R.id.id_confirm_button_find_password);
        mConfirmButton.setOnClickListener(this);
        mConfirmButton.setEnabled(false);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_confirm_button_find_password) {
            resetPassword();
        }
    }

    private void resetPassword() {
        if (!TextUtils.equals(mNewPassword, mNewPasswordAgain)) {
            mPasswordAgainWrongTip.setVisibility(View.VISIBLE);
            return;
        }
        mPasswordAgainWrongTip.setVisibility(View.GONE);

        final String password = mPasswordEditText.getText().toString();
        showWaitingFragment("密码重置中...");
        mConfirmButton.setEnabled(false);
        UserApi.getInstance().resetPassword(mPhone, mVCode, password, password, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                if (isAvailableActivity()) {
                    closeWaitingFragment();
                    mConfirmButton.setEnabled(true);
                    try {
                        String code = message.getString("code");
                        if (code.equals("000")) {
                            saveLocalUserInformation(mPhone);
                            ToastUtil.makeText(getActivity(), "恭喜，重置成功！", true);
                            close(FindPasswordFragmentThree.this);
                        } else if (code.equals("02303")) {
                            ToastUtil.makeText(getActivity(), "密码必须大于5位", false);
                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        }
                    } catch (JSONException e) {
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
        },mContext);
    }

    @Override
    public void close(Fragment fragment) {
        super.close(fragment);
        Fragment loginFragment = LoginFragment.getInstance();
        loginFragment.getArguments().putString(GAME_CAT_PASSWORD, mNewPassword);
        FragmentManagerUtil.getInstance().openFragment(getActivity(), loginFragment);
        FragmentManagerUtil.getInstance().closeFragment(getActivity(), fragment, false);
    }


    private void saveLocalUserInformation(String account) {
        AppCacheSharedPreferences.putCacheString(SharePreferenceConstant.GAME_CAT_ACCOUNT, account);
    }
}
