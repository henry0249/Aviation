package com.example.administrator.aviation.model.homemessge;

import android.content.Context;
import android.util.Xml;

import com.example.administrator.aviation.util.PreferenceUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析登录得到xml数据
 */

public class PrefereceHomeMessage {
    // 经典的pull解析方法
    public static List<HomeMessage> pullXml (String xml,Context context) {
        List<HomeMessage> xmlList = null;
        HomeMessage homeMessage = null;
        AppConfig appConfig = null;
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
                        } else if (name.equalsIgnoreCase("AppConfig")) {
                            appConfig = new AppConfig();
                        } else if (name.equalsIgnoreCase("APPVersion")) {
                            appConfig.setAPPVersion(parser.nextText());
                        } else if (name.equalsIgnoreCase("APPDescribe")) {
                            appConfig.setAPPDescribe(parser.nextText());
                        } else if (name.equalsIgnoreCase("APPURL")) {
                            appConfig.setAPPURL(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("Table")) {
                            assert xmlList != null;
                            xmlList.add(homeMessage);
                        } else if (parser.getName().equalsIgnoreCase("AppConfig")) {
                            homeMessage.setAppconfig(appConfig);

                            // 保存版本升级的内容
                            String version = appConfig.getAPPVersion();
                            String describe = appConfig.getAPPDescribe();
                            String url = appConfig.getAPPURL();
                            PreferenceUtils.saveAPPVersion(context,version);
                            PreferenceUtils.saveAPPDescribe(context,describe);
                            PreferenceUtils.saveAppUrl(context,url);

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
