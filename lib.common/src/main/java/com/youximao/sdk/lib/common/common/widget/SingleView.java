package com.youximao.sdk.lib.common.common.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youximao.sdk.lib.common.common.MResource;

import org.json.JSONException;
import org.json.JSONObject;

public class SingleView extends LinearLayout implements Checkable {

    private TextView vouchers_amount;
    private TextView conditions;
    private TextView before_date;
    private CheckBox mCheckBox;
    private Context mContext;

    public SingleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initView(context);
    }

    public SingleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView(context);
    }

    public SingleView(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    private void initView(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(getLayoutId("gamecat_pay_order_dialog_list_item"), this, true);

        mCheckBox = (CheckBox) view.findViewById(getIdId("checkbox"));
        vouchers_amount = (TextView) view.findViewById(getIdId("vouchers_amount"));
        conditions = (TextView) view.findViewById(getIdId("conditions"));
        before_date = (TextView) view.findViewById(getIdId("before_date"));
    }

    @Override
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);

    }

    @Override
    public void toggle() {
        mCheckBox.toggle();
    }

    public void setData(String jsonData) {
        if (jsonData.equals("不使用代金券")) {
            vouchers_amount.setText(jsonData);
            conditions.setVisibility(View.GONE);
            before_date.setVisibility(View.GONE);
        } else {
            try {
                JSONObject jsonObject = new JSONObject(jsonData);
                vouchers_amount.setText("•  " + jsonObject.getString("cc_pname"));
                conditions.setText("•  " + jsonObject.getString("desc"));
                before_date.setText("•  有效期至:" + jsonObject.getString("end_date"));

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }

    }

    /**
     * 获取id
     *
     * @param name id的名字
     * @return
     */
    public int getLayoutId(String name) {
        int id = MResource.getResource(mContext, "layout", name);

        return id;
    }

    /**
     * 获取id
     *
     * @param name id的名字
     * @return
     */
    public int getIdId(String name) {
        int id = MResource.getResource(mContext, "id", name);

        return id;
    }

}
