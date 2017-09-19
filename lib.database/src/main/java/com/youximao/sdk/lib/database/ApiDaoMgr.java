package com.youximao.sdk.lib.database;


import com.youximao.sdk.lib.database.proxy.DaoMgrProxyInvocationHandler;

public class ApiDaoMgr {

    /**
     * @param clazz        ApiDaoProxy interface
     * @param clazzColumns database access Tables Columns class array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T onCreateApiDaoMgr(Class<T> clazz) {
        DaoMgrProxyInvocationHandler proxyHandler = new DaoMgrProxyInvocationHandler();
        return (T) proxyHandler.bindDaoMgrProxyHandler(clazz);
    }

}
