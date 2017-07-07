package com.example.administrator.aviation.model.intcgrbb;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析国际出港日报表
 */

public class PrepareIntExportDayInfo {
    // 经典的pull解析方法
    public static List<IntExportDayInfo> pullExportDayInfoXml (String xml) {
        List<IntExportDayInfo> xmlList = null;
        IntExportDayInfo intExportDayInfo = null;
        ByteArrayInputStream tInputStringStream = null;
        try {
            if (xml != null && !xml.trim().equals("")) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            }
        } catch (Exception e) {
            return null;
        }

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(tInputStringStream, "UTF-8");

            // 产生第一个事件
            int evenType = parser.getEventType();
            while (evenType != XmlPullParser.END_DOCUMENT) {
                switch (evenType) {
                    // 判断当前是不是文档开始的事件
                    case XmlPullParser.START_DOCUMENT:
                        xmlList = new ArrayList<IntExportDayInfo>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("ReportInfo")) {
                            intExportDayInfo = new IntExportDayInfo();
                        } else if (name.equalsIgnoreCase("Carrier")) {
                            intExportDayInfo.setCarrier(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            intExportDayInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            intExportDayInfo.setPc(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            intExportDayInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            intExportDayInfo.setVolume(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("ReportInfo")) {
                            assert xmlList != null;
                            xmlList.add(intExportDayInfo);
                        }

                        break;

                    default:
                        break;
                }
                evenType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xmlList;
    }
}
