package com.example.administrator.aviation.http.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.administrator.aviation.LoginActivity;
import com.example.administrator.aviation.ui.activity.UserHomePageActivity;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

public class UserLogin extends AsyncTask<Object, Object, String> {
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private Activity activity;

    public UserLogin(String userBumen, String userName, String userPass, Activity activity) {
        this.userBumen = userBumen;
        this.userName = userName;
        this.userPass = userPass;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (activity instanceof LoginActivity) {
            ((LoginActivity) activity).showProgressBar();
        }
    }

    @Override
    protected String doInBackground(Object... objects) {
        SoapObject object = HttpParams.login(userBumen, userName, userPass);
        if (object == null) {
            ErrString = "服务器响应失败";
            return null;
        } else {
            String result = object.getProperty(0).toString();
            ErrString = object.getProperty(2).toString();
            if (result.equals("anyType{}") || result.equals("False")) {
                ErrString = object.getProperty(2).toString();
                return ErrString;
            }else {
                // 得到每次请求传回的ID值（此ID时每次登录都不一样）并保存
                AviationCommons.LoginFlag = object.getProperty(1).toString();
                PreferenceUtils.saveLoginFlag(activity, AviationCommons.LoginFlag);

                result = object.getProperty(0).toString();

                return result;
            }
        }

    }

    @Override
    protected void onPostExecute(String request) {
        if (activity instanceof LoginActivity) {
            ((LoginActivity)activity).dismissProgressBar();
        }
        if (!ErrString.equals("anyType{}") ) {
//            Toast.makeText(activity, ErrString, Toast.LENGTH_LONG).show();
            ToastUtils.showToast(activity, ErrString, Toast.LENGTH_LONG);
        } else if ( request.equals("anyType{}")) {
//            Toast.makeText(activity, ErrString, Toast.LENGTH_LONG).show();
            ToastUtils.showToast(activity, ErrString, Toast.LENGTH_LONG);
        }  else {
            PreferenceUtils.saveUser(activity, userBumen, userName, userPass);
            PreferenceUtils.saveIsFirst(activity);
            Intent intent = new Intent(activity, UserHomePageActivity.class);

            // 网络请求获取的xml值传递
            intent.putExtra(AviationCommons.LOGIN_XML, request);
            activity.startActivity(intent);
            activity.finish();
        }
        super.onPostExecute(request);
    }
}

