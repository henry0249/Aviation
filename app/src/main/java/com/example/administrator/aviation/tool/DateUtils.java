package com.example.administrator.aviation.tool;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换工具类
 * 用于转换时间信息
 */

public class DateUtils {
    // 常用的日期时间格式
    public static final String FMT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String FMT_YMD = "yyyy-MM-dd";
    public static final String FMT_YM = "yyyy-MM";
    public static final String FMT_HMS = "HH:mm:ss";
    public static final String FMT_YMDHMS2 = "yyyyMMddHHmmss";

    /**
     *
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return true 表示在使用周期内
     *          false 表示不在使用周期内
     */
    public static boolean compareDate(Date beginTime, Date endTime) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(beginTime);
        c2.setTime(endTime);

        int result = c1.compareTo(c2);
        if (result >= 0) {
            return true;
        } else {
            return false;
        }
    }

    // 获取今天的时间
    public static String getTodayDateTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return format.format(new Date());
    }

    /**
     * 转换指定 YYYY-MM-DD 对应的 日期
     */
    public static Date convertFromStrYMD(String ymd) {
        if (ymd == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(FMT_YMD);
        try  {
            return format.parse(ymd);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
