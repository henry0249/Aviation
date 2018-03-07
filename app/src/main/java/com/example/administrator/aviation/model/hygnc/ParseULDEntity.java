package com.example.administrator.aviation.model.hygnc;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.administrator.aviation.util.AviationCommons;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/3/2.
 */

public class ParseULDEntity {
    public static List<ULDEntity> parseULDEntityXMLto (String xml) {
        List<ULDEntity> list = new ArrayList<>();

        try {
            ULDEntity uldEnt = null;
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

                        if (name.equalsIgnoreCase("ULDEntity")) {
                            uldEnt = new ULDEntity();
                        }else if (name.equalsIgnoreCase("ULD")) {
                            uldEnt.setULD(parser.nextText());
                        }else if (name.equalsIgnoreCase("ULDWeight")) {
                            uldEnt.setULDWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("MaxWeight")) {
                            uldEnt.setMaxWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("MaxVolume")) {
                            uldEnt.setMaxVolume(parser.nextText());
                        }else if (name.equalsIgnoreCase("ULDType")) {
                            uldEnt.setULDType(parser.nextText());
                        }else if (name.equalsIgnoreCase("ULDFlag")) {
                            uldEnt.setULDFlag(parser.nextText());
                        }else if (name.equalsIgnoreCase("Carrier")) {
                            uldEnt.setCarrier(parser.nextText());
                        }else if (name.equalsIgnoreCase("AirPort")) {
                            uldEnt.setAirPort(parser.nextText());
                        }else if (name.equalsIgnoreCase("Status")) {
                            uldEnt.setStatus(parser.nextText());
                        }else if (name.equalsIgnoreCase("Remark")) {
                            uldEnt.setRemark(parser.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("ULDEntity")) {
                            list.add(uldEnt);
                            uldEnt = null;
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
