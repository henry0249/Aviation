package com.example.administrator.aviation.sys;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

import java.lang.reflect.Method;

/**
 * 检测手机wifi和数据流量是否开启(单例模式)
 */

public class NetMgr {
    private static volatile NetMgr instance;

    private Context context;

    private NetMgr() {

    }

    public static NetMgr getInstance() {
        if (instance ==null) {
            synchronized (NetMgr.class) {
                if (instance == null) {
                    instance = new NetMgr();
                }
            }
        }
        return instance;
    }

    // 判断wifi是否连接可用
    public boolean isWifiConnected() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifiNetInfo != null && wifiNetInfo.isConnected();
    }

    // 移动网络是否连接可用（在WiFi连接状态下无效）
    public boolean isMobileConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }

        ConnectivityManager connMgr
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (mobile == NetworkInfo.State.CONNECTED) {
            return true;
        } else {
            return false;
        }
    }

    // 判断移动网络连接是否可用（在WiFi已连接条件下也可使用）
    public boolean isNetConnect() {
        ConnectivityManager connManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Class cmClass = connManager.getClass();
        Class[] argClasses = null;
        Object[] argObject = null;
        Boolean isOpen = false;
        try {
            Method method = cmClass.getMethod("getMobileDataEnabled", argClasses);

            isOpen = (Boolean) method.invoke(connManager, argObject);
            if (isOpen == true) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
