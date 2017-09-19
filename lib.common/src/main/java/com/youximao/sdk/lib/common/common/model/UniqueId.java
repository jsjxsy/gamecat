package com.youximao.sdk.lib.common.common.model;

import java.io.Serializable;

/**
 * Created by yulinsheng on 16-11-8.
 * 设备sid
 */
public class UniqueId implements Serializable {

    private String uniqueId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
