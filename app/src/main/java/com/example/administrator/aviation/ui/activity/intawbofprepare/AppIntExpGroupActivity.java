package com.example.administrator.aviation.ui.activity.intawbofprepare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntMawbDeclare;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntMawbDelete;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntMawbUpdate;
import com.example.administrator.aviation.model.intawbprepare.MawbInfo;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.activity.domprepareawb.AwbDetailActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.ChoseTimeMethod;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 国际出港预录入父界面（显示主单的信息信息，并做增删改查）
 */

public class AppIntExpGroupActivity extends Activity implements View.OnClickListener{
    private MawbInfo mawbInfo;
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
    private String cnfreightPayment;
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
    private EditText businessTypeEt;
    private EditText packAgeEt;
    private EditText originEt;
    private EditText depEt;
    private EditText dest1Et;
    private EditText dest2Et;
    private LinearLayout dest2Layout;
    private EditText by1Et;
    private EditText tranFlagEt;
    private EditText remarkEt;
    private EditText fDateEt;
    private EditText fnoEt;
    private EditText customsCodeEt;
    private EditText transPortModeEt;
    private EditText freightPaymentEt;
    private EditText cNEECityEt;
    private EditText cNEECountryEt;
    private EditText mftStatusEt;
    private EditText shipperEt;
    private EditText consigneeEt;
    private EditText gpriceEt;
    private EditText cIQStatusEt;
    private EditText cIQNumberEt;
    private Button updateBtn;
    private Button sureBtn;
    private Button deleteBtn;
    private Button declareBtn;
    private LinearLayout hideLayout;
    private ImageView changeTimeIv;

    private ArrayAdapter<String> businessTypeAdapter;
    private Spinner businessTypeSpinner;
    private List<String> businessTypeList;

    private ArrayAdapter<String> transPortModeAdapter;
    private Spinner transPortModeSpinner;
    private List<String> transPortModeList;

    private ArrayAdapter<String> tranFlagAdapter;
    private Spinner tranFlagSpinner;
    private List<String> tranFlagList;

    private ArrayAdapter<String> freightPaymentAdapter;
    private Spinner freightPaymentSpinner;
    private List<String> freightPaymentList;

    private int businessTypeSpinnerPosition;
    private int transPortModeSpinnerPosition;
    private int tranFlagSpinnerPosition;
    private int freightPaymentPosition;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexpint_group);
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
        mawbInfo = (MawbInfo) getIntent().getSerializableExtra(AviationCommons.INT_GROUP_INFO);
        mawbId = mawbInfo.getMawbID();
        NavBar navBar = new NavBar(this);
        navBar.setTitle(R.string.int_prepare_group_title);
        navBar.showRight();
        navBar.setRight(R.drawable.add);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 主单列表+号新增分单
                Intent intent = new Intent(AppIntExpGroupActivity.this, AppIntExpChildAddActivity.class);
                intent.putExtra(AviationCommons.INT_GROUP_MAWB, mawbInfo.getMawb());
                startActivity(intent);
            }
        });
        mawbEt = (EditText) findViewById(R.id.int_group_mawb_detail_tv);
        pCEt = (EditText) findViewById(R.id.int_group_pc_detail_tv);
        weightEt = (EditText) findViewById(R.id.int_group_weight_detail_tv);
        volumeEt = (EditText) findViewById(R.id.int_group_volume_detail_tv);
        spCodeEt = (EditText) findViewById(R.id.int_group_spcode_detail_tv);
        goodsEt = (EditText) findViewById(R.id.int_group_goods_detail_tv);
        goodsEt.setTransformationMethod(new AllCapTransformationMethod());
        goodsCNEt = (EditText) findViewById(R.id.int_group_goodscn_detail_tv);
        businessTypeEt = (EditText) findViewById(R.id.int_group_businesstype_detail_tv);
        packAgeEt = (EditText) findViewById(R.id.int_group_package_detail_tv);
        originEt = (EditText) findViewById(R.id.int_group_origin_detail_tv);
        originEt.setTransformationMethod(new AllCapTransformationMethod());
        depEt = (EditText) findViewById(R.id.int_group_dep_detail_tv);
        depEt.setTransformationMethod(new AllCapTransformationMethod());
        dest1Et = (EditText) findViewById(R.id.int_group_dest_detail_tv);
        dest1Et.setTransformationMethod(new AllCapTransformationMethod());
        dest2Et = (EditText) findViewById(R.id.int_group_dest2_detail_tv);
        dest2Et.setTransformationMethod(new AllCapTransformationMethod());
        dest2Layout = (LinearLayout) findViewById(R.id.group_dest2_layout);
        by1Et = (EditText) findViewById(R.id.int_group_by_detail_tv);
        by1Et.setTransformationMethod(new AllCapTransformationMethod());
        tranFlagEt = (EditText) findViewById(R.id.int_group_tranFlag_detail_tv);
        remarkEt = (EditText) findViewById(R.id.int_group_remake_detail_tv);
        fDateEt = (EditText) findViewById(R.id.int_group_fdate_detail_tv);
        fnoEt = (EditText) findViewById(R.id.int_group_fno_detail_tv);
        customsCodeEt = (EditText) findViewById(R.id.int_group_customsCode_detail_tv);
        transPortModeEt = (EditText) findViewById(R.id.int_group_transPortMode_detail_tv);
        freightPaymentEt = (EditText) findViewById(R.id.int_group_freightPayment_detail_tv);
        cNEECityEt = (EditText) findViewById(R.id.int_group_cNEECity_detail_tv);
        cNEECountryEt = (EditText) findViewById(R.id.int_group_cNEECountry_detail_tv);
        cNEECountryEt.setTransformationMethod(new AllCapTransformationMethod());
        mftStatusEt = (EditText) findViewById(R.id.int_group_mftStatus_detail_tv);
        shipperEt = (EditText) findViewById(R.id.int_group_shipper_detail_tv);
        shipperEt.setTransformationMethod(new AllCapTransformationMethod());
        consigneeEt = (EditText) findViewById(R.id.int_group_consignee_detail_tv);
        consigneeEt.setTransformationMethod(new AllCapTransformationMethod());
        gpriceEt = (EditText) findViewById(R.id.int_group_gprice_detail_tv);
        cIQStatusEt = (EditText) findViewById(R.id.int_group_cIQStatus_detail_tv);
        cIQNumberEt = (EditText) findViewById(R.id.int_group_cIQNumber_detail_tv);
        updateBtn = (Button) findViewById(R.id.update_int_group_btn);
        sureBtn = (Button) findViewById(R.id.sure_int_group_btn);
        deleteBtn = (Button) findViewById(R.id.delete_int_group_btn);
        declareBtn = (Button) findViewById(R.id.declare_mawb_btn);
        hideLayout = (LinearLayout) findViewById(R.id.hide_int_house_sure_linearlayout);
        changeTimeIv = (ImageView) findViewById(R.id.change_group_time);

        // 国际出港入库管理进入主单界面不能修改和删除还有增加主单
        if (getIntent().getStringExtra(AviationCommons.HIDE_INT_AWB_UPDATE) != null && getIntent().getStringExtra(AviationCommons.HIDE_INT_AWB_UPDATE).equals("hide")) {
            hideLayout.setVisibility(View.GONE);
        } else {
            hideLayout.setVisibility(View.VISIBLE);
        }

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        updateBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        declareBtn.setOnClickListener(this);
        changeTimeIv.setOnClickListener(this);
        setGroupEdiText();
        setEditTextInvisible();

        // 下拉选择数据
        businessTypeSpinner = (Spinner) findViewById(R.id.update_group_businesstype_spinner);
        businessTypeList = new ArrayList<>();
        businessTypeList.add("普通货物运输");
        businessTypeList.add("国际快件");
        businessTypeList.add("D类快件");
        businessTypeList.add("国际邮包");
        businessTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, businessTypeList);
        businessTypeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        businessTypeSpinner.setAdapter(businessTypeAdapter);

        // tranFlag报关类型
        tranFlagSpinner = (Spinner) findViewById(R.id.update_group_traflag_spinner);
        tranFlagList = new ArrayList<>();
        tranFlagList.add("本关");
        tranFlagList.add("转关");
        tranFlagList.add("大通关");
        tranFlagAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tranFlagList);
        tranFlagAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        tranFlagSpinner.setAdapter(tranFlagAdapter);

        // 离境方式
        transPortModeSpinner = (Spinner) findViewById(R.id.update_group_transPortMode_spinner);
        transPortModeList = new ArrayList<>();
        transPortModeList.add("陆运");
        transPortModeList.add("空运");
        transPortModeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transPortModeList);
        transPortModeAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        transPortModeSpinner.setAdapter(transPortModeAdapter);

        // 支付方式
        freightPaymentSpinner = (Spinner) findViewById(R.id.update_group_freightPayment_spinner);
        freightPaymentList = new ArrayList<>();
        freightPaymentList.add("到付");
        freightPaymentList.add("预付");
        freightPaymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, freightPaymentList);
        freightPaymentAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        freightPaymentSpinner.setAdapter(freightPaymentAdapter);
//=============================================================================================================
        for (int i = 0; i <businessTypeList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(businessTypeList.get(i)).equals(businessType)) {
                businessTypeSpinnerPosition =  i;
            }
        }

        for (int i = 0; i <tranFlagList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(tranFlagList.get(i)).equals(tranFlag)) {
                tranFlagSpinnerPosition =  i;
            }
        }

        for (int i = 0; i <transPortModeList.size() ; i++) {
            if (AviationNoteConvert.cNtoEn(transPortModeList.get(i)).equals(transPortMode)) {
                transPortModeSpinnerPosition =  i;
            }
        }

        for (int i = 0; i <freightPaymentList.size(); i++) {
            if (AviationNoteConvert.cNtoEn(freightPaymentList.get(i)).equals(freightPayment)) {
                freightPaymentPosition = i;
            }
        }
//================================================================================================================
    }

    // 给EditText赋值
    private void setGroupEdiText() {
        mawb = mawbInfo.getMawb();
        mawbEt.setText(mawb);
        pC = mawbInfo.getPC();
        pCEt.setText(pC);
        weight = mawbInfo.getWeight();
        weightEt.setText(weight);
        volume = mawbInfo.getVolume();
        volumeEt.setText(volume);
        spCode = mawbInfo.getSpCode();
        spCodeEt.setText(spCode);
        goods = mawbInfo.getGoods();
        goodsEt.setText(goods);
        goodsCN = mawbInfo.getGoodsCN();
        goodsCNEt.setText(goodsCN);
        businessType = mawbInfo.getBusinessType();
        String cnBusinessType = AviationNoteConvert.getCNBusinessType(businessType);
        businessTypeEt.setText(cnBusinessType);
        packAge = mawbInfo.getPackage();
        packAgeEt.setText(packAge);
        origin = mawbInfo.getOrigin();
        originEt.setText(origin);
        dep = mawbInfo.getDep();
        depEt.setText(dep);
        dest1 = mawbInfo.getDest1();
        dest2 = mawbInfo.getDest2();
        if (dest1.equals("") && !dest2.equals("")) {
            dest1Et.setText(dest2);
        } else {
            dest2Layout.setVisibility(View.VISIBLE);
            dest1Et.setText(dest1);
            dest2Et.setText(dest2);
        }

        by1 = mawbInfo.getBy1();
        by1Et.setText(by1);
        tranFlag = mawbInfo.getTranFlag();
        String cnTranFlag = AviationNoteConvert.getCNTranFlag(tranFlag);
        tranFlagEt.setText(cnTranFlag);
        remark = mawbInfo.getRemark();
        remarkEt.setText(remark);
        fDate = mawbInfo.getMawbm().getFDate();
        fDateEt.setText(fDate);
        fno = mawbInfo.getMawbm().getFno();
        fnoEt.setText(fno);
        customsCode = mawbInfo.getMawbm().getCustomsCode();
        customsCodeEt.setText(customsCode);
        transPortMode = mawbInfo.getMawbm().getTransPortMode();
        String cnTransPortMode = AviationNoteConvert.getCNTransPortMode(transPortMode);
        transPortModeEt.setText(cnTransPortMode);
        freightPayment = mawbInfo.getMawbm().getFreightPayment();
        String cnFreightPayment = AviationNoteConvert.getCNfreightPayment(freightPayment);
        freightPaymentEt.setText(cnFreightPayment);
        cNEECity = mawbInfo.getMawbm().getCNEECity();
        cNEECityEt.setText(cNEECity);
        cNEECountry = mawbInfo.getMawbm().getCNEECountry();
        cNEECountryEt.setText(cNEECountry);
        mftStatus = mawbInfo.getMawbm().getMftStatus();
        mftStatusEt.setText(mftStatus);
        shipper = mawbInfo.getMawbm().getShipper();
        shipperEt.setText(shipper);
        consignee = mawbInfo.getMawbm().getConsignee();
        consigneeEt.setText(consignee);
        gprice = mawbInfo.getMawbm().getGprice();
        gpriceEt.setText(gprice);
        cIQStatus = mawbInfo.getMawbm().getCIQStatus();
        cIQStatusEt.setText(cIQStatus);
        cIQNumber = mawbInfo.getMawbm().getCIQNumber();
        cIQNumberEt.setText(cIQNumber);

    }

    // 隐藏EditText
    private void setEditTextInvisible() {
        mawbEt.setEnabled(false);
        pCEt.setEnabled(false);
        weightEt.setEnabled(false);
        volumeEt.setEnabled(false);
        spCodeEt.setEnabled(false);
        goodsEt.setEnabled(false);
        goodsCNEt.setEnabled(false);
        businessTypeEt.setEnabled(false);
        packAgeEt.setEnabled(false);
        originEt.setEnabled(false);
        depEt.setEnabled(false);
        dest1Et.setEnabled(false);
        by1Et.setEnabled(false);
        tranFlagEt.setEnabled(false);
        remarkEt.setEnabled(false);
        fDateEt.setEnabled(false);
        fnoEt.setEnabled(false);
        customsCodeEt.setEnabled(false);
        transPortModeEt.setEnabled(false);
        freightPaymentEt.setEnabled(false);
        cNEECityEt.setEnabled(false);
        cNEECountryEt.setEnabled(false);
        mftStatusEt.setEnabled(false);
        shipperEt.setEnabled(false);
        consigneeEt.setEnabled(false);
        gpriceEt.setEnabled(false);
        cIQStatusEt.setEnabled(false);
        cIQNumberEt.setEnabled(false);
    }

    // 显示ditText
    private void setEditTextVisible() {
        mawbEt.setEnabled(true);
        pCEt.setEnabled(true);
        weightEt.setEnabled(true);
        volumeEt.setEnabled(true);
        spCodeEt.setEnabled(true);
        goodsEt.setEnabled(true);
        goodsCNEt.setEnabled(true);
        businessTypeEt.setEnabled(true);
        packAgeEt.setEnabled(true);
        originEt.setEnabled(true);
        depEt.setEnabled(true);
        dest1Et.setEnabled(true);
        by1Et.setEnabled(true);
        tranFlagEt.setEnabled(true);
        remarkEt.setEnabled(true);
        fDateEt.setEnabled(true);
        fnoEt.setEnabled(true);
        customsCodeEt.setEnabled(true);
        transPortModeEt.setEnabled(true);
        freightPaymentEt.setEnabled(true);
        cNEECityEt.setEnabled(true);
        cNEECountryEt.setEnabled(true);
        mftStatusEt.setEnabled(true);
        shipperEt.setEnabled(true);
        consigneeEt.setEnabled(true);
        gpriceEt.setEnabled(true);
        cIQStatusEt.setEnabled(true);
        cIQNumberEt.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 更新主单按钮
            case R.id.update_int_group_btn:
                setEditTextVisible();
                sureBtn.setVisibility(View.VISIBLE);
                updateBtn.setVisibility(View.GONE);
                dest2Layout.setVisibility(View.VISIBLE);
                businessTypeEt.setVisibility(View.GONE);
                businessTypeSpinner.setVisibility(View.VISIBLE);
                tranFlagEt.setVisibility(View.GONE);
                tranFlagSpinner.setVisibility(View.VISIBLE);
                transPortModeEt.setVisibility(View.GONE);
                transPortModeSpinner.setVisibility(View.VISIBLE);
                freightPaymentEt.setVisibility(View.GONE);
                freightPaymentSpinner.setVisibility(View.VISIBLE);
                changeTimeIv.setVisibility(View.VISIBLE);

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

                freightPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        cnfreightPayment = freightPaymentAdapter.getItem(position);
                        freightPayment = AviationNoteConvert.cNtoEn(cnfreightPayment);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        cnfreightPayment = freightPaymentAdapter.getItem(0);
                        freightPayment = AviationNoteConvert.cNtoEn(cnfreightPayment);
                    }
                });


                businessTypeSpinner.setSelection(businessTypeSpinnerPosition);
                tranFlagSpinner.setSelection(tranFlagSpinnerPosition);
                transPortModeSpinner.setSelection(transPortModeSpinnerPosition);
                freightPaymentSpinner.setSelection(freightPaymentPosition);
                break;

            // 更新确定按钮
            case R.id.sure_int_group_btn:
                getEditText();
                xml = HttpIntMawbUpdate.updateIntMawbXml(mawbId,mawb,pC,weight,volume,spCode,goods,goodsCN,businessType,
                        packAge,origin,dep,dest1,dest2,by1,tranFlag,remark,fDate,fno,customsCode,transPortMode,freightPayment,shipper,
                        consignee,gprice);
                new UpdateGroupAsyncTask().execute();
                break;

            case R.id.delete_int_group_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(AppIntExpGroupActivity.this);
                builder.setTitle("删除订单")
                        .setMessage("确定删除订单号为:"+mawb+"订单吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteGroupAsyncTask().execute();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();
                break;

            // 预配申报
            case R.id.declare_mawb_btn:
                new DeclareMawbAsyncTask().execute();
                break;

            // 更新时间
            case R.id.change_group_time:
                choseTimeMethod.getCurrentTime(AppIntExpGroupActivity.this, fDateEt);
            default:
                break;
        }
    }

    // 更新分单列表
    class UpdateGroupAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntMawbUpdate.updateIntMawb(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntExpGroupActivity.this, "修改成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AppIntExpGroupActivity.this, AppIntExpPrepareAWBActivity.class);
//                startActivityForResult(intent, 5);
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 删除主单
    class DeleteGroupAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntMawbDelete.deleteIntMawb(userBumen, userName, userPass, loginFlag, mawb);
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
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("")) {
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")) {
                Toast.makeText(AppIntExpGroupActivity.this, "删除成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AppIntExpGroupActivity.this, AppIntExpPrepareAWBActivity.class);
//                startActivityForResult(intent, 6);
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 预配申报异步任务
    class DeclareMawbAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntMawbDeclare.declareIntMawb(userBumen, userName, userPass, loginFlag, mawb);
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
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("")) {
                Toast.makeText(AppIntExpGroupActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")) {
                Toast.makeText(AppIntExpGroupActivity.this, "申报成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntExpGroupActivity.this, AppIntExpPrepareAWBActivity.class);
                startActivity(intent);
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 获得EditText值
    private void getEditText() {
        mawb = mawbEt.getText().toString();
        pC = pCEt.getText().toString();
        weight = weightEt.getText().toString();
        volume = volumeEt.getText().toString();
        spCode = spCodeEt.getText().toString();
        goods = goodsEt.getText().toString();
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
        remark = remarkEt.getText().toString();
        fDate = fDateEt.getText().toString();
        fno = fnoEt.getText().toString();
        customsCode = customsCodeEt.getText().toString();

        // 将得到输入框的值转换成大写字母
        cNEECity = cNEECityEt.getText().toString();
        cNEECountry = cNEECountryEt.getText().toString();
        cNEECountry = cNEECountry.toUpperCase();
        mftStatus = mftStatusEt.getText().toString();
        shipper = shipperEt.getText().toString();
        shipper = shipper.toUpperCase();
        consignee = consigneeEt.getText().toString();
        consignee = consignee.toUpperCase();
        gprice = gpriceEt.getText().toString();
        cIQStatus = cIQStatusEt.getText().toString();
        cIQNumber = cIQNumberEt.getText().toString();

    }
}
