package com.youximao.sdk.app.personal.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.youximao.sdk.app.personal.R;
import com.youximao.sdk.app.personal.model.GiftPackage;
import com.youximao.sdk.lib.common.base.AdapterParentBase;

/**
 * Created by zhan on 17-3-20.
 *
 * 礼包列表
 */
public class GiftPackageAdapter extends AdapterParentBase<GiftPackage> {
    public GiftPackageAdapter(Context ctx) {
        super(ctx);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            convertView = getLayoutInflater().inflate(R.layout.gamecat_gift_package_item_layout,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    static class ViewHolder {
        TextView mGiftName;
        TextView mLevel;
        TextView mContent;
        Button mBtnReceive;
        public ViewHolder(View view) {
            mGiftName = (TextView) view.findViewById(R.id.tv_gift_name);
            mLevel = (TextView) view.findViewById(R.id.tv_level);
            mContent = (TextView) view.findViewById(R.id.tv_content);
            mBtnReceive = (Button) view.findViewById(R.id.btn_receive);
        }
    }
}
