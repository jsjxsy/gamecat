package com.youximao.sdk.app.pay.model;

import java.io.Serializable;

/**
 * Created by zhan on 17-3-20.
 * <p>
 * 代金券
 */
public class CashCoupon implements Serializable{
    private String mId;
    private boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }
}
