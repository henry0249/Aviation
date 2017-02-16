package com.example.administrator.aviation.ui.activity.house;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.house.HttpPrepareHouse;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.ChoseTimeMethod;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * AppDomExpWareHouse功能模块
 * 输入信息查询结果
 */

public class AppDomExpWareHouseActivity extends Activity implements View.OnClickListener{
    private EditText mawbEt;
    private EditText begainTimeEt;
    private EditText endTimeEt;
    private EditText destEt;
    private Button houseSearchBtn;

    private String mawb;
    private String begainTime;
    private String endTime;
    private String dest;

    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String xml;
    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();
    // 获取当前时间
    private String currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdomexpwarehouse);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle(R.string.house_guonei_ruku);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        mawbEt = (EditText) findViewById(R.id.house_mawb_et);
        begainTimeEt = (EditText) findViewById(R.id.house_begin_time_et);
        endTimeEt = (EditText) findViewById(R.id.house_end_time_et);
        destEt = (EditText) findViewById(R.id.house_dest_et);
        houseSearchBtn = (Button) findViewById(R.id.house_search_btn);
        Button beginTime = (Button) findViewById(R.id.house_begin_time_btn);
        Button endTime = (Button) findViewById(R.id.house_end_time_btn);
        currentTime = DateUtils.getTodayDateTime();
        begainTimeEt.setText(currentTime);
        endTimeEt.setText(currentTime);

        beginTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        houseSearchBtn.setOnClickListener(this);
    }

    private void getEditext() {
        mawb = mawbEt.getText().toString().trim();
        begainTime = begainTimeEt.getText().toString().trim();
        endTime = endTimeEt.getText().toString().trim();
        dest = destEt.getText().toString().trim();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.house_search_btn:
                getEditext();
                xml = HttpPrepareHouse.getHouseXml(mawb, dest);
                new GetHouseXmlAsyncTask().execute();
                break;

            case R.id.house_begin_time_btn:
                choseTimeMethod.getCurrentTime(AppDomExpWareHouseActivity.this, begainTimeEt);
                break;

            case R.id.house_end_time_btn:
                choseTimeMethod.getCurrentTime(AppDomExpWareHouseActivity.this, endTimeEt);
                break;

            default:
                break;
        }
    }

    // 上传xml到服务器
    class GetHouseXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareHouse.getHouseDetail(userBumen, userName,userPass,loginFlag, xml);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                String result = object.getProperty(0).toString();
                if (result.equals("false")) {
                    ErrString = object.getProperty(1).toString();
                    return result;
                } else {
                    result = object.getProperty(0).toString();
                    return result;
                }
            }
        }

        @Override
        protected void onPostExecute(String request) {
            if (request == null && !ErrString.equals("")) {
                Toast.makeText(AppDomExpWareHouseActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppDomExpWareHouseActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(AppDomExpWareHouseActivity.this, AppDomExpWareHouseItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("houseXml", request);
                intent.putExtras(bundle);
                startActivity(intent);
                // 上传成功后finish掉当前的activity
                finish();
            }
            super.onPostExecute(request);
        }
    }
}
