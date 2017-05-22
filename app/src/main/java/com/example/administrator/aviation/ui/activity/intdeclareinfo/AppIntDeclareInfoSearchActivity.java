package com.example.administrator.aviation.ui.activity.intdeclareinfo;

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
import com.example.administrator.aviation.http.getIntdeclareinfo.HttpIntDeclareInfo;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.activity.intexpawbhousemanage.AppIntExpAwbHouseItemActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ChoseTimeMethod;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;

/**
 * 出港联检状态查询界面
 */

public class AppIntDeclareInfoSearchActivity extends Activity implements View.OnClickListener{
    private EditText mawbEt;
    private EditText startTimeEt;
    private EditText endTimeEt;
    private ImageView startTimeIv;
    private ImageView endTimeIv;
    private Button searchBtn;
    private ProgressBar progressBar;

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

    private GetIntDeclareInfoXmlAsyncTask task;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    // 获取当前时间
    private String currentTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intdeclareinfo_search);
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
        navBar.setTitle("联检状态查询");
        navBar.hideRight();

        // 获取用户登录信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        mawbEt = (EditText) findViewById(R.id.declare_info_mawb_et);
        startTimeEt = (EditText) findViewById(R.id.declare_info_begin_time_et);
        endTimeEt = (EditText) findViewById(R.id.declare_info_end_time_et);
        startTimeIv = (ImageView) findViewById(R.id.declare_info_begin_time_btn);
        endTimeIv = (ImageView) findViewById(R.id.declare_info_end_time_btn);
        searchBtn = (Button) findViewById(R.id.declare_info_search_btn);
        progressBar = (ProgressBar) findViewById(R.id.declare_info_pb);

        currentTime = DateUtils.getTodayDateTime();

        startTimeEt.setText(currentTime);
        endTimeEt.setText(currentTime);

        // 点击事件
        startTimeIv.setOnClickListener(this);
        endTimeIv.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.declare_info_begin_time_btn:
                choseTimeMethod.getCurrentTime(AppIntDeclareInfoSearchActivity.this, startTimeEt);
                break;

            case R.id.declare_info_end_time_btn:
                choseTimeMethod.getCurrentTime(AppIntDeclareInfoSearchActivity.this, endTimeEt);
                break;

            case R.id.declare_info_search_btn:
                progressBar.setVisibility(View.VISIBLE);
                try {
                    getEditext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (countTime>3) {
                    Toast.makeText(this, "时间差不能超过3天", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    xml = HttpIntDeclareInfo.getIntDeclareInfoXml(mawb, begainTime, endTime);
                    task = new GetIntDeclareInfoXmlAsyncTask();
                    task.execute();
                }
                break;

            default:
                break;
        }

    }

    private void getEditext() throws ParseException {
        mawb = mawbEt.getText().toString().trim();
        begainTime = startTimeEt.getText().toString().trim();
        endTime = endTimeEt.getText().toString().trim();
        countTime = DateUtils.daysBetween(begainTime, endTime);
    }


    // 上传xml到服务器
    class GetIntDeclareInfoXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            if (isCancelled()) {
                return null;
            }
            SoapObject object = HttpIntDeclareInfo.getIntDeclareInfoDetail(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntDeclareInfoSearchActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntDeclareInfoSearchActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(AppIntDeclareInfoSearchActivity.this, AppIntDeclareInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AviationCommons.DECLARE_INFO_DEATIL, request);
//                bundle.putString(AviationCommons.MANAGE_HOUSE_MAWAB, mawb);
//                bundle.putString(AviationCommons.MANAGE_HOUSE_BEGAIN_TIME, begainTime);
//                bundle.putString(AviationCommons.MANAGE_HOUSE_END_TIME, endTime);
                intent.putExtras(bundle);
                startActivity(intent);
                // 上传成功后finish掉当前的activity
//                finish();
                progressBar.setVisibility(View.GONE);
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
