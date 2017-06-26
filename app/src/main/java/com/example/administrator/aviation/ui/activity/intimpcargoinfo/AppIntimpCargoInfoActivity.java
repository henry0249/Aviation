package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

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
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoInfo;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ChoseTimeMethod;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.ParseException;

/**
 * 进港.货站信息查询界面
 */

public class AppIntimpCargoInfoActivity extends Activity implements View.OnClickListener{
    private EditText mawbEt;
    private EditText fnoEt;
    private EditText begainTimeEt;
    private EditText endtimeEt;
    private ImageView begainTimeIv;
    private ImageView endTimeIv;
    private Button searchBtn;
    private ProgressBar progressBar;

    private String xml;
    private String mawb;
    private String fno;
    private String begainTime;
    private String endTime;
    private int countTime;

    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

    private GetIntImCargoXmlAsyncTask task;

    // 获取当前时间
    private String currentTime;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimpcargoinfo);
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
        navBar.setTitle("进港货站信息查询");
        navBar.hideRight();
        mawbEt = (EditText) findViewById(R.id.cargoinfo_mawb_et);
        fnoEt = (EditText) findViewById(R.id.cargoinfo_hno_et);
        fnoEt.setTransformationMethod(new AllCapTransformationMethod());
        begainTimeEt = (EditText) findViewById(R.id.cargoinfo_begin_time_et);
        endtimeEt = (EditText) findViewById(R.id.cargoinfo_end_time_et);
        begainTimeIv = (ImageView) findViewById(R.id.cargoinfo_begin_time_iv);
        endTimeIv = (ImageView) findViewById(R.id.cargoinfo_end_time_iv);
        searchBtn = (Button) findViewById(R.id.cargoinfo_search_btn);
        progressBar = (ProgressBar) findViewById(R.id.cargoinfo_pb);

        currentTime = DateUtils.getTodayDateTime();
        begainTimeEt.setText(currentTime);
        endtimeEt.setText(currentTime);

        // 获取用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        begainTimeIv.setOnClickListener(this);
        endTimeIv.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cargoinfo_begin_time_iv:
                choseTimeMethod.getCurrentTime(AppIntimpCargoInfoActivity.this, begainTimeEt);
                break;

            case R.id.cargoinfo_end_time_iv:
                choseTimeMethod.getCurrentTime(AppIntimpCargoInfoActivity.this, endtimeEt);
                break;

            case R.id.cargoinfo_search_btn:
                progressBar.setVisibility(View.VISIBLE);
                try {
                    getEditextValue();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (countTime>3) {
                    Toast.makeText(this, "时间差不能超过3天", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    xml = HttpPrepareImpCargoInfo.getImpCargoXml(mawb, fno,begainTime, endTime);
                    task = new GetIntImCargoXmlAsyncTask();
                    task.execute();
                }
                break;

            default:
                break;
        }
    }

    private void getEditextValue() throws ParseException {
        mawb = mawbEt.getText().toString().trim();
        fno = fnoEt.getText().toString().trim();
        fno = fno.toUpperCase();
        begainTime = begainTimeEt.getText().toString().trim();
        endTime = endtimeEt.getText().toString().trim();
        countTime = DateUtils.daysBetween(begainTime, endTime);
    }

    // 上传xml到服务器
    class GetIntImCargoXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            if (isCancelled()) {
                return null;
            }
            SoapObject object = HttpPrepareImpCargoInfo.getImpCargoInfo(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntimpCargoInfoActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntimpCargoInfoActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else{
                Intent intent = new Intent(AppIntimpCargoInfoActivity.this, AppIntimpCarGoInfoItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(AviationCommons.IMP_CARGO_INFO, request);
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
