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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/10/9.
 */

public class ParseGncVSLoading {
    public static List<GNCManifestVSLoading> parseULDEntityXMLto (String xml) {
        List<GNCManifestVSLoading> list = new ArrayList<>();

        try {
            GNCManifestVSLoading VSLoading = null;
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

                        if (name.equalsIgnoreCase("VSRow")) {
                            VSLoading = new GNCManifestVSLoading();
                        }else if (name.equalsIgnoreCase("CarID")) {
                            VSLoading.setCarID(parser.nextText());
                        }else if (name.equalsIgnoreCase("ULD")) {
                            VSLoading.setULD(parser.nextText());
                        }else if (name.equalsIgnoreCase("Dest")) {
                            VSLoading.setDest(parser.nextText());
                        }else if (name.equalsIgnoreCase("netWeight")) {
                            VSLoading.setNetWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("CargoWeight")) {
                            VSLoading.setCargoWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("awbWeight")) {
                            VSLoading.setAwbWeight(parser.nextText());
                        }else if (name.equalsIgnoreCase("Contrast")) {
                            VSLoading.setContrast(parser.nextText());
                        }else if (name.equalsIgnoreCase("Result")) {
                            VSLoading.setResult(parser.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                        if (parser.getName().equalsIgnoreCase("VSRow")) {

                            Double Contrast = Double.parseDouble(VSLoading.getContrast().trim());
                            Double CangDan = Double.parseDouble(VSLoading.getAwbWeight().trim());
                            if ( Contrast > 0 && CangDan > 0){
                                Double xx = Contrast / CangDan * 100;
                                String dou_str = PublicFun.DoubleToStr(xx,2);
                                VSLoading.setResult(dou_str);
                            }

                            list.add(VSLoading);
                            VSLoading = null;
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
