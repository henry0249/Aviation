package com.example.administrator.aviation.util;

/**
 * 用于将服务器的英文注释转换成中文
 */

public class AviationNoteConvert {

    // 此方法是将业务类型转换为中文（业务类型，说明：ANR，普通货物运输；EMS，国际快件；KJD，D类快件；YJE，国际邮包 ）
    public static String getCNBusinessType(String name) {
        if (name.equals("ANR") && !name.equals("")) {
            return "普通货物运输";
        } else if (name.equals("EMS") && !name.equals("")) {
            return "国际快件";
        } else if (name.equals("KJD") && !name.equals("")) {
            return "D类快件";
        } else if (name.equals("YJE") && !name.equals("")) {
            return "国际邮包";
        } else {
            return "未知";
        }
    }

    // 此方法是将报关类型转换为中文（说明：0，本关；1，转关；2，大通关）
    public static String getCNTranFlag(String name) {
        if (name.equals("0") && !name.equals("")) {
            return "本关";
        } else if (name.equals("1") && !name.equals("")) {
            return "转关";
        } else if (name.equals("2") && !name.equals("")) {
            return "大通关";
        } else {
            return "未知";
        }
    }

    public static String getCNTransPortMode(String name) {
        if (name.equals("3") && !name.equals("")) {
            return "陆运";
        } else if (name.equals("4") && !name.equals("")) {
            return "空运 ";
        }
            return "未知";
        }

    // 将中文转换成英文
    public static String  cNtoEn(String name) {
        if (name.equals("普通货物运输") && !name.equals("")) {
            return "ANR";
        } else if (name.equals("国际快件") && !name.equals("")) {
            return "EMS";
        } else if (name.equals("类快件") && !name.equals("")) {
            return "KJD";
        } else if (name.equals("国际邮包") && !name.equals("")) {
            return "YJE";
        } else if (name.equals("本关") && !name.equals("")) {
            return "0";
        } else if (name.equals("转关") && !name.equals("")) {
            return "1";
        } else if (name.equals("大通关") && !name.equals("")) {
            return "2";
        } else if (name.equals("陆运") && !name.equals("")) {
            return "3";
        } else if (name.equals("空运") && !name.equals("")) {
            return "4";
        }else {
            return "未知";
        }
    }

}
