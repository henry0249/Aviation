package com.example.administrator.aviation.ui.cgo.domestic;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class expULDcargoInfo extends AppCompatActivity {
    @BindView(R.id.uldInfo_btn_ZhuangZaiXinXi)
    Button ZhuangZaiXinXi;
    @BindView(R.id.uldInfo_btn_DaiZhuangXinXi)
    Button DaiZhuangXinXi;

    private NavBar navBar;
    private ZhuangZaiFragment zhuangzaiFragment;
    private DaiZhuangFragment daizhuangFragment;

    private final String TAG = "expULDcargoInfo";
    private final String page = "one";
    private HashMap<String, String> idArrary = new HashMap<>();

    //region 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_uldcargo_info);
        ButterKnife.bind(this);
        initView();
        initFragment(0);
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("装载详情");
        navBar.setRight(R.drawable.ic_menu_two);
        setListener();

        idArrary = (HashMap<String, String>) getIntent().getSerializableExtra("Info");
    }

    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragment(transaction);
        switch (index) {
            case 0:
                if (zhuangzaiFragment == null) {
                    zhuangzaiFragment = new ZhuangZaiFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("zhuangzai",idArrary);
                    zhuangzaiFragment.setArguments(bundle);
                    transaction.add(R.id.framelayout, zhuangzaiFragment);
                } else {
                    transaction.show(zhuangzaiFragment);
                }
                break;

            case 1:
                if (daizhuangFragment == null) {
                    daizhuangFragment = new DaiZhuangFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("daizhuang",idArrary);
                    daizhuangFragment.setArguments(bundle);
                    transaction.add(R.id.framelayout, daizhuangFragment);
                } else {
                    transaction.show(daizhuangFragment);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {

        //region 标题栏右边图片的点击事件
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //endregion

        //region 装载信息按钮点击事件
        ZhuangZaiXinXi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhuangZaiXinXi.setBackground(getResources().getDrawable(R.drawable.button_selector));
                ZhuangZaiXinXi.setTextColor(getResources().getColor(R.color.colorGray));
                DaiZhuangXinXi.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                DaiZhuangXinXi.setTextColor(getResources().getColor(R.color.colorTitle));
                initFragment(0);
            }
        });
        //endregion

        //region 待装信息按钮点击事件
        DaiZhuangXinXi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaiZhuangXinXi.setBackground(getResources().getDrawable(R.drawable.button_selector));
                DaiZhuangXinXi.setTextColor(getResources().getColor(R.color.colorGray));
                ZhuangZaiXinXi.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                ZhuangZaiXinXi.setTextColor(getResources().getColor(R.color.colorTitle));
                initFragment(1);
            }
        });
        //endregion

    }
    //endregion

    //endregion

    //region 功能方法

    // region 隐藏fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (daizhuangFragment != null) {
            transaction.hide(daizhuangFragment);
        }

        if (zhuangzaiFragment != null) {
            transaction.hide(zhuangzaiFragment);
        }
    }
    //endregion

    //endregion
}
