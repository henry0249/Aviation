package com.example.administrator.aviation.ui.activity.domprepareawb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.ChoseTimeMethod;
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
    private EditText consigneeTv;
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

    private ArrayAdapter<String> goodsAdapter;
    private Spinner goodsSpinner;
    private List<String> goodsList;
    private int goodsSpinnerPosition;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();
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
        weightTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeTv = (EditText) findViewById(R.id.volume_detail_tv);
        volumeTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        spcodeTv = (EditText) findViewById(R.id.spcode_detail_tv);
        spcodeTv.setTransformationMethod(new AllCapTransformationMethod());
        goodsTv = (EditText) findViewById(R.id.goods_detail_tv);
        goodsTv.setTransformationMethod(new AllCapTransformationMethod());
        businessTypeTv = (EditText) findViewById(R.id.businesstype_detail_tv);
        businessTypeTv.setTransformationMethod(new AllCapTransformationMethod());
        packageTv = (EditText) findViewById(R.id.package_detail_tv);
        packageTv.setTransformationMethod(new AllCapTransformationMethod());
        byTv = (EditText) findViewById(R.id.by_detail_tv);
        byTv.setTransformationMethod(new AllCapTransformationMethod());
        depTv = (EditText) findViewById(R.id.dep_detail_tv);
        depTv.setTransformationMethod(new AllCapTransformationMethod());
        dest1Tv = (EditText) findViewById(R.id.dest_detail_tv);
        dest1Tv.setTransformationMethod(new AllCapTransformationMethod());
        dest2Tv = (EditText) findViewById(R.id.dest2_detail_tv);
        dest2Tv.setTransformationMethod(new AllCapTransformationMethod());
        remarkTv = (EditText) findViewById(R.id.remake_detail_tv);
        remarkTv.setTransformationMethod(new AllCapTransformationMethod());
        flightCheckedTv = (EditText) findViewById(R.id.flightchecked_detail_tv);
        flightCheckedTv.setTransformationMethod(new AllCapTransformationMethod());
        fDateTv = (EditText) findViewById(R.id.fdate_detail_tv);
        changeTimeBtn = (Button) findViewById(R.id.change_awb_time);
        fnoTv = (EditText) findViewById(R.id.fno_detail_tv);
        fnoTv.setTransformationMethod(new AllCapTransformationMethod());
        shipperTv = (EditText) findViewById(R.id.shipper_detail_tv);
        shipperTv.setTransformationMethod(new AllCapTransformationMethod());
        consigneeTv = (EditText) findViewById(R.id.consignee_detail_tv);
        consigneeTv.setTransformationMethod(new AllCapTransformationMethod());
        transportNOTv = (EditText) findViewById(R.id.transportno_detail_tv);
        allowTransNOTv = (EditText) findViewById(R.id.allowtransno_detail_tv);
        allowTransNOTv.setTransformationMethod(new AllCapTransformationMethod());
        cIQNumberTv = (EditText) findViewById(R.id.ciqnumber_detail_tv);
        cIQNumberTv.setTransformationMethod(new AllCapTransformationMethod());

        updateBtn = (Button) findViewById(R.id.update_detail_btn);
        updateBtn.setOnClickListener(this);
        sureBtn = (Button) findViewById(R.id.sure_detail_btn);
        sureBtn.setOnClickListener(this);
        dest2Layout = (LinearLayout) findViewById(R.id.dest2_layout);
        changeTimeBtn.setOnClickListener(this);

        setEditTextInvisible();

        // 品名
        goodsSpinner = (Spinner) findViewById(R.id.update_awb_goods_spinner);
        goodsList = new ArrayList<>();
        goodsList.add("再生木托");
        goodsList.add("塑料编织袋");
        goodsList.add("夹板箱");
        goodsList.add("托盘");
        goodsList.add("木托");
        goodsList.add("木箱");
        goodsList.add("桶");
        goodsList.add("真空包装");
        goodsList.add("箱柜");
        goodsList.add("纸箱");
        goodsList.add("纸袋");
        goodsList.add("薄膜包装");
        goodsList.add("金属桶");
        goodsList.add("金属罐");
        goodsList.add("麻包");
        goodsList.add("其他");
        goodsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goodsList);
        goodsAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        goodsSpinner.setAdapter(goodsAdapter);


        // 下拉选择数据
        businessTypeSpinner = (Spinner) findViewById(R.id.update_awb_businesstype_spinner);
        businessTypeList = new ArrayList<>();
        businessTypeList.add("普通货物运输");
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
        consignee = mawbInfo.getMawbm().getConsignee();
        consigneeTv.setText(consignee);
        cNEETEL = mawbInfo.getMawbm().getCNEETEL();
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
        consigneeTv.setEnabled(false);
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
        consigneeTv.setEnabled(true);
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
                goodsSpinner.setVisibility(View.VISIBLE);
                businessTypeSpinner.setVisibility(View.VISIBLE);
                goodsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        goods = goodsAdapter.getItem(position);
                        goodsTv.setText(goods);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                goodsSpinner.setSelection(goodsSpinnerPosition);
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
                choseTimeMethod.getCurrentTime(AwbDetailActivity.this, fDateTv);
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
        spCode = spCode.toUpperCase();
        goods = goodsTv.getText().toString();
        goods = goods.toUpperCase();
//        businessType = businessTypeTv.getText().toString();
        packg = packageTv.getText().toString();
        packg = packg.toUpperCase();
        by = byTv.getText().toString();
        by = by.toUpperCase();
        dep = depTv.getText().toString();
        dep = dep.toUpperCase();
        dest1 = dest1Tv.getText().toString();
        dest1 = dest1.toUpperCase();
        dest2 = dest2Tv.getText().toString();
        dest2 = dest2.toUpperCase();
        remark = remarkTv.getText().toString();
        remark = remark.toUpperCase();
        flightChecked = flightCheckedTv.getText().toString();
        fDate = fDateTv.getText().toString();
        fNo = fnoTv.getText().toString();
        fNo = fNo.toUpperCase();
        shipper = shipperTv.getText().toString();
        shipper = shipper.toUpperCase();
        consignee = consigneeTv.getText().toString();
        consignee = consignee.toUpperCase();
        transportNO = transportNOTv.getText().toString();
        transportNO = transportNO.toUpperCase();
        allowTransNO = allowTransNOTv.getText().toString();
        allowTransNO = allowTransNO.toUpperCase();
        cIQNumber = cIQNumberTv.getText().toString();
        cIQNumber = cIQNumber.toUpperCase();
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
                Intent intent = new Intent(AwbDetailActivity.this, AppDomExpPrePareAWBActivity.class);
                startActivityForResult(intent, AviationCommons.AWB_UPDATA);
                finish();
            }
            super.onPostExecute(request);
        }
    }

}
