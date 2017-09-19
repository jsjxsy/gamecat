package com.youximao.sdk.app.usercenter.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.service.UserApi;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.CheckPhone;
import com.youximao.sdk.lib.common.common.config.Config;
import com.youximao.sdk.lib.common.common.util.AES.AESUtil;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;
import com.youximao.sdk.lib.common.common.widget.SimpleTextWatcher;
import com.youximao.sdk.lib.common.common.widget.ToastUtil;
import com.youximao.sdk.lib.common.sdk.GameCatSDKListener;

import org.json.JSONObject;

/**
 * Created by admin on 2016/10/22.
 */

public class FindPasswordFragmentOne extends BaseFragment implements OnClickListener {
    private EditText mPhoneEditText;
    private TextView mPhoneWrongTip;
    private String mAccount;
    private Button mNextButton;
    private ImageView mClear;
    //手机号码输入监听
    private final TextWatcher mPhoneEditWatcher = new SimpleTextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            if (!isAvailableActivity()) {
                return;
            }
            if (s.length() > 0) {
                mClear.setVisibility(View.VISIBLE);
            } else {
                mClear.setVisibility(View.GONE);
            }
            checkIfNextStep();

        }
    };

    public static FindPasswordFragmentOne getInstance() {
        FindPasswordFragmentOne fragment = new FindPasswordFragmentOne();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_find_password_input_account;
    }

    public void init(View view) {
        mPhoneEditText = (EditText) view.findViewById(R.id.id_edit_text_input_phone);
        mPhoneEditText.addTextChangedListener(mPhoneEditWatcher);
        mPhoneWrongTip = (TextView) view.findViewById(R.id.id_text_view_wrong_phone);
        mPhoneWrongTip.setVisibility(View.GONE);

        mClear = (ImageView) view.findViewById(R.id.id_image_view_clear);
        mClear.setOnClickListener(this);
        mClear.setVisibility(View.GONE);

        mNextButton = (Button) view.findViewById(R.id.id_confirm_button);
        mNextButton.setOnClickListener(this);
        mNextButton.setEnabled(false);
    }


    private void checkIfNextStep() {
        mAccount = mPhoneEditText.getEditableText().toString();
        if (mAccount.length() < 6) {
            mNextButton.setEnabled(false);
        } else {
            mNextButton.setEnabled(true);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_confirm_button) {
            Collect.getInstance().custom(CustomId.id_225000);
            if (CheckPhone.isPhone(mAccount)){
                validMobile();
            }else {
                ToastUtil.makeText(getActivity(), "手机号不合法", false);
            }
        } else if (v.getId() == R.id.id_image_view_clear) {
            mPhoneEditText.getEditableText().clear();
        }

    }

    private void validMobile() {
        mNextButton.setEnabled(false);
        UserApi.getInstance().validMobile(mAccount, new GameCatSDKListener() {
            @Override
            public void onSuccess(JSONObject message) {
                try {
                    closeWaitingFragment();
                    if (isAvailableActivity()) {
                        mNextButton.setEnabled(true);
                        String code = message.getString("code");
                        if (code.equals("000")) {
                            String data = message.getString("data");
                            String content = AESUtil.decryptAES(data, Config.getAESKey());
                            String mobile = new JSONObject(content).getString("mobile");
                            if (TextUtils.isEmpty(mobile)) {
                                ToastUtil.makeText(getActivity(), "喵号未绑定手机号！", false);
                            } else {
                                FragmentManagerUtil.getInstance().openFragment(getActivity(), FindPasswordFragmentTwo.getInstance(mobile), true);
                            }
                        } else {
                            ToastUtil.makeText(getActivity(), message.getString("message"), false);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    if (isAvailableActivity()) {
                        mNextButton.setEnabled(true);
                        ToastUtil.makeText(getActivity(), "数据解析错误", false);
                    }
                }

            }

            @Override
            public void onFail(String message) {
                if (isAvailableActivity()) {
                    mNextButton.setEnabled(true);
                    closeWaitingFragment();
                    ToastUtil.makeText(getActivity(), "网络有误", false);
                }
            }
        },mContext);
    }

}
