package com.example.administrator.aviation.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.administrator.aviation.LoginActivity;
import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 修改密码界面
 */

public class ChangePassActivity extends AppCompatActivity implements View.OnClickListener{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    private String oldPass;
    private String newPass;
    private String newPassTwo;

    private boolean  isPassOpened;

    private EditText oldPassEt;
    private EditText newPassEt;
    private EditText newPassTwoEt;
    private Button changePassBtn;
    private ImageView oldPassIv;
    private ImageView newPassIv;
    private ImageView newPassTwoIv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle(R.string.change_pass);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        // 初始化控件
        oldPassEt = (EditText) findViewById(R.id.old_pass_et);
        newPassEt = (EditText) findViewById(R.id.new_pass_et);
        newPassTwoEt = (EditText) findViewById(R.id.new_pass_two_et);

        oldPassIv = (ImageView) findViewById(R.id.old_pass_iv);
        oldPassIv.setOnClickListener(this);
        newPassIv = (ImageView) findViewById(R.id.new_pass_iv);
        newPassIv.setOnClickListener(this);
        newPassTwoIv = (ImageView) findViewById(R.id.new_pass_two_iv);
        newPassTwoIv.setOnClickListener(this);

        changePassBtn = (Button) findViewById(R.id.change_pass_btn);
        changePassBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_pass_btn:
                // 获取输入值
                getEditText();
                if (!userPass.equals(oldPass)) {
                    Toast.makeText(this, "原始密码不正确！", Toast.LENGTH_SHORT).show();
                } else if (!newPass.equals(newPassTwo)) {
                    Toast.makeText(this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                }else {
                    Map<String, String> params = new HashMap<>();
                    params.put("newPassWord", newPass);
                    HttpRoot.getInstance().requstAync(this, HttpCommons.CHANGE_PASS_NAME, HttpCommons.CHANGE_PASS_ACTION, params,
                            new HttpRoot.CallBack() {
                                @Override
                                public void onSucess(Object result) {
                                    Toast.makeText(ChangePassActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ChangePassActivity.this, LoginActivity.class);
                                    intent.putExtra("change","change");
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailed(String message) {

                                }

                                @Override
                                public void onError() {

                                }
                            });
                }
                break;

            case R.id.old_pass_iv:
                if (!isPassOpened) { //显示
                    oldPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    oldPassIv.setImageResource(R.drawable.see);
                } else { //隐藏
                    oldPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    oldPassIv.setImageResource(R.drawable.unsee);
                }
                oldPassEt.setSelection(oldPassEt.getText().toString().length());//定位光标
                isPassOpened = !isPassOpened;
                break;

            case R.id.new_pass_iv:
                if (!isPassOpened) { //显示
                    newPassEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassIv.setImageResource(R.drawable.see);
                } else { //隐藏
                    newPassEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassIv.setImageResource(R.drawable.unsee);
                }
                newPassEt.setSelection(newPassEt.getText().toString().length());//定位光标
                isPassOpened = !isPassOpened;
                break;

            case R.id.new_pass_two_iv:
                if (!isPassOpened) { //显示
                    newPassTwoEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPassTwoIv.setImageResource(R.drawable.see);
                } else { //隐藏
                    newPassTwoEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPassTwoIv.setImageResource(R.drawable.unsee);
                }
                newPassTwoEt.setSelection(newPassTwoEt.getText().toString().length());//定位光标
                isPassOpened = !isPassOpened;
                break;

            default:
                break;
        }
    }

    private void getEditText() {
        oldPass = oldPassEt.getText().toString().trim();
//        oldPass = oldPass.toUpperCase();
        newPass = newPassEt.getText().toString().trim();
//        newPass = newPass.toUpperCase();
        newPassTwo = newPassTwoEt.getText().toString().trim();
//        newPassTwo = newPassTwo.toUpperCase();
    }
}
