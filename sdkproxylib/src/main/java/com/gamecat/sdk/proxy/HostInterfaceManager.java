package com.gamecat.sdk.proxy;


/**
 * Created by Administrator on 2017/2/13.
 */

public class HostInterfaceManager {
    private HostInterface hostInterface;
    private static HostInterfaceManager instance;

    private HostInterfaceManager() {
    }

    public static HostInterfaceManager getInstance() {
        if (null == instance) {
            instance = new HostInterfaceManager();
        }
        return instance;
    }


    public void setHostInterface(HostInterface hostInterface) {
        this.hostInterface = hostInterface;
    }

    public HostInterface getHostInterface() {
        return this.hostInterface;
    }
}
