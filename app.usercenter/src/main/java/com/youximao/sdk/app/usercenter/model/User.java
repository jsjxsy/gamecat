package com.youximao.sdk.app.usercenter.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/2.
 */

public class User implements Serializable{
    /**
     * openId :
     * token :
     * userId :
     */

    private String openId;
    private String token;
    private String userId;
    private String userGameBindingChannelId;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserGameBindingChannelId() {
        return userGameBindingChannelId;
    }

    public void setUserGameBindingChannelId(String userGameBindingChannelId) {
        this.userGameBindingChannelId = userGameBindingChannelId;
    }
}