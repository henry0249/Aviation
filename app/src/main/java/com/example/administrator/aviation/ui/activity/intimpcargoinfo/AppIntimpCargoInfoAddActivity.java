package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoADD;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 进港货站信息新增分单界面
 */

public class AppIntimpCargoInfoAddActivity extends Activity implements View.OnClickListener{
    private EditText hnoEt;
    private EditText pcEt;
    private EditText weightEt;
    private EditText volumeEt;
    private EditText goodsEt;

    private Button sureBtn;

    private String mawb;
    private String hno;
    private String type;
    private String pc;
    private String weight;
    private String volume;
    private String goods;
    private String hawbID;

    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintimpcargoinfoadd);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("进港货站新增分单");
        navBar.hideRight();
        hnoEt = (EditText) findViewById(R.id.imp_hno_add_tv);
        pcEt = (EditText) findViewById(R.id.imp_pc_add_tv);
        weightEt = (EditText) findViewById(R.id.imp_weight_add_tv);
        volumeEt = (EditText) findViewById(R.id.imp_volume_add_tv);
        goodsEt = (EditText) findViewById(R.id.imp_goods_add_tv);

        hawbID = getIntent().getStringExtra(AviationCommons.IMP_HAWBID);
        mawb = getIntent().getStringExtra(AviationCommons.IMP_MAWB);
        type = getIntent().getStringExtra(AviationCommons.IMP_TYPE);

        sureBtn = (Button) findViewById(R.id.imp_sure_add_btn);
        sureBtn.setOnClickListener(this);
    }

    // 按钮点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imp_sure_add_btn:
                getEditText();
                xml = HttpPrepareImpCargoInfo.getAddUpdataDeleteXml(hawbID, mawb, hno, type, pc,weight, volume, goods, "");
                userBumen = PreferenceUtils.getUserBumen(this);
                userName = PreferenceUtils.getUserName(this);
                userPass = PreferenceUtils.getUserPass(this);
                loginFlag = PreferenceUtils.getLoginFlag(this);
                new AddXmlAsyncTask().execute();
                break;

            default:
                break;
        }

    }

    private void getEditText() {
        hno = hnoEt.getText().toString().trim();
        pc = pcEt.getText().toString().trim();
        weight = weightEt.getText().toString().trim();
        volume = volumeEt.getText().toString().trim();
        goods = goodsEt.getText().toString().trim();
    }

    // 上传xml到服务器
    private class AddXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareImpCargoADD.addCargodetail(userBumen, userName,userPass,loginFlag, xml);
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
            super.onPostExecute(request);
            if (request == null && !ErrString.equals("")) {
                Toast.makeText(AppIntimpCargoInfoAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntimpCargoInfoAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntimpCargoInfoAddActivity.this, "上传成功", Toast.LENGTH_LONG).show();
            }
        }
    }
}
