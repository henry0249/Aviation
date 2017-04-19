package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpCGOResetExportDeclareInfo;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mengxing on 2017/3/27.
 * 预配与运抵详情界面
 */

public class AppIntOneKeyDeclareItemDetailActivity extends Activity implements View.OnClickListener{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String mawb="";
    private String newRearchID="";
    private String rearchID="";
    private String customsCode="";
    private String transportmode="";
    private String cntransPortMode;
    private String freightPayment="";
    private String cnFreightPayment;
    private String cNEECity="";
    private String cNEECountry="";
    private String fDate="";
    private String fno="";


    private TextView mawbTv;
    private TextView hnoTv;
    private EditText rearchIDTv;

    // 新运抵编号设置
    private LinearLayout newDeclareRerchIDLayout;
    private EditText newRechidTv;
    private TextView cMDStatusTv;
    private TextView spCodeTv;
    private TextView goodsTv;
    private EditText fDateTv;
    private EditText fnoTv;
    private TextView dest1Tv;
    private TextView destTv;
    private EditText customsCodeTv;
    private EditText transportmodeTv;
    private EditText freightPaymentTv;
    private EditText cNEECityTv;
    private EditText cNEECountryTv;
    private TextView mftStatusTv;
    private TextView mftMSGIDTv;
    private TextView pcTv;
    private TextView weightTv;
    private TextView volumeTv;
    private TextView arrivalStatusTv;
    private TextView arrMSGIDTv;
    private TextView responseTv;

    // 离境方式
    private ArrayAdapter<String> transPortModeAdapter;
    private Spinner transPortModeSpinner;
    private List<String> transPortModeList;
    private int transPortModeSpinnerPosition;

    // 支付方式
    private ArrayAdapter<String> freightPaymentAdapter;
    private Spinner freightPaymentSpinner;
    private List<String> freightPaymentList;
    private int freightPaymentPosition;

    private Button resrtBtn;
    private Button sureResrtBtn;
    private Button spliteBtn;
    private String pc;
    private Declare declare;
    private String weight;
    private String volume;

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

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        mawbTv = (TextView) findViewById(R.id.declare_mawb_detail_tv);
        hnoTv = (TextView) findViewById(R.id.declare_hno_detail_tv);
        rearchIDTv = (EditText) findViewById(R.id.declare_rearchid_detail_tv);
        newDeclareRerchIDLayout = (LinearLayout) findViewById(R.id.new_declare_layout);
        newRechidTv = (EditText) findViewById(R.id.declare_new_rearchid_detail_tv);
        cMDStatusTv = (TextView) findViewById(R.id.declare_cmdstatus_detail_tv);
        spCodeTv = (TextView) findViewById(R.id.declare_spcode_detail_tv);
        goodsTv = (TextView) findViewById(R.id.declare_goods_detail_tv);
        fDateTv = (EditText) findViewById(R.id.declare_fdate_detail_tv);
        fnoTv = (EditText) findViewById(R.id.declare_fno_detail_tv);
        fnoTv.setTransformationMethod(new AllCapTransformationMethod());
        dest1Tv = (TextView) findViewById(R.id.declare_dest1_detail_tv);
        destTv = (TextView) findViewById(R.id.declare_dest_detail_tv);
        customsCodeTv = (EditText) findViewById(R.id.declare_customscode_detail_tv);
        transportmodeTv = (EditText) findViewById(R.id.declare_transPortMode_detail_tv);
        freightPaymentTv = (EditText) findViewById(R.id.declare_FreightPayment_detail_tv);
        cNEECityTv = (EditText) findViewById(R.id.declare_cneecity_detail_tv);
        cNEECityTv.setTransformationMethod(new AllCapTransformationMethod());
        cNEECountryTv = (EditText) findViewById(R.id.declare_cneecountry_detail_tv);
        cNEECountryTv.setTransformationMethod(new AllCapTransformationMethod());
        mftStatusTv = (TextView) findViewById(R.id.declare_MftStatus_detail_tv);
        mftMSGIDTv = (TextView) findViewById(R.id.declare_MftMSGID_detail_tv);
        pcTv = (TextView) findViewById(R.id.declare_pc_detail_tv);
        weightTv = (TextView) findViewById(R.id.declare_Weight_detail_tv);
        volumeTv = (TextView) findViewById(R.id.declare_Volume_detail_tv);
        arrivalStatusTv = (TextView) findViewById(R.id.declare_ArrivalStatus_detail_tv);
        arrMSGIDTv = (TextView) findViewById(R.id.declare_ArrMSGID_detail_tv);
        responseTv = (TextView) findViewById(R.id.declare_Response_detail_tv);

        // 重置申报按钮
        resrtBtn = (Button) findViewById(R.id.resert_btn);
        resrtBtn.setOnClickListener(this);
        sureResrtBtn = (Button) findViewById(R.id.resert_sure_btn);
        sureResrtBtn.setOnClickListener(this);

        // 支线拆分
        spliteBtn = (Button) findViewById(R.id.splite_btn);
        spliteBtn.setOnClickListener(this);

        // 给控件赋值
        declare = (Declare) getIntent().getSerializableExtra(AviationCommons.DECLARE_INFO);
        setTextView();
        setEditTextInvisable();

        // 离境方式
        transPortModeSpinner = (Spinner) findViewById(R.id.declare_transPortMode_spinner);
        transPortModeList = new ArrayList<>();
        transPortModeList.add("陆运");
        transPortModeList.add("空运");
        transPortModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transPortModeList);
        transPortModeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        transPortModeSpinner.setAdapter(transPortModeAdapter);

        for (int i = 0; i <transPortModeList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(transPortModeList.get(i)).equals(transportmode)) {
                transPortModeSpinnerPosition =  i;
            }
        }

        // 支付方式
        freightPaymentSpinner = (Spinner) findViewById(R.id.declare_FreightPayment_spinner);
        freightPaymentList = new ArrayList<>();
        freightPaymentList.add("预付");
        freightPaymentList.add("到付");
        freightPaymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, freightPaymentList);
        freightPaymentAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        freightPaymentSpinner.setAdapter(freightPaymentAdapter);

        for (int i = 0; i <freightPaymentList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(freightPaymentList.get(i)).equals(freightPayment)) {
                freightPaymentPosition =  i;
            }
        }
    }

    // 点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 重置申报
            case R.id.resert_btn:
                setEditTextVisable();
                resrtBtn.setVisibility(View.GONE);
                sureResrtBtn.setVisibility(View.VISIBLE);
                rearchID = getIntent().getStringExtra(AviationCommons.DECLARE_REARCHID);
                // 判断原始编号不为空
                if (!rearchID.equals("")) {
                    newDeclareRerchIDLayout.setVisibility(View.VISIBLE);
                    newRearchID = newRechidTv.getText().toString();
                }
                transPortModeSpinner.setVisibility(View.VISIBLE);
                transportmodeTv.setVisibility(View.GONE);
                transPortModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        cntransPortMode = transPortModeAdapter.getItem(position);
                        transportmode = AviationNoteConvert.cNtoEn(cntransPortMode);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        cntransPortMode = transPortModeAdapter.getItem(0);
                        transportmode = AviationNoteConvert.cNtoEn(cntransPortMode);
                    }
                });
                freightPaymentSpinner.setVisibility(View.VISIBLE);
                freightPaymentTv.setVisibility(View.GONE);

                freightPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        cnFreightPayment = freightPaymentAdapter.getItem(position);
                        freightPayment = AviationNoteConvert.cNtoEn(cnFreightPayment);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        cnFreightPayment = freightPaymentAdapter.getItem(0);
                        freightPayment = AviationNoteConvert.cNtoEn(cnFreightPayment);
                    }
                });
                transPortModeSpinner.setSelection(transPortModeSpinnerPosition);
                freightPaymentSpinner.setSelection(freightPaymentPosition);
                break;

            // 提交重置申报信息
            case R.id.resert_sure_btn:
                mawb = getIntent().getStringExtra(AviationCommons.DECLARE_MAWB);
                getEditext();
                 String xml = HttpCGOResetExportDeclareInfo.resetXml(mawb, fDate, fno, customsCode, transportmode, freightPayment,
                        cNEECity, cNEECountry, newRearchID, rearchID);
                new ResetDeclareAsyTask(xml).execute();
                break;

            // 支线拆分事件
            case R.id.splite_btn:
                rearchID = getIntent().getStringExtra(AviationCommons.DECLARE_REARCHID);
                if (rearchID.equals("")) {
                    Toast.makeText(AppIntOneKeyDeclareItemDetailActivity.this, "不支持支线拆分", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(this, AppIntSplitSubLineArrivalActivity.class);
                    intent.putExtra(AviationCommons.SPLITE_REARCHID, rearchID);
                    intent.putExtra(AviationCommons.SPLITE_PC, pc);
                    intent.putExtra(AviationCommons.SPLITE_WEIGHT, weight);
                    intent.putExtra(AviationCommons.SPLITE_VOLUME, volume);
                    startActivity(intent);
                }
                break;

            default:
                break;
        }

    }

    // 给textView赋值
    private void setTextView() {
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

        transportmode = declare.getTransportmode();
        String cnTransPortMode = AviationNoteConvert.getCNTransPortMode(transportmode);
        transportmodeTv.setText(cnTransPortMode);

        freightPayment = declare.getFreightPayment();
        String cnFreightPayment = AviationNoteConvert.getCNfreightPayment(freightPayment);
        freightPaymentTv.setText(cnFreightPayment);

        String cNEECity = declare.getCNEECity();
        cNEECityTv.setText(cNEECity);
        String cNEECountry = declare.getCNEECountry();
        cNEECountryTv.setText(cNEECountry);
        String mftStatus = declare.getMftStatus();
        mftStatusTv.setText(mftStatus);
        String mftMSGID = declare.getMftMSGID();
        mftMSGIDTv.setText(mftMSGID);
        pc = declare.getPC();
        pcTv.setText(pc);
        weight = declare.getWeight();
        weightTv.setText(weight);
        volume = declare.getVolume();
        volumeTv.setText(volume);
        String arrivalStatus = declare.getArrivalStatus();
        arrivalStatusTv.setText(arrivalStatus);
        String arrMSGID = declare.getArrMSGID();
        arrMSGIDTv.setText(arrMSGID);
        String response = declare.getResponse();
        responseTv.setText(response);
    }

    // 设置editText不可以编辑
    private void setEditTextInvisable() {
        rearchIDTv.setEnabled(false);
        fnoTv.setEnabled(false);
        fDateTv.setEnabled(false);
        freightPaymentTv.setEnabled(false);
        cNEECityTv.setEnabled(false);
        cNEECountryTv.setEnabled(false);
        customsCodeTv.setEnabled(false);
        transportmodeTv.setEnabled(false);
    }

    // 设置editText可编辑
    private void setEditTextVisable() {
//        rearchIDTv.setEnabled(true);
        fnoTv.setEnabled(true);
        fDateTv.setEnabled(true);
        freightPaymentTv.setEnabled(true);
        cNEECityTv.setEnabled(true);
        cNEECountryTv.setEnabled(true);
        customsCodeTv.setEnabled(true);
        transportmodeTv.setEnabled(true);
    }

    // 获取editText值
    private void getEditext() {
        newRearchID = "";
//        rearchID = rearchIDTv.getText().toString();
        customsCode = customsCodeTv.getText().toString();
//        transportmode = transportmodeTv.getText().toString();
//        freightPayment = freightPaymentTv.getText().toString();
        cNEECity = cNEECityTv.getText().toString();
        cNEECity = cNEECity.toUpperCase();
        cNEECountry = cNEECountryTv.getText().toString();
        cNEECountry = cNEECountry.toUpperCase();
        fDate = fDateTv.getText().toString();
        fno = fnoTv.getText().toString();
        fno = fno.toUpperCase();
    }

    private class ResetDeclareAsyTask extends AsyncTask<Void, Void, String> {
        String xml = null;
        String result = null;
        public ResetDeclareAsyTask(String xml) {
            this.xml = xml;
        }

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpCGOResetExportDeclareInfo.resetExportOneKeyDeclare(userBumen, userName, userPass, loginFlag,xml);
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
                Toast.makeText(AppIntOneKeyDeclareItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntOneKeyDeclareItemDetailActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("true")) {
                Toast.makeText(AppIntOneKeyDeclareItemDetailActivity.this, "成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntOneKeyDeclareItemDetailActivity.this, AppIntExpOneKeyDeclareActivity.class);
                startActivity(intent);
            }
        }
    }

}
