package com.example.administrator.aviation.ui.cgo.domestic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/2/7.
 */

public class ActLiHuoXinZenPinBan extends AppCompatActivity {
    @BindView(R.id.uldloading_edtTxt_PinBanHao_A)
    EditText PinBanHao_A;
    @BindView(R.id.uldloading_edtTxt_PinBanZhong_A)
    EditText PinBanZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_A)
    EditText uldBianHao_A;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_A)
    EditText uldBianZhong_A;
    @BindView(R.id.uldloading_edtTxt_uldBianHao_B)
    EditText uldBianHao_B;
    @BindView(R.id.uldloading_edtTxt_uldBianZhong_B)
    EditText uldBianZhong_B;
    @BindView(R.id.uldloading_Btn_QueDin)
    Button btn_QueDin;
    @BindView(R.id.uldloading_Btn_QinChu)
    Button btn_QinChu;

    private NavBar navBar;
    private final String TAG = "ActLiHuoXinZenPinBan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gnlh_xinzenpinban);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("新增平板");
        EditViewSetEmpty();
        setListener();
    }

    private void EditViewSetEmpty() {
        PinBanHao_A.setText("");
        PinBanZhong_A.setText("");
        uldBianHao_A.setText("");
        uldBianZhong_A.setText("");
        uldBianHao_B.setText("");
        uldBianZhong_B.setText("");
    }

    private void setListener() {

        btn_QueDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_QinChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditViewSetEmpty();
            }
        });
    }
}
