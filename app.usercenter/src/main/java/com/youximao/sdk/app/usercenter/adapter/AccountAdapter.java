package com.youximao.sdk.app.usercenter.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.app.usercenter.R;
import com.youximao.sdk.app.usercenter.callback.SelectAccountListener;
import com.youximao.sdk.app.usercenter.fragment.DeleteDialogFragment;
import com.youximao.sdk.app.usercenter.fragment.LoginFragment;
import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.lib.common.alianalytics.Collect;
import com.youximao.sdk.lib.common.alianalytics.constant.CustomId;
import com.youximao.sdk.lib.common.base.AdapterParentBase;
import com.youximao.sdk.lib.common.common.util.FragmentManagerUtil;

/**
 * Created by admin on 2016/10/24.
 */

public class AccountAdapter extends AdapterParentBase<AccountType> implements AdapterView.OnItemClickListener {
    private LoginFragment mFragment;
    private SelectAccountListener mListener;

    public AccountAdapter(Context ctx, LoginFragment fragment) {
        super(ctx);
        this.mFragment = fragment;
    }

    public void setSelectAccountListener(SelectAccountListener l) {
        this.mListener = l;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.gamecat_account_item;
            convertView = getLayoutInflater().inflate(layoutId, parent, false);
            holder = new ItemViewHolder();
            holder.mItemTitle = (TextView) convertView.findViewById(R.id.id_text_view_account);
            holder.mItemGameName = (TextView) convertView.findViewById(R.id.id_text_view_game_name);
            holder.mItemClear = (ImageView) convertView.findViewById(R.id.id_image_view_clear_account);
            holder.mItemType = (ImageView) convertView.findViewById(R.id.id_image_view_account_type);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        final AccountType account = getItem(position);
        if (account != null) {

            switch (account.getType()) {
                case LoginFragment.QQ_TYPE:
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_qq);
                    break;
                case LoginFragment.QUICK_REGISTER_TYPE:
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_one_key_login);
                    break;
                case LoginFragment.Mobile_TYPE:
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_mobile);
                    break;
                case LoginFragment.CAT_POINT_TYPE:
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_cat_point);
                    break;
                default:
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_cat_point);
                    break;
            }

            String userName = account.getUserName();
            String catAccount = account.getAccount();
            String mobile = account.getSafeMobile();
            switch (account.getType()) {
                case LoginFragment.QQ_TYPE:
                    if (!TextUtils.isEmpty(userName)) {
                        holder.mItemTitle.setText(userName);
                    } else {
                        holder.mItemTitle.setText(account.getAccount());
                    }
                    holder.mItemType.setImageResource(R.drawable.game_cat_account_qq);
                    break;
                case LoginFragment.CAT_POINT_TYPE:
                    holder.mItemTitle.setText(catAccount);
                    break;
                case LoginFragment.Mobile_TYPE:
                case LoginFragment.QUICK_REGISTER_TYPE:
                default:
                    if (!TextUtils.isEmpty(mobile)) {
                        holder.mItemTitle.setText(mobile);
                    } else {
                        holder.mItemTitle.setText(catAccount);
                    }
                    break;
            }

            holder.mItemClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Collect.getInstance().custom(CustomId.id_270000);
                    DeleteDialogFragment deleteDialog = DeleteDialogFragment.getInstance(account);
                    if (mFragment.getAccountsPopWindow() != null) {
                        mFragment.getAccountsPopWindow().dismiss();
                    }
                    deleteDialog.setDeleteDialogListener(mFragment);
                    FragmentManagerUtil.getInstance().openDialogFragment((Activity) getContext(), deleteDialog);
                }
            });
        }

        return convertView;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, View view, int position, long id) {
        AccountType userInfo = (AccountType) parent.getItemAtPosition(position);
        if (mListener != null) {
            if (mFragment.getAccountsPopWindow() != null) {
                mFragment.getAccountsPopWindow().dismiss();
            }
            mListener.selectAccount(userInfo);
        }
    }

    public static class ItemViewHolder {
        public TextView mItemGameName;
        public ImageView mItemType;
        public TextView mItemTitle;
        public ImageView mItemClear;
    }
}
