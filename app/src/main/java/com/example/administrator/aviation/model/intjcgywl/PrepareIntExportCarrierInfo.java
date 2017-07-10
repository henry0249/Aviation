package com.example.administrator.aviation.model.intjcgywl;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析国际出港日报表
 */

public class PrepareIntExportCarrierInfo {
    // 经典的pull解析方法
    public static List<IntExportCarrierInfo> pullExportCarrierInfoXml(String xml) {
        List<IntExportCarrierInfo> xmlList = null;
        IntExportCarrierInfo intExportCarrierInfo = null;
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
                        xmlList = new ArrayList<IntExportCarrierInfo>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("ReportInfo")) {
                            intExportCarrierInfo = new IntExportCarrierInfo();
                        } else if (name.equalsIgnoreCase("Carrier")) {
                            intExportCarrierInfo.setCarrier(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            intExportCarrierInfo.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            intExportCarrierInfo.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDate")) {
                            intExportCarrierInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            intExportCarrierInfo.setPc(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            intExportCarrierInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            intExportCarrierInfo.setVolume(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("ReportInfo")) {
                            assert xmlList != null;
                            xmlList.add(intExportCarrierInfo);
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
