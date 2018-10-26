package com.example.administrator.aviation.model.hygnc;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.util.AviationCommons;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/17.
 */

public class ParseGncFlightControl {
    public static List<GncFlightControl> parseFlightControlToEntity (String xml) {
        List<GncFlightControl> list = new ArrayList<>();

        try {
            GncFlightControl flightControl = null;
            ByteArrayInputStream tInputStringStream = null;

            if (!TextUtils.isEmpty(xml.trim())) {
                tInputStringStream = new ByteArrayInputStream(xml.getBytes());
            } else {
                return list;
            }

            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(tInputStringStream, "UTF-8");
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:// 文档开始事件,可以进行数据初始化处理
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();

                        if (name.equalsIgnoreCase("FlightRow")) {
                            flightControl = new GncFlightControl();
                        }else if (name.equalsIgnoreCase("aFDate")) {
                            flightControl.setaFDate(parser.nextText());
                        }else if (name.equalsIgnoreCase("aFno")) {
                            flightControl.setaFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("aSegment")) {
                            flightControl.setaSegment(parser.nextText());
                        }else if (name.equalsIgnoreCase("PassWeight")) {
                            flightControl.setPassWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("aETakeOff ")) {
                            flightControl.setaETakeOff(parser.nextText());
                        }else if (name.equalsIgnoreCase("aATakeOff")) {
                            flightControl.setaATakeOff(parser.nextText());
                        }else if (name.equalsIgnoreCase("aEArrival")) {
                            flightControl.setaEArrival(parser.nextText());
                        }else if (name.equalsIgnoreCase("aAArivall")) {
                            flightControl.setaAArivall(parser.nextText());
                        }else if (name.equalsIgnoreCase("FID")) {
                            flightControl.setFID(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDate")) {
                            flightControl.setFDate(parser.nextText());
                        }else if (name.equalsIgnoreCase("Fno")) {
                            flightControl.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("dSegment")) {
                            flightControl.setdSegment(parser.nextText());
                        }else if (name.equalsIgnoreCase("lNumber")) {
                            flightControl.setlNumber(parser.nextText());
                        }else if (name.equalsIgnoreCase("netWeight")) {
                            flightControl.setNetWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("dETakeOff")) {
                            flightControl.setdETakeOff(parser.nextText());
                        }else if (name.equalsIgnoreCase("dATackOff")) {
                            flightControl.setdATackOff(parser.nextText());
                        }else if (name.equalsIgnoreCase("dEArrival")) {
                            flightControl.setdEArrival(parser.nextText());
                        }else if (name.equalsIgnoreCase("FlightStatus")) {
                            flightControl.setFlightStatus(parser.nextText());
                        }else if (name.equalsIgnoreCase("DelayFreeText")) {
                            flightControl.setDelayFreeText(parser.nextText());
                        }else if (name.equalsIgnoreCase("Registeration")) {
                            flightControl.setRegisteration(parser.nextText());
                        }else if (name.equalsIgnoreCase("AirCraftCode")) {
                            flightControl.setAirCraftCode(parser.nextText());
                        }else if (name.equalsIgnoreCase("TallyStart")) {
                            flightControl.setTallyStart(parser.nextText());
                        }else if (name.equalsIgnoreCase("TallyEnd")) {
                            flightControl.setTallyEnd(parser.nextText());
                        }else if (name.equalsIgnoreCase("OutBound")) {
                            flightControl.setOutBound(parser.nextText());
                        }else if (name.equalsIgnoreCase("CheckTime")) {
                            flightControl.setCheckTime(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("FlightRow")) {
                            list.add(flightControl);
                            flightControl = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
            tInputStringStream.close();
        } catch (XmlPullParserException | IOException e) {
            // TODO Auto-generated catch block
            Log.d(AviationCommons.Log_TAG, e.toString());
        } catch (Exception e) {
            Log.d(AviationCommons.Log_TAG, e.toString());
        }

        return list;
    }
}
