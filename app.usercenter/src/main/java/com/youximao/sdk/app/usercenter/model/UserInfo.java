package com.youximao.sdk.app.usercenter.model;

import java.io.Serializable;

/**
 * Created by admin on 2016/11/3.
 */

public class UserInfo implements Serializable {
    /**
     * account : 88888888
     * chlId : 101
     * gameId : youximao_jufeng_2
     * guildChlId : 1
     * openId : 123456789
     * safeMobile : 18616871698
     * userId : 126433
     * userName : test
     */

    private String account;
    private String chlId;
    private String gameId;
    private String guildChlId;
    private String openId;
    private String safeMobile;
    private int userId;
    private String userName;
    // 新增字段
    private int type;
    private boolean selected;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChlId() {
        return chlId;
    }

    public void setChlId(String chlId) {
        this.chlId = chlId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGuildChlId() {
        return guildChlId;
    }

    public void setGuildChlId(String guildChlId) {
        this.guildChlId = guildChlId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSafeMobile() {
        return safeMobile;
    }

    public void setSafeMobile(String safeMobile) {
        this.safeMobile = safeMobile;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
