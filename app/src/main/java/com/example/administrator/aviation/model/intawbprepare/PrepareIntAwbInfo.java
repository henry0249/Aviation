package com.example.administrator.aviation.model.intawbprepare;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 国际出港预录入解析
 */

public class PrepareIntAwbInfo {
    // 经典的pull解析方法
    public static List<MawbInfo> parseAwbInfoXml(String xml) {
        List<MawbInfo> list = null;
        List<Hawb> hawbList = null;
        MawbInfo mawbInfo = null;
        Mawbm mawbm = null;
        Hawb hawb = null;
        boolean pointToHawb = false;
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
                        if (name.equalsIgnoreCase("MawbInfo")) {
                            mawbInfo = new MawbInfo();
                            hawbList = new ArrayList<>();
                        }else if (name.equalsIgnoreCase("MawbID")) {
                            mawbInfo.setMawbID(parser.nextText());
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            mawbInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            if(pointToHawb ){
                                hawb.setPC(parser.nextText());
                            }else{
                                mawbInfo.setPC(parser.nextText());
                            }

                        } else if (name.equalsIgnoreCase("Weight")) {
                            if(pointToHawb ){
                                hawb.setWeight(parser.nextText());
                            }else{
                                mawbInfo.setWeight(parser.nextText());
                            }

                        }else if (name.equalsIgnoreCase("Volume")) {
                            if(pointToHawb ){
                                hawb.setVolume(parser.nextText());
                            }else{
                                mawbInfo.setVolume(parser.nextText());
                            }

                        }
                        else if (name.equalsIgnoreCase("SpCode")) {
                            mawbInfo.setSpCode(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Goods")) {
                            if(pointToHawb ){
                                hawb.setGoods(parser.nextText());
                            }else{
                                mawbInfo.setGoods(parser.nextText());
                            }

                        }
                        else if (name.equalsIgnoreCase("GoodsCN")) {
                            if(pointToHawb ){
                                hawb.setGoodsCN(parser.nextText());
                            }else{
                                mawbInfo.setGoodsCN(parser.nextText());
                            }
                        }

                        else if (name.equalsIgnoreCase("BusinessType")) {
                            mawbInfo.setBusinessType(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Package")) {
                            mawbInfo.setPackage(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Origin")) {
                            mawbInfo.setOrigin(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Dep")) {
                            mawbInfo.setDep(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Dest1")) {
                            mawbInfo.setDest1(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Dest2")) {
                            mawbInfo.setDest2(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("By1")) {
                            mawbInfo.setBy1(parser.nextText());
                        }
                        else if (name.equals("TranFlag")) {
                            mawbInfo.setTranFlag(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Remark")) {
                            mawbInfo.setRemark(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Mawbm")) {
                            mawbm = new Mawbm();
                        }
                        else if (name.equalsIgnoreCase("FDate")) {
                            mawbm.setFDate(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Fno")) {
                            mawbm.setFno(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CustomsCode")) {
                            mawbm.setCustomsCode(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("TransPortMode")) {
                            mawbm.setTransPortMode(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("FreightPayment")) {
                            mawbm.setFreightPayment(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CNEECity")) {
                            mawbm.setCNEECity(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CNEECountry")) {
                            mawbm.setCNEECountry(parser.nextText());
                        }

                        // 有疑问
                        else if (name.equalsIgnoreCase("MftStatus")) {
                            if(pointToHawb) {
                                hawb.setMftStatus(parser.nextText());
                            } else {
                                mawbm.setMftStatus(parser.nextText());
                            }
                        }
                        else if (name.equalsIgnoreCase("Shipper")) {
                            mawbm.setShipper(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Consignee")) {
                            mawbm.setConsignee(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Gprice")) {
                            mawbm.setGprice(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CIQStatus")) {
                            mawbm.setCIQStatus(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CIQNumber")) {
                            mawbm.setCIQNumber(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Hawb")) {
                            hawb = new Hawb();
                            pointToHawb = true;
                        }
                        else if (name.equalsIgnoreCase("HawbID")) {
                            hawb.setHawbID(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Hno")) {
                            hawb.setHno(parser.nextText());
                        }
//                        else if (name.equalsIgnoreCase("MftStatus")) {
//                            hawb.setMftStatus(parser.nextText());
//                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("MawbInfo")) {
                            mawbInfo.setHawb(hawbList);
                            list.add(mawbInfo);
                        }
                        else if(parser.getName().equalsIgnoreCase("Mawbm")){
                            mawbInfo.setMawbm(mawbm);
                        }
                        else if (parser.getName().equalsIgnoreCase("Hawb")) {
                            hawbList.add(hawb);
                            pointToHawb  = false;
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
