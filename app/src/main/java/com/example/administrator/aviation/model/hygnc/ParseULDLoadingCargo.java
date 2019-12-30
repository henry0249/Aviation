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
 * Created by Administrator on 2017/12/7.
 */

public class ParseULDLoadingCargo {
    public static List<ULDLoadingCargo> parseULDLoadingCargoXMLto (String xml,int f) {
        List<ULDLoadingCargo> list = new ArrayList<>();
        String key = "";
        String flag = "";
        if (f == 0) {
            flag = "CGOLoading";
        } else {
            flag = "fitCGOLoading";
        }

        try {
            ULDLoadingCargo LoadingInfo = null;
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

                        if (name.equalsIgnoreCase(flag)) {
                            key = name;
                        }

                        if (key.equalsIgnoreCase(flag)) {
                            if (name.equalsIgnoreCase(flag)) {
                                LoadingInfo = new ULDLoadingCargo();
                            } else if (name.equalsIgnoreCase("WHID")) {
                                LoadingInfo.setWHID(parser.nextText());
                            } else if (name.equalsIgnoreCase("Mawb")) {
                                LoadingInfo.setMawb(parser.nextText());
                            } else if (name.equalsIgnoreCase("AgentCode")) {
                                LoadingInfo.setAgentCode(parser.nextText());
                            } else if (name.equalsIgnoreCase("PC")) {
                                LoadingInfo.setPC(parser.nextText());
                            } else if (name.equalsIgnoreCase("Weight")) {
                                LoadingInfo.setWeight(parser.nextText());
                            } else if (name.equalsIgnoreCase("Volume")) {
                                LoadingInfo.setVolume(parser.nextText());
                            } else if (name.equalsIgnoreCase("SpCode")) {
                                LoadingInfo.setSpCode(parser.nextText());
                            } else if (name.equalsIgnoreCase("Goods")) {
                                LoadingInfo.setGoods(parser.nextText());
                            } else if (name.equalsIgnoreCase("Dest1")) {
                                LoadingInfo.setDest1(parser.nextText());
                            } else if (name.equalsIgnoreCase("Dest")) {
                                LoadingInfo.setDest(parser.nextText());
                            } else if (name.equalsIgnoreCase("By1")) {
                                LoadingInfo.setBy1(parser.nextText());
                            } else if (name.equalsIgnoreCase("FDate")) {
                                String nTxt = parser.nextText().trim();
                                if (!TextUtils.isEmpty(nTxt)) {
                                    nTxt += "_" + nTxt.replace("-", "");
                                    LoadingInfo.setFDate(nTxt);
                                }
                            }else if (name.equalsIgnoreCase("Fno")) {
                                LoadingInfo.setFno(parser.nextText());
                            }else if (name.equalsIgnoreCase("Location")) {
                                LoadingInfo.setLocation(parser.nextText());
                            }else if (name.equalsIgnoreCase("LocFlag")) {
                                LoadingInfo.setLocFlag(parser.nextText());
                            }else if (name.equalsIgnoreCase("LocID")) {
                                LoadingInfo.setLocID(parser.nextText());
                            }else if (name.equalsIgnoreCase("PlanFDate")) {
                                String nTxt = parser.nextText().trim();
                                if (!TextUtils.isEmpty(nTxt)) {
                                    nTxt += "_" + nTxt.replace("-", "");
                                    LoadingInfo.setPlanFDate(nTxt);
                                }
                            else if (name.equalsIgnoreCase("PlanFno")) {
                                LoadingInfo.setPlanFno(parser.nextText());
                            }
                            }else if (name.equalsIgnoreCase("Remark")) {
                                LoadingInfo.setRemark(parser.nextText());
                            }
                        }


                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase(flag)) {
                            list.add(LoadingInfo);
                            LoadingInfo = null;
                            key = "";
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
