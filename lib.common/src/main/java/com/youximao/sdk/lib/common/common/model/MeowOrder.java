package com.youximao.sdk.lib.common.common.model;

import java.io.Serializable;

/**
 * Created by yulinsheng on 16-11-21.
 */
public class MeowOrder implements Serializable {

    private String orderId = "";
    private String orderPrice = "";
    private String payPrice = "";
    private String point = "";

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }
}
