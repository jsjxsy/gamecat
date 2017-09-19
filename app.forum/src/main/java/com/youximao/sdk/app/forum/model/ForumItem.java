package com.youximao.sdk.app.forum.model;

import java.io.Serializable;

/**
 * Created by yulinsheng on 16-12-13.
 */
public class ForumItem implements Serializable{

    private String auth;
    private String cookiepre;
    private String formhash;
    private String saltkey;

    public String getSaltkey() {
        return saltkey;
    }

    public void setSaltkey(String saltkey) {
        this.saltkey = saltkey;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getCookiepre() {
        return cookiepre;
    }

    public void setCookiepre(String cookiepre) {
        this.cookiepre = cookiepre;
    }

    public String getFormhash() {
        return formhash;
    }

    public void setFormhash(String formhash) {
        this.formhash = formhash;
    }
}
