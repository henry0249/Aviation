package com.example.administrator.aviation.model.intjcgrbb;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析国际出港舱单信息
 */

public class PrepareIntExportDayMainfestInfo {
    // 经典的pull解析方法
    public static List<IntExportDayManifestInfo> pullExportDayManifetInfoXml(String xml) {
        List<IntExportDayManifestInfo> xmlList = null;
        IntExportDayManifestInfo intExportDayManifestInfo = null;
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
                        xmlList = new ArrayList<IntExportDayManifestInfo>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("ReportInfo")) {
                            intExportDayManifestInfo = new IntExportDayManifestInfo();
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            intExportDayManifestInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentCode")) {
                            intExportDayManifestInfo.setAgentCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentName")) {
                            intExportDayManifestInfo.setAgentName(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dep")) {
                            intExportDayManifestInfo.setDep(parser.nextText());
                        } else if (name.equalsIgnoreCase("Origin")) {
                            intExportDayManifestInfo.setOrigin(parser.nextText());
                        }else if (name.equalsIgnoreCase("Dest")) {
                            intExportDayManifestInfo.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("TotalPC")) {
                            intExportDayManifestInfo.setTotalPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            intExportDayManifestInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            intExportDayManifestInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            intExportDayManifestInfo.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("Goods")) {
                            intExportDayManifestInfo.setGoods(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("ReportInfo")) {
                            assert xmlList != null;
                            xmlList.add(intExportDayManifestInfo);
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
