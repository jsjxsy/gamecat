package com.youximao.sdk.app.usercenter.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.callback.DeleteDialogListener;
import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.BaseFragment;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;

/**
 * Created by admin on 2016/10/27.
 */

public class DeleteDialogFragment extends BaseFragment implements View.OnClickListener {

    private static final String USER_INFO = "userInfo";
    private AccountType mUserInfo;
    private TextView mAccount;
    private DeleteDialogListener mDeleteDialogListener;

    public static DeleteDialogFragment getInstance(AccountType userInfo) {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(USER_INFO, userInfo);
        fragment.setArguments(args);
        return fragment;
    }

    public void setDeleteDialogListener(DeleteDialogListener l) {
        this.mDeleteDialogListener = l;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mUserInfo = (AccountType) args.getSerializable(USER_INFO);
        }
    }

    @Override
    public void init(View view) {
        view.findViewById(R.id.id_text_view_cancel_button).setOnClickListener(this);
        view.findViewById(R.id.id_text_view_confirm_button).setOnClickListener(this);
        mAccount = (TextView) view.findViewById(R.id.id_text_view_account_delete);
        if (mUserInfo != null) {
            String userName = mUserInfo.getUserName();
            if (!TextUtils.isEmpty(userName)) {
                mAccount.setText(userName);
            } else {
                if (mUserInfo.getType() == LoginFragment.Mobile_TYPE) {
                    mAccount.setText(mUserInfo.getSafeMobile());
                } else {
                    mAccount.setText(mUserInfo.getAccount());
                }

            }
        }

    }

    @Override
    public int getLocalLayoutId() {
        return R.layout.gamecat_delete_account_dialog;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_text_view_cancel_button) {
            FragmentManagerUtil.getInstance().closeDialogFragment(getActivity(), DeleteDialogFragment.this);
        }
        if (v.getId() == R.id.id_text_view_confirm_button) {
            if (mDeleteDialogListener != null) {
                Collect.getInstance().custom(CustomId.id_271000);
                Log.e("CustomId", "id_271000");
                mDeleteDialogListener.deleteAccount(mUserInfo);
            }
            FragmentManagerUtil.getInstance().closeDialogFragment(getActivity(), DeleteDialogFragment.this);

        }

    }
}
