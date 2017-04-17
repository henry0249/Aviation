package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

/**
 * 国际支线拆分支线拆分
 */

public class AppIntSplitSubLineArrivalActivity extends Activity implements View.OnClickListener{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

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
        volumeOneEt = (EditText) findViewById(R.id.splite_volumeone_et);

        pcTwoEt = (EditText) findViewById(R.id.splite_pctwo_et);
        weightTwoEt = (EditText) findViewById(R.id.splite_weighttwo_et);
        volumeTwoEt = (EditText) findViewById(R.id.splite_volumetwo_et);

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
                break;

            default:
                break;
        }
    }

    private void getEditText() {
        pcOne = pcOneEt.getText().toString().trim();
        pcTwo = pcTwoEt.getText().toString().trim();
        weightOne = weightOneEt.getText().toString().trim();
        weightTwo = weightTwoEt.getText().toString().trim();
        volumeOne = volumeOneEt.getText().toString().trim();
        volumeTwo = volumeTwoEt.getText().toString().trim();
        intpc = Integer.parseInt(pc);
        doubleweight = Double.valueOf(weight);
        doublevolume = Double.valueOf(volume);
        intpcOne = Integer.parseInt(pcOne);
//        doubleweightOne = Double.valueOf(weightOne);
//        doublevolumeOne = Double.valueOf(volumeOne);
//        intpcTwo = Integer.parseInt(pcTwo);
//        doubleweightTwo = Double.valueOf(weightTwo);
//        doublevolumeTwo = Double.valueOf(volumeTwo);
        if (!pcOne.equals("")) {
            intpcTwo = intpc - intpcOne;
            pcTwo = String.valueOf(intpcTwo);
            pcTwoEt.setText(pcTwo);
        }
    }
}
