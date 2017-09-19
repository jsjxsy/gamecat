package com.youximao.sdk.app.pay.model;


import java.io.Serializable;

/**
 * Created by admin on 16/9/30.
 */

public class PayWay implements Serializable {
    public boolean selected;
    public int iconResId = -1;
    public String title;
    public String payInterface;
    public boolean clickEnable;

//    private OnGridViewItemClickListener onClickListener;
//
//    public OnGridViewItemClickListener getOnClickListener() {
//        return onClickListener;
//    }
//
//    public void setOnClickListener(OnGridViewItemClickListener onClickListener) {
//        this.onClickListener = onClickListener;
//    }
//
//    public interface OnGridViewItemClickListener {
//        void onItemClickListener(View v, PayWay payWay);
//    }
}
