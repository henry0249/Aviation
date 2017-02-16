package com.example.administrator.aviation.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.adapter.HomePageAdapter;
import com.example.administrator.aviation.model.homemessge.HomeMessage;
import com.example.administrator.aviation.model.homemessge.PrefereceHomeMessage;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.activity.intawbofprepare.AppIntExpPrepareAWBActivity;
import com.example.administrator.aviation.ui.activity.house.AppDomExpWareHouseActivity;
import com.example.administrator.aviation.ui.activity.intexpawbmanage.AppIntExpAWBManageActivity;
import com.example.administrator.aviation.ui.activity.prepareawb.AppDomExpPrePareAWBActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页fragment
 */
public class HomePageFragment extends Fragment{
    // 用户权限
    private String userPermission = "";

    // 锁屏密码
    private String lockPass = "";

    private GridView grid_home;
    private BaseAdapter mAdapter = null;

    private String xml = "";

    private View view;
    private List<HomeMessage> list;

    // 时间比较
    private String time;
    private String splitBeginTime;
    private  Date beginTime;
    private  Date endTime;
    private String todayTime;// 获得当前时间
    private boolean result;// 时间返回结果（true表示在使用周期，false表示不在使用周期）

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.homepage_fragment, container, false);
        initView();
        return view;
    }
    private void initView() {
        NavBar navBar = new NavBar(getActivity());
        navBar.setTitle("首页");
        navBar.hideRight();
        navBar.hideLeft();

        // 初始化gridView
        grid_home = (GridView) view.findViewById(R.id.home_gridView);

        // 得到锁屏密码
        lockPass = PreferenceUtils.getLockPass(getActivity());

        // 得到登录传递过来的xml数据
        xml = getActivity().getIntent().getStringExtra(AviationCommons.LOGIN_XML);
        list = PrefereceHomeMessage.pullXml(xml);

        // 判断是否设置过锁屏密码
//        if (!TextUtils.isEmpty(lockPass)) {
//            // 解锁用户名
//            String userName = PreferenceUtils.getUserName(getActivity());
//            userPermission = getActivity().getIntent().getStringExtra("userPermission");
//        }

        mAdapter = new HomePageAdapter<HomeMessage>((ArrayList<HomeMessage>) list, R.layout.homepage_item) {

            @Override
            public void bindView(ViewHolder holder, HomeMessage obj) {
                if (obj.getName().equals(AviationCommons.APP_DOM_EXP_PREPARE_AWB)) {
                    holder.setImageResource(R.id.image_iv,  R.drawable.awb);
                }else if (obj.getName().equals(AviationCommons.APP_DOM_EXP_WARE_HOUSE)){
                    holder.setImageResource(R.id.image_iv,  R.drawable.house);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXP_AWB_MANAGE)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.awb);
                } else if (obj.getName().equals(AviationCommons.APP_INT_EXP_PREPARE_AWB)) {
                    holder.setImageResource(R.id.image_iv, R.drawable.awb);
                }
                Log.d("guoji", obj.getName());
                holder.setText(R.id.image_tv, obj.getNameCN());
            }
        };
        grid_home.setAdapter(mAdapter);

        grid_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                time = list.get(position).getActiveDate();
                splitBeginTime = time.split("T")[0];
                todayTime = DateUtils.getTodayDateTime();
                beginTime = DateUtils.convertFromStrYMD(splitBeginTime);
                endTime = DateUtils.convertFromStrYMD(todayTime);

                result = DateUtils.compareDate(beginTime, endTime);
                if (result && list.get(position).getName().equals(AviationCommons.APP_DOM_EXP_PREPARE_AWB)) {
                    Intent intentAWB = new Intent(getActivity(), AppDomExpPrePareAWBActivity.class);
                    startActivity(intentAWB);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_DOM_EXP_WARE_HOUSE)) {
                    Intent intentHouse = new Intent(getActivity(), AppDomExpWareHouseActivity.class);
                    startActivity(intentHouse);
                }else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXP_AWB_MANAGE)) {
                    Intent intentIntManage = new Intent(getActivity(), AppIntExpAWBManageActivity.class);
                    startActivity(intentIntManage);
                } else if (result && list.get(position).getName().equals(AviationCommons.APP_INT_EXP_PREPARE_AWB)) {
                    Intent intentIntAwb = new Intent(getActivity(), AppIntExpPrepareAWBActivity.class);
                    startActivity(intentIntAwb);
                } else {
                    Toast.makeText(getActivity(), "过了使用期", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}