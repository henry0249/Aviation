package com.example.administrator.aviation.ui.activity.intawbofprepare;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntHawbAdd;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntHawbUpdate;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * 分单列表增加界面
 */

public class AppIntExpChildAddActivity extends Activity implements View.OnClickListener{
    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;

    private String mawb;
    private String hno;
    private String pc;
    private String weight;
    private String volume;
    private String goods;
    private String goodsCN;

    private EditText hnoEt;
    private EditText pcEt;
    private EditText weightEt;
    private EditText volumeEt;
    private EditText goodsEt;
    private EditText goodsCNEt;
    private Button sureBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexpint_child_add);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle(R.string.int_child_add);
        navBar.hideRight();
        hnoEt = (EditText) findViewById(R.id.hno_int_child_add_tv);
        pcEt = (EditText) findViewById(R.id.pc_int_child_add_tv);
        weightEt = (EditText) findViewById(R.id.weight_int_child_add_tv);
        volumeEt = (EditText) findViewById(R.id.volume_int_child_add_tv);
        goodsEt = (EditText) findViewById(R.id.goods_int_child_add_tv);
        goodsEt.setTransformationMethod(new AllCapTransformationMethod());
        goodsCNEt = (EditText) findViewById(R.id.goodscn_int_child_add_tv);
        sureBtn = (Button) findViewById(R.id.int_child_add_btn);
        sureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.int_child_add_btn:
                getEditText();
                mawb = getIntent().getStringExtra(AviationCommons.INT_GROUP_MAWB);
                xml = HttpIntHawbUpdate.addIntHawbXml(mawb, "0",hno,pc,weight,volume,goods,goodsCN);
                userBumen = PreferenceUtils.getUserBumen(this);
                userName = PreferenceUtils.getUserName(this);
                userPass = PreferenceUtils.getUserPass(this);
                loginFlag = PreferenceUtils.getLoginFlag(this);
                new AddChildAsyncTask().execute();
                break;

            default:
                break;
        }
    }

    class AddChildAsyncTask extends AsyncTask<Object, Object, String> {

        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntHawbAdd.addIntChild(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntExpChildAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpChildAddActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntExpChildAddActivity.this, "上传成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AppIntExpChildAddActivity.this, AppIntExpPrepareAWBActivity.class);
//                startActivityForResult(intent, 3);
                // 上传成功后finish掉当前的activity
                finish();
            }
            super.onPostExecute(request);
        }
    }

    private void getEditText() {
        hno = hnoEt.getText().toString();
        pc = pcEt.getText().toString();
        weight = weightEt.getText().toString();
        volume = volumeEt.getText().toString();
        goods = goodsEt.getText().toString();
        goods = goods.toUpperCase();
        goodsCN = goodsCNEt.getText().toString();
    }
}
