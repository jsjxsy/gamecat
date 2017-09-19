package com.youximao.sdk.app.usercenter.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.SimpleTextWatcher;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2016/10/22.
 */

public class FindPasswordFragmentTwo extends BaseFragment implements View.OnClickListener {

    private String mPhone;

    private EditText mVerificationEditText;
    private TextView mVerificationWrongTip;
    private TextView mGetVerificationCode;
    private Button mConfirmButton;
    private String mVCode;
    private final TextWatcher mTextEditWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (!isAvailableActivity()) {
                return;
            }
            mVCode = mVerificationEditText.getEditableText().toString();
            if (mVCode.length() == 4) {
                mConfirmButton.setEnabled(true);
            } else {
                mVerificationWrongTip.setVisibility(View.GONE);
            }
        }
    };
    private TextView mPhoneTextView;
    private Task mTask;

    public static FindPasswordFragmentTwo getInstance(String phone) {
        FindPasswordFragmentTwo fragment = new FindPasswordFragmentTwo();
        Bundle args = new Bundle();
        args.putString("account", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mPhone = args.getString("account");
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_find_password_verification_code;
    }

    public void init(View view) {
        mPhoneTextView = (TextView) view.findViewById(R.id.id_send_verification_code);

        mVerificationEditText = (EditText) view.findViewById(R.id.id_edit_text_verification_code_phone_register);
        mVerificationEditText.addTextChangedListener(mTextEditWatcher);
        mVerificationWrongTip = (TextView) view.findViewById(R.id.id_text_view_verification_code_phone_register);
        mVerificationWrongTip.setVisibility(View.GONE);
        mGetVerificationCode = (TextView) view.findViewById(R.id.id_text_view_verification_code_time_phone_register);
        mGetVerificationCode.setOnClickListener(this);
        mConfirmButton = (Button) view.findViewById(R.id.ic_confirm_button);
        mConfirmButton.setOnClickListener(this);
        mConfirmButton.setEnabled(false);

        getVerificationCode();
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
    public void onClick(View v) {
        if (v.getId() == R.id.ic_confirm_button) {
            Collect.getInstance().custom(CustomId.id_228000);
            validMobileCode();
        } else if (v.getId() == R.id.id_text_view_verification_code_time_phone_register) {
            getVerificationCode();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mTask != null) {
            mGetVerificationCode.removeCallbacks(mTask);
            mGetVerificationCode.setEnabled(true);
            mTask = null;
        }
    }

    /**
     * 设置校验码是否处于激活状态
     *
     * @param isAction
     */
    private void setCheckCodeEnabled(boolean isAction) {
        if (isAction) {
            mGetVerificationCode.setText("重新获取");
            if (mPhone.length() == 11) {
                mGetVerificationCode.setEnabled(true);
                mVerificationWrongTip.setVisibility(View.GONE);
            }
        } else {
            mGetVerificationCode.setEnabled(false);
        }
    }

    private void getVerificationCode() {
        if (mGetVerificationCode.getText().toString().equals("重新获取")) {
            Collect.getInstance().custom(CustomId.id_227000);
        } else {
            Collect.getInstance().custom(CustomId.id_226000);
        }

        mGetVerificationCode.setEnabled(false);
        UserApi.getInstance().getVerificationCode(String.valueOf(2), mPhone, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    if (isAvailableActivity()) {
                        closeWaitingFragment();
                        String code = message.getString("code");
                        switch (code) {
                            case "000":
                                mPhoneTextView.setVisibility(View.VISIBLE);
                                //157****5001
                                String phonePrefix = mPhone.substring(0, 3);
                                String phoneSuffix = mPhone.substring(7, 11);
                                String phoneContent = String.format("已发送验证码到%s", phonePrefix + "****" + phoneSuffix);
                                mPhoneTextView.setText(phoneContent);
                                Countdown(60);
                                break;
                            case "2302":
                                mVerificationWrongTip.setText("*获取失败，已超获取次数");
                                mVerificationWrongTip.setVisibility(View.VISIBLE);
                                mGetVerificationCode.setEnabled(true);
                                break;
                            case "2303":
                                mVerificationWrongTip.setText("*获取失败，手机已注册");
                                mVerificationWrongTip.setVisibility(View.VISIBLE);
                                mGetVerificationCode.setEnabled(true);
                                break;
                            default:
                                ToastUtil.makeText(getActivity(), message.getString("message"), false);
                                mGetVerificationCode.setEnabled(true);
                                break;
                        }
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
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                }
            }
        },mContext);

    }

    private void validMobileCode() {
        showWaitingFragment("检查验证码...");
        UserApi.getInstance().validMobileCode(mPhone, mVCode, new GameCatSDKListener() {

            @Override
            public void onSuccess(JSONObject message) {

                try {
                    closeWaitingFragment();
                    String code = message.getString("code");
                    switch (code) {
                        case "000":
                            Collect.getInstance().custom(CustomId.id_229000);
                            FragmentManagerUtil.getInstance().openFragment(getActivity(), FindPasswordFragmentThree.getInstance(mPhone, mVCode), true);
                            break;
                        case "2305":
                            mVerificationWrongTip.setText("*验证码不正确");
                            mVerificationWrongTip.setVisibility(View.VISIBLE);
                            break;
                        default:
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.makeText(getActivity(), "数据格式有误", false);
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
