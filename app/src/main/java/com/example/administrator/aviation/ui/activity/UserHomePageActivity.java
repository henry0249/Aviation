package com.example.administrator.aviation.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;

import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.fragment.HomePageFragment;
import com.example.administrator.aviation.ui.fragment.PersonFragment;

/**
 * 所有用户首页
 * 不同的用户跳到该页面是显示是自己可选界面
 */
public class UserHomePageActivity extends FragmentActivity implements View.OnClickListener {
    // 标题
    private NavBar navBar;

    // 底部的几个layout（例如：首页）
    private LinearLayout homePageLayout;
    private LinearLayout personLayout;

    // 底部的textView和imageView
    private TextView homePageTv;
    private TextView personTv;
    private ImageView homePageIv;
    private ImageView personIv;

    // 4个Fragment
    private Fragment homePageFragment;
    private Fragment personFragment;

    // 定义一个标识来判断是否退出
    private static boolean isExit = false;
    Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        initView();

        initFragment(0);
    }

    // 按两次退出程序
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 主要代码实现
    private void initView() {
        // 设置标题
        NavBar navBar = new NavBar(this);
        navBar.setTitle("首页");
        navBar.hideLeft();
        navBar.hideRight();

        // 初始化textView和imageView
        homePageTv = (TextView) findViewById(R.id.home_page_tv);
        homePageIv = (ImageView) findViewById(R.id.home_page_iv);
        personTv = (TextView) findViewById(R.id.person_tv);
        personIv = (ImageView) findViewById(R.id.person_iv);

        // 初始化linearLayout
        homePageLayout = (LinearLayout) findViewById(R.id.home_page_layout);
        personLayout = (LinearLayout) findViewById(R.id.person_layout);
        homePageLayout.setOnClickListener(this);
        personLayout.setOnClickListener(this);

        // 首次进入
        homePageTv.setTextColor(0xff1d2089);
        homePageIv.setImageResource(R.drawable.zhuy1e);

    }

    // 退出方法
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_LONG).show();

            // 利用handler延迟发送更改状态信息
            myHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

    // 所有点击事件
    @Override
    public void onClick(View view) {
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBottom();
        switch (view.getId()) {
            case R.id.home_page_layout:
                navBar = new NavBar(this);
                navBar.setTitle("首页");
                navBar.hideRight();
                navBar.hideLeft();

                // 设置点击效果
                homePageTv.setTextColor(0xff1d2089);
                homePageIv.setImageResource(R.drawable.zhuy1e);

                initFragment(0);
                break;

            case R.id.person_layout:
                navBar = new NavBar(this);
                navBar.setTitle("我的");
                navBar.hideRight();
                // 暂时隐藏掉（navbar目前是关闭当前activity）
                navBar.hideLeft();

                // 设置点击效果
                personTv.setTextColor(0xff1d2089);
                personIv.setImageResource(R.drawable.wode1);

                initFragment(3);
                break;

            default:
                break;
        }
    }

    private void initFragment(int index) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // 开启事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 隐藏所有fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.content_frame, homePageFragment);
                } else {
                    transaction.show(homePageFragment);
                }
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                if (personFragment == null) {
                    personFragment = new PersonFragment();
                    transaction.add(R.id.content_frame, personFragment);
                } else {
                    transaction.show(personFragment);
                }
                break;
            default:
                break;
        }
        // 提交事物
        transaction.commit();

    }

    // 隐藏fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }

        if(personFragment != null) {
            transaction.hide(personFragment);
        }
    }

    // 重置底部
    private void restartBottom() {
        homePageIv.setImageResource(R.drawable.zhuye2);
        homePageTv.setTextColor(0xff666666);

        personIv.setImageResource(R.drawable.wode2);
        personTv.setTextColor(0xff666666);
    }
}
