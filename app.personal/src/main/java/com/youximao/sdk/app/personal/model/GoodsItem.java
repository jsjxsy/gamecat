package com.youximao.sdk.app.personal.model;

import java.io.Serializable;

/**
 * Created by yulinsheng on 16-11-9.
 */
public class GoodsItem implements Serializable{
    private String goodsId;
    private String isEnable;
    private String point;
    private double price;

    /**
     * 商品ID
     *
     * @return
     */
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 是否展示
     *
     * @return
     */
    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    /**
     * 喵点
     *
     * @return
     */
    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    /**
     * 单价
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
