package com.youximao.sdk.lib.database;

import android.content.Context;
import android.os.Environment;

import java.io.File;


/**
 * @author guangming
 */
public class DatabaseCache {


    private static final byte[] LOCK = new byte[1];
    private static DatabaseCache sSingleton = null;
    private static final String DB_DIR = Environment.getExternalStorageDirectory().getPath()
            + File.separator + "youximao" + File.separator
            + "gamecat_sdk.db";


    protected DatabaseCache() {
    }

    public static DatabaseCache getInstance() {
        if (sSingleton == null) {
            synchronized (LOCK) {
                sSingleton = new DatabaseCache();
            }
        }

        return sSingleton;
    }

    public void initContext(Context ctx) {
        SQLiteOpenManager.initContext(ctx, DB_DIR, 99);
    }

    public void initContext(Context ctx, String name, int version) {
        SQLiteOpenManager.initContext(ctx, name, version);
    }

    public synchronized void declareColumnsClass(ApiTableClazzDeclare clazz) {
        SQLiteOpenManager.getInstance().onCreateOrUpdateDatabaseTables(clazz.getTableClazzDeclare());
    }


}
