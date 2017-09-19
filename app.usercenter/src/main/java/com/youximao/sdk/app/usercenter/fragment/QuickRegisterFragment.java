package com.youximao.sdk.app.usercenter.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.widget.togglebutton.ToggleButton;

/**
 * Created by admin on 2017/3/16.
 */

public class QuickRegisterFragment extends BaseFragment {
    private View mPasswordEditText;
    private ToggleButton mSwitchQuickRegister;
    private View mConfirmButton;
    private LinearLayout mQuickRegisterProtocolLayout;

    public static QuickRegisterFragment getInstance() {
        QuickRegisterFragment fragment = new QuickRegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void init(View view) {
        mPasswordEditText = view.findViewById(R.id.id_edit_text_quick_register);
        mSwitchQuickRegister = (ToggleButton) view.findViewById(R.id.id_switch_quick_register);
        mConfirmButton = view.findViewById(R.id.ic_confirm_button);
        mConfirmButton.setOnClickListener(this);
        mQuickRegisterProtocolLayout = (LinearLayout) view.findViewById(R.id.id_quick_register_protocol_layout);
        mQuickRegisterProtocolLayout.setOnClickListener(this);
    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_quick_register;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ic_confirm_button:
                break;
            case R.id.id_quick_register_protocol_layout:
                break;
        }
    }
}
