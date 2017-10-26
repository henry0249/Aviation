package com.example.administrator.aviation.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.util.Map;
import java.util.Set;

/**
 * 网络请求公用类
 */

public class HttpRoot {
    private static HttpRoot instance;
    private Handler handler;

    private HttpRoot() {
        handler = new Handler();
    }

    public static HttpRoot getInstance() {
        if (instance == null) {
            synchronized (HttpRoot.class) {
                if (instance == null) {
                    instance = new HttpRoot();
                }
            }
        }
        return instance;
    }

    public void requstAync(final Context context, final String methodName, final String methodAction, final Map<String, String> params, final CallBack callBack) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                final SoapObject object = callService(context, methodName, methodAction, params);
                if (callBack != null) {
                    if (object == null) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
//                                Toast.makeText(context, "请检查网络状态", Toast.LENGTH_SHORT).show();
                                ToastUtils.showToast(context,"请检查网络状态",Toast.LENGTH_LONG);
                                callBack.onError();
                            }
                        });
                    } else {
                        String result = object.getProperty(0).toString();
                        if (result.equals("false") || result.equals("anyType{}")) {
                            final String errString = object.getProperty(1).toString();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if  (null != errString &&!errString.equals(""))
//                                        Toast.makeText(context, errString, Toast.LENGTH_LONG).show();
                                    ToastUtils.showToast(context,errString,Toast.LENGTH_LONG);
                                    callBack.onFailed(errString);
                                }
                            });
                        }else {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    callBack.onSucess(object);
                                }
                            });
                        }
                        return result;
                    }
                }
                return null;
            }
        }.execute();
    }


    private SoapObject callService(Context context, String methodName, String methodAction, Map<String, String> params) {
        // 定义SoapHeader，加入4个节点
        Element[] healder = new Element[1];
        healder[0] = new Element().createElement(HttpCommons.NAME_SPACE, "AuthHeaderNKG");

        Element userBumenE = new Element().createElement(HttpCommons.NAME_SPACE, "IATACode");
        userBumenE.addChild(Node.TEXT, PreferenceUtils.getUserBumen(context));
        healder[0].addChild(Node.ELEMENT, userBumenE);

        Element userNameE = new Element().createElement(HttpCommons.NAME_SPACE, "LoginID");
        userNameE.addChild(Node.TEXT, PreferenceUtils.getUserName(context));
        healder[0].addChild(Node.ELEMENT, userNameE);

        Element userPassE = new Element().createElement(HttpCommons.NAME_SPACE, "Password");
        userPassE.addChild(Node.TEXT, PreferenceUtils.getUserPass(context));
        healder[0].addChild(Node.ELEMENT, userPassE);

        Element loginFlagE = new Element().createElement(HttpCommons.NAME_SPACE, "LoginFlag");
        loginFlagE.addChild(Node.TEXT, PreferenceUtils.getLoginFlag(context));
        healder[0].addChild(Node.ELEMENT, loginFlagE);

        // 指定WebService的命名空间和调用的方法名
        SoapObject rpc = new SoapObject(HttpCommons.NAME_SPACE, methodName);

        Set<String> keys = params.keySet();
        for (String key : keys) {
            // 设置调用webservice接口需要传入的参数
            rpc.addProperty(key, params.get(key));
        }

//        rpc.addProperty("hawbXml", xml);
//        rpc.addProperty("ErrString", "");

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
            transportSE.call(methodAction, envelope);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 获取返回数据
        SoapObject object = (SoapObject) envelope.bodyIn;
        // 获取返回结果
        if (object != null) {
            String result = object.toString();
            Log.e("text", result);
        }
        return object;
    }


    public interface CallBack {
        void onSucess(Object result);//服务器成功返回数据
        void onFailed(String message);//服务器返回了错误的消息
        void onError();//没有网络
    }

    private CallBack callBack;
}
