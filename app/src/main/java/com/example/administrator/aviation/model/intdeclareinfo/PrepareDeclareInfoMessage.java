package com.example.administrator.aviation.model.intdeclareinfo;

import android.content.Context;
import android.util.Xml;

import com.example.administrator.aviation.model.homemessge.HomeMessage;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析联检申报状态信息
 */

public class PrepareDeclareInfoMessage {
    // 经典的pull解析方法
    public static List<DeclareInfoMessage> pullDeclareInfoXml (String xml) {
        List<DeclareInfoMessage> xmlList = null;
        DeclareInfoMessage declareInfoMessage = null;
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
                        xmlList = new ArrayList<DeclareInfoMessage>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("AWB")) {
                            declareInfoMessage = new DeclareInfoMessage();
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            declareInfoMessage.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("CIQNumber")) {
                           declareInfoMessage.setCIQNumber(parser.nextText());
                        }else if (name.equalsIgnoreCase("CIQStatus")) {
                            declareInfoMessage.setCIQStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("CMDStatus")) {
                            declareInfoMessage.setCMDStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("MftStatus")) {
                            declareInfoMessage.setMftStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("ArrivalStatus")) {
                            declareInfoMessage.setArrivalStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("LoadStatus")) {
                            declareInfoMessage.setLoadStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("TallyStatus")) {
                            declareInfoMessage.setTallyStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            declareInfoMessage.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            declareInfoMessage.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            declareInfoMessage.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("GPrice")) {
                            declareInfoMessage.setGPrice(parser.nextText());
                        } else if (name.equalsIgnoreCase("Shipper")) {
                            declareInfoMessage.setShipper(parser.nextText());
                        } else if (name.equalsIgnoreCase("OPDate")) {
                            declareInfoMessage.setOPDate(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("AWB")) {
                            assert xmlList != null;
                            xmlList.add(declareInfoMessage);
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
