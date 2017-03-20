package com.example.administrator.aviation.ui.activity.domprepareawb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBUpdate;
import com.example.administrator.aviation.model.prepareawb.MawbInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表详情页面
 */

public class AwbDetailActivity extends Activity implements View.OnClickListener{
    private EditText mawbTv;
    private EditText pcTv;
    private EditText weightTv;
    private EditText volumeTv;
    private EditText spcodeTv;
    private EditText goodsTv;
    private EditText businessTypeTv;
    private EditText packageTv;
    private EditText byTv;
    private EditText depTv;
    private EditText dest1Tv;
    private EditText dest2Tv;
    private EditText remarkTv;
    private EditText flightCheckedTv;
    private EditText fDateTv;
    private Button changeTimeBtn;
    private EditText fnoTv;
    private EditText shipperTv;
    private EditText shipperTELTv;
    private EditText consigneeTv;
    private EditText cNEETELTv;
    private EditText transportNOTv;
    private EditText allowTransNOTv;
    private EditText cIQNumberTv;
    private Button updateBtn;
    private Button sureBtn;
    private LinearLayout dest2Layout;

    private MawbInfo mawbInfo;

    private String mawbId;
    private String mawb;
    private String pc;
    private String weight;
    private String volume;
    private String spCode;
    private String goods;
    private String businessType;
    private String cnbusinessType;
    private String packg;
    private String by;
    private String dep;
    private String dest1;
    private String dest2;
    private String remark;
    private String flightChecked;
    private String fDate;
    private String fNo;
    private String shipper;
    private String shipperTEL;
    private String consignee;
    private String cNEETEL;
    private String transportNO;
    private String allowTransNO;
    private String cIQNumber;
    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;

    private ArrayAdapter<String> businessTypeAdapter;
    private Spinner businessTypeSpinner;
    private List<String> businessTypeList;
    private int businessTypeSpinnerPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awbdetail);
        initView();
        showDetail();
    }

    // 点击空白处隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    // 初始化控件
    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("订单详情");
        navBar.hideRight();
        Intent intent = getIntent();
        mawbInfo = (MawbInfo) intent.getSerializableExtra(AviationCommons.AWB_ITEM_INFO);
        mawbTv = (EditText) findViewById(R.id.mawb_detail_tv);
        pcTv = (EditText) findViewById(R.id.pc_detail_tv);
        weightTv = (EditText) findViewById(R.id.weight_detail_tv);
        volumeTv = (EditText) findViewById(R.id.volume_detail_tv);
        spcodeTv = (EditText) findViewById(R.id.spcode_detail_tv);
        goodsTv = (EditText) findViewById(R.id.goods_detail_tv);
        businessTypeTv = (EditText) findViewById(R.id.businesstype_detail_tv);
        packageTv = (EditText) findViewById(R.id.package_detail_tv);
        byTv = (EditText) findViewById(R.id.by_detail_tv);
        depTv = (EditText) findViewById(R.id.dep_detail_tv);
        dest1Tv = (EditText) findViewById(R.id.dest_detail_tv);
        dest2Tv = (EditText) findViewById(R.id.dest2_detail_tv);
        remarkTv = (EditText) findViewById(R.id.remake_detail_tv);
        flightCheckedTv = (EditText) findViewById(R.id.flightchecked_detail_tv);
        fDateTv = (EditText) findViewById(R.id.fdate_detail_tv);
        changeTimeBtn = (Button) findViewById(R.id.change_awb_time);
        fnoTv = (EditText) findViewById(R.id.fno_detail_tv);
        shipperTv = (EditText) findViewById(R.id.shipper_detail_tv);
        shipperTELTv = (EditText) findViewById(R.id.shippertel_detail_tv);
        consigneeTv = (EditText) findViewById(R.id.consignee_detail_tv);
        cNEETELTv = (EditText) findViewById(R.id.cneetel_detail_tv);
        transportNOTv = (EditText) findViewById(R.id.transportno_detail_tv);
        allowTransNOTv = (EditText) findViewById(R.id.allowtransno_detail_tv);
        cIQNumberTv = (EditText) findViewById(R.id.ciqnumber_detail_tv);

        updateBtn = (Button) findViewById(R.id.update_detail_btn);
        updateBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.sure_detail_btn);
        sureBtn.setOnClickListener(this);
        dest2Layout = (LinearLayout) findViewById(R.id.dest2_layout);
        changeTimeBtn.setOnClickListener(this);

        setEditTextInvisible();

        // 下拉选择数据
        businessTypeSpinner = (Spinner) findViewById(R.id.update_awb_businesstype_spinner);
        businessTypeList = new ArrayList<>();
        businessTypeList.add("普通货物运输");
        businessTypeList.add("国际快件");
        businessTypeList.add("D类快件");
        businessTypeList.add("国际邮包");
        businessTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, businessTypeList);
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        businessTypeSpinner.setAdapter(businessTypeAdapter);
        for (int i = 0; i <businessTypeList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(businessTypeList.get(i)).equals(businessType)) {
                businessTypeSpinnerPosition =  i;
            }
        }
    }

    // 给控件赋值显示
    private void showDetail() {
        mawb = mawbInfo.getMawb();
        mawbTv.setText(mawb);
        pc = mawbInfo.getPC();
        pcTv.setText(pc);
        weight = mawbInfo.getWeight();
        weightTv.setText(weight);
        volume = mawbInfo.getVolume();
        volumeTv.setText(volume);
        spCode = mawbInfo.getSpCode();
        spcodeTv.setText(spCode);
        goods = mawbInfo.getGoods();
        goodsTv.setText(goods);
        businessType = mawbInfo.getBusinessType();
        String cnBusinessType = AviationNoteConvert.getCNBusinessType(businessType);
        businessTypeTv.setText(cnBusinessType);
        packg = mawbInfo.getPackage();
        packageTv.setText(packg);
        by = mawbInfo.getBy();
        byTv.setText(by);
        dep = mawbInfo.getDep();
        depTv.setText(dep);
        dest1 = mawbInfo.getDest1();
        dest2 = mawbInfo.getDest2();
        if (!dest2.equals("")) {
            dest1Tv.setText(dest2);
        } else {
            dest1Tv.setText(dest1);
        }
        remark = mawbInfo.getRemark();
        remarkTv.setText(remark);
        flightChecked = mawbInfo.getMawbm().getFlightChecked();
        flightCheckedTv.setText(flightChecked);
        fDate = mawbInfo.getMawbm().getFDate();
        fDateTv.setText(fDate);
        fNo = mawbInfo.getMawbm().getFno();
        fnoTv.setText(fNo);
        shipper = mawbInfo.getMawbm().getShipper();
        shipperTv.setText(shipper);
        shipperTEL = mawbInfo.getMawbm().getShipperTEL();
        shipperTELTv.setText(shipperTEL);
        consignee = mawbInfo.getMawbm().getConsignee();
        consigneeTv.setText(consignee);
        cNEETEL = mawbInfo.getMawbm().getCNEETEL();
        cNEETELTv.setText(cNEETEL);
        transportNO = mawbInfo.getMawbm().getTransportNO();
        transportNOTv.setText(transportNO);
        allowTransNO = mawbInfo.getMawbm().getAllowTransNO();
        allowTransNOTv.setText(allowTransNO);
        cIQNumber = mawbInfo.getMawbm().getCIQNumber();
        cIQNumberTv.setText(cIQNumber);
    }

    // 设置EditText不可编辑
    private void setEditTextInvisible() {
        mawbTv.setEnabled(false);
        pcTv.setEnabled(false);
        weightTv.setEnabled(false);
        volumeTv.setEnabled(false);
        spcodeTv.setEnabled(false);
        goodsTv.setEnabled(false);
        businessTypeTv.setEnabled(false);
        packageTv.setEnabled(false);
        byTv.setEnabled(false);
        depTv.setEnabled(false);
        dest1Tv.setEnabled(false);
        remarkTv.setEnabled(false);
        flightCheckedTv.setEnabled(false);
        fDateTv.setEnabled(false);
        fnoTv.setEnabled(false);
        shipperTv.setEnabled(false);
        shipperTELTv.setEnabled(false);
        consigneeTv.setEnabled(false);
        cNEETELTv.setEnabled(false);
        transportNOTv.setEnabled(false);
        allowTransNOTv.setEnabled(false);
        cIQNumberTv.setEnabled(false);
    }

    // 设置EditText可编辑
    private void setEditTextVisible() {
        mawbTv.setEnabled(true);
        pcTv.setEnabled(true);
        weightTv.setEnabled(true);
        volumeTv.setEnabled(true);
        spcodeTv.setEnabled(true);
        goodsTv.setEnabled(true);
        businessTypeTv.setEnabled(true);
        packageTv.setEnabled(true);
        byTv.setEnabled(true);
        depTv.setEnabled(true);
        dest1Tv.setEnabled(true);
        remarkTv.setEnabled(true);
        flightCheckedTv.setEnabled(true);
        fDateTv.setEnabled(true);
        fnoTv.setEnabled(true);
        shipperTv.setEnabled(true);
        shipperTELTv.setEnabled(true);
        consigneeTv.setEnabled(true);
        cNEETELTv.setEnabled(true);
        transportNOTv.setEnabled(true);
        allowTransNOTv.setEnabled(true);
        cIQNumberTv.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update_detail_btn:
                setEditTextVisible();
                updateBtn.setVisibility(View.GONE);
                sureBtn.setVisibility(View.VISIBLE);
                changeTimeBtn.setVisibility(View.VISIBLE);
                dest2Layout.setVisibility(View.VISIBLE);
                businessTypeTv.setVisibility(View.GONE);
                businessTypeSpinner.setVisibility(View.VISIBLE);
                businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        cnbusinessType = businessTypeAdapter.getItem(position);
                        businessType = AviationNoteConvert.cNtoEn(cnbusinessType);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        cnbusinessType = businessTypeAdapter.getItem(0);
                        businessType = AviationNoteConvert.cNtoEn(cnbusinessType);
                    }
                });
                businessTypeSpinner.setSelection(businessTypeSpinnerPosition);
                break;
            case R.id.sure_detail_btn:
                getEditText();
                xml = HttpPrepareAWBUpdate.getXml(mawbId, mawb, pc,weight,volume,spCode,
                         goods,  businessType, packg, by, dep, dest1,dest2,
                         remark, flightChecked, fDate, fNo, shipper, shipperTEL,
                         consignee, cNEETEL,transportNO,allowTransNO, cIQNumber);
                userBumen = PreferenceUtils.getUserBumen(this);
                userName = PreferenceUtils.getUserName(this);
                userPass = PreferenceUtils.getUserPass(this);
                loginFlag = PreferenceUtils.getLoginFlag(this);
                new PushXmlAsyncTask().execute();
                break;
            case R.id.change_awb_time:
                new DatePickerDialog(AwbDetailActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fDateTv.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2016,12,20).show();
                break;
            default:
                break;
        }

    }

    // 获取EditText值
    private void getEditText() {
        mawbId = mawbInfo.getMawbID();
        mawb = mawbTv.getText().toString();
        pc = pcTv.getText().toString();
        weight= weightTv.getText().toString();
        volume= volumeTv.getText().toString();
        spCode= spcodeTv.getText().toString();
        goods = goodsTv.getText().toString();
//        businessType = businessTypeTv.getText().toString();
        packg = packageTv.getText().toString();
        by = byTv.getText().toString();
        dep = depTv.getText().toString();
        dest1 = dest1Tv.getText().toString();
        dest2 = dest2Tv.getText().toString();
        remark = remarkTv.getText().toString();
        flightChecked = flightCheckedTv.getText().toString();
        fDate = fDateTv.getText().toString();
        fNo = fnoTv.getText().toString();
        shipper = shipperTv.getText().toString();
        shipperTEL = shipperTELTv.getText().toString();
        consignee = consigneeTv.getText().toString();
        cNEETEL = cNEETELTv.getText().toString();
        transportNO = transportNOTv.getText().toString();
        allowTransNO = allowTransNOTv.getText().toString();
        cIQNumber = cIQNumberTv.getText().toString();
    }

    // 上传xml到服务器
    class PushXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareAWBUpdate.updateAWBdetail(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AwbDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AwbDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AwbDetailActivity.this, "修改成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AwbDetailActivity.this, AppDomExpPrePareAWBActivity.class);
//                startActivityForResult(intent, 1);
                finish();
            }
            super.onPostExecute(request);
        }
    }

}
