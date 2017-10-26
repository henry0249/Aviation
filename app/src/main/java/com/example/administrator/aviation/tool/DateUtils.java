package com.example.administrator.aviation.tool;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换工具类
 * 用于转换时间信息
 * （用于将服务器的日期格式转化为自己需要的格式）
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

    public static int getGapCount(Date startDate, Date endDate) {
        Calendar fromCalendar = Calendar.getInstance();
        fromCalendar.setTime(startDate);
        fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
        fromCalendar.set(Calendar.MINUTE, 0);
        fromCalendar.set(Calendar.SECOND, 0);
        fromCalendar.set(Calendar.MILLISECOND, 0);

        Calendar toCalendar = Calendar.getInstance();
        toCalendar.setTime(endDate);
        toCalendar.set(Calendar.HOUR_OF_DAY, 0);
        toCalendar.set(Calendar.MINUTE, 0);
        toCalendar.set(Calendar.SECOND, 0);
        toCalendar.set(Calendar.MILLISECOND, 0);

        return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
    }

    public static int daysBetween(String date1, String date2)throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(date1));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(date2));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }
}
