package com.youximao.sdk.app.usercenter.dialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.adapter.AccountAdapter;
import com.youximao.sdk.app.usercenter.database.AccountTypeDao;
import com.youximao.sdk.app.usercenter.fragment.LoginFragment;
import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.lib.common.base.BasePopupWindow;

import java.util.ArrayList;

/**
 * Created by admin on 2017/3/28.
 */

public class AccountsPopWindow extends BasePopupWindow {
    private ListView mAccountList;
    private AccountAdapter mAccountAdapter;
    private ArrayList<AccountType> mAccountArrayList = new ArrayList<>();

    public AccountsPopWindow(Context context, LoginFragment loginFragment) {
        super(context);
        View rootView = LayoutInflater.from(context).inflate(R.layout.gamecat_accounts, null);
        setContentView(rootView);
        setPopupWindowHeightAndWidth();
        ArrayList<AccountType> accountTypes = AccountTypeDao.getInstance().listAccounts();
        Log.e("xsy", "accounts" + accountTypes);
        if (accountTypes != null && !accountTypes.isEmpty()) {
            mAccountArrayList.clear();
            mAccountArrayList.addAll(accountTypes);
        }
        mAccountList = (ListView) rootView.findViewById(R.id.id_list_view_switch_account);
        mAccountAdapter = new AccountAdapter(context, loginFragment);
        mAccountList.setAdapter(mAccountAdapter);
        mAccountAdapter.setArrayList(mAccountArrayList);
        mAccountList.setOnItemClickListener(mAccountAdapter);
    }

    /**
     * 初始化BasePopupWindow的一些信息
     */
    private void setPopupWindowHeightAndWidth() {
        int width = getContext().getResources().getDimensionPixelOffset(R.dimen.list_view_width);
        setWidth(width);
        update();
    }


    public AccountAdapter getAccountAdapter() {
        return this.mAccountAdapter;
    }

}
