package com.example.administrator.aviation.model.gnj;

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
 * Created by 46296 on 2020/8/27.
 */

public class gnjPickUpConverter {
    public static List<gnjPickUpModel> gnjPickUpXMLtoMdoel (String xml) {
        List<gnjPickUpModel> list = new ArrayList<>();

        try {
            gnjPickUpModel pickup = null;
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

                        if (name.equalsIgnoreCase("AWBInfo")) {
                            pickup = new gnjPickUpModel();
                        }else if (name.equalsIgnoreCase("ID")) {
                            pickup.setID(parser.nextText());
                        }else if (name.equalsIgnoreCase("PKID")) {
                            pickup.setPKID(parser.nextText());
                        }else if (name.equalsIgnoreCase("Mawb")) {
                            pickup.setMawb(parser.nextText());
                        }else if (name.equalsIgnoreCase("CHGMode")) {
                            pickup.setCHGMode(parser.nextText());
                        }else if (name.equalsIgnoreCase("AgentCode")) {
                            pickup.setAgentCode(parser.nextText());
                        }else if (name.equalsIgnoreCase("AWBPC")) {
                            pickup.setAWBPC(parser.nextText());
                        }else if (name.equalsIgnoreCase("PC")) {
                            pickup.setPC(parser.nextText());
                        }else if (name.equalsIgnoreCase("SpCode")) {
                            pickup.setSpCode(parser.nextText());
                        }else if (name.equalsIgnoreCase("Goods")) {
                            pickup.setGoods(parser.nextText());
                        }else if (name.equalsIgnoreCase("Origin")) {
                            pickup.setOrigin(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDate")) {
                            pickup.setFDate(parser.nextText());
                        }else if (name.equalsIgnoreCase("Fno")) {
                            pickup.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("ChargeTime")) {
                            pickup.setChargeTime(parser.nextText());
                        }else if (name.equalsIgnoreCase("PickFlag")) {
                            pickup.setPickFlag(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVTime")) {
                            pickup.setDLVTime(parser.nextText());
                        }else if (name.equalsIgnoreCase("CNEName")) {
                            pickup.setCNEName(parser.nextText());
                        }else if (name.equalsIgnoreCase("CNEIDType")) {
                            pickup.setCNEIDType(parser.nextText());
                        }else if (name.equalsIgnoreCase("CNEID")) {
                            pickup.setCNEID(parser.nextText());
                        }else if (name.equalsIgnoreCase("CNEPhone")) {
                            pickup.setCNEPhone(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVName")) {
                            pickup.setDLVName(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVIDType")) {
                            pickup.setDLVIDType(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVID")) {
                            pickup.setDLVID(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVPhone")) {
                            pickup.setDLVPhone(parser.nextText());
                        }else if (name.equalsIgnoreCase("REFID")) {
                            pickup.setREFID(parser.nextText());
                        }
                        
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("AWBInfo")) {
                            list.add(pickup);
                            pickup = null;
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

    public static List<gnjPickUpModel> gnjSignXMLtoMdoel (String xml) {
        List<gnjPickUpModel> list = new ArrayList<>();

        try {
            gnjPickUpModel pickup = null;
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

                        if (name.equalsIgnoreCase("SignInfo")) {
                            pickup = new gnjPickUpModel();
                        }else if (name.equalsIgnoreCase("PKID")) {
                            pickup.setPKID(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVTime")) {
                            pickup.setDLVTime(parser.nextText());
                        }else if (name.equalsIgnoreCase("CNEIDCard")) {
                            pickup.setCNEIDCard(parser.nextText());
                        }else if (name.equalsIgnoreCase("DLVIDCard")) {
                            pickup.setDLVIDCard(parser.nextText());
                        }else if (name.equalsIgnoreCase("Sign")) {
                            pickup.setSign(parser.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("SignInfo")) {
                            list.add(pickup);
                            pickup = null;
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

    public static String SwitchPickUpFlag(String xx){
        String result = "";
        switch (xx) {
            case "0":
                result = "待出库";
                break;
            case "1":
                result = "出库中";
                break;
            case "2":
                result = "待提取";
                break;
            case "3":
                result = "已提取";
                break;
            case "待出库":
                result = "0";
                break;
            case "出库中":
                result = "1";
                break;
            case "待提取":
                result = "2";
                break;
            case "已提取":
                result = "3";
                break;
        }

        return result;
    };
}
