package com.example.administrator.aviation.ui.activity.intexpawbhousemanage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.intExportawbofwarehouse.HttpIntawbPrepareHouse;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ChoseTimeMethod;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;

/**
 * 国际出港入库管理
 */

public class AppIntExpAWBHouseManageActivity extends Activity implements View.OnClickListener{
    private EditText mawbEt;
    private EditText begainTimeEt;
    private EditText endTimeEt;
    private Button houseSearchBtn;
    private ProgressBar wareHousePb;

    private String mawb;
    private String begainTime;
    private String endTime;
    private int countTime;

    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String xml;
    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    private GetIntHouseXmlAsyncTask task;
    // 获取当前时间
    private String currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexpwarehouse);
        initView();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (task != null && task.getStatus() == AsyncTask.Status.RUNNING) {
//            task.cancel(true);
//        }
    }

    @Override
    protected void onDestroy() {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(true);
        }
        super.onDestroy();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle(R.string.int_awb_house_title);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        mawbEt = (EditText) findViewById(R.id.int_house_mawb_et);
        begainTimeEt = (EditText) findViewById(R.id.int_house_begin_time_et);
        endTimeEt = (EditText) findViewById(R.id.int_house_end_time_et);
        wareHousePb = (ProgressBar) findViewById(R.id.int_ware_house_pb);
        houseSearchBtn = (Button) findViewById(R.id.int_house_search_btn);
        ImageView beginTime = (ImageView) findViewById(R.id.int_house_begin_time_btn);
        ImageView endTime = (ImageView) findViewById(R.id.int_house_end_time_btn);
        currentTime = DateUtils.getTodayDateTime();
        begainTimeEt.setText(currentTime);
        endTimeEt.setText(currentTime);

        beginTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        houseSearchBtn.setOnClickListener(this);
    }

    private void getEditext() throws ParseException {
        mawb = mawbEt.getText().toString().trim();
        begainTime = begainTimeEt.getText().toString().trim();
        endTime = endTimeEt.getText().toString().trim();
        countTime = DateUtils.daysBetween(begainTime, endTime);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.int_house_search_btn:
                wareHousePb.setVisibility(View.VISIBLE);
                try {
                    getEditext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (countTime>3) {
                    Toast.makeText(this, "时间差不能超过3天", Toast.LENGTH_LONG).show();
                    wareHousePb.setVisibility(View.GONE);
                } else {
                    xml = HttpIntawbPrepareHouse.getIntWareHouseXml(mawb, begainTime, endTime);
                    task = new GetIntHouseXmlAsyncTask();
                    task.execute();
                }
                break;

            case R.id.int_house_begin_time_btn:
                choseTimeMethod.getCurrentTime(AppIntExpAWBHouseManageActivity.this, begainTimeEt);
                break;

            case R.id.int_house_end_time_btn:
                choseTimeMethod.getCurrentTime(AppIntExpAWBHouseManageActivity.this, endTimeEt);
                break;

            default:
                break;
        }
    }

    // 上传xml到服务器
    class GetIntHouseXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            if (isCancelled()) {
                return null;
            }
            SoapObject object = HttpIntawbPrepareHouse.getIntWareHouseDetail(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntExpAWBHouseManageActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpAWBHouseManageActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(AppIntExpAWBHouseManageActivity.this, AppIntExpAwbHouseItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AviationCommons.INT_AWB_HOUSE, request);
                bundle.putString(AviationCommons.MANAGE_HOUSE_MAWAB, mawb);
                bundle.putString(AviationCommons.MANAGE_HOUSE_BEGAIN_TIME, begainTime);
                bundle.putString(AviationCommons.MANAGE_HOUSE_END_TIME, endTime);
                intent.putExtras(bundle);
                startActivity(intent);
                // 上传成功后finish掉当前的activity
//                finish();
                wareHousePb.setVisibility(View.GONE);
            }
            super.onPostExecute(request);
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            if (isCancelled()) {
                return;
            }
        }
    }
}
