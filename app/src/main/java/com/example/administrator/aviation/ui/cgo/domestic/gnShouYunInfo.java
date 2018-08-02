package com.example.administrator.aviation.ui.cgo.domestic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.ToastUtils;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.view;
import static java.security.AccessController.getContext;

public class gnShouYunInfo extends AppCompatActivity {
    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;

    private ArrayList<View> aList;
    //endregion

    //region 未预设XML控件
    private QMUITabSegment mTabSegment;
    private QMUIViewPager mContentViewPager;
    private PopupWindow pw;
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
        setContentView(R.layout.gn_shou_yun_info_activity);
        mContext = gnShouYunInfo.this;
        mAct = (Activity) mContext;
        ButterKnife.bind(this);
        init();
        setListener();

    }
    //endregion

    //region 设置初始化
    public void init() {
        navBar = new NavBar(this);
        navBar.setTitle("当前板号 " + "111");
        navBar.setRight(R.drawable.ic_menu_two);

        mContentViewPager = (QMUIViewPager)findViewById(R.id.contentViewPager);
        mTabSegment = (QMUITabSegment)findViewById(R.id.gnShouYunInfo_Tab);


        initTabAndPager();
    }
    //endregion

    //region Pager初始化
    private void initTabAndPager() {
        List<Fragment> fragments= new ArrayList<>();
        DQrukuFragment mFirstFragment = new DQrukuFragment();
        RuKuInfoFragment mThreeFragment = new RuKuInfoFragment();

        fragments.add(mFirstFragment);
        fragments.add(mThreeFragment);
        BaseFragmentPagerAdapter adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);



        mContentViewPager.setAdapter(adapter);
        for (int i = 0; i < fragments.size(); i++) {
            switch (i) {
                case 0:
                    mTabSegment.addTab(new QMUITabSegment.Tab("当前入库"));
                    break;
                case 1:
                    mTabSegment.addTab(new QMUITabSegment.Tab("入库信息"));
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
                ToastUtils.showToast(gnShouYunInfo.this, "您选择了第" + (index + 1) + "个页卡", Toast.LENGTH_SHORT);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }
    //endregion

    //region 输入框置空

    //endregion

    //endregion

    //region 控件事件

    //region 页面上所有的点击事件
    private void setListener() {
        //region 标题栏右侧图片点击按钮
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View myView = LayoutInflater.from(gnShouYunInfo.this).inflate(R.layout.pop_expuld_info, null);
                pw = new PopupWindow(myView, 400, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                pw.showAsDropDown(navBar.getPopMenuView());

                List list = new ArrayList<String>();
                list.add(0,"平板设置");

                uldAdapter ul = new uldAdapter(gnShouYunInfo.this, R.layout.pop_expuld_list_item, list);
                ListView lv = (ListView) myView.findViewById(R.id.list_pop_expUld);
                lv.setAdapter(ul);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        pw.dismiss();
                        if (position == 0) {
                            Intent intent = new Intent(gnShouYunInfo.this, gnShouYunSheZhi.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        //endregion
    }
    //endregion

    //endregion

    //region 功能方法

    // region 自定义弹出菜单适配器
    private class uldAdapter extends ArrayAdapter<String> {
        private int resourceID;

        public uldAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<String> objects) {
            super(context, resource, objects);
            this.resourceID = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            String a = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            TextView tx = (TextView) view.findViewById(R.id.pop_listitem);
            tx.setText(a);
            return view;
        }
    }
    //endregion

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
