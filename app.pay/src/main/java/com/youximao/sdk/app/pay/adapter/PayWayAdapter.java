package com.youximao.sdk.app.pay.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.youximao.sdk.app.pay.R;
import com.youximao.sdk.app.pay.model.PayWay;
import com.youximao.sdk.lib.common.base.AdapterParentBase;

/**
 * Created by admin on 16/9/30.
 */

public class PayWayAdapter extends AdapterParentBase<PayWay> {

    public PayWayAdapter(Context ctx) {
        super(ctx);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemViewHolder holder;
        if (convertView == null) {
            int layoutId = R.layout.gamecat_pay_confirm_pay_way_item;
            convertView = getLayoutInflater().inflate(layoutId, parent, false);
            holder = new ItemViewHolder();

            holder.mItemSelectedIcon = (ImageView) convertView.findViewById(R.id.id_image_view_selected_pay_item);
            holder.mItemPayIcon = (ImageView) convertView.findViewById(R.id.id_image_view_pay_item);
            convertView.setTag(holder);
        } else {
            holder = (ItemViewHolder) convertView.getTag();
        }

        PayWay payWay = getItem(position);
        if (payWay != null) {
            if (payWay.selected) {
                holder.mItemSelectedIcon.setVisibility(View.VISIBLE);
            } else {
                holder.mItemSelectedIcon.setVisibility(View.GONE);
            }
            if (payWay.iconResId != -1) {
                int resId = payWay.iconResId;
                Log.e("xsy", "resId" + resId + " icon " + payWay.iconResId);
                holder.mItemPayIcon.setImageResource(resId);
            }
        }

        return convertView;
    }


    @Override
    public boolean isEnabled(int position) {
        if (mArrayList != null && !mArrayList.isEmpty()) {
            return mArrayList.get(position).clickEnable;
        }
        return super.isEnabled(position);

    }

    public static class ItemViewHolder {

        public ImageView mItemSelectedIcon;
        public ImageView mItemPayIcon;
    }

}
