package com.example.administrator.aviation.model.house;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析house提交到服务器获取数据
 */

public class PrepareceWhsInfo {
    // 经典的pull解析方法
    public static List<WhsInfo> parseWhsInfoXml(String xml) {
        List<WhsInfo> list = null;
        WhsInfo whsInfo = null;
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
                        list = new ArrayList<WhsInfo>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        //Log.e("TAG","name="+parser.getName()+"\ntext="+parser.getText()+"\nn");
                        if (name.equalsIgnoreCase("whsInfo")) {
                            whsInfo = new WhsInfo();
                        }else if (name.equalsIgnoreCase("Mawb")) {
                            whsInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("BusinessType")) {
                            whsInfo.setBusinessType(parser.nextText());
                        } else if (name.equalsIgnoreCase("awbPC")) {
                            whsInfo.setAwbPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            whsInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            whsInfo.setWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            whsInfo.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("SpCode")) {
                            whsInfo.setSpCode(parser.nextText());
                        } else if (name.equalsIgnoreCase("Goods")) {
                            whsInfo.setGoods(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dep")) {
                            whsInfo.setDep(parser.nextText());
                        } else if (name.equalsIgnoreCase("Dest")) {
                            whsInfo.setDest(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fdate")) {
                            whsInfo.setFdate(parser.nextText());
                        } else if (name.equalsIgnoreCase("Fno")) {
                            whsInfo.setFno(parser.nextText());
                        } else if (name.equalsIgnoreCase("PaperTime")) {
                            whsInfo.setPaperTime(parser.nextText());
                        } else if (name.equalsIgnoreCase("BillsNO")) {
                            whsInfo.setBillsNO(parser.nextText());
                        } else if (name.equalsIgnoreCase("OPDate")) {
                            whsInfo.setOPDate(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("whsInfo")) {
                            list.add(whsInfo);
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
