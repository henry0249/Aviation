package com.example.administrator.aviation.ui.activity.domexphouse;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.house.WhsInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

/**
 * 显示house订单详情列表
 */

public class AppDomExpWareHouseDetailActivity extends Activity{
    private WhsInfo mWhsInfo;
    private TextView mawbTv;
    private TextView awbPcTv;
    private TextView pcTv;
    private TextView weightTv;
    private TextView volumeTv;
    private TextView spcodeTv;
    private TextView goodsTv;
    private TextView depTv;
    private TextView destTv;
    private TextView fdateTv;
    private TextView fnoTv;
    private TextView paperTimeTv;
    private TextView billsnoTv;
    private TextView opdateTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphousedetail);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle("国内出港入库列表详情");

        // 初始化组件
        mawbTv = (TextView) findViewById(R.id.house_mawb_detail_tv);
        awbPcTv = (TextView) findViewById(R.id.house_awb_pc_detail_tv);
        pcTv = (TextView) findViewById(R.id.house_pc_detail_tv);
        weightTv = (TextView) findViewById(R.id.house_detail_weight_tv);
        spcodeTv = (TextView) findViewById(R.id.house_spcode_detail_tv);
        goodsTv = (TextView) findViewById(R.id.house_goods_detail_tv);
        depTv = (TextView) findViewById(R.id.house_dep_detail_tv);
        destTv = (TextView) findViewById(R.id.house_dest_detail_tv);
        fdateTv = (TextView) findViewById(R.id.house_fdate_detail_tv);
        fnoTv = (TextView) findViewById(R.id.house_fno_detail_tv);
        paperTimeTv = (TextView) findViewById(R.id.house_papertime_detail_tv);
        billsnoTv = (TextView) findViewById(R.id.house_billsno_detail_tv);
        opdateTv = (TextView) findViewById(R.id.house_opdate_detail_tv);
        mWhsInfo = (WhsInfo) getIntent().getSerializableExtra(AviationCommons.HOUSE_ITEM_INFO);
        setTextView();
    }

    // 给textView赋值
    private void setTextView() {
        mawbTv.setText(mWhsInfo.getMawb());
        awbPcTv.setText(mWhsInfo.getAwbPC());
        pcTv.setText(mWhsInfo.getPC());
        weightTv.setText(mWhsInfo.getWeight());
        spcodeTv.setText(mWhsInfo.getSpCode());
        goodsTv.setText(mWhsInfo.getGoods());
        depTv.setText(mWhsInfo.getDep());
        destTv.setText(mWhsInfo.getDest());
        fdateTv.setText(mWhsInfo.getFdate());
        fnoTv.setText(mWhsInfo.getFno());
        paperTimeTv.setText(mWhsInfo.getPaperTime());
        billsnoTv.setText(mWhsInfo.getBillsNO());
        opdateTv.setText(mWhsInfo.getOPDate());
    }

}
