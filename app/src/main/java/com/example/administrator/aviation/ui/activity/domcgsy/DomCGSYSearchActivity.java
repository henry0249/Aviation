package com.example.administrator.aviation.ui.activity.domcgsy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.ChoseTimeMethod;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国内出港收运查询界面
 */

public class DomCGSYSearchActivity extends Activity implements View.OnClickListener {
    @BindView(R.id.cgsy_jhtime_et)
    EditText cgsyJhtimeEt;
    @BindView(R.id.cgsy_chose_time_im)
    ImageView cgsyChoseTimeIm;
    @BindView(R.id.cgsy_hangbanhao_et)
    EditText cgsyHangbanhaoEt;
    @BindView(R.id.cgsy_dailiren_et)
    EditText cgsyDailirenEt;
    @BindView(R.id.cgsy_yundanhao_et)
    EditText cgsyYundanhaoEt;
    @BindView(R.id.cgsy_search_btn)
    Button cgsySearchBtn;
    @BindView(R.id.cgsy_saoma_btn)
    Button cgsySaomaBtn;
    @BindView(R.id.ware_house_pb)
    ProgressBar wareHousePb;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    // 输入框值
    private String time;
    private String hangbanhao;
    private String dailiren;
    private String yundanhao;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_domcgsy_house);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("国内出港收运查询");
        navBar.hideRight();

        // 显示进度
        loadingDialog = new LoadingDialog(this);

        String currentTime = DateUtils.getTodayDateTime();
        cgsyJhtimeEt.setText(currentTime);

        // 默认光标定位在最后面
        cgsyJhtimeEt.setSelection(cgsyJhtimeEt.getText().toString().trim().length());

        // 将输入框值标记为大写
        cgsyHangbanhaoEt.setTransformationMethod(new AllCapTransformationMethod());
        cgsyDailirenEt.setTransformationMethod(new AllCapTransformationMethod());
        cgsyYundanhaoEt.setTransformationMethod(new AllCapTransformationMethod());

        // 点击事件
        cgsyChoseTimeIm.setOnClickListener(this);
        cgsySearchBtn.setOnClickListener(this);
        cgsySaomaBtn.setOnClickListener(this);
    }

    // 点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 选择事件
            case R.id.cgsy_chose_time_im:
                choseTimeMethod.getCurrentTime(DomCGSYSearchActivity.this, cgsyJhtimeEt);
                break;

            // 查询出港收运信息
            case R.id.cgsy_search_btn:
                getEditextValue();
                break;

            // 扫码查询运单
            case R.id.cgsy_saoma_btn:
                break;

            default:
                break;
        }
    }

    // 得到输入框的值
    private void getEditextValue() {
        time = cgsyJhtimeEt.getText().toString().trim();
        time = time.toUpperCase();
        hangbanhao = cgsyHangbanhaoEt.getText().toString().trim();
        hangbanhao = hangbanhao.toUpperCase();
        dailiren = cgsyDailirenEt.getText().toString().trim();
        dailiren = dailiren.toUpperCase();
        yundanhao = cgsyYundanhaoEt.getText().toString().trim();
        yundanhao = yundanhao.toUpperCase();
    }
}
