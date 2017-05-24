package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intimpcargoinfo.CargoInfoMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

/**
 * 进港货站信息详情界面
 */

public class AppIntimpCarGoInfoItemDetailActivity extends Activity implements View.OnClickListener{
    private EditText fdateEt;
    private EditText fnoEt;
    private EditText mawbEt;
    private EditText hnoEt;
    private EditText awbPcEt;
    private EditText pcEt;
    private EditText weightEt;
    private EditText volumeEt;
    private EditText goodsEt;
    private EditText origenEt;
    private EditText depEt;
    private EditText destEt;
    private EditText awbTypeNameEt;
    private EditText businessNameEt;
    private EditText isEDIAWBEt;
    private EditText mftStatusEt;
    private EditText tallyStatusEt;
    private Button updataBtn;
    private Button deleteBtn;
    private Button sureBtn;

    private  CargoInfoMessage cargoInfoMessage;

    // 传递到新增界面需要的值
    private String mawb;
    private String hawbID;
    private String businessType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impcargoinfo_detail);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("进港货站信息详情");
        navBar.setRight(R.drawable.jia);

        // 跳转到新增分单界面
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppIntimpCarGoInfoItemDetailActivity.this, AppIntimpCargoInfoAddActivity.class);
                cargoInfoMessage = (CargoInfoMessage) getIntent().getSerializableExtra(AviationCommons.IMP_CARGO_INFO_ITEM);
                mawb = cargoInfoMessage.getMawb();
                hawbID = cargoInfoMessage.getAwbID();
                businessType = cargoInfoMessage.getBusinessType();
                intent.putExtra(AviationCommons.IMP_MAWB, mawb);
                intent.putExtra(AviationCommons.IMP_HAWBID, hawbID);
                intent.putExtra(AviationCommons.IMP_TYPE, businessType);
                startActivity(intent);
            }
        });

        fdateEt = (EditText) findViewById(R.id.imp_fdate_detail_et);
        fnoEt = (EditText) findViewById(R.id.imp_fno_detail_et);
        mawbEt = (EditText) findViewById(R.id.imp_mawb_detail_et);
        hnoEt = (EditText) findViewById(R.id.imp_hno_detail_et);
        awbPcEt = (EditText) findViewById(R.id.imp_awbpc_detail_et);
        pcEt = (EditText) findViewById(R.id.imp_pc_detail_et);
        weightEt = (EditText) findViewById(R.id.imp_weight_detail_et);
        volumeEt = (EditText) findViewById(R.id.imp_volume_detail_et);
        goodsEt = (EditText) findViewById(R.id.imp_goods_detail_et);
        origenEt = (EditText) findViewById(R.id.imp_origin_detail_et);
        depEt = (EditText) findViewById(R.id.imp_dep_detail_et);
        destEt = (EditText) findViewById(R.id.imp_dest_detail_et);
        awbTypeNameEt = (EditText) findViewById(R.id.imp_awbTypeName_detail_et);
        businessNameEt = (EditText) findViewById(R.id.imp_BusinessName_detail_et);
        isEDIAWBEt = (EditText) findViewById(R.id.imp_isEDIAWB_detail_et);
        mftStatusEt = (EditText) findViewById(R.id.imp_mftstatus_detail_et);
        tallyStatusEt = (EditText) findViewById(R.id.imp_tallystatus_detail_et);

        // Button按钮
        updataBtn = (Button) findViewById(R.id.imp_change_fendan_btn);
        sureBtn = (Button) findViewById(R.id.imp_fendan_sure_btn);
        deleteBtn = (Button) findViewById(R.id.imp_delete_fendan_btn);
        updataBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        setEditextValues();
        setEditextInvisbale();
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 修改分单按钮
            case R.id.imp_change_fendan_btn:
                break;

            // 确定修改按钮
            case R.id.imp_fendan_sure_btn:
                break;

            // 删除分单按钮
            case R.id.imp_delete_fendan_btn:
                break;

            default:
                break;
        }

    }

    // 给EditText赋值
    private void setEditextValues() {
        cargoInfoMessage = (CargoInfoMessage) getIntent().getSerializableExtra(AviationCommons.IMP_CARGO_INFO_ITEM);
        String fdate = cargoInfoMessage.getFDate();
        if (!fdate.equals("")) {
            fdateEt.setText(fdate);
        } else {
            fdateEt.setText("无信息");
        }
        String fno = cargoInfoMessage.getFno();
        if (!fno.equals("")) {
            fnoEt.setText(fno);
        } else {
            fnoEt.setText("无信息");
        }
        String mawb = cargoInfoMessage.getMawb();
        if (!mawb.equals("")) {
            mawbEt.setText(mawb);
        } else {
            mawbEt.setText("无信息");
        }
        String hno = cargoInfoMessage.getHno();
        hnoEt.setText(hno);
        String awbPc = cargoInfoMessage.getAwbPC();
        awbPcEt.setText(awbPc);
        String pc = cargoInfoMessage.getPC();
        pcEt.setText(pc);
        String weight = cargoInfoMessage.getWeight();
        weightEt.setText(weight);
        String volume = cargoInfoMessage.getVolume();
        volumeEt.setText(volume);
        String goods = cargoInfoMessage.getGoods();
        goodsEt.setText(goods);
        String origen = cargoInfoMessage.getOrigin();
        origenEt.setText(origen);
        String dep = cargoInfoMessage.getDep();
        depEt.setText(dep);
        String dest = cargoInfoMessage.getDest();
        destEt.setText(dest);
        String awbTypeName = cargoInfoMessage.getAwbTypeName();
        awbTypeNameEt.setText(awbTypeName);
        String businessName = cargoInfoMessage.getBusinessName();
        businessNameEt.setText(businessName);
        String isEDIAWB = cargoInfoMessage.getIsEDIAWB();
        isEDIAWBEt.setText(isEDIAWB);
        String mftStatus = cargoInfoMessage.getMftStatus();
        mftStatusEt.setText(mftStatus);
        String tallyStatus = cargoInfoMessage.getTallyStatus();
        tallyStatusEt.setText(tallyStatus);
    }

    // 设置EditText不可修改
    private void setEditextInvisbale() {
        fdateEt.setEnabled(false);
        fnoEt.setEnabled(false);
        mawbEt.setEnabled(false);
        hnoEt.setEnabled(false);
        awbPcEt.setEnabled(false);
        pcEt.setEnabled(false);
        weightEt.setEnabled(false);
        volumeEt.setEnabled(false);
        goodsEt.setEnabled(false);
        origenEt.setEnabled(false);
        depEt.setEnabled(false);
        destEt.setEnabled(false);
        awbTypeNameEt.setEnabled(false);
        businessNameEt.setEnabled(false);
        isEDIAWBEt.setEnabled(false);
        mftStatusEt.setEnabled(false);
        tallyStatusEt.setEnabled(false);
    }

    // 设置EditText可以修改
    private void setEditextVisable() {
        mawbEt.setEnabled(true);
        hnoEt.setEnabled(true);
        pcEt.setEnabled(true);
        weightEt.setEnabled(true);
        volumeEt.setEnabled(true);
        goodsEt.setEnabled(true);
        businessNameEt.setEnabled(true);
    }
}
