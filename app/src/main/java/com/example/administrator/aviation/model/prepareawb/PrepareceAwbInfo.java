package com.example.administrator.aviation.model.prepareawb;

import android.text.TextUtils;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析运单详情的xml
 */

public class PrepareceAwbInfo {
    // 经典的pull解析方法
    public static List<MawbInfo> parseAwbInfoXml(String xml) {
        List<MawbInfo> list = null;
        MawbInfo mawbInfo = null;
        Mawbm mawbm = null;
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
                        list = new ArrayList<MawbInfo>();
                        break;
                    case XmlPullParser.START_TAG:// 开始元素事件
                        String name = parser.getName();
                        //Log.e("TAG","name="+parser.getName()+"\ntext="+parser.getText()+"\nn");
                        if (name.equalsIgnoreCase("MawbInfo")) {
                            mawbInfo = new MawbInfo();
                        }else if (name.equalsIgnoreCase("MawbID")) {
                            mawbInfo.setMawbID(parser.nextText());
                        } else if (name.equalsIgnoreCase("Mawb")) {
                            mawbInfo.setMawb(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            mawbInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("Weight")) {
                            mawbInfo.setWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("Volume")) {
                            mawbInfo.setVolume(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("SpCode")) {
                            mawbInfo.setSpCode(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Goods")) {
                            mawbInfo.setGoods(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("BusinessType")) {
                            mawbInfo.setBusinessType(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Package")) {
                            mawbInfo.setPackage(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("By")) {
                            mawbInfo.setBy(parser.nextText());
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
                        else if (name.equalsIgnoreCase("Remark")) {
                            mawbInfo.setRemark(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CargoType")) {
                            mawbInfo.setCargoType(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Mawbm")) {
                            mawbm = new Mawbm();
                        }
                        else if (name.equalsIgnoreCase("FlightChecked")) {
                            String nTxt = parser.nextText().trim();
                            if (!TextUtils.isEmpty(nTxt)) {
                                if (nTxt.equals("0")) {
                                    nTxt = "";
                                } else if (nTxt.equals("1")) {
                                    nTxt = "已审批";
                                }
                                mawbm.setFlightChecked(nTxt);
                            }
                        }
                        else if (name.equalsIgnoreCase("FDate")) {
                            mawbm.setFDate(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Fno")) {
                            mawbm.setFno(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Shipper")) {
                            mawbm.setShipper(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("ShipperTEL")) {
                            mawbm.setShipperTEL(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Consignee")) {
                            mawbm.setConsignee(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CNEETEL")) {
                            mawbm.setCNEETEL(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("TransportNO")) {
                            mawbm.setTransportNO(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("AllowTransNO")) {
                            mawbm.setAllowTransNO(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("ProTime")) {
                            mawbm.setProTime(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CIQNumber")) {
                            mawbm.setCIQNumber(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("Priority")) {
                            String nTxt = parser.nextText().trim();
                            if (!TextUtils.isEmpty(nTxt)) {
                                switch (nTxt) {
                                    case "1":
                                        nTxt = "1:特别级";
                                        break;
                                    case "2":
                                        nTxt = "2:优先级";
                                        break;
                                    case "3":
                                        nTxt = "3:普通级";
                                        break;
                                    case "4":
                                        nTxt = "4:重点级";
                                        break;
                                    default:
                                        nTxt = "";
                                        break;
                                }
                                mawbm.setPriority(nTxt);
                            }
                        }
                        else if (name.equalsIgnoreCase("CheckID")) {
                            mawbm.setCheckID(parser.nextText());
                        }
                        else if (name.equalsIgnoreCase("CheckTime")) {
                            mawbm.setCheckTime(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("MawbInfo")) {
                            list.add(mawbInfo);
                        }else if(parser.getName().equalsIgnoreCase("Mawbm")){
                            mawbInfo.setMawbm(mawbm);
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
