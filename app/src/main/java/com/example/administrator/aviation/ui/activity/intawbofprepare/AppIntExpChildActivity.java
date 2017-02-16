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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntHawbDelete;
import com.example.administrator.aviation.http.getIntawbofprepare.HttpIntHawbUpdate;
import com.example.administrator.aviation.model.intawbprepare.Hawb;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

/**
 * 国际出港预录入子界面
 */

public class AppIntExpChildActivity extends Activity implements View.OnClickListener{
    private Hawb hawb;
    private EditText hnoEt;
    private EditText pcEt;
    private EditText weightEt;
    private EditText volumeEt;
    private EditText goodsEt;
    private EditText goodsCNEt;
    private EditText mftstatusEt;
    private Button updataBtn;
    private Button sureBtn;
    private Button deleteBtn;

    private String mawb;
    private String mawbId;
    private String hawbId;
    private String hno;
    private String pc;
    private String weight;
    private String volume;
    private String goods;
    private String goodscn;
    private String mftstatus;

    private String xml;
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String ErrString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appexpint_child);
        initView();
        setChildEdiText();
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
        navBar.setTitle(R.string.int_prepare_child_title);
        navBar.hideRight();
        hnoEt = (EditText) findViewById(R.id.hno_int_child_tv);
        pcEt = (EditText) findViewById(R.id.pc_int_child_tv);
        weightEt = (EditText) findViewById(R.id.weight_int_child_tv);
        volumeEt = (EditText) findViewById(R.id.volume_int_child_tv);
        goodsEt = (EditText) findViewById(R.id.goods_int_child_tv);
        goodsCNEt = (EditText) findViewById(R.id.goodscn_int_child_tv);
        mftstatusEt = (EditText) findViewById(R.id.mftstatus_int_child_tv);
        updataBtn = (Button) findViewById(R.id.int_update_child_btn);
        sureBtn = (Button) findViewById(R.id.int_sure_child_btn);
        deleteBtn = (Button) findViewById(R.id.int_delete_child_btn);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        // 点击更新分单信息的点击事件
        updataBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        hawb = (Hawb) getIntent().getSerializableExtra(AviationCommons.INT_CHILD_INFO);
        mawb = getIntent().getStringExtra(AviationCommons.INT_GROUP_MAWB);

        mawbId = getIntent().getStringExtra("mawbId");
        hawbId = hawb.getHawbID();

        setEditTextInvisible();
    }
    // 给EditText赋值
    private void setChildEdiText() {
        hno = hawb.getHno();
        hnoEt.setText(hno);
        pc = hawb.getPC();
        pcEt.setText(pc);
        weight = hawb.getWeight();
        weightEt.setText(weight);
        volume = hawb.getVolume();
        volumeEt.setText(volume);
        goods = hawb.getGoods();
        goodsEt.setText(goods);
        goodscn = hawb.getGoodsCN();
        goodsCNEt.setText(goodscn);
        mftstatus = hawb.getMftStatus();
        mftstatusEt.setText(mftstatus);
    }

    // 让editText不可编辑
    private void setEditTextInvisible() {
        hnoEt.setEnabled(false);
        pcEt.setEnabled(false);
        weightEt.setEnabled(false);
        volumeEt.setEnabled(false);
        goodsEt.setEnabled(false);
        goodsCNEt.setEnabled(false);
        mftstatusEt.setEnabled(false);
    }
    // 让editText可编辑
    private void setEditTextVisible() {
        hnoEt.setEnabled(true);
        pcEt.setEnabled(true);
        weightEt.setEnabled(true);
        volumeEt.setEnabled(true);
        goodsEt.setEnabled(true);
        goodsCNEt.setEnabled(true);
        mftstatusEt.setEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.int_update_child_btn:
                setEditTextVisible();
                updataBtn.setVisibility(View.GONE);
                sureBtn.setVisibility(View.VISIBLE);
                break;

            // 更新分单
            case R.id.int_sure_child_btn:
                getEditext();
                xml = HttpIntHawbUpdate.getIntHawbXml(mawbId, mawb,hawbId,hno,pc,weight,volume,goods,goodscn);

                new UpdateChildAsyncTask().execute();
                break;

            // 删除分单
            case R.id.int_delete_child_btn:
                AlertDialog.Builder builder = new AlertDialog.Builder(AppIntExpChildActivity.this);
                builder.setTitle("删除订单")
                        .setMessage("确定删除订单号为:"+hno+"订单吗?")
                        .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new DeleteChildAsyncTask().execute();
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

    // 更新分单列表
    class UpdateChildAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntHawbUpdate.updateIntHawb(userBumen, userName,userPass,loginFlag, xml);
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
                Toast.makeText(AppIntExpChildActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpChildActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntExpChildActivity.this, "修改成功", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(AppIntExpChildActivity.this, AppIntExpPrepareAWBActivity.class);
//                startActivityForResult(intent, 3);
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 删除分单列表
    class DeleteChildAsyncTask extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpIntHawbDelete.deleteIntHawb(userBumen, userName,userPass,loginFlag,mawb, hno);
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
                Toast.makeText(AppIntExpChildActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntExpChildActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("true")){
                Toast.makeText(AppIntExpChildActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntExpChildActivity.this, AppIntExpPrepareAWBActivity.class);
                startActivityForResult(intent, 4);
                finish();
            }
            super.onPostExecute(request);
        }
    }

    // 获取输入框的值
    private void getEditext() {
        hno = hnoEt.getText().toString();
        pc = pcEt.getText().toString();
        weight = weightEt.getText().toString();
        volume = volumeEt.getText().toString();
        goods = goodsEt.getText().toString();
        goodscn = goodsCNEt.getText().toString();
    }
}
