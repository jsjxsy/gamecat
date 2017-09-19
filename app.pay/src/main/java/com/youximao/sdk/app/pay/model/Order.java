package com.youximao.sdk.app.pay.model;

import java.io.Serializable;

/**
 * Created by admin on 16/9/30.
 */

public class Order implements Serializable {

    /**
     * payAmount : 1
     * surplusPoint : 0
     * orderAmount : 1
     * offsetAmount : 0
     * orderId : 1475217754060133
     */

    private String payAmount;
    private int surplusPoint;
    private String orderAmount;
    private String offsetAmount;
    private String orderId;
    /**
     * orderAmountRMB : 57878
     * payAmountRMB : 1
     */

    private String orderAmountRMB;
    private String payAmountRMB;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public int getSurplusPoint() {
        return surplusPoint;
    }

    public void setSurplusPoint(int surplusPoint) {
        this.surplusPoint = surplusPoint;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOffsetAmount() {
        return offsetAmount;
    }

    public void setOffsetAmount(String offsetAmount) {
        this.offsetAmount = offsetAmount;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderAmountRMB() {
        return orderAmountRMB;
    }

    public void setOrderAmountRMB(String orderAmountRMB) {
        this.orderAmountRMB = orderAmountRMB;
    }

    public String getPayAmountRMB() {
        return payAmountRMB;
    }

    public void setPayAmountRMB(String payAmountRMB) {
        this.payAmountRMB = payAmountRMB;
    }

}
