package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpCGOSplitSubLineArrival;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;

/**
 * 国际支线拆分支线拆分
 */

public class AppIntSplitSubLineArrivalActivity extends Activity implements View.OnClickListener{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String xml;

    private String rechid;
    private String pc;
    private String weight;
    private String volume;

    private String pcOne;
    private String weightOne;
    private String volumeOne;

    private String pcTwo;
    private String weightTwo;
    private String volumeTwo;

    private int intpc;
    private double doubleweight;
    private double doublevolume;

    private int intpcOne;
    private double doubleweightOne;
    private double doublevolumeOne;

    private int intpcTwo;
    private double doubleweightTwo;
    private double doublevolumeTwo;

    private EditText pcOneEt;
    private EditText weightOneEt;
    private EditText volumeOneEt;

    private EditText pcTwoEt;
    private EditText weightTwoEt;
    private EditText volumeTwoEt;

    private Button sureBtn;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintsplitesublinearrival);
        initView();
    }
    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle("支线拆分");

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        rechid = getIntent().getStringExtra(AviationCommons.SPLITE_REARCHID);
        pc = getIntent().getStringExtra(AviationCommons.SPLITE_PC);
        weight = getIntent().getStringExtra(AviationCommons.SPLITE_WEIGHT);
        volume = getIntent().getStringExtra(AviationCommons.SPLITE_VOLUME);

        pcOneEt = (EditText) findViewById(R.id.splite_pcone_et);
        weightOneEt = (EditText) findViewById(R.id.splite_weightone_et);
        weightOneEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeOneEt = (EditText) findViewById(R.id.splite_volumeone_et);
        volumeOneEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        pcTwoEt = (EditText) findViewById(R.id.splite_pctwo_et);
        weightTwoEt = (EditText) findViewById(R.id.splite_weighttwo_et);
        weightTwoEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeTwoEt = (EditText) findViewById(R.id.splite_volumetwo_et);
        volumeTwoEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        sureBtn = (Button) findViewById(R.id.splite_sure_btn);
        sureBtn.setOnClickListener(this);
        submitBtn = (Button) findViewById(R.id.splite_submit_btn);
        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.splite_sure_btn:
                getEditText();
                break;

            case R.id.splite_submit_btn:
                pcOne = pcOneEt.getText().toString().trim();
                weightOne = weightOneEt.getText().toString().trim();
                volumeOne = volumeOneEt.getText().toString().trim();
                xml = HttpCGOSplitSubLineArrival.getSpilteXml(rechid, pcOne, weightOne, volumeOne);
                new SpliteAsynck(xml).execute();
                break;

            default:
                break;
        }
    }

    //直线拆分
    private void getEditText() {
        pcOne = pcOneEt.getText().toString().trim();
        if (!pcOne.equals("") ) {
            intpcOne = Integer.parseInt(pcOne);
            intpc = Integer.parseInt(pc);
            doubleweight = Double.valueOf(weight);
            doublevolume = Double.valueOf(volume);
            intpcOne = Integer.parseInt(pcOne);
            double oneWeight = doubleweight / intpc;
            double oneVolume = doublevolume / intpc;
            DecimalFormat df = new DecimalFormat("0.0");
            intpcTwo = intpc - intpcOne;
            pcTwo = String.valueOf(intpcTwo);
            pcTwoEt.setText(pcTwo);
            doubleweightOne = oneWeight * intpcOne;
            weightOne = df.format(doubleweightOne);
            weightOneEt.setText(weightOne);
            doublevolumeOne = oneVolume * intpcOne;
            volumeOne = df.format(doublevolumeOne);
            volumeOneEt.setText(volumeOne);
            doubleweightTwo = oneWeight * intpcTwo;
            weightTwo = df.format(doubleweightTwo);
            weightTwoEt.setText(weightTwo);
            doublevolumeTwo = oneVolume * intpcTwo;
            volumeTwo = df.format(doublevolumeTwo);
            volumeTwoEt.setText(volumeTwo);
        } else {
            Toast.makeText(AppIntSplitSubLineArrivalActivity.this, "件数不能为空", Toast.LENGTH_SHORT).show();
        }
        pcOne = pcOneEt.getText().toString().trim();
        weightOne = weightOneEt.getText().toString().trim();
        volumeOne = volumeOneEt.getText().toString().trim();
    }

    private class SpliteAsynck extends AsyncTask<Void, Void, String> {
        String result = null;
        String xml;
        public SpliteAsynck (String xml) {
            this.xml = xml;
        }
        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpCGOSplitSubLineArrival.cGOSplitSubLineArrival(userBumen, userName, userPass, loginFlag, xml);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                result = object.getProperty(0).toString();
                if (result.equals("false")) {
                    ErrString = object.getProperty(1).toString();
                    return result;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (result == null && !ErrString.equals("")) {
                Toast.makeText(AppIntSplitSubLineArrivalActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntSplitSubLineArrivalActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("true")) {
                Toast.makeText(AppIntSplitSubLineArrivalActivity.this, "成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntSplitSubLineArrivalActivity.this, AppIntExpOneKeyDeclareActivity.class);
                startActivity(intent);
            }
        }
    }
}
