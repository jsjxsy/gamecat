package com.youximao.sdk.app.usercenter.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/12/7.
 */

public class AccountType implements Serializable {
    // 新增字段
    private int type;
    private String token;
    private String account;
    private String safeMobile;
    private String userName;

    //排序
    private long time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSafeMobile() {
        return safeMobile;
    }

    public void setSafeMobile(String safeMobile) {
        this.safeMobile = safeMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "AccountType{" +
                "type=" + type +
                ", token='" + token + '\'' +
                ", account='" + account + '\'' +
                ", safeMobile='" + safeMobile + '\'' +
                ", userName='" + userName + '\'' +
                ", time=" + time +
                '}';
    }
}
