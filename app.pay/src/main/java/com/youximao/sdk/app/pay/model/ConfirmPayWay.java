package com.youximao.sdk.app.pay.model;

/**
 * Created by admin on 16/9/30.
 */

public class ConfirmPayWay {


    /**
     * interfaceId : 3
     * payWay : 5
     */

    private String interfaceId;
    private String payWay;
    private int status;

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
