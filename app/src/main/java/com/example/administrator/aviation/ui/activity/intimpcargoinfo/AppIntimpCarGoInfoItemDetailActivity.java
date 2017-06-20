package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoChangeHno;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoDeleteHno;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoInfo;
import com.example.administrator.aviation.model.intimpcargoinfo.CargoInfoMessage;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * 进港货站信息详情界面
 */

public class AppIntimpCarGoInfoItemDetailActivity extends Activity implements View.OnClickListener{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

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
    private String hno;

    private String type;
    private String pc;
    private String weight;
    private String volume;
    private String goods;


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

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        fdateEt = (EditText) findViewById(R.id.imp_fdate_detail_et);
        fnoEt = (EditText) findViewById(R.id.imp_fno_detail_et);
        mawbEt = (EditText) findViewById(R.id.imp_mawb_detail_et);
        hnoEt = (EditText) findViewById(R.id.imp_hno_detail_et);
        awbPcEt = (EditText) findViewById(R.id.imp_awbpc_detail_et);
        pcEt = (EditText) findViewById(R.id.imp_pc_detail_et);
        weightEt = (EditText) findViewById(R.id.imp_weight_detail_et);
        volumeEt = (EditText) findViewById(R.id.imp_volume_detail_et);
        goodsEt = (EditText) findViewById(R.id.imp_goods_detail_et);
        goodsEt.setTransformationMethod(new AllCapTransformationMethod());
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

        cargoInfoMessage = (CargoInfoMessage) getIntent().getSerializableExtra(AviationCommons.IMP_CARGO_INFO_ITEM);
        hno = cargoInfoMessage.getHno();
        // 设置button不可点击
        if (hno.equals("")) {
            updataBtn.setBackgroundColor(Color.parseColor("#e3e3e3"));
            updataBtn.setEnabled(Boolean.FALSE);
            deleteBtn.setBackgroundColor(Color.parseColor("#e3e3e3"));
            deleteBtn.setEnabled(Boolean.FALSE);
        } else {
            updataBtn.setEnabled(Boolean.TRUE);
            deleteBtn.setEnabled(Boolean.TRUE);
        }
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 修改分单按钮
            case R.id.imp_change_fendan_btn:
                setEditextVisable();
                updataBtn.setVisibility(View.GONE);
                sureBtn.setVisibility(View.VISIBLE);
                break;

            // 确定修改按钮
            case R.id.imp_fendan_sure_btn:
                getEditTextVolues();
                String xml = HttpPrepareImpCargoInfo.getAddUpdataDeleteXml(hawbID, mawb, hno, type, pc,weight, volume, goods, "");
                new ChangeHonAsyTask(xml).execute();
                break;

            // 删除分单按钮
            case R.id.imp_delete_fendan_btn:
                getEditTextVolues();
                final String deleteXml = HttpPrepareImpCargoInfo.getAddUpdataDeleteXml(hawbID, mawb, hno, type, pc,weight, volume, goods, "");
                AlertDialog.Builder builder = new AlertDialog.Builder(AppIntimpCarGoInfoItemDetailActivity.this);
                builder.setTitle("删除订单")
                        .setMessage("确定删除分单号为:"+hno+"订单吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteHonAsyTask(deleteXml).execute();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
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
        pc = cargoInfoMessage.getPC();
        pcEt.setText(pc);
        weight = cargoInfoMessage.getWeight();
        weightEt.setText(weight);
        volume = cargoInfoMessage.getVolume();
        volumeEt.setText(volume);
        goods = cargoInfoMessage.getGoods();
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

    // 获取editext值
    private void getEditTextVolues() {
        cargoInfoMessage = (CargoInfoMessage) getIntent().getSerializableExtra(AviationCommons.IMP_CARGO_INFO_ITEM);
        type = cargoInfoMessage.getBusinessType();
        hawbID = cargoInfoMessage.getAwbID();
        mawb = cargoInfoMessage.getMawb();
        volume = volumeEt.getText().toString().trim();
        weight = weightEt.getText().toString().trim();
        pc = pcEt.getText().toString().trim();
        goods = goodsEt.getText().toString().trim();
        goods = goods.toUpperCase();

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
//        mawbEt.setEnabled(true);
//        hnoEt.setEnabled(true);
        pcEt.setEnabled(true);
        weightEt.setEnabled(true);
        volumeEt.setEnabled(true);
        goodsEt.setEnabled(true);
//        businessNameEt.setEnabled(true);
    }

    // 修改分单
    private class ChangeHonAsyTask extends AsyncTask<Void, Void, String> {
        String xml = null;
        String result = null;
        public ChangeHonAsyTask(String xml) {
            this.xml = xml;
        }

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpPrepareImpCargoChangeHno.changeHno(userBumen, userName, userPass, loginFlag,xml);
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
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("true")) {
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, "成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntimpCarGoInfoItemDetailActivity.this, AppIntimpCargoInfoActivity.class);
                startActivity(intent);
            }
        }
    }

    // 删除分单
    private class DeleteHonAsyTask extends AsyncTask<Void, Void, String> {
        String xml = null;
        String result = null;
        public DeleteHonAsyTask(String xml) {
            this.xml = xml;
        }

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpPrepareImpCargoDeleteHno.deleteCargodetail(userBumen, userName, userPass, loginFlag, xml);
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
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("true")) {
                Toast.makeText(AppIntimpCarGoInfoItemDetailActivity.this, "成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntimpCarGoInfoItemDetailActivity.this, AppIntimpCargoInfoActivity.class);
                startActivity(intent);
            }
        }
    }
}
