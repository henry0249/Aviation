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
        } else {
            return "未知";
        }

    }

    public static String getCNfreightPayment(String name) {
        if (name.equals("PP") && !name.equals("")) {
            return "预付";
        } else if (name.equals("CC") && !name.equals("")) {
            return "到付 ";
        } else {
            return "未知";
        }

    }

    // 将英文转换为中文
    public static String enTocn (String name) {
        if (name.equals("C") && !name.equals("")) {
            return "货机";
        } else if (name.equals("P") && !name.equals("")) {
            return "客机";
        } else if (name.equals("T") && !name.equals("")) {
            return "卡车";
        } else if (name.equals("D") && !name.equals("")) {
            return "国内";
        } else if (name.equals("I") && !name.equals("")) {
            return "国际";
        } else if (name.equals("S") || name.equals("N") || name.equals("F")) {
            return "计划";
        } else if (name.equals("P") || name.equals("T")) {
            return "起飞";
        } else if (name.equals("+") || name.equals("Z")) {
            return "结束";
        } else if (name.equals("L")) {
            return "到达";
        } else if (name.equals("D")) {
            return "延误";
        } else if (name.equals("X")) {
            return "取消";
        }else {
            return "未知";
        }
    }

    // 将中文转换成英文
    public static String cNtoEn(String name) {
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
        } else if (name.equals("预付") && !name.equals("")) {
            return "PP";
        } else if (name.equals("到付") && !name.equals("")) {
            return "CC";
        } else if (name.equals("货机") && !name.equals("")) {
            return "C";
        } else if (name.equals("客机") && !name.equals("")) {
            return "P";
        } else if (name.equals("卡车") && !name.equals("")) {
            return "T";
        } else if (name.equals("国内") && !name.equals("")) {
            return "D";
        } else if (name.equals("国际") && !name.equals("")) {
            return "I";
        } else if (name.equals("进港") && !name.equals("")) {
            return "I";
        } else if (name.equals("出港") && !name.equals("")) {
            return "E";
        } else if (name.equals("业务量")) {
            return "";
        } else if (name.equals("目的港")) {
            return "DEST";
        } else if (name.equals("航班号")) {
            return "FNO";
        } else if (name.equals("日")) {
            return "DAY";
        }else {
            return "未知";
        }
    }

}
