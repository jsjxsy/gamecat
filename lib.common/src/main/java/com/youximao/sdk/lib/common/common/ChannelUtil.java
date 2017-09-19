package com.youximao.sdk.lib.common.common;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.youximao.sdk.lib.common.common.util.AppCacheSharedPreferences;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

//import com.gamecat.common.util.AppCacheSharedPreferences;

/**
 * Created by davy on 16/9/10.
 */
public class ChannelUtil {


    private static String channel = null;

    public static String getChannel(Context context) {
        if (!AppCacheSharedPreferences.getCacheString("guildChlId").equals("")) {
            if (AppCacheSharedPreferences.getCacheString("guildChlId").equals("mao")) {
                channel = null;
            } else {
                channel = AppCacheSharedPreferences.getCacheString("guildChlId");
            }
        }

        if (channel != null) {
            return channel;
        }

        final String start_flag = "META-INF/channel_";
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration<?> entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = ((ZipEntry) entries.nextElement());
                String entryName = entry.getName();
                if (entryName.contains(start_flag)) {
                    channel = entryName.replace(start_flag, "");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (channel == null || channel.length() <= 0) {
            channel = "0";//读不到渠道号就默认官方渠道
        }
        Log.e("ChannelId", channel);
        return channel;
    }

    public static void setChannel(String channel) {
        channel = channel;
    }

}
