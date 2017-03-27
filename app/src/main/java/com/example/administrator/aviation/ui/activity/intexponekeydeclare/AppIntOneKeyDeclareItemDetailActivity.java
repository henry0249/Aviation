package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

/**
 * Created by mengxing on 2017/3/27.
 * 预配与运抵详情界面
 */

public class AppIntOneKeyDeclareItemDetailActivity extends Activity{
    private TextView mawbTv;
    private TextView hnoTv;
    private TextView rearchIDTv;
    private TextView cMDStatusTv;
    private TextView spCodeTv;
    private TextView goodsTv;
    private TextView fDateTv;
    private TextView fnoTv;
    private TextView dest1Tv;
    private TextView destTv;
    private TextView customsCodeTv;
    private TextView transportmodeTv;
    private TextView freightPaymentTv;
    private TextView cNEECityTv;
    private TextView cNEECountryTv;
    private TextView mftStatusTv;
    private TextView mftMSGIDTv;
    private TextView pcTv;
    private TextView weightTv;
    private TextView volumeTv;
    private TextView arrivalStatusTv;
    private TextView arrMSGIDTv;
    private TextView responseTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexponekeydeclareitemdetail);
        initView();
    }
    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("预配与运抵信息详情");
        navBar.hideRight();
        mawbTv = (TextView) findViewById(R.id.declare_mawb_detail_tv);
        hnoTv = (TextView) findViewById(R.id.declare_hno_detail_tv);
        rearchIDTv = (TextView) findViewById(R.id.declare_rearchid_detail_tv);
        cMDStatusTv = (TextView) findViewById(R.id.declare_cmdstatus_detail_tv);
        spCodeTv = (TextView) findViewById(R.id.declare_spcode_detail_tv);
        goodsTv = (TextView) findViewById(R.id.declare_goods_detail_tv);
        fDateTv = (TextView) findViewById(R.id.declare_fdate_detail_tv);
        fnoTv = (TextView) findViewById(R.id.declare_fno_detail_tv);
        dest1Tv = (TextView) findViewById(R.id.declare_dest1_detail_tv);
        destTv = (TextView) findViewById(R.id.declare_dest_detail_tv);
        customsCodeTv = (TextView) findViewById(R.id.declare_customscode_detail_tv);
        transportmodeTv = (TextView) findViewById(R.id.declare_transPortMode_detail_tv);
        freightPaymentTv = (TextView) findViewById(R.id.declare_FreightPayment_detail_tv);
        cNEECityTv = (TextView) findViewById(R.id.declare_cneecity_detail_tv);
        cNEECountryTv = (TextView) findViewById(R.id.declare_cneecountry_detail_tv);
        mftStatusTv = (TextView) findViewById(R.id.declare_MftStatus_detail_tv);
        mftMSGIDTv = (TextView) findViewById(R.id.declare_MftMSGID_detail_tv);
        pcTv = (TextView) findViewById(R.id.declare_pc_detail_tv);
        weightTv = (TextView) findViewById(R.id.declare_Weight_detail_tv);
        volumeTv = (TextView) findViewById(R.id.declare_Volume_detail_tv);
        arrivalStatusTv = (TextView) findViewById(R.id.declare_ArrivalStatus_detail_tv);
        arrMSGIDTv = (TextView) findViewById(R.id.declare_ArrMSGID_detail_tv);
        responseTv = (TextView) findViewById(R.id.declare_Response_detail_tv);

        // 给控件赋值
        setTextView();
    }

    // 给textView赋值
    private void setTextView() {
        Declare declare;
        declare = (Declare) getIntent().getSerializableExtra(AviationCommons.DECLARE_INFO);
        String mawb = declare.getMawb();
        mawbTv.setText(mawb);
        String hno = declare.getHno();
        hnoTv.setText(hno);
        String rearchId = declare.getRearchID();
        rearchIDTv.setText(rearchId);
        String cMDSStatus = declare.getCMDStatus();
        cMDStatusTv.setText(cMDSStatus);
        String spCode = declare.getSpCode();
        spCodeTv.setText(spCode);
        String goods = declare.getGoods();
        goodsTv.setText(goods);
        String fDate = declare.getFDate();
        fDateTv.setText(fDate);
        String fno = declare.getFno();
        fnoTv.setText(fno);
        String dest1 = declare.getDest1();
        dest1Tv.setText(dest1);
        String dest = declare.getDest();
        destTv.setText(dest);
        String customsCode = declare.getCustomsCode();
        customsCodeTv.setText(customsCode);
        String transportmode = declare.getTransportmode();
        transportmodeTv.setText(transportmode);
        String freightPayment = declare.getFreightPayment();
        freightPaymentTv.setText(freightPayment);
        String cNEECity = declare.getCNEECity();
        cNEECityTv.setText(cNEECity);
        String cNEECountry = declare.getCNEECountry();
        cNEECountryTv.setText(cNEECountry);
        String mftStatus = declare.getMftStatus();
        mftStatusTv.setText(mftStatus);
        String mftMSGID = declare.getMftMSGID();
        mftMSGIDTv.setText(mftMSGID);
        String pc = declare.getPC();
        pcTv.setText(pc);
        String weight = declare.getWeight();
        weightTv.setText(weight);
        String volume = declare.getVolume();
        volumeTv.setText(volume);
        String arrivalStatus = declare.getArrivalStatus();
        arrivalStatusTv.setText(arrivalStatus);
        String arrMSGID = declare.getArrMSGID();
        arrMSGIDTv.setText(arrMSGID);
        String response = declare.getResponse();
        responseTv.setText(response);
    }
}
