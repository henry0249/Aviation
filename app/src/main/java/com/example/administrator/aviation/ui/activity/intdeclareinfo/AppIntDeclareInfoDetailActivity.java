package com.example.administrator.aviation.ui.activity.intdeclareinfo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intdeclareinfo.DeclareInfoMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

/**
 * 联检申报详情页
 */

public class AppIntDeclareInfoDetailActivity extends Activity{
    private DeclareInfoMessage declareInfoMessage;
    private TextView mawbTv;
    private TextView ciqNumbertv;
    private TextView ciqStatusTv;
    private TextView cMDStatusTv;
    private TextView mftStatusTv;
    private TextView arrivalStatusTv;
    private TextView loadStatusTv;
    private TextView tallyStatusTv;
    private TextView pcTv;
    private TextView weightTv;
    private TextView destTv;
    private TextView gPriceTv;
    private TextView shippter;
    private TextView opDateTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declareinfo_deatil);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("联检状态信息详情页");
        navBar.hideRight();
        mawbTv = (TextView) findViewById(R.id.declareinfo_mawb_detail_tv);
        ciqNumbertv = (TextView) findViewById(R.id.declareinfo_ciqnumber_detail_tv);
        ciqStatusTv = (TextView) findViewById(R.id.declareinfo_ciqstatus_detail_tv);
        cMDStatusTv = (TextView) findViewById(R.id.declareinfo_cmdstatus_detail_tv);
        mftStatusTv = (TextView) findViewById(R.id.declareinfo_mftstatus_detail_tv);
        arrivalStatusTv = (TextView) findViewById(R.id.declareinfo_arrivalstatus_detail_tv);
        loadStatusTv = (TextView) findViewById(R.id.declareinfo_loadstatus_detail_tv);
        tallyStatusTv = (TextView) findViewById(R.id.declareinfo_tallystatus_detail_tv);
        pcTv = (TextView) findViewById(R.id.declareinfo_pc_detail_tv);
        weightTv = (TextView) findViewById(R.id.declareinfo_weight_detail_tv);
        destTv = (TextView) findViewById(R.id.declareinfo_dest_detail_tv);
        gPriceTv = (TextView) findViewById(R.id.declareinfo_gprice_detail_tv);
        shippter = (TextView) findViewById(R.id.declareinfo_ship_tv);
        opDateTv = (TextView) findViewById(R.id.declareinfo_opdate_detail_tv);
        declareInfoMessage = (DeclareInfoMessage) getIntent().getSerializableExtra(AviationCommons.DECLAREINFO_DEATIL);
        setTextView();
    }

    private void setTextView() {
        mawbTv.setText(declareInfoMessage.getMawb());
        ciqNumbertv.setText(declareInfoMessage.getCIQNumber());
        ciqStatusTv.setText(declareInfoMessage.getCIQStatus());
        cMDStatusTv.setText(declareInfoMessage.getCMDStatus());
        mftStatusTv.setText(declareInfoMessage.getMftStatus());
        arrivalStatusTv.setText(declareInfoMessage.getArrivalStatus());
        loadStatusTv.setText(declareInfoMessage.getLoadStatus());
        tallyStatusTv.setText(declareInfoMessage.getTallyStatus());
        pcTv.setText(declareInfoMessage.getPC());
        weightTv.setText(declareInfoMessage.getWeight());
        destTv.setText(declareInfoMessage.getDest());
        gPriceTv.setText(declareInfoMessage.getGPrice());
        shippter.setText(declareInfoMessage.getShipper());
        opDateTv.setText(declareInfoMessage.getOPDate());
    }
}
