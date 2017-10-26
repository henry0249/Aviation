package com.example.administrator.aviation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.aviation.http.login.UserLogin;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.ui.activity.SettingPasswordActivity;
import com.example.administrator.aviation.util.PreferenceUtils;

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText userNameEt;
    private EditText userPassEt;
    private EditText userBumenEt;
    private Button loginBtn;
    private CheckBox savePassCheckBox;
    private CheckBox autoLoginCheckBox;

    // 数据存储和用户信息
    private SharedPreferences sp;
    private String userName;
    private String userPass;
    private String userBumen;

    // 判断是否第一次输入
    private boolean isFirst;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        // 得到自动登录判断标识
        isFirst = sp.getBoolean("ISFIRST", true);

        // 得到锁屏密码
        String passwordStr = PreferenceUtils.getLockPass(LoginActivity.this);
        //判断是否已经设置过密码
        if (!TextUtils.isEmpty(passwordStr)) {
            if (sp.getBoolean("AUTO_ISCHECK",false) && !isFirst) {
                Intent intent = new Intent(this,SettingPasswordActivity.class);
                intent.putExtra(SettingPasswordActivity.ARG_TYPE, SettingPasswordActivity.TYPE_CHECK);
                startActivity(intent);
                // 在用户首页两次退出，不回到这个登录界面
                finish();

            }else {
                Toast.makeText(LoginActivity.this, "请确认记住密码和自动登录已经勾选", Toast.LENGTH_LONG).show();
            }
        }

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
        // 获得实例对象
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userBumenEt = (EditText) findViewById(R.id.user_bumen_et);
        userNameEt = (EditText) findViewById(R.id.userName_et);
        userPassEt = (EditText) findViewById(R.id.userPass_et);

        // 测试（后期删除此方法跳转到新界面即即将开发的界面界面写好了，逻辑没写）
//        TextView company_tv = (TextView) findViewById(R.id.company_tv);
//        company_tv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, DomCGSYSearchActivity.class);
//                startActivity(intent);
//            }
//        });

        // 设置输入字段变成大写
        userBumenEt.setTransformationMethod(new AllCapTransformationMethod());
        userNameEt.setTransformationMethod(new AllCapTransformationMethod());

        loginBtn = (Button) findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(this);

        savePassCheckBox = (CheckBox) findViewById(R.id.savePass_checkbox);
        autoLoginCheckBox = (CheckBox) findViewById(R.id.autoLogin_checkbox);
        autoLoginCheckBox.setOnClickListener(this);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // 监听记住密码多选框按钮事件
        savePassCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (savePassCheckBox.isChecked()) {
                    sp.edit().putBoolean("ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("ISCHECK", false).commit();
                }
            }
        });

        // 自动登录按钮的监听事件
        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (autoLoginCheckBox.isChecked()) {
                    sp.edit().putBoolean("AUTO_ISCHECK", true).commit();
                } else {
                    sp.edit().putBoolean("AUTO_ISCHECK", false).commit();
                }
            }
        });

        // 判断记住密码多选框状态
        if (sp.getBoolean("ISCHECK", false)) {
            // 设置默认是记住密码状态
            savePassCheckBox.setChecked(true);

            // 从share中取出用户名和密码
            userBumen = PreferenceUtils.getUserBumen(LoginActivity.this);
            userName = PreferenceUtils.getUserName(LoginActivity.this);
            userPass = PreferenceUtils.getUserPass(LoginActivity.this);
            userBumenEt.setText(userBumen);
            userNameEt.setText(userName);

            // 记住密码后光标位于最后
            userBumenEt.setSelection(userBumenEt.getText().toString().trim().length());

            // 修改密码成功后输入框内容置空
            String a = getIntent().getStringExtra("change");
            if (a != null && a.equals("change") && !a.equals("")) {
                userPassEt.setText("");
            } else {
                userPassEt.setText(userPass);
            }

            // 判断自动登录多选框状态
            if (sp.getBoolean("AUTO_ISCHECK", false)) {
                // 设置默认是自动登录状态
                autoLoginCheckBox.setChecked(true);
                savePassCheckBox.setChecked(true);
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 登录事件
            case R.id.login_btn:
                login();
                new UserLogin(userBumen, userName, userPass, this).execute();
                break;

            default:
                break;
        }

    }

    public void dismissProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    public void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }


    public void login() {

        if (PreferenceUtils.getIsFirst(LoginActivity.this)){
            userBumen = PreferenceUtils.getUserBumen(LoginActivity.this);
            userName = PreferenceUtils.getUserName(LoginActivity.this);
            userPass = PreferenceUtils.getUserPass(LoginActivity.this);
        } else {
            // 将得到的string值转换成大写.toUpperCase()方法
            userBumen = userBumenEt.getText().toString();
            userBumen = userBumen.toUpperCase();
            userName = userNameEt.getText().toString();
            userName = userName.toUpperCase();
            userPass = userPassEt.getText().toString();
        }

    }

}
