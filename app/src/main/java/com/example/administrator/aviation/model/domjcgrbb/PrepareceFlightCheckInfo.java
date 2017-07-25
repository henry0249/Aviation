package com.example.administrator.aviation.model.domjcgrbb;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析订舱计划提交到服务器获取数据
 */

public class PrepareceFlightCheckInfo {
    // 经典的pull解析方法
    public static List<FlightCheckInfo> parseFlightCheckInfoInfoXml(String xml) {
        List<FlightCheckInfo> list = null;
        FlightCheckInfo flightCheckInfo = null;
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
                            flightCheckInfo = new FlightCheckInfo();
                        }else if (name.equalsIgnoreCase("Carrier")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setCarrier(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setFno(parser.nextText());
                        } else if (name.equalsIgnoreCase("Registeration")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setRegisteration(parser.nextText());
                        } else if (name.equalsIgnoreCase("AircraftCode")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setAircraftCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDest")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setFDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("JTZ")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setJTZ(parser.nextText());
                        } else if (name.equalsIgnoreCase("EstimatedTakeOff")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setEstimatedTakeOff(parser.nextText());
                        } else if (name.equalsIgnoreCase("MaxWeight")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setMaxWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("MaxVolume")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setMaxVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("UseableWeight")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setUseableWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("UseableVolume")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setUseableVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("WaitCheckInWeight")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setWaitCheckInWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("WaitCheckInVolume")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setWaitCheckInVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("DelTransWeight")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setDelTransWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("DelTransVolume")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setDelTransVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("InWeight")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setInWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("InVolume")) {
                            assert flightCheckInfo != null;
                            flightCheckInfo.setInVolume(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("PlanInfo")) {
                            assert list != null;
                            list.add(flightCheckInfo);
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
