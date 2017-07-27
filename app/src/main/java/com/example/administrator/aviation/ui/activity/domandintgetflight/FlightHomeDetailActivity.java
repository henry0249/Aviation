package com.example.administrator.aviation.ui.activity.domandintgetflight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 航班动态查询首次进入界面
 */

public class FlightHomeDetailActivity extends FragmentActivity implements View.OnClickListener{
    @BindView(R.id.flight_in_btn)
    Button flightInBtn;
    @BindView(R.id.flight_out_btn)
    Button flightOutBtn;
    @BindView(R.id.framelayout)
    FrameLayout framelayout;
    @BindView(R.id.flight_search_tv)
    TextView flightSearchTv;

    // 初始化fragment
    private FlightHomeInFragment flightHomeInFragment;
    private FlightHomeOutFragment flightHomeOutFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_show);
        ButterKnife.bind(this);
        initView();
        initFragment(0);
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle("航班动态");
        flightInBtn.setOnClickListener(this);
        flightOutBtn.setOnClickListener(this);
        flightSearchTv.setOnClickListener(this);
    }

    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        hideFragment(transaction);
        switch (index) {
            case 0:
                if (flightHomeInFragment == null) {
                    flightHomeInFragment = new FlightHomeInFragment();
                    transaction.add(R.id.framelayout, flightHomeInFragment);
                } else {
                    transaction.show(flightHomeInFragment);
                }
                break;

            case 1:
                if (flightHomeOutFragment == null) {
                    flightHomeOutFragment = new FlightHomeOutFragment();
                    transaction.add(R.id.framelayout, flightHomeOutFragment);
                } else {
                    transaction.show(flightHomeOutFragment);
                }
                break;

            default:
                break;
        }
        transaction.commit();
    }

    // 隐藏fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (flightHomeOutFragment != null) {
            transaction.hide(flightHomeOutFragment);
        }
        if (flightHomeInFragment != null) {
            transaction.hide(flightHomeInFragment);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flight_in_btn:
                flightInBtn.setBackground(getResources().getDrawable(R.drawable.button_selector));
                flightInBtn.setTextColor(getResources().getColor(R.color.colorGray));
                flightOutBtn.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                flightOutBtn.setTextColor(getResources().getColor(R.color.colorTitle));
                initFragment(0);
                break;

            case R.id.flight_out_btn:
                flightInBtn.setBackground(getResources().getDrawable(R.drawable.button_noselector));
                flightInBtn.setTextColor(getResources().getColor(R.color.colorTitle));
                flightOutBtn.setBackground(getResources().getDrawable(R.drawable.button_selector));
                flightOutBtn.setTextColor(getResources().getColor(R.color.colorGray));
                initFragment(1);
                break;

            case R.id.flight_search_tv:
                Intent intent = new Intent(FlightHomeDetailActivity.this, FlightActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
