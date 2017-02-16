package com.example.administrator.aviation.model.updateversion;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class StringUtils {
    public static boolean isNetUrl(String url) {
        return url.contains("http");
//        return url.substring(0, 3).equals("");
    }
}
