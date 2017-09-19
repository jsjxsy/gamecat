package com.youximao.sdk.app.pay.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.youximao.sdk.lib.common.base.AdapterParentBase;
import com.youximao.sdk.app.pay.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by davy on 16/7/26.
 */
public class VoucherAdapter extends AdapterParentBase<String> implements AdapterView.OnItemClickListener {
    private int mSelectedItem;

    public VoucherAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ItemViewHolder itemViewHolder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.layout.gamecat_pay_order_dialog_list_item, parent, false);

            itemViewHolder = new ItemViewHolder();
            itemViewHolder.mCheckBox = (RadioButton) convertView.findViewById(R.id.checkbox);
            itemViewHolder.mVouchersAmount = (TextView) convertView.findViewById(R.id.vouchers_amount);
            itemViewHolder.mBeforeDate = (TextView) convertView.findViewById(R.id.before_date);
            convertView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }
        String jsonData = getItem(position);
        if (!TextUtils.isEmpty(jsonData)) {
            if (jsonData.equals("不使用代金券")) {
                itemViewHolder.mVouchersAmount.setText(jsonData);
                itemViewHolder.mBeforeDate.setVisibility(View.GONE);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(jsonData);
                    itemViewHolder.mBeforeDate.setVisibility(View.VISIBLE);
                    itemViewHolder.mVouchersAmount.setText("•  " + jsonObject.getString("cc_pname"));
                    itemViewHolder.mBeforeDate.setText("•  有效期至:" + jsonObject.getString("end_date"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if (position == mSelectedItem) {
                itemViewHolder.mCheckBox.setChecked(true);
            } else {
                itemViewHolder.mCheckBox.setChecked(false);
            }

        }

        return convertView;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mSelectedItem = position;
        notifyDataSetChanged();
    }

    public int getSelectedItem() {
        return this.mSelectedItem;
    }

    public static class ItemViewHolder {
        public RadioButton mCheckBox;
        public TextView mVouchersAmount;
        public TextView mBeforeDate;
    }

}
