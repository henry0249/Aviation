package com.example.administrator.aviation.http.getintimpcargoinfo;

import android.util.Log;

import com.example.administrator.aviation.http.HttpCommons;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

/**
 * 进港货站信息修改分单
 */

public class HttpPrepareImpCargoChangeHno {
    public static SoapObject changeHno(String userBumen, String userName, String userPass, String loginFlag, String xml){
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
        SoapObject rpc = new SoapObject(HttpCommons.NAME_SPACE, HttpCommons.CGO_UPDATE_INT_IMPORT_HAWB_NAME);

        // 设置调用webservice接口需要传入的参数
        rpc.addProperty("hawbXml", xml);
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
            transportSE.call(HttpCommons.CGO_UPDATE_INT_IMPORT_HAWB_ACTION, envelope);
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
}
