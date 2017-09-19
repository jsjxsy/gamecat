package com.youximao.sdk.app.personal.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yulinsheng on 16-11-7.
 */
public class PointRecharge implements Serializable {

    CustomItem custom;
    ArrayList<GoodsItem> goods;

    public PointRecharge() {
    }

    public CustomItem getCustom() {
        return custom;
    }

    public void setCustom(CustomItem custom) {
        this.custom = custom;
    }

    public ArrayList<GoodsItem> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<GoodsItem> goods) {
        this.goods = goods;
    }
}
