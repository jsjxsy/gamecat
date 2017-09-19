package com.youximao.sdk.app.usercenter.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/24.
 */

public class OpenId implements Serializable{

    private String client_id;
    private String openid;
    private String unionid;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}
