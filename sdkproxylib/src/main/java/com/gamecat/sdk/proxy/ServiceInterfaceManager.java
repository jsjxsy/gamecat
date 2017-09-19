package com.gamecat.sdk.proxy;

/**
 * Created by admin on 2017/2/20.
 */

public class ServiceInterfaceManager {
    private static ServiceInterface serviceInterface;

    public static ServiceInterface getServiceInterface() {
        return serviceInterface;
    }

    public static void setServiceInterface(ServiceInterface serviceInterface) {
        ServiceInterfaceManager.serviceInterface = serviceInterface;
    }
}
