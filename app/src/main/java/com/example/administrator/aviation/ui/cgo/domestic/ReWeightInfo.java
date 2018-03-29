package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.AbsCommonAdapter;
import com.example.administrator.aviation.model.hygnc.ULDLoadingCargo;
import com.example.administrator.aviation.ui.base.TableModel;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.WeakHandler;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.ReWeight_right_title_container;
import static com.example.administrator.aviation.R.id.right_title_container;
import static com.example.administrator.aviation.R.id.view;
import static java.security.AccessController.getContext;

public class ReWeightInfo extends AppCompatActivity {

    //region 自定义变量和未在XML中声明的控件
    // 初始化数据加载提示（即对话框）
    private LoadingDialog Ldialog;
    private TextView DaiLiRen;
    private AbsCommonAdapter<TableModel> mLeftAdapter, mRightAdapter;
    private WeakHandler mHandler = new WeakHandler();
    private Context mContext;
    private Activity mAct;

    private final String TAG = "ReWeightInfo";
    private final String page = "one";
    private int talHeight = 0;
    //用于存放标题的id,与textview引用
    private SparseArray<TextView> mTitleTvArray;
    //endregion

    //region 初始化

    //region 入口函数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_weight_info);
        mContext = ReWeightInfo.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {



        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ReWeight_right_title_container);
        layoutInflater.inflate(R.layout.reweight_table_right_title,linearLayout,true);

        findTitleTextViewIds();

    }
    //endregion

    //region 利用反射初始化标题的TextView的item引用
    private void findTitleTextViewIds() {
        mTitleTvArray = new SparseArray<>();
        for (int i = 0; i < 8; i++) {
            try {
                Field field = R.id.class.getField("ReWeight_tv_table_title_" + i);
                int key = field.getInt(new R.id());
                TextView textView = (TextView) findViewById(key);

                mTitleTvArray.put(key, textView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        for(int i = 0; i < mTitleTvArray.size(); i++) {
            int key = 0;
            key = mTitleTvArray.keyAt(i);
            TextView tx = mTitleTvArray.get(key);
            if (tx.getText().equals("ULD")) {
                DaiLiRen = tx;
                DaiLiRen.setText("hello");
            }
        };

    }
    //endregion

    //endregion

    //region 控件事件
    //endregion

    //region 功能方法
    //endregion
}
