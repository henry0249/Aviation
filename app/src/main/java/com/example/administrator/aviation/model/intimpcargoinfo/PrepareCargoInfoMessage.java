package com.example.administrator.aviation.model.intimpcargoinfo;

import android.util.Xml;

import com.example.administrator.aviation.model.intdeclareinfo.DeclareInfoMessage;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析联检申报状态信息
 */

public class PrepareCargoInfoMessage {
    // 经典的pull解析方法
    public static List<CargoInfoMessage> pullCargoInfoXml (String xml) {
        List<CargoInfoMessage> xmlList = null;
        CargoInfoMessage cargoInfoMessage = null;
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
                        xmlList = new ArrayList<CargoInfoMessage>();
                        break;

                    // 判断当前事件是不是标签元素的开始事件
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("awbInfo")) {
                            cargoInfoMessage = new CargoInfoMessage();
                        } else if (name.equalsIgnoreCase("FDate")) {
                            cargoInfoMessage.setFDate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                           cargoInfoMessage.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("awbID")) {
                            cargoInfoMessage.setAwbID(parser.nextText());
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            cargoInfoMessage.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("Hno")) {
                            cargoInfoMessage.setHno(parser.nextText());
                        } else if (name.equalsIgnoreCase("awbPC")) {
                            cargoInfoMessage.setAwbPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            cargoInfoMessage.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            cargoInfoMessage.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            cargoInfoMessage.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("Goods")) {
                            cargoInfoMessage.setGoods(parser.nextText());
                        } else if (name.equalsIgnoreCase("Origin")) {
                            cargoInfoMessage.setOrigin(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dep")) {
                            cargoInfoMessage.setDep(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            cargoInfoMessage.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("BusinessType")) {
                            cargoInfoMessage.setBusinessType(parser.nextText());
                        } else if (name.equalsIgnoreCase("awbTypeName")) {
                            cargoInfoMessage.setAwbTypeName(parser.nextText());
                        } else if (name.equalsIgnoreCase("BusinessName")) {
                            cargoInfoMessage.setBusinessName(parser.nextText());
                        } else if (name.equalsIgnoreCase("isEDIAWB")) {
                            cargoInfoMessage.setIsEDIAWB(parser.nextText());
                        } else if (name.equalsIgnoreCase("mftStatus")) {
                            cargoInfoMessage.setMftStatus(parser.nextText());
                        } else if (name.equalsIgnoreCase("tallyStatus")) {
                            cargoInfoMessage.setTallyStatus(parser.nextText());
                        }
                        break;

                    // 判断当前事件是否为标签结束事件
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equalsIgnoreCase("awbInfo")) {
                            assert xmlList != null;
                            xmlList.add(cargoInfoMessage);
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
