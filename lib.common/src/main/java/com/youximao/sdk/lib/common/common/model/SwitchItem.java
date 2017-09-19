package com.youximao.sdk.lib.common.common.model;

import java.io.Serializable;

/**
 * Created by yulinsheng on 16-12-9.
 */
public class SwitchItem implements Serializable{

    private String active;
    private String bbs;
    private String giftPackage;
    private String qqLogin;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getBbs() {
        return bbs;
    }

    public void setBbs(String bbs) {
        this.bbs = bbs;
    }

    public String getGiftPackage() {
        return giftPackage;
    }

    public void setGiftPackage(String giftPackage) {
        this.giftPackage = giftPackage;
    }

    public String getQqLogin() {
        return qqLogin;
    }

    public void setQqLogin(String qqLogin) {
        this.qqLogin = qqLogin;
    }
}
