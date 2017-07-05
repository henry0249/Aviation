package com.example.administrator.aviation.model.intanddomflight;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析航班动态数据
 */

public class PrepareFlightMessage {
    // 经典的pull解析方法
    public static List<FlightMessage> pullFlightXml (String xml) {
        List<FlightMessage> xmlList = null;
        FlightMessage flightMessage = null;
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
                        xmlList = new ArrayList<FlightMessage>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("FlightInfo")) {
                            flightMessage = new FlightMessage();
                        } else if (name.equalsIgnoreCase("FDate")) {
                            flightMessage.setfDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            flightMessage.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDep")) {
                            flightMessage.setfDep(parser.nextText());
                        } else if (name.equalsIgnoreCase("JTZ")) {
                            flightMessage.setJtz(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDest")) {
                            flightMessage.setfDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("ServiceType")) {
                            flightMessage.setServiceType(parser.nextText());
                        } else if (name.equalsIgnoreCase("CountryType")) {
                            flightMessage.setCountryType(parser.nextText());
                        } else if (name.equalsIgnoreCase("EstimatedTakeOff")) {
                            flightMessage.setEstimatedTakeOff(parser.nextText());
                        } else if (name.equalsIgnoreCase("ActualTakeOff")) {
                            flightMessage.setActualTakeOff(parser.nextText());
                        } else if (name.equalsIgnoreCase("EstimatedArrival")) {
                            flightMessage.setEstimatedArrival(parser.nextText());
                        } else if (name.equalsIgnoreCase("ActualArrival")) {
                            flightMessage.setActualArrival(parser.nextText());
                        } else if (name.equalsIgnoreCase("Registeration")) {
                            flightMessage.setRegisteration(parser.nextText());
                        } else if (name.equalsIgnoreCase("AircraftCode")) {
                            flightMessage.setAircraftCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("StandID")) {
                            flightMessage.setStandID(parser.nextText());
                        } else if (name.equalsIgnoreCase("FlightStatus")) {
                            flightMessage.setFlightStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("FlightTerminalID")) {
                            flightMessage.setFlightTerminalID(parser.nextText());
                        } else if (name.equalsIgnoreCase("DelayFreeText")) {
                            flightMessage.setDelayFreeText(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("FlightInfo")) {
                            assert xmlList != null;
                            xmlList.add(flightMessage);
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
