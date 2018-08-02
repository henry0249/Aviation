package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.sys.PublicFun;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.util.AviationCommons.GNC_ULDinfo_CAMERA_REQUEST;

public class gnShouYunSheZhi extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    //endregion

    //region 未预设XML控件

    //endregion

    //region 其他控件

    //endregion

    //region Layout控件

    //endregion

    //region Button控件
    @BindView(R.id.btn_gnShouYun_TDH)
    Button btnTongDaoHao;
    //endregion

    //region EditText控件

    //endregion

    //region 滚动View控件

    //endregion

    //region TextView控件

    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gn_shou_yun_she_zhi_activity);
        mContext = gnShouYunSheZhi.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        navBar.setTitle("页面设置");
        setListener();
    }
    //endregion

    //endregion

    //endregion

    //region 控件事件

    //region 界面上所有控件的点击事件
    private void setListener() {

        //region 通道号设置按钮
        btnTongDaoHao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(mAct);
                String tdh;

                if (TextUtils.isEmpty(PreferenceUtils.getTongDaoHao(mContext))) {
                    tdh = "请输入通道号";
                } else {
                    tdh = "当前通道号: " + PreferenceUtils.getTongDaoHao(mContext);
                }
                builder.setTitle("通道号")
                        .setPlaceholder(tdh)
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                CharSequence text = builder.getEditText().getText();
                                if (text != null && text.length() > 0) {
                                    if (PublicFun.isNumeric(text.toString().trim())) {
                                        PreferenceUtils.saveTDH(mContext, text.toString().trim());
                                        ToastUtils.showToast(mContext, "已保存", Toast.LENGTH_SHORT);
                                    }
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(mAct, "请输入通道号", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .show();
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    //endregion
}
