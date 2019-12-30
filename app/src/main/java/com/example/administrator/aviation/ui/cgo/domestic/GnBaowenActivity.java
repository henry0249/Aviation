package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class GnBaowenActivity extends AppCompatActivity {

    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    //endregion

    //region 未预设XML控件
    private QMUITabSegment mTabSegment;
    private QMUIViewPager mContentViewPager;
    //endregion

    //region 其他控件

    //endregion

    //region Layout控件

    //endregion

    //region Button控件

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
        setContentView(R.layout.activity_gn_baowen);
        mContext = GnBaowenActivity.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        navBar.setTitle("报文发送");

        mContentViewPager = (QMUIViewPager)findViewById(R.id.gnBaoWenInfoViewPager);
        mTabSegment = (QMUITabSegment)findViewById(R.id.gnBaoWenInfo_Tab);

        initTabAndPager();
    }
    //endregion

    //region Pager初始化
    private void initTabAndPager() {
        List<Fragment> fragments= new ArrayList<>();
        gnBaoWenFaSong mFirstFragment = new gnBaoWenFaSong();
        gnBaoWenChaXun mThreeFragment = new gnBaoWenChaXun();

        fragments.add(mFirstFragment);
        fragments.add(mThreeFragment);
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);



        mContentViewPager.setAdapter(adapter);
        for (int i = 0; i < fragments.size(); i++) {
            switch (i) {
                case 0:
                    mTabSegment.addTab(new QMUITabSegment.Tab("报文发送"));
                    break;
                case 1:
                    mTabSegment.addTab(new QMUITabSegment.Tab("快速查询"));
                    break;
            }

        }
        int space = QMUIDisplayHelper.dp2px(mContext, 16);
        mTabSegment.setHasIndicator(true);
        mTabSegment.setMode(QMUITabSegment.MODE_FIXED);  //MODE_SCROLLABLE 自适应宽度+滚动   MODE_FIXED  均分
        mTabSegment.setItemSpaceInScrollMode(space);
        mTabSegment.setupWithViewPager(mContentViewPager, false,true);
        mTabSegment.setPadding(space, 0, space, 0);

        mContentViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int index = position;
                ToastUtils.showToast(GnBaowenActivity.this, "您选择了第" + (index + 1) + "个页卡", Toast.LENGTH_SHORT);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mDataList;

        public BaseFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> dataList) {
            super(fm);
            mDataList = dataList;
        }

        @Override
        public Fragment getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }
    }
    //endregion

    //region 输入框置空

    //endregion

    //region 类的Intent

    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件

    //endregion

    //endregion

    //region 功能方法

    //region 打开编辑区

    //endregion

    //region 关闭编辑区

    //endregion

    //region 句柄监听

    //endregion

    //region 请求数据

    //endregion

    //region 文本框赋值

    //endregion

    //endregion
}
