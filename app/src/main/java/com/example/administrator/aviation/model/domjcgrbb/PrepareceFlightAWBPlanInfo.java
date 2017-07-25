package com.example.administrator.aviation.model.domjcgrbb;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析订舱确认提交到服务器获取数据
 */

public class PrepareceFlightAWBPlanInfo {
    // 经典的pull解析方法
    public static List<FlightAWBPlanInfo> parseFlightAWBPlanInfoXml(String xml) {
        List<FlightAWBPlanInfo> list = null;
        FlightAWBPlanInfo flightAWBPlanInfo = null;
        ByteArrayInputStream tInputStringStream = null;
        try {
            if (xml != null && !xml.trim().equals("")) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            }
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(tInputStringStream, "UTF-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        //Log.e("TAG","name="+parser.getName()+"\ntext="+parser.getText()+"\nn");
                        if (name.equalsIgnoreCase("PlanInfo")) {
                            flightAWBPlanInfo = new FlightAWBPlanInfo();
                        }else if (name.equalsIgnoreCase("Mawb")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("awbType")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setAwbType(parser.nextText());
                        } else if (name.equalsIgnoreCase("cStatus")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setcStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentCode")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setAgentCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("AgentName")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setAgentName(parser.nextText());
                        } else if (name.equalsIgnoreCase("FlightChecked")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setFlightChecked(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("Goods")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setGoods(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setFno(parser.nextText());
                        } else if (name.equalsIgnoreCase("CheckID")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setCheckID(parser.nextText());
                        } else if (name.equalsIgnoreCase("CheckTime")) {
                            assert flightAWBPlanInfo != null;
                            flightAWBPlanInfo.setCheckTime(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("PlanInfo")) {
                            assert list != null;
                            list.add(flightAWBPlanInfo);
                        }
                        break;
                }
                eventType = parser.next();
            }
            assert tInputStringStream != null;
            tInputStringStream.close();
            // return persons;
        } catch (XmlPullParserException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
