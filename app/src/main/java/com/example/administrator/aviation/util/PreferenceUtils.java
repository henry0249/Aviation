package com.example.administrator.aviation.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 保存数据到sharedPreference
 *
 * 主要存储一些信息（例如：自动登录 保存用户密码）
 */

public class PreferenceUtils {
    // sharedPreference文件名
    private static final String PREFERENCES_NAME = "userInfo";

    // 部门名称
    private static final String userBumenKey = "USER_BUMEN";

    // 用户名
    private static final String userNameKey = "USER_NAME";

    // 密码
    private static final String userPassKey = "USER_PASS";

    // 登录获取防止多次登录的ID值
    private static final String loginFlagKey = "LOGIN_FLAG";

    // 锁屏密码
    private static final String lockPassKey = "password";

    // 通道号
    private static final String TongDaoHao = "TongDaoHao";

    // APP升级用到的信息
    private static final String APP_VERSION = "APPVersion";
    private static final String APP_DESCRIBE = "APPDescribe";
    private static final String APP_URL = "appurl";

    private static final String JCGK = "GNCjcgk";

    // -------------以下是存储信息-------------
    /**
     * 获取用户和密码
     * @param context 上下文
     * @param userName 用户名
     * @param userPass 密码
     */
    public static void saveUser(Context context, String userBumen, String userName, String userPass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(userBumenKey, userBumen);
        editor.putString(userNameKey, userName);
        editor.putString(userPassKey, userPass);
        editor.commit();
    }

    /**
     * 锁屏密码的保存
     * @param context 上下文
     * @param lockPass 锁屏密码
     */
    public static void saveLockPass(Context context, String lockPass) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(lockPassKey, lockPass);
        editor.commit();

    }

    //保存通道号
    public static void saveTDH(Context context, String TDH) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TongDaoHao, TDH);
        editor.commit();

    }

    //保存排序信息
    public static void savejcgk(Context context, String TDH) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(JCGK, TDH);
        editor.commit();

    }

    public static String getjcgk(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(JCGK,"");
    }

    /**
     *
     * @param context 上下文
     * @param loginFlag 登录获取的手机ID
     */

    public static void saveLoginFlag(Context context, String loginFlag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(loginFlagKey, loginFlag);
        editor.commit();
    }


    public static void saveIsFirst(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("ISFIRST", false);
        editor.commit();
    }
    public static void saveAppUrl(Context context, String url) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_URL, url);
        editor.commit();
    }
    public static String getAppUrl(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(APP_URL, "");
    }
    public static void saveAPPVersion(Context context, String version) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_VERSION, version);
        editor.commit();
    }
    public static String getAPPVersion(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(APP_VERSION, "");
    }
    public static void saveAPPDescribe(Context context, String describe) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(APP_DESCRIBE, describe);
        editor.commit();
    }
    public static String getAPPDescribe(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(APP_DESCRIBE, "");
    }
    //---------以下是取得信息-------------------
    /**
     * 获取用户名
     * @param context 上下文
     * @return  返回用户名
     */
    public static String getUserName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(userNameKey, "");
    }

    public static boolean getIsFirst(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("ISFIRST", false);
    }

    //获取通道号
    public static String getTongDaoHao(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(TongDaoHao, "");
    }

    /**
     * 获取用户密码
     * @param context 上下文
     * @return 返回密码
     */
    public static String getUserPass(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(userPassKey, "");
    }

    /**
     * 获取部门信息
     * @param context 上下文
     * @return
     */
    public static String getUserBumen(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(userBumenKey, "");
    }

    public static String getLoginFlag(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(loginFlagKey, "");
    }

    /**
     *
     * @param context 上下文
     * @return 锁屏密码值
     */

    public static String getLockPass(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(lockPassKey, "");
    }

    //------------------以下是清除数据---------------

    /**
     * 清除share数据
     * @param context 上下文
     */
    public static void clearShare(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
    }

}
