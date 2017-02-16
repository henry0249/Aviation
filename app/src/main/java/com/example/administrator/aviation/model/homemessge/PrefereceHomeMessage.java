package com.example.administrator.aviation.model.homemessge;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析登录得到xml数据
 */

public class PrefereceHomeMessage {
    // 经典的pull解析方法
    public static List<HomeMessage> pullXml (String xml) {
        List<HomeMessage> xmlList = null;
        HomeMessage homeMessage = null;
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
                        xmlList = new ArrayList<HomeMessage>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("Table")) {
                            homeMessage = new HomeMessage();
                        } else if (name.equalsIgnoreCase("NAME")) {
                            homeMessage.setName(parser.nextText());
                        } else if (name.equalsIgnoreCase("NameCN")) {
                            homeMessage.setNameCN(parser.nextText());
                        }else if (name.equalsIgnoreCase("ISREADONLY")) {
                            homeMessage.setIsreadonly(parser.nextText());
                        } else if (name.equalsIgnoreCase("ACTIVEDATE")) {
                            homeMessage.setActiveDate(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table")) {
                            assert xmlList != null;
                            xmlList.add(homeMessage);
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
