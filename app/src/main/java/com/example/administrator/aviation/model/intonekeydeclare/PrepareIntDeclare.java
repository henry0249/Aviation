package com.example.administrator.aviation.model.intonekeydeclare;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析
 */

public class PrepareIntDeclare {
    // 经典的pull解析方法
    public static List<Declare> parseIntDeclareXml(String xml) {
        List<Declare> list = null;
        Declare declare = null;
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
                        list = new ArrayList<Declare>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        //Log.e("TAG","name="+parser.getName()+"\ntext="+parser.getText()+"\nn");
                        if (name.equalsIgnoreCase("Declare")) {
                            declare = new Declare();
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            declare.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("Hno")) {
                            declare.setHno(parser.nextText());
                        } else if (name.equalsIgnoreCase("RearchID")) {
                            declare.setRearchID(parser.nextText());
                        } else if (name.equalsIgnoreCase("CMDStatus")) {
                            declare.setCMDStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("SpCode")) {
                            declare.setSpCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("Goods")) {
                            declare.setGoods(parser.nextText());
                        } else if (name.equalsIgnoreCase("FDate")) {
                            declare.setFDate(parser.nextText());
                        }else if (name.equalsIgnoreCase("Fno")) {
                            declare.setFno(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest1")) {
                            declare.setDest1(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            declare.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("CustomsCode")) {
                            declare.setCustomsCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("Transportmode")) {
                            declare.setTransportmode(parser.nextText());
                        } else if (name.equalsIgnoreCase("FreightPayment")) {
                            declare.setFreightPayment(parser.nextText());
                        } else if (name.equalsIgnoreCase("CNEECity")) {
                            declare.setCNEECity(parser.nextText());
                        } else if (name.equalsIgnoreCase("CNEECountry")) {
                            declare.setCNEECountry(parser.nextText());
                        } else if (name.equalsIgnoreCase("MftStatus")) {
                            declare.setMftStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("MftMSGID")) {
                            declare.setMftMSGID(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            declare.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            declare.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            declare.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("ArrivalStatus")) {
                            declare.setArrivalStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("ArrMSGID")) {
                            declare.setArrMSGID(parser.nextText());
                        } else if (name.equalsIgnoreCase("Response")) {
                            declare.setResponse(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("Declare")) {
                            list.add(declare);
                        }
                        break;
                }
                eventType = parser.next();
            }
            tInputStringStream.close();
            // return persons;
        } catch (XmlPullParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
}
