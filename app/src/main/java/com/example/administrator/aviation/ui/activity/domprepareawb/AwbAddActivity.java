package com.example.administrator.aviation.ui.activity.domprepareawb;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBAdd;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBUpdate;
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
 * 订单增加
 */

public class AwbAddActivity extends Activity implements View.OnClickListener {
    private EditText mawbTv;
    private EditText pcTv;
    private EditText weightTv;
    private EditText volumeTv;
    private EditText goodsTv;
    private EditText packageTv;
    private EditText dest1Tv;
    private EditText dest2Tv;
    private EditText remarkTv;
    private ImageView choseDateBtn;
    private EditText fDateTv;
    private EditText fnoTv;
    private EditText shipperTv;
    private EditText consigneeTv;
    private Button addSureBtn;

    private String mawbId = "";
    private String mawb;
    private String pc;
    private String weight;
    private String volume;
    private String goods;
    private String businessType;
    private String cnbusinessType;
    private String packg;
    private String dest1;
    private String dest2;
    private String remark;
    private String fDate;
    private String fNo;
    private String shipper;
    private String consignee;
    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;
    private ArrayAdapter<String> businessTypeAdapter;
    private ArrayAdapter<String> goodsAdapter;
    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awbadd);
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
        navBar.setTitle(R.string.awb_add);
        navBar.hideRight();

        mawbTv = (EditText) findViewById(R.id.mawb_add_tv);
        pcTv = (EditText) findViewById(R.id.pc_add_tv);
        weightTv = (EditText) findViewById(R.id.weight_add_tv);
        weightTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        volumeTv = (EditText) findViewById(R.id.volume_add_tv);
        volumeTv.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        goodsTv = (EditText) findViewById(R.id.goods_add_tv);
        goodsTv.setTransformationMethod(new AllCapTransformationMethod());
        packageTv = (EditText) findViewById(R.id.package_add_tv);
        packageTv.setTransformationMethod(new AllCapTransformationMethod());
        dest1Tv = (EditText) findViewById(R.id.dest_add_tv);
        dest1Tv.setTransformationMethod(new AllCapTransformationMethod());
        dest2Tv = (EditText) findViewById(R.id.dest2_add_tv);
        dest2Tv.setTransformationMethod(new AllCapTransformationMethod());
        remarkTv = (EditText) findViewById(R.id.remake_add_tv);
        remarkTv.setTransformationMethod(new AllCapTransformationMethod());
        choseDateBtn = (ImageView) findViewById(R.id.date_chose_btn);
        fDateTv = (EditText) findViewById(R.id.fdate_add_tv);
        fnoTv = (EditText) findViewById(R.id.fno_add_tv);
        fnoTv.setTransformationMethod(new AllCapTransformationMethod());
        shipperTv = (EditText) findViewById(R.id.shipper_add_tv);
        shipperTv.setTransformationMethod(new AllCapTransformationMethod());
        consigneeTv = (EditText) findViewById(R.id.consignee_add_tv);
        consigneeTv.setTransformationMethod(new AllCapTransformationMethod());
        addSureBtn = (Button) findViewById(R.id.sure_add_btn);

        addSureBtn.setOnClickListener(this);

        // 获取选择的计划航班日
        choseDateBtn.setOnClickListener(this);
        // 下拉选择数据
        Spinner businessTypeSpinner = (Spinner) findViewById(R.id.awb_businesstype_spinner);
        List<String> businessTypeList = new ArrayList<>();
        businessTypeList.add("普通货物运输");
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

        Spinner goodsSpinner = (Spinner) findViewById(R.id.awb_package_spinner);
        List<String> goodsList = new ArrayList<>();
        goodsList.add("");
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
        goodsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                packg = goodsAdapter.getItem(position);
                packageTv.setText(packg);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                packageTv.setText("");
            }
        });

    }

    // 提交订单到服务器
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sure_add_btn:
                getEditText();
                xml = HttpPrepareAWBUpdate.getXml(mawbId, mawb, pc,weight,volume,"",
                        goods,  businessType, packg, "", "NKG", dest1,dest2,
                        remark, "", fDate, fNo, shipper,"",
                        consignee,"","","", "");
                userBumen = PreferenceUtils.getUserBumen(this);
                userName = PreferenceUtils.getUserName(this);
                userPass = PreferenceUtils.getUserPass(this);
                loginFlag = PreferenceUtils.getLoginFlag(this);
                new AddXmlAsyncTask().execute();
                break;

            case R.id.date_chose_btn:
                choseTimeMethod.getCurrentTime(AwbAddActivity.this, fDateTv);
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
            super.onPostExecute(request);
            if (request == null && !ErrString.equals("")) {
                Toast.makeText(AwbAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AwbAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AwbAddActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AwbAddActivity.this, AppDomExpPrePareAWBActivity.class);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    // 获取EditText值
    private void getEditText() {
        mawb = mawbTv.getText().toString();
        pc = pcTv.getText().toString();
        weight= weightTv.getText().toString();
        volume= volumeTv.getText().toString();
        goods = goodsTv.getText().toString();
        goods = goods.toUpperCase();
        packg = packageTv.getText().toString();
        packg = packg.toUpperCase();
        dest1 = dest1Tv.getText().toString();
        dest1 = dest1.toUpperCase();
        dest2 = dest2Tv.getText().toString();
        dest2 = dest2.toUpperCase();
        remark = remarkTv.getText().toString();
        remark = remark.toUpperCase();
        fDate = fDateTv.getText().toString();
        fNo = fnoTv.getText().toString();
        fNo = fNo.toUpperCase();
        shipper = shipperTv.getText().toString();
        shipper = shipper.toUpperCase();
        consignee = consigneeTv.getText().toString();
        consignee = consignee.toUpperCase();
    }
}
