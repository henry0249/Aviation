package com.example.administrator.aviation.model.updateversion;

/**
 * Created by Administrator on 2017/2/8 0008.
 *判断试试属于url链接
 */

public class StringUtils {
    public static boolean isNetUrl(String url) {
        return url.contains("http");
//        return url.substring(0, 3).equals("");
    }
}
