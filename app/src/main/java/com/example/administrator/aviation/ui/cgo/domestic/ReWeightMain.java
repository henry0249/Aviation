package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.WeakHandler;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReWeightMain extends AppCompatActivity {

    @BindView(R.id.qmuiTest)
    Button qq;
    private Context mContext;
    private Activity mAct;
    private QMUITipDialog tipDialog;
    private WeakHandler mHandler = new WeakHandler();
    private NavBar navBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reweight_main_activity);
        mContext = ReWeightMain.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);

        navBar = new NavBar(this);

        qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog = new QMUITipDialog.Builder(mContext)
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                        .setTipWord("操作成功")
                        .create();
                tipDialog.show();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tipDialog.dismiss();
                    }
                }, 2000);

            }
        });
    }
}
