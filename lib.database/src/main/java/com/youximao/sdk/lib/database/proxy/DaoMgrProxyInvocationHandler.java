package com.youximao.sdk.lib.database.proxy;

import android.database.Cursor;

import com.youximao.sdk.lib.database.SQLiteOpenManager;
import com.youximao.sdk.lib.database.annotation._DB_METHOD;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;


public class DaoMgrProxyInvocationHandler implements InvocationHandler {

    /**
     *
     * @param clazz
     * @param clazzColumns
     * @return
     */
    public Object bindDaoMgrProxyHandler(Class<?> clazz) {
        Class<?>[] clazzs = {clazz};
        Object newProxyInstance = Proxy.newProxyInstance(clazz.getClassLoader(), clazzs, this);

        return newProxyInstance;
    }

    /**
     * 创建或升级ApiDaoMgr业务所使用的数据表
     *
     * @param clazzColumns 表结构对应的类列对应
     */
    public void onCreateOrUpgradeTables(ArrayList<Class<?>> clazzColumns) {
        SQLiteOpenManager.getInstance().onCreateOrUpdateDatabaseTables(clazzColumns);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        _DB_METHOD optMethod = method.getAnnotation(_DB_METHOD.class);

        // get parameters for method.
        Annotation[][] annosArr = method.getParameterAnnotations();
        switch (optMethod.method()) {
            case _OPT_QUERY: {
                break;
            }
            case _OPT_INSERT:
            case _OPT_UPDATE: {

                break;
            }
            case _OPT_DELETE: {

                break;
            }
        }

        return null;
    }

    public Cursor onQueryDatabase() {

        return null;
    }

    public Long onDeleteDatabase() {
        return null;
    }

}
