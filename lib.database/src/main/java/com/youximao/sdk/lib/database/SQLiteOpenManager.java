package com.youximao.sdk.lib.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import com.youximao.sdk.lib.database.annotation._DB_TABLE;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * @author guangming.chen
 */
public class SQLiteOpenManager extends SQLiteOpenHelper {

    private static final int _DB_TABLE_STATUS_NONE = 0;
    private static final int _DB_TABLE_STATUS_CREATE = 1;
    private static final int _DB_TABLE_STATUS_UPGRADE = 2;

    private static final String TABLES_MASTER = "tb_tables_master";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    /* create table static string.
     */
    private static final String CREATE_TABLE = "CREATE TABLE ";
    private static final String PRIMARY_KEY = " PRIMARY KEY AUTOINCREMENT";
    private static final String LEFT_BRACKET = "(";
    private static final String RIGHT_BRACKET = ")";
    private static final String SPACE = " ";
    private static final String TEXT = "TEXT";
    private static final String INTEGER = "INTEGER";
    private static final String COMMA = ",";

    /* query from table static string.
     */
    private static final String SELECT_TABLE = "SELECT ";
    private static final String FROM = " FROM ";
    private static final String WHERE = " WHERE ";

    private static final byte[] LOCK = new byte[1];

    private static SQLiteOpenManager sSingleton = null;
    private static Context sContext = null;
    private static String sDatabaseName = "db.db";
    private static int sDatabaseCode = 99;

    private SQLiteDatabase mdb = null;
    private HashMap<String, Integer> mHashmapTableMaster = null;


    /**
     * constructor
     */
    protected SQLiteOpenManager() {
        super(sContext, sDatabaseName, null, sDatabaseCode);
    }

    /**
     * get singleton class instance.
     */
    public static SQLiteOpenManager getInstance() {
        if (sContext == null) {
            try {
                throw new Exception("please initial context by call method initContext(...).");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sSingleton == null) {
            synchronized (LOCK) {
                sSingleton = new SQLiteOpenManager();
            }
        }
        return sSingleton;
    }

    /**
     * initial context
     */
    public static void initContext(Context ctx, String name, int version) {
        if (ctx != null) {
            sContext = ctx;
        }
        sDatabaseName = name;
        sDatabaseCode = version;
    }

    /**
     * save (include insert or update) data to target table.
     */
    public long doSaveDataAction(String tableName,
                                 ContentValues values, String whereClause, String[] whereArgs) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }
        Cursor cursor = null;
        if (whereClause != null || whereArgs != null) {
            String sqlCheckExist = getSqlCheckExist(tableName, whereClause, whereArgs);
            if (!TextUtils.isEmpty(sqlCheckExist)) {
                cursor = doQueryDataAction(sqlCheckExist, whereArgs);
            }
        }
        if (cursor == null || cursor.getCount() <= 0) {
            if (cursor != null) {
                cursor.close();
            }
            return doInsertDataAction(tableName, values);
        } else {
            cursor.close();
            return doUpdateDataAction(tableName, values, whereClause, whereArgs);
        }
    }

    private String getSqlCheckExist(String tableName, String whereClause, String[] whereArgs) {
        if (whereClause == null && (whereArgs == null || whereArgs.length == 0)) {
            return null;
        }
        StringBuffer sbSqlCheckExist = new StringBuffer();
        sbSqlCheckExist.append(SELECT_TABLE).append(BaseColumns._ID).append(FROM);
        sbSqlCheckExist.append(tableName);
        sbSqlCheckExist.append(WHERE).append(whereClause); // where

        return sbSqlCheckExist.toString();
    }

    /**
     * query data from target table.
     */
    public Cursor doQueryDataAction(String sql) {
        return doQueryDataAction(sql, null);
    }

    /**
     * query data from target table.
     */
    public Cursor doQueryDataAction(String sql, String[] selectionArgs) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }
        return mdb.rawQuery(sql, selectionArgs);
    }

    /**
     * delete data from target table.
     */
    public long doDeleteDataAction(String tableName, String whereClause, String[] whereArgs) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }

        return mdb.delete(tableName, whereClause, whereArgs);
    }

    /**
     * update data into target table.
     */
    public long doUpdateDataAction(String tableName,
                                    ContentValues values, String whereClause, String[] whereArgs) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }

        return mdb.update(tableName, values, whereClause, whereArgs);
    }

    /**
     * insert data into target table.
     */
    public long doInsertDataAction(String table, ContentValues values) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }

        return mdb.insert(table, null, values);
    }

    public void doExcuteUpdateSql(String sql) {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }
        mdb.execSQL(sql);
    }

    /**
     * 建多个表
     */
    public boolean onCreateOrUpdateDatabaseTables(ArrayList<Class<?>> clazzColumns) {
        if (clazzColumns == null) {
            return true;
        }
        if (mdb == null) {
            mdb = getWritableDatabase();
        }
        boolean isSuccess = true;
        for (int i = 0; i < clazzColumns.size(); i++) {
            try {
                onCreateOrUpgradeDatabaseTable(clazzColumns.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return isSuccess;
    }

    /**
     * 建表方法,这里直接通过反射来拼接,需要传入表名和columns类.
     *
     * @param clazz columns定义类
     * @param db    db类
     */
    public void onCreateOrUpgradeDatabaseTable(Class<?> clazz) {
        _DB_TABLE tbAnnotation = clazz.getAnnotation(_DB_TABLE.class);
        if (tbAnnotation == null) {
            return;
        }
        int tableVersion = tbAnnotation.version();
        String tableName = tbAnnotation.name();

        int tableStatus = isTableNeedCreateOrUpgrade(tableName, tableVersion);
        if (tableStatus == _DB_TABLE_STATUS_NONE) {
            return;
        }

        StringBuffer sbSql = new StringBuffer();
        sbSql.append(CREATE_TABLE);
        sbSql.append(tableName).append(LEFT_BRACKET);
        sbSql.append(BaseColumns._ID).append(SPACE).append(INTEGER).append(PRIMARY_KEY).append(COMMA);
        // loop class all field, and create columns.
        for (Field f : clazz.getDeclaredFields()) {
            try {
                sbSql.append(f.get(null).toString()).append(SPACE).append(TEXT).append(COMMA);
            } catch (IllegalAccessException e) {
                // 创建数据库失败,理论上不可能
                e.printStackTrace(); //("createTable error " + tableName, e.getMessage());
            }
        }
        sbSql.deleteCharAt(sbSql.length() - 1).append(RIGHT_BRACKET);

        try {
            mdb = onBeginTransaction(); // drop, create, note opt in one transaction.
            // drop table first.
            if (tableStatus == _DB_TABLE_STATUS_UPGRADE) {
                mdb.execSQL(DROP_TABLE + tableName);
            }
            // create table
            mdb.execSQL(sbSql.toString());
            // update tableName and tableVersion into index table 'tb_tables_master'.
            updateCheckTableIndex(tableName, tableVersion);

            onSetTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            onEndTransaction();
        }
    }

    private int isTableNeedCreateOrUpgrade(String tableName, int tableVersion) {
        initCheckTableIndex();
        // table need create.
        if (!mHashmapTableMaster.containsKey(tableName)) {
            Log.w("cgm", "cgm call isDatabaseTableNeedCreateOrUpgrade() create " + tableName);
            return _DB_TABLE_STATUS_CREATE;
        }
        Integer oldVersion = mHashmapTableMaster.get(tableName);
        // table need upgrade, so
        if (tableVersion > oldVersion) {
            Log.w("cgm", "cgm call isDatabaseTableNeedCreateOrUpgrade() upgrade " + tableName);
            return _DB_TABLE_STATUS_UPGRADE;
        }
        // table don't need create or upgrade.
        Log.w("cgm", "cgm call isDatabaseTableNeedCreateOrUpgrade() none " + tableName);
        return _DB_TABLE_STATUS_NONE;
    }

    private void updateCheckTableIndex(String tableName, int tableVersion) {
        if (mHashmapTableMaster == null) {
            initCheckTableIndex();
        }

        ContentValues values = new ContentValues();
        values.put(ColumnTablesMaster._NAME, tableName);
        values.put(ColumnTablesMaster._CUR_VERSION, tableVersion);
        String whereClause = ColumnTablesMaster._NAME + "=?";
        try {

            doSaveDataAction(TABLES_MASTER, values, whereClause, new String[]{tableName});

            mHashmapTableMaster.put(tableName, tableVersion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCheckTableIndex() {
        if (mHashmapTableMaster != null) {
            return;
        }
        mHashmapTableMaster = new HashMap<String, Integer>();
        String sql = "SELECT * FROM " + TABLES_MASTER;
        Cursor cursor = doQueryDataAction(sql);
        if (cursor != null) {
            try {
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                    String tableName = cursor.getString(cursor.getColumnIndex(ColumnTablesMaster._NAME));
                    String tableVersion = cursor.getString(cursor.getColumnIndex(ColumnTablesMaster._CUR_VERSION));
                    if (!TextUtils.isEmpty(tableName) && !TextUtils.isEmpty(tableVersion)) {
                        mHashmapTableMaster.put(tableName, Integer.parseInt(tableVersion));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    cursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * begin transaction
     */
    public SQLiteDatabase onBeginTransaction() {
        if (mdb == null) {
            mdb = getWritableDatabase();
        }
        mdb.beginTransaction();

        return mdb;
    }

    /**
     * commit transaction
     */
    public void onSetTransactionSuccessful() {
        if (mdb == null) {
            return;
        }
        mdb.setTransactionSuccessful();
    }

    /**
     * end transaction
     */
    public void onEndTransaction() {
        if (mdb == null) {
            return;
        }
        mdb.endTransaction();
    }

    /**
     * create table master.
     */
    private void onCreateTableMaster(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLES_MASTER + " ("
                + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ColumnTablesMaster._NAME + " TEXT,"
                + ColumnTablesMaster._CUR_VERSION + " TEXT,"
                + ColumnTablesMaster._DESC + "  TEXT" + ");");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateTableMaster(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void finalize() throws Throwable {
        this.close();
        if (null != sSingleton) {
            sSingleton = null;
        }
        super.finalize();
    }

    @_DB_TABLE(name = TABLES_MASTER, version = 1)
    public static class ColumnTablesMaster {
        public static final String _NAME = "_name";
        public static final String _CUR_VERSION = "_cur_version";
        public static final String _DESC = "_desc";
    }
}
