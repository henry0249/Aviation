package com.example.administrator.aviation.model.edeclareinfo;

import android.util.Xml;

import com.example.administrator.aviation.model.intdeclareinfo.DeclareInfoMessage;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析出港承运人联检状态
 */

public class PrepareEdeclareInfo {
    // 经典的pull解析方法
    public static List<EdeclareInfo> pullEDeclareInfoXml (String xml) {
        List<EdeclareInfo> xmlList = null;
        EdeclareInfo edeclareInfo = null;
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
                        xmlList = new ArrayList<EdeclareInfo>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("ReportInfo")) {
                            edeclareInfo = new EdeclareInfo();
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            edeclareInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("Carrier")) {
                            edeclareInfo.setCarrier(parser.nextText());
                        }else if (name.equalsIgnoreCase("BusinessName")) {
                            edeclareInfo.setBusinessName(parser.nextText());
                        } else if (name.equalsIgnoreCase("CIQStatus")) {
                            edeclareInfo.setCIQStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("CMDStatus")) {
                            edeclareInfo.setCMDStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentCode")) {
                            edeclareInfo.setAgentCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentName")) {
                            edeclareInfo.setAgentName(parser.nextText());
                        } else if (name.equalsIgnoreCase("PaperTime")) {
                            edeclareInfo.setPaperTime(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            edeclareInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            edeclareInfo.setFno(parser.nextText());
                        } else if (name.equalsIgnoreCase("TotalPC")) {
                            edeclareInfo.setTotalPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            edeclareInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            edeclareInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            edeclareInfo.setVolume(parser.nextText());
                        }  else if (name.equalsIgnoreCase("Dest")) {
                            edeclareInfo.setDest(parser.nextText());
                        }  else if (name.equalsIgnoreCase("GoodsCN")) {
                            edeclareInfo.setGoodsCN(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("ReportInfo")) {
                            assert xmlList != null;
                            xmlList.add(edeclareInfo);
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
