package com.lzy.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lzy.okgo.OkGo;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by zhan on 17-2-24.
 */
public class NETReceiver extends BroadcastReceiver {

    private static Set<NetWorkListener> listeners = new HashSet();

    public static void setNetWorkListener(NetWorkListener listener) {
        listeners.add(listener);
    }

    public static void removeNetWorkListener(NetWorkListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != OkGo.getContext()) {
            int type = NetWorkUtils.getNetWorkType(OkGo.getContext());
            for (NetWorkListener listener : listeners) {
                listener.onNetWorkChanged(type);
            }
        }
    }


    public interface NetWorkListener {

        void onNetWorkChanged(int netType);
    }
}
