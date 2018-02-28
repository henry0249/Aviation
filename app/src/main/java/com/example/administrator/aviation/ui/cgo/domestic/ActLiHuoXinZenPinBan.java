package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.id.button1;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_CAMERA_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDLOADING_XinZenPinBan_REQUEST;
import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;

/**
 * Created by Administrator on 2018/2/7.
 */

public class ActLiHuoXinZenPinBan extends AppCompatActivity {

    //region EditText控件
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
    //endregion

    //region Button控件
    @BindView(R.id.uldloading_Btn_QueDin)
    Button btn_QueDin;
    @BindView(R.id.uldloading_Btn_QinChu)
    Button btn_QinChu;
    //endregion

    //region 自定义控件
    private NavBar navBar;
    //endregion

    //region 自定义全局变量
    private final String TAG = "ActLiHuoXinZenPinBan";
    private Context mContext;
    private Activity mAct;
    //endregion

    //region 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gnlh_xinzenpinban);
        mContext = ActLiHuoXinZenPinBan.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("新增平板");

        EditViewSetEmpty();
        setListener();
    }

    //region 所有输入框置空
    private void EditViewSetEmpty() {
        PinBanHao_A.setText("");
        PinBanZhong_A.setText("");
        uldBianHao_A.setText("");
        uldBianZhong_A.setText("");
        uldBianHao_B.setText("");
        uldBianZhong_B.setText("");
    }
    //endregion

    //endregion

    //region 控件事件
    //region 页面上所有的点击事件
    private void setListener() {
        btn_QueDin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_QinChu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditViewSetEmpty();
            }
        });

        uldBianHao_A.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    String str = uldBianHao_A.getText().toString();
                    if (str.length() > 11) {
                        handler.sendEmptyMessage(2);
                        ToastUtils.showToast(mContext, "位数不能大于11位", Toast.LENGTH_LONG);
                    }
                }
            }
        });

        uldBianHao_B.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {
                    String str = uldBianHao_B.getText().toString();
                    if (str.length() > 11) {
                        handler.sendEmptyMessage(3);
                        ToastUtils.showToast(mContext, "位数不能大于11位", Toast.LENGTH_LONG);
                    }
                }
            }
        });
    }
    //endregion

    //endregion

    //region 功能方法
    @Override
    public void finish() {
        Integer req = (Integer) getIntent().getSerializableExtra("id");
        Intent intent = new Intent(ActLiHuoXinZenPinBan.this,expULDLoading.class);
        if (req == GNC_ULDLOADING_XinZenPinBan_REQUEST) {
            Bundle bundle = new Bundle();
            if (TextUtils.isEmpty(PinBanHao_A.getText().toString().trim())) {
                bundle.putString("result", "");
            } else {
                bundle.putString("result", PinBanHao_A.getText().toString().trim());
            }

            intent.putExtras(bundle);
            setResult(AviationCommons.GNC_ULDLOADING_XinZenPinBan_RESULT, intent);
        }
        super.finish();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View rootview = mAct.getWindow().getDecorView();
            if (rootview.findFocus() != null) {
                View FocusView = rootview.findFocus();
                FocusView.clearFocus();
                //焦点失去时的校验方法，用于更新ID
                switch (msg.what) {
                    case 1:

                        break;
                    case 2:
                        uldBianHao_A.setText("");
                        uldBianHao_A.requestFocus();
                        break;
                    case 3:
                        uldBianHao_B.setText("");
                        uldBianHao_B.requestFocus();
                        break;
                }
            }
        }
    };
    //endregion
}
