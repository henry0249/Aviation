package com.example.administrator.aviation.ui.activity.intawbofprepare;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntMawbAdd;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 国际出港预录入新增主单界面
 */

public class AppIntExpGroupAddActivity extends Activity implements View.OnClickListener{
    private String mawbId;
    private String mawb;
    private String pC;
    private String weight;
    private String volume;
    private String spCode;
    private String goods;
    private String goodsCN;
    private String businessType;
    private String cnbusinessType;
    private String packAge;
    private String origin;
    private String dep;
    private String dest1;
    private String dest2;
    private String by1;
    private String tranFlag;
    private String cntranFlag;
    private String remark;
    private String fDate;
    private String fno;
    private String customsCode;
    private String transPortMode;
    private String cntransPortMode;
    private String freightPayment;
    private String cNEECity;
    private String cNEECountry;
    private String mftStatus;
    private String shipper;
    private String consignee;
    private String gprice;
    private String cIQStatus;
    private String cIQNumber;

    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;

    private EditText mawbEt;
    private EditText pCEt;
    private EditText weightEt;
    private EditText volumeEt;
    private EditText spCodeEt;
    private EditText goodsEt;
    private EditText goodsCNEt;
    private TextView businessTypeEt;
    private EditText packAgeEt;
    private EditText originEt;
    private EditText depEt;
    private EditText dest1Et;
    private EditText dest2Et;
    private LinearLayout dest2Layout;
    private EditText by1Et;
    private TextView tranFlagEt;
    private EditText remarkEt;
    private EditText fDateEt;
    private EditText fnoEt;
    private EditText customsCodeEt;
    private TextView transPortModeEt;
    private EditText freightPaymentEt;
    private EditText cNEECityEt;
    private EditText cNEECountryEt;
    private EditText mftStatusEt;
    private EditText shipperEt;
    private EditText consigneeEt;
    private EditText gpriceEt;
    private EditText cIQStatusEt;
    private EditText cIQNumberEt;
    private Button sureBtn;

    private ArrayAdapter<String> businessTypeAdapter;

    private ArrayAdapter<String> tranFlagAdapter;

    private ArrayAdapter<String> transPortModeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexpint_group_add);
        initView();
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

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle("新增主单");

        mawbEt = (EditText) findViewById(R.id.int_group_add_mawb_detail_tv);
        pCEt = (EditText) findViewById(R.id.int_group_add_pc_detail_tv);
        weightEt = (EditText) findViewById(R.id.int_group_add_weight_detail_tv);
        volumeEt = (EditText) findViewById(R.id.int_group_add_volume_detail_tv);
        spCodeEt = (EditText) findViewById(R.id.int_group_add_spcode_detail_tv);
        goodsEt = (EditText) findViewById(R.id.int_group_add_goods_detail_tv);
        goodsEt.setTransformationMethod(new AllCapTransformationMethod());
        goodsCNEt = (EditText) findViewById(R.id.int_group_add_goodscn_detail_tv);
        businessTypeEt = (TextView) findViewById(R.id.int_group_add_businesstype_detail_tv);
        packAgeEt = (EditText) findViewById(R.id.int_group_add_package_detail_tv);
        originEt = (EditText) findViewById(R.id.int_group_add_origin_detail_tv);
        originEt.setTransformationMethod(new AllCapTransformationMethod());
        depEt = (EditText) findViewById(R.id.int_group_add_dep_detail_tv);
        depEt.setTransformationMethod(new AllCapTransformationMethod());
        dest1Et = (EditText) findViewById(R.id.int_group_add_dest_detail_tv);
        dest1Et.setTransformationMethod(new AllCapTransformationMethod());
        dest2Et = (EditText) findViewById(R.id.int_group_add_dest2_detail_tv);
        dest2Et.setTransformationMethod(new AllCapTransformationMethod());
        dest2Layout = (LinearLayout) findViewById(R.id.group_add_dest2_layout);
        by1Et = (EditText) findViewById(R.id.int_group_add_by_detail_tv);
        by1Et.setTransformationMethod(new AllCapTransformationMethod());
        tranFlagEt = (TextView) findViewById(R.id.int_group_add_tranFlag_detail_tv);
        remarkEt = (EditText) findViewById(R.id.int_group_add_remake_detail_tv);
        fDateEt = (EditText) findViewById(R.id.int_group_add_fdate_detail_tv);
        fnoEt = (EditText) findViewById(R.id.int_group_add_fno_detail_tv);
        customsCodeEt = (EditText) findViewById(R.id.int_group_add_customsCode_detail_tv);
        transPortModeEt = (TextView) findViewById(R.id.int_group_add_transPortMode_detail_tv);
        freightPaymentEt = (EditText) findViewById(R.id.int_group_add_freightPayment_detail_tv);
        freightPaymentEt.setTransformationMethod(new AllCapTransformationMethod());
        cNEECityEt = (EditText) findViewById(R.id.int_group_add_cNEECity_detail_tv);
        cNEECountryEt = (EditText) findViewById(R.id.int_group_add_cNEECountry_detail_tv);
        cNEECountryEt.setTransformationMethod(new AllCapTransformationMethod());
        mftStatusEt = (EditText) findViewById(R.id.int_group_add_mftStatus_detail_tv);
        shipperEt = (EditText) findViewById(R.id.int_group_add_shipper_detail_tv);
        consigneeEt = (EditText) findViewById(R.id.int_group_add_consignee_detail_tv);
        gpriceEt = (EditText) findViewById(R.id.int_group_add_gprice_detail_tv);
        cIQStatusEt = (EditText) findViewById(R.id.int_group_add_cIQStatus_detail_tv);
        cIQNumberEt = (EditText) findViewById(R.id.int_group_add_cIQNumber_detail_tv);

        sureBtn = (Button) findViewById(R.id.add_int_group_btn);
        sureBtn.setOnClickListener(this);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        // 下拉选择数据
        Spinner businessTypeSpinner = (Spinner) findViewById(R.id.businesstype_spinner);
        List<String> businessTypeList = new ArrayList<>();
        businessTypeList.add("普通货物运输");
        businessTypeList.add("国际快件");
        businessTypeList.add("D类快件");
        businessTypeList.add("国际邮包");
        businessTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, businessTypeList);
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        businessTypeSpinner.setAdapter(businessTypeAdapter);
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

        // tranFlag报关类型
        Spinner tranFlagSpinner = (Spinner) findViewById(R.id.tranFlag_spinner);
        List<String> tranFlagList = new ArrayList<>();
        tranFlagList.add("本关");
        tranFlagList.add("转关");
        tranFlagList.add("大通关");
        tranFlagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tranFlagList);
        tranFlagAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        tranFlagSpinner.setAdapter(tranFlagAdapter);
        tranFlagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                cntranFlag = tranFlagAdapter.getItem(position);
                tranFlag = AviationNoteConvert.cNtoEn(cntranFlag);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cntranFlag = tranFlagAdapter.getItem(0);
                tranFlag = AviationNoteConvert.cNtoEn(cntranFlag);
            }
        });
        if (tranFlagSpinner.getCount() > 0) {
            tranFlagSpinner.setSelection(0);
        }

        // 离境方式
        Spinner transPortModeSpinner = (Spinner) findViewById(R.id.transPortMode_spinner);
        List<String> transPortModeList = new ArrayList<>();
        transPortModeList.add("陆运");
        transPortModeList.add("空运");
        transPortModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transPortModeList);
        transPortModeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        transPortModeSpinner.setAdapter(transPortModeAdapter);
        transPortModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                cntransPortMode = transPortModeAdapter.getItem(position);
                transPortMode = AviationNoteConvert.cNtoEn(cntransPortMode);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                cntransPortMode = transPortModeAdapter.getItem(0);
                transPortMode = AviationNoteConvert.cNtoEn(cntransPortMode);
            }
        });
        if (transPortModeSpinner.getCount() > 0) {
            transPortModeSpinner.setSelection(0);
        }

    }

    // 获得EditText值
    private void getEditText() {
        mawb = mawbEt.getText().toString().trim();
        pC = pCEt.getText().toString();
        weight = weightEt.getText().toString();
        volume = volumeEt.getText().toString();
        spCode = spCodeEt.getText().toString();
        goods = goodsEt.getText().toString();

        // 将输入框的内容转换成大写
        goods = goods.toUpperCase();
        goodsCN = goodsCNEt.getText().toString();
        packAge = packAgeEt.getText().toString();
        origin = originEt.getText().toString();
        origin = origin.toUpperCase();
        dep = depEt.getText().toString();
        dep = dep.toUpperCase();
        dest1 = dest1Et.getText().toString();
        dest1 = dest1.toUpperCase();
        dest2 = dest2Et.getText().toString();
        dest2 = dest2.toUpperCase();
        by1 = by1Et.getText().toString();
        by1 = by1.toUpperCase();
//        String cntranFlag = tranFlagEt.getText().toString();
//        tranFlag = AviationNoteConvert.cNtoEn(cntranFlag);
        remark = remarkEt.getText().toString();
        fDate = fDateEt.getText().toString();
        fno = fnoEt.getText().toString();
        customsCode = customsCodeEt.getText().toString();
//        String cntransPortMode = transPortModeEt.getText().toString();
//        transPortMode = AviationNoteConvert.cNtoEn(cntransPortMode);
        freightPayment = freightPaymentEt.getText().toString();
        freightPayment = freightPayment.toUpperCase();
        cNEECity = cNEECityEt.getText().toString();
        cNEECountry = cNEECountryEt.getText().toString();
        cNEECountry = cNEECountry.toUpperCase();
        mftStatus = mftStatusEt.getText().toString();
        shipper = shipperEt.getText().toString();
        consignee = consigneeEt.getText().toString();
        gprice = gpriceEt.getText().toString();
        cIQStatus = cIQStatusEt.getText().toString();
        cIQNumber = cIQNumberEt.getText().toString();
    }

    // 新增主订单
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_int_group_btn:
                getEditText();
                xml = HttpIntMawbAdd.addIntMawbXml(mawb,pC,weight,volume,spCode,goods,goodsCN,businessType,
                        packAge,origin,dep,dest1,dest2,by1,tranFlag,remark,fDate,fno,customsCode,transPortMode,freightPayment,shipper,
                        consignee,gprice);
                new AddGroupAsyncTask().execute();
                break;

            default:
                break;
        }
    }

    class AddGroupAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntMawbAdd.addIntGroup(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntExpGroupAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpGroupAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntExpGroupAddActivity.this, "上传成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AppIntExpGroupAddActivity.this, AppIntExpPrepareAWBActivity.class);
//                startActivityForResult(intent, 7);
                finish();
            }
            super.onPostExecute(request);
        }
    }
}
