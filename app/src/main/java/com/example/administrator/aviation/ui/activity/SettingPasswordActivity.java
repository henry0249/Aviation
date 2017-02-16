package com.example.administrator.aviation.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.LoginActivity;
import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.login.UserLogin;
import com.example.administrator.aviation.model.view.LockPatternView;
import com.example.administrator.aviation.util.PreferenceUtils;

public class SettingPasswordActivity extends Activity implements LockPatternView.OnPatterChangeListner, View.OnClickListener {

    public static final String TYPE_SETTING = "setting";
    public static final String TYPE_CHECK = "check";
    private TextView mainLock;
    private LockPatternView lockPatternView;
    private LinearLayout btnLinearLayout;
    private LinearLayout goLoginLayout;
    public static final String ARG_TYPE = "type";
    public static final String PERMISSION = "permission";
    private String passString;
    private String permission;
    private String type = "";

    // 两次密码
    private String firstPass="";

    // 确定和重置密码Button
    private Button passCommitBtn;
    private Button passForget;

    // 返回到登录界面的Button
    private Button goLoginBtn;

    //是否是第一次输入密码
    private boolean isFirst = true;

    // 记录输错数
    private int erroCount = 0;

    private String userBumen;
    private String userName;
    private String userPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);
        initView();
        initData();
    }

    private void initData() {
        if (getIntent() != null && getIntent().hasExtra(ARG_TYPE)) {
            type = getIntent().getStringExtra(ARG_TYPE);
        }
        if (getIntent() != null && getIntent().hasExtra(PERMISSION)) {
            permission = getIntent().getStringExtra(PERMISSION);
        }

    }

    private void initView() {
        mainLock = (TextView) findViewById(R.id.fragment_password_lock_hint);
        lockPatternView = (LockPatternView) findViewById(R.id.fragment_password_lock);
        btnLinearLayout = (LinearLayout) findViewById(R.id.fragment_password_btn_layout);
        goLoginLayout = (LinearLayout) findViewById(R.id.go_login_layout);
        // 设置密码
        if (getIntent() != null) {
            if (TYPE_SETTING.equals(getIntent().getStringExtra(ARG_TYPE))) {
                btnLinearLayout.setVisibility(View.VISIBLE);
            }
        }

        // 确定和重置密码的点击事件
        passCommitBtn = (Button) findViewById(R.id.fragment_password_btn_commit);
        passForget = (Button) findViewById(R.id.fragment_password_btn_forget);
        goLoginBtn = (Button) findViewById(R.id.go_login_btn);
        passForget.setVisibility(View.GONE);
        passCommitBtn.setVisibility(View.GONE);
        lockPatternView.setPatterChangeListner(this);
        passCommitBtn.setOnClickListener(this);

        // 返回到登录界面
        goLoginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //确定
            case R.id.fragment_password_btn_commit:
                // 设置密码
                PreferenceUtils.saveLockPass(SettingPasswordActivity.this, passString);
                finish();
                break;

            //重置密码
            case R.id.fragment_password_btn_forget:
                // 设置锁屏密码
                PreferenceUtils.saveLockPass(SettingPasswordActivity.this, passString);

                findViewById(R.id.fragment_password_btn_layout).setVisibility(View.VISIBLE);
                Toast.makeText(SettingPasswordActivity.this, "密码清除，请输出新密码", Toast.LENGTH_LONG).show();
                mainLock.setText("请输入新密码");
                break;

            // 跳到登录界面(并清除share数据)
            case R.id.go_login_btn:
                Intent intent = new Intent(SettingPasswordActivity.this, LoginActivity.class);
                PreferenceUtils.clearShare(SettingPasswordActivity.this);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPatterChange(String passString) {
        this.passString = passString;
        erroCount++;
        if (!TextUtils.isEmpty(passString)) {
            // 设置两次密码（第一次输入）
            if (isFirst) {
                firstPass = passString;
                isFirst = false;
                mainLock.setText("请再次输入密码");
                lockPatternView.resets();
            }else {
                if (firstPass.equals(passString)) {
                    passCommitBtn.setVisibility(View.VISIBLE);
                } else {
                    mainLock.setText("两次输入密码不一致");
                    lockPatternView.errorPoint();
                    passCommitBtn.setVisibility(View.GONE);
                }
            }

            // 检查密码
            if (getIntent() != null) {
                String lockPass = PreferenceUtils.getLockPass(SettingPasswordActivity.this);
                if (TYPE_CHECK.equals(getIntent().getStringExtra(ARG_TYPE))) {

                    if (passString.equals(lockPass)) {
                        // 输入密码检测成功 不显示密码
                        mainLock.setVisibility(View.INVISIBLE);
                        userBumen = PreferenceUtils.getUserBumen(this);
                        userName = PreferenceUtils.getUserName(this);
                        userPass = PreferenceUtils.getUserPass(this);
                        new UserLogin(userBumen,userName,userPass,this).execute();
                        // 检查失败
                    } else {
                        mainLock.setText("密码错误");
                        lockPatternView.errorPoint();

                        // 判断用户输出错误超过3次就
                        if (erroCount >= 4) {
                            goLoginLayout.setVisibility(View.VISIBLE);
                            mainLock.setText("输错超过3次");
                        }
                    }
                }
            }
        } else {
            mainLock.setText("请设置至少5位密码");
        }
    }

    @Override
    public void onPatterStart(boolean isStart) {
        if (isStart) {
            mainLock.setText("请绘制图案");
        }
    }
}
