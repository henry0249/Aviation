package com.example.administrator.aviation.model.hygnc;

import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.example.administrator.aviation.model.domjcgrbb.FlightAWBPlanInfo;
import com.example.administrator.aviation.util.AviationCommons;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/1.
 */

public class ParseGNCmessage {
    public static List<GNCULDLoading> parseGNCULDLoadingXMLto (String xml) {
        List<GNCULDLoading> list = new ArrayList<>();

        try {
            GNCULDLoading LoadingInfo = null;
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
                        //Log.e("TAG","name="+parser.getName()+"\ntext="+parser.getText()+"\nn");
                        if (name.equalsIgnoreCase("Loading")) {
                            LoadingInfo = new GNCULDLoading();
                        } else if (name.equalsIgnoreCase("ID")) {
                            LoadingInfo.setID(parser.nextText());
                        } else if (name.equalsIgnoreCase("CarID")) {
                            LoadingInfo.setCarID(parser.nextText());
                        } else if (name.equalsIgnoreCase("FID")) {
                            LoadingInfo.setFID(parser.nextText());
                        } else if (name.equalsIgnoreCase("ULD")) {
                            LoadingInfo.setULD(parser.nextText());
                        } else if (name.equalsIgnoreCase("ULDFlag")) {
                            LoadingInfo.setULDFlag(parser.nextText());
                        } else if (name.equalsIgnoreCase("ULDWeight")) {
                            LoadingInfo.setULDWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("MaxWeight")) {
                            LoadingInfo.setMaxWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("MaxVolume")) {
                            LoadingInfo.setMaxVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("BoardType")) {
                            LoadingInfo.setBoardType(parser.nextText());
                        } else if (name.equalsIgnoreCase("NetWeight")) {
                            LoadingInfo.setNetWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("CargoWeight")) {
                            LoadingInfo.setCargoWeight(parser.nextText());
                        } else if (name.equalsIgnoreCase("Volume")) {
                            LoadingInfo.setVolume(parser.nextText());
                        } else if (name.equalsIgnoreCase("PC")) {
                            LoadingInfo.setPC(parser.nextText());
                        } else if (name.equalsIgnoreCase("CargoType")) {
                            String nTxt = parser.nextText().trim();
                            if (!TextUtils.isEmpty(nTxt)) {
                                loopCargoType:switch (nTxt) {
                                    case "C":
                                        LoadingInfo.setCargoType(nTxt + "-" + "货物");
                                        break;
                                    case "M":
                                        LoadingInfo.setCargoType(nTxt + "-" + "邮件");
                                        break;
                                    case "X":
                                        LoadingInfo.setCargoType(nTxt + "-" + "退空");
                                        break;
                                    default:
                                        LoadingInfo.setCargoType(nTxt);
                                        break;
                                }
                            }
                        } else if (name.equalsIgnoreCase("MailWeight")) {
                            LoadingInfo.setMailWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("Priority")) {
                            LoadingInfo.setPriority(parser.nextText());
                        }else if (name.equalsIgnoreCase("cFlag")) {
                            String nTxt = parser.nextText().trim();
                            if (!TextUtils.isEmpty(nTxt)) {
                                switch (nTxt) {
                                    case "0":
                                        LoadingInfo.setcFlag("");
                                        break;
                                    case "1":
                                        LoadingInfo.setcFlag(nTxt + "-" + "复磅");
                                        break;
                                    case "2":
                                        LoadingInfo.setcFlag(nTxt + "-" + "二次复磅");
                                        break ;
                                    case "3":
                                        LoadingInfo.setcFlag(nTxt + "-" + "配载确认");
                                        break ;
                                    case "4":
                                        LoadingInfo.setcFlag(nTxt + "-" + "直接录入");
                                        break ;
                                    default:
                                        LoadingInfo.setcFlag(nTxt);
                                        break ;
                                }
                            }
                        }else if (name.equalsIgnoreCase("Carrier")) {
                            LoadingInfo.setCarrier(parser.nextText());
                        }else if (name.equalsIgnoreCase("FDate")) {
                            String fda = parser.nextText();
                            if (!TextUtils.isEmpty(fda)) {
                                LoadingInfo.setFDate(fda + "_" + fda.replaceAll("-",""));
                            }
                        }else if (name.equalsIgnoreCase("Fno")) {
                            LoadingInfo.setFno(parser.nextText());
                        }else if (name.equalsIgnoreCase("Dest")) {
                            LoadingInfo.setDest(parser.nextText());
                        }else if (name.equalsIgnoreCase("Location")) {
                            LoadingInfo.setLocation(parser.nextText());
                        }else if (name.equalsIgnoreCase("Remark")) {
                            LoadingInfo.setRemark(parser.nextText());
                        }else if (name.equalsIgnoreCase("EmptyULD")) {
                            LoadingInfo.setEmptyULD(parser.nextText());
                        }else if (name.equalsIgnoreCase("FClose")) {
                            LoadingInfo.setFClose(parser.nextText());
                        }else if (name.equalsIgnoreCase("OPDate")) {
                            LoadingInfo.setOPDate(parser.nextText());
                        }else if (name.equalsIgnoreCase("OPID")) {
                            LoadingInfo.setOPID(parser.nextText());
                        }else if (name.equalsIgnoreCase("CarWeight")) {
                            LoadingInfo.setCarWeight(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("Loading")) {
                            list.add(LoadingInfo);
                            LoadingInfo = null;
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
