package com.youximao.sdk.app.usercenter.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.youximao.sdk.app.usercenter.model.AccountType;
import com.youximao.sdk.app.usercenter.model.UserInfo;
import com.youximao.sdk.lib.database.SQLiteOpenManager;

import java.util.ArrayList;

/**
 * Created by admin on 2017/3/13.
 */

public class AccountTypeDao {

    private static AccountTypeDao INSTANCE;

    private AccountTypeDao() {

    }

    public static synchronized AccountTypeDao getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AccountTypeDao();
        }
        return INSTANCE;
    }


    public void saveAccountType(UserInfo userInfo, int type, String token, long time) {
        ContentValues values = new ContentValues();
        values.put(UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT, userInfo.getAccount());
        values.put(UserCenterDatabaseConstant.UserInformationColumns.LOGIN_TYPE, type);
        values.put(UserCenterDatabaseConstant.UserInformationColumns.PHONE, userInfo.getSafeMobile());
        values.put(UserCenterDatabaseConstant.UserInformationColumns.TOKEN, token);
        values.put(UserCenterDatabaseConstant.UserInformationColumns.USER_ID, userInfo.getUserId());
        values.put(UserCenterDatabaseConstant.UserInformationColumns.USER_NAME, userInfo.getUserName());
        values.put(UserCenterDatabaseConstant.UserInformationColumns.TIME, time);
        String whereClause = " " + UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT + "=?";
        String[] whereArgs = new String[]{userInfo.getAccount()};
        SQLiteOpenManager.getInstance().doSaveDataAction(UserCenterDatabaseConstant.Tables.USER_INFORMATION, values, whereClause, whereArgs);
    }

    public int findTypeByAccount(String account) {
        String sql = "SELECT " + UserCenterDatabaseConstant.UserInformationColumns.LOGIN_TYPE + " FROM " + UserCenterDatabaseConstant.Tables.USER_INFORMATION
                + " WHERE " + UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT + " = ? ";
        String[] whereArgs = new String[]{account};
        Cursor cursor = null;
        try {
            cursor = SQLiteOpenManager.getInstance().doQueryDataAction(sql, whereArgs);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.LOGIN_TYPE));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return -1;
    }

    public void deleteAccount(String account) {
        String[] whereArgs = new String[]{account};
        String whereClause = " " + UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT + "=?";
        SQLiteOpenManager.getInstance().doDeleteDataAction(UserCenterDatabaseConstant.Tables.USER_INFORMATION, whereClause, whereArgs);
    }

    public String findTokenByAccount(String account) {
        String sql = "SELECT " + UserCenterDatabaseConstant.UserInformationColumns.LOGIN_TYPE + " FROM " + UserCenterDatabaseConstant.Tables.USER_INFORMATION
                + " WHERE " + UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT + " = ? ";
        String[] whereArgs = new String[]{account};
        Cursor cursor = null;
        try {
            cursor = SQLiteOpenManager.getInstance().doQueryDataAction(sql, whereArgs);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.TOKEN));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }


    public ArrayList<AccountType> listAccounts() {
        String sql = "SELECT * FROM " + UserCenterDatabaseConstant.Tables.USER_INFORMATION + " ORDER BY " + UserCenterDatabaseConstant.UserInformationColumns.TIME + " DESC; ";
        Cursor cursor = SQLiteOpenManager.getInstance().doQueryDataAction(sql);
        if (cursor == null) {
            return null;
        }

        ArrayList<AccountType> accountTypes = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            AccountType accountType = new AccountType();
            accountType.setAccount(cursor.getString(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.ACCOUNT)));
            accountType.setSafeMobile(cursor.getString(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.PHONE)));
            accountType.setTime(cursor.getLong(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.TIME)));
            accountType.setToken(cursor.getString(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.TOKEN)));
            accountType.setUserName(cursor.getString(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.USER_NAME)));
            accountType.setType(cursor.getInt(cursor.getColumnIndex(UserCenterDatabaseConstant.UserInformationColumns.LOGIN_TYPE)));
            accountTypes.add(accountType);
        }
        cursor.close();
        return accountTypes;
    }

}
