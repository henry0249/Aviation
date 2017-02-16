package com.example.administrator.aviation.ui.activity.prepareawb;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBAdd;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBUpdate;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单增加
 */

public class AwbAddActivity extends Activity implements View.OnClickListener {
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
    private Button choseDateBtn;
    private EditText fDateTv;
    private EditText fnoTv;
    private EditText shipperTv;
    private EditText shipperTELTv;
    private EditText consigneeTv;
    private EditText cNEETELTv;
    private EditText transportNOTv;
    private EditText allowTransNOTv;
    private EditText cIQNumberTv;
    private Button addSureBtn;

    private String mawbId = "";
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awbadd);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle(R.string.awb_add);
        navBar.hideRight();

        mawbTv = (EditText) findViewById(R.id.mawb_add_tv);
        pcTv = (EditText) findViewById(R.id.pc_add_tv);
        weightTv = (EditText) findViewById(R.id.weight_add_tv);
        volumeTv = (EditText) findViewById(R.id.volume_add_tv);
        spcodeTv = (EditText) findViewById(R.id.spcode_add_tv);
        goodsTv = (EditText) findViewById(R.id.goods_add_tv);
//        businessTypeTv = (EditText) findViewById(R.id.businesstype_add_tv);
        packageTv = (EditText) findViewById(R.id.package_add_tv);
        byTv = (EditText) findViewById(R.id.by_add_tv);
        depTv = (EditText) findViewById(R.id.dep_add_tv);
        dest1Tv = (EditText) findViewById(R.id.dest_add_tv);
        dest2Tv = (EditText) findViewById(R.id.dest2_add_tv);
        remarkTv = (EditText) findViewById(R.id.remake_add_tv);
        flightCheckedTv = (EditText) findViewById(R.id.flightchecked_add_tv);
        choseDateBtn = (Button) findViewById(R.id.date_chose_btn);
        fDateTv = (EditText) findViewById(R.id.fdate_add_tv);
        fnoTv = (EditText) findViewById(R.id.fno_add_tv);
        shipperTv = (EditText) findViewById(R.id.shipper_add_tv);
        shipperTELTv = (EditText) findViewById(R.id.shippertel_add_tv);
        consigneeTv = (EditText) findViewById(R.id.consignee_add_tv);
        cNEETELTv = (EditText) findViewById(R.id.cneetel_add_tv);
        transportNOTv = (EditText) findViewById(R.id.transportno_add_tv);
        allowTransNOTv = (EditText) findViewById(R.id.allowtransno_add_tv);
        cIQNumberTv = (EditText) findViewById(R.id.ciqnumber_add_tv);
        addSureBtn = (Button) findViewById(R.id.sure_add_btn);

        addSureBtn.setOnClickListener(this);

        // 获取选择的计划航班日
        choseDateBtn.setOnClickListener(this);
        // 下拉选择数据
        Spinner businessTypeSpinner = (Spinner) findViewById(R.id.awb_businesstype_spinner);
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

    }

    // 提交订单到服务器
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_add_btn:
                getEditText();
                xml = HttpPrepareAWBUpdate.getXml(mawbId, mawb, pc,weight,volume,spCode,
                        goods,  businessType, packg, by, dep, dest1,dest2,
                        remark, flightChecked, fDate, fNo, shipper, shipperTEL,
                        consignee, cNEETEL,transportNO,allowTransNO, cIQNumber);
                userBumen = PreferenceUtils.getUserBumen(this);
                userName = PreferenceUtils.getUserName(this);
                userPass = PreferenceUtils.getUserPass(this);
                loginFlag = PreferenceUtils.getLoginFlag(this);
                new AddXmlAsyncTask().execute();
                break;

            case R.id.date_chose_btn:
                new DatePickerDialog(AwbAddActivity.this,new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        fDateTv.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
                    }
                },2016,12,20).show();

            default:
                break;
        }
    }

    // 上传xml到服务器
    class AddXmlAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareAWBAdd.addAWBdetail(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AwbAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AwbAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AwbAddActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AwbAddActivity.this, AppDomExpPrePareAWBActivity.class);
                startActivityForResult(intent, 0);
                // 上传成功后finish掉当前的activity
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 获取EditText值
    private void getEditText() {
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
}
