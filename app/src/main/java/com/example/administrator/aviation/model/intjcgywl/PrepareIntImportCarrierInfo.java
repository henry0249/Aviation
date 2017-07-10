package com.example.administrator.aviation.model.intjcgywl;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析国际进港日报表
 */

public class PrepareIntImportCarrierInfo {
    // 经典的pull解析方法
    public static List<IntImportCarrierInfo> pullImportCarrierInfoXml(String xml) {
        List<IntImportCarrierInfo> xmlList = null;
        IntImportCarrierInfo intImportCarrierInfo = null;
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
                        xmlList = new ArrayList<IntImportCarrierInfo>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("ReportInfo")) {
                            intImportCarrierInfo = new IntImportCarrierInfo();
                        } else if (name.equalsIgnoreCase("Carrier")) {
                            intImportCarrierInfo.setCarrier(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            intImportCarrierInfo.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            intImportCarrierInfo.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDate")) {
                            intImportCarrierInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            intImportCarrierInfo.setPc(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            intImportCarrierInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            intImportCarrierInfo.setVolume(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("ReportInfo")) {
                            assert xmlList != null;
                            xmlList.add(intImportCarrierInfo);
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
