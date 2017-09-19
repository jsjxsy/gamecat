package com.lzy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class AppCacheSharedPreferences {

    public static final int VALUE_INT_DEFAULT = -1;
    public static final long VALUE_LONG_DEFAULT = -1L;

    private static String sAppCachePath = "GameCatSDKConfig";
    private static Context mContext;

    public static String GAME_CAT_APP_KEY = "AppKey";
    public static void init(Context activity) {
        if (mContext == null) {
            mContext = activity;
        }
    }

    /**
     * @param name
     * @return
     */
    public static String getCacheString(String name) {
        return getCacheString(sAppCachePath, name);
    }

    private static String getCacheString(String cacheName, String name) {
        if (TextUtils.isEmpty(cacheName)) {
            return "";
        }
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        return sp.getString(name, "");
    }

    /**
     * @param name
     * @param value
     */
    public static void putCacheString(String name, String value) {
        putCacheString(sAppCachePath, name, value);
    }

    public static void putCacheString(String cacheName, String name, String value) {
        if (TextUtils.isEmpty(cacheName)) {
            return;
        }
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(name, value);
        editor.apply();
    }

    /**
     * @param name
     * @return
     */
    public static int getCacheInteger(String name) {
        return getCacheInteger(name, VALUE_INT_DEFAULT);
    }

    /**
     * @param name
     * @param def
     * @return
     */
    public static int getCacheInteger(String name, int def) {
        if(null == mContext){
            return -1;  //出错情况
        }
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        return sp.getInt(name, def);
    }

    /**
     * @param name
     * @param value
     */
    public static void putCacheInteger(String name, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    public static int getCacheInteger(String cacheName, String name, int def) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        return sp.getInt(name, def);
    }

    /**
     * @param name
     * @param value
     */
    public static void putCacheInteger(String cacheName, String name, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.apply();
    }

    /**
     * @param name
     * @return
     */
    public static long getCacheLong(String name) {
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        return sp.getLong(name, VALUE_LONG_DEFAULT);
    }

    public static long getCacheLong(String cacheName, String name) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        return sp.getLong(name, VALUE_LONG_DEFAULT);
    }

    /**
     * @param name
     * @param value
     */
    public static void putCacheBoolean(String name, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    /**
     * @param name
     * @return
     */
    public static boolean getCacheBoolean(String name, boolean defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        return sp.getBoolean(name, defaultValue);
    }

    public static void putCacheBoolean(String cacheName, String name, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(name, value);
        editor.apply();
    }

    /**
     * @param name
     * @return
     */
    public static boolean getCacheBoolean(String cacheName, String name, boolean defaultValue) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        return sp.getBoolean(name, defaultValue);
    }

    /**
     * @param name
     * @param value
     */
    public static void putCacheLong(String name, long value) {
        SharedPreferences sp = mContext.getSharedPreferences(sAppCachePath, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(name, value);
        editor.apply();
    }

    public static void putCacheLong(String cacheName, String name, long value) {
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(name, value);
        editor.apply();
    }


    /**
     * 保存ArrayList 必须是String类型
     */
    public static void putCacheArrayList(Context ctx, String key, ArrayList<String> list) {
        putCacheArrayList(ctx.getApplicationContext(), sAppCachePath, key, list);
    }

    public static void putCacheArrayList(Context ctx, String cacheName, String key, ArrayList<String> list) {
        if (TextUtils.isEmpty(cacheName)) {
            return;
        }
        JSONArray ret = new JSONArray(list);
        putCacheString(cacheName, key, ret.toString());
    }

    /**
     * @param ctx
     * @param key
     * @return
     */
    public static ArrayList<String> getCacheArrayList(Context ctx, String key) {
        return getCacheArrayList(ctx.getApplicationContext(), sAppCachePath, key);
    }

    public static ArrayList<String> getCacheArrayList(Context ctx, String cacheName, String key) {
        ArrayList<String> ret = new ArrayList<String>();
        if (TextUtils.isEmpty(cacheName)) {
            return ret;
        }

        String listStr = getCacheString(cacheName, key);
        if (TextUtils.isEmpty(listStr)) {
            listStr = "{}";
        }
        JSONArray listJson = null;
        try {
            listJson = new JSONArray(listStr);
        } catch (Exception e) {
            return ret;
        }
        if (listJson != null) {
            for (int i = 0; i < listJson.length(); i++) {
                String temp = listJson.optString(i);
                ret.add(temp);
            }
        }

        return ret;
    }

    /**
     * 保存LinkedList 必须是String类型
     */
    public static void putCacheLinkedList(String key, LinkedList<String> list) {
        JSONArray ret = new JSONArray(list);
        putCacheString(key, ret.toString());
    }

    /**
     * @param key
     * @return
     */
    public static LinkedList<String> getCacheLinkedList(String key) {
        LinkedList<String> ret = new LinkedList<String>();
        String listStr = getCacheString(key);
        if (TextUtils.isEmpty(listStr)) {
            listStr = "{}";
        }
        JSONArray listJson = null;
        try {
            listJson = new JSONArray(listStr);
        } catch (Exception e) {
            return ret;
        }
        if (listJson != null) {
            for (int i = 0; i < listJson.length(); i++) {
                String temp = listJson.optString(i);
                ret.add(temp);
            }
        }

        return ret;
    }

    /**
     * 保存hashmap key和value必须是Stirng类型
     */
    public static void putCacheHashMap(String key, HashMap<String, String> map) {
        JSONObject ret = new JSONObject(map);
        putCacheString(key, ret.toString());
    }

    public static HashMap<String, String> getCacheHashMap(String key) {
        return getCacheHashMapByKey(key);
    }

    public static HashMap<String, String> getCacheHashMapByKey(String key) {
        HashMap<String, String> ret = new HashMap<String, String>();

        String mapStr = getCacheString(key);
        if (TextUtils.isEmpty(mapStr)) {
            mapStr = "{}";
        }

        JSONObject mapJson = null;
        try {
            mapJson = new JSONObject(mapStr);
        } catch (Exception e) {
            return ret;
        }

        if (mapJson != null) {
            @SuppressWarnings("unchecked")
            Iterator<String> it = mapJson.keys();
            while (it.hasNext()) {
                String theKey = it.next();
                String theValue = mapJson.optString(theKey);
                ret.put(theKey, theValue);
            }
        }

        return ret;
    }


    /**
     * 移除元素
     */
    public static void removeByKey(String key) {
        removeByKey(mContext.getApplicationContext(), sAppCachePath, key);
    }

    public static void removeByKey(Context ctx, String cacheName, String key) {
        SharedPreferences sp = ctx.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearCacheByName(String cacheName) {
        if (TextUtils.isEmpty(cacheName)) {
            return;
        }
        SharedPreferences sp = mContext.getSharedPreferences(cacheName, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

}
