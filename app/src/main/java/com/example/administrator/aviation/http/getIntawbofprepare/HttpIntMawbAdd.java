package com.example.administrator.aviation.http.getIntawbofprepare;

import android.util.Log;

import com.example.administrator.aviation.http.HttpCommons;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * 新增主单
 */

public class HttpIntMawbAdd {
    public static SoapObject addIntGroup(String userBumen, String userName, String userPass, String loginFlag, String xml){
        // 定义SoapHeader，加入4个节点
        Element[] healder = new Element[1];
        healder[0] = new Element().createElement(HttpCommons.NAME_SPACE, "AuthHeaderNKG");

        Element userBumenE = new Element().createElement(HttpCommons.NAME_SPACE, "IATACode");
        userBumenE.addChild(Node.TEXT, userBumen);
        healder[0].addChild(Node.ELEMENT, userBumenE);

        Element userNameE = new Element().createElement(HttpCommons.NAME_SPACE, "LoginID");
        userNameE.addChild(Node.TEXT, userName);
        healder[0].addChild(Node.ELEMENT, userNameE);

        Element userPassE = new Element().createElement(HttpCommons.NAME_SPACE, "Password");
        userPassE.addChild(Node.TEXT, userPass);
        healder[0].addChild(Node.ELEMENT, userPassE);

        Element loginFlagE = new Element().createElement(HttpCommons.NAME_SPACE, "LoginFlag");
        loginFlagE.addChild(Node.TEXT, loginFlag);
        healder[0].addChild(Node.ELEMENT, loginFlagE);

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(HttpCommons.NAME_SPACE, HttpCommons.ADD_INT_MAWB_METHOD_NAME);

        // 设置调用webservice接口需要传入的参数
        rpc.addProperty("mawbXml", xml);
        rpc.addProperty("ErrString", "");

        // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.headerOut = healder;
        envelope.bodyOut = rpc;

        // 设置是否调用的是dotNet开发的WebService
        envelope.dotNet = true;
        envelope.setOutputSoapObject(rpc);

        HttpTransportSE transportSE = new HttpTransportSE(HttpCommons.END_POINT);
        try {
            // 调用webservice
            transportSE.call(HttpCommons.ADD_INT_MAWB_METHOD_ACTION, envelope);
        } catch (Exception e){
            e.printStackTrace();
        }

        // 获取返回数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回结果
        if (object != null) {
            String result = object.toString();
            Log.e("text",result);
        }
        return object;
    }

    // 增加主单xml
    public static String addIntMawbXml(String mawb,String pc,String weight,String volume,
                                          String spcode,String goods, String goodsCN, String businessType,
                                          String pacKage,String origin, String dep,String dest1, String dest2,String by1,
                                          String tranFlag,String remark, String fDate, String fno, String customsCode,
                                          String transPortMode, String freightPayment, String shipper,
                                          String consignee, String gprice) {
        String xml = new String ("<IntExportPrepareAWB>"
                +"<MawbInfo>"
                +"<Mawb>"+ mawb + "</Mawb>"
                +"<PC>"+ pc + "</PC>"
                +"<Weight>" + weight + "</Weight>"
                +"<Volume>" + volume + "</Volume>"
                +"<SpCode>" + spcode + "</SpCode>"
                +"<Goods>" + goods + "</Goods>"
                +"<GoodsCN>" + goodsCN + "</GoodsCN>"
                +"<BusinessType>" + businessType + "</BusinessType>"
                +"<Package>" + pacKage + "</Package>"
                +"<Origin>" + origin + "</Origin>"
                +"<Dep>" + dep + "</Dep>"
                +"<Dest1>" + dest1 + "</Dest1>"
                +"<Dest2>" + dest2 + "</Dest2>"
                +"<By1>" + by1 + "</By1>"
                +"<TranFlag>" + tranFlag + "</TranFlag>"
                +"<Remark>" + remark + "</Remark>"
                +"<Mawbm>"
                +"<FDate>"+ fDate + "</FDate>"
                +"<Fno>"+ fno + "</Fno>"
                +"<CustomsCode>"+ customsCode + "</CustomsCode>"
                +"<TransPortMode>"+ transPortMode + "</TransPortMode>"
                +"<FreightPayment>"+ freightPayment + "</FreightPayment>"
                +"<Shipper>"+ shipper + "</Shipper>"
                +"<Consignee>"+ consignee + "</Consignee>"
                +"<Gprice>"+ gprice + "</Gprice>"
                +"</Mawbm>"
                +"</MawbInfo>"
                +"</IntExportPrepareAWB>");
        return xml;
    }
}
