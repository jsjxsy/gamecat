package com.youximao.sdk.app.personal.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/12/12.
 */

public class CustomItem implements Serializable {
    private String goodsId;
    private String isEnable;
    private String point;
    private String price;

    public CustomItem() {
    }

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
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
