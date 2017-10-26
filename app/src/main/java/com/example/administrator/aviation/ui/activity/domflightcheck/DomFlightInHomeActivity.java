package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.domjcgrbb.FlightCheckInfo;
import com.example.administrator.aviation.model.domjcgrbb.PrepareceFlightCheckInfo;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国内承运人订舱计划首次进入显示页面
 */

public class DomFlightInHomeActivity extends Activity {
    @BindView(R.id.flight_in_home_lv)
    ListView flightInHomeLv;
    @BindView(R.id.flight_in_home_refresh)
    PullToRefreshView flightInHomeRefresh;

    private List<FlightCheckInfo> flightCheckInfoList;
    private MyFlightInHomeAdapter myFlightCheckAdapter;
    private LoadingDialog loadingDialog;

    private String xml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdomflightinhome);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("待确认航班计划列表");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DomFlightInHomeActivity.this, DomFlightCheckInSearchActivity.class);
                startActivity(intent);
            }
        });

        // 关闭上拉加载
        flightInHomeRefresh.disableScroolUp();

        loadingDialog = new LoadingDialog(this);

        // 首次进入加载数据
        String currentTime = DateUtils.getTodayDateTime();
        xml = getXml(currentTime, "", "1", "1");
        Map<String, String> params = new HashMap<>();
        params.put("fltXml", xml);
        params.put("ErrString", "");
        loadingDialog.show();
        HttpRoot.getInstance().requstAync(DomFlightInHomeActivity.this, HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_NAME,
                HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject soapObject = (SoapObject) result;
                        String a =  soapObject.getProperty(0).toString();

                        // 解析得到的返回数据信息
                        flightCheckInfoList = PrepareceFlightCheckInfo.parseFlightCheckInfoInfoXml(a);
                        myFlightCheckAdapter = new MyFlightInHomeAdapter(DomFlightInHomeActivity.this, flightCheckInfoList);
                        flightInHomeLv.setAdapter(myFlightCheckAdapter);
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        loadingDialog.dismiss();
                    }
                });

        // 下拉刷新
        flightInHomeRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                String currentTime = DateUtils.getTodayDateTime();
                xml = getXml(currentTime, "", "1", "1");
                Map<String, String> params = new HashMap<>();
                params.put("fltXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(DomFlightInHomeActivity.this, HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_NAME,
                        HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject soapObject = (SoapObject) result;
                                String a =  soapObject.getProperty(0).toString();

                                // 解析得到的返回数据信息
                                flightCheckInfoList = PrepareceFlightCheckInfo.parseFlightCheckInfoInfoXml(a);
                                myFlightCheckAdapter = new MyFlightInHomeAdapter(DomFlightInHomeActivity.this, flightCheckInfoList);
                                flightInHomeLv.setAdapter(myFlightCheckAdapter);
                                flightInHomeRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                flightInHomeRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                flightInHomeRefresh.onHeaderRefreshComplete();
                            }
                        });
            }
        });

        // ListView每一项点击事件
        flightInHomeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlightCheckInfo flightCheckInfo = (FlightCheckInfo) myFlightCheckAdapter.getItem(position);
                Intent intent = new Intent(DomFlightInHomeActivity.this, DomFlightCheckInDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("domflight", flightCheckInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    // 数据适配器
    private class MyFlightInHomeAdapter extends BaseAdapter{
        private List<FlightCheckInfo> flightCheckInfoList;
        private Context context;

        public MyFlightInHomeAdapter(Context context, List<FlightCheckInfo> flightCheckInfoList) {
            this.context = context;
            this.flightCheckInfoList = flightCheckInfoList;
        }

        @Override
        public int getCount() {
            return flightCheckInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return flightCheckInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.domflightinhome_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.flight_in_home_fno_tv);
                viewHolder.destTv = (TextView) convertView.findViewById(R.id.flight_in_home_dest_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.flight_in_home_yjqf_tv);
                viewHolder.rukuTv = (TextView) convertView.findViewById(R.id.flight_in_home_ruku_tv);
                viewHolder.lahuoTv = (TextView) convertView.findViewById(R.id.flight_in_home_lahuo_tv);
                viewHolder.restWeightTv = (TextView) convertView.findViewById(R.id.flight_in_home_shengyu_tv);
                viewHolder.guanliBtn = (Button) convertView.findViewById(R.id.flight_in_home_btn);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.flight_in_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String fno = flightCheckInfoList.get(position).getFno();
            if (fno != null && !fno.equals("")) {
                viewHolder.fnoTv.setText(fno);
            } else {
                viewHolder.fnoTv.setText("");
            }
            String mudigang = flightCheckInfoList.get(position).getFDest();
            if (mudigang != null && !mudigang.equals("")) {
                viewHolder.destTv.setText(mudigang);
            } else {
                viewHolder.destTv.setText("");
            }
            String yjqf = flightCheckInfoList.get(position).getEstimatedTakeOff();
            if (yjqf != null && !yjqf.equals("")) {
                viewHolder.yjqfTv.setText(yjqf);
            } else {
                viewHolder.yjqfTv.setText("");
            }
            String ruku = flightCheckInfoList.get(position).getWaitCheckInWeight();
            if (ruku != null && !ruku.equals("")) {
                viewHolder.rukuTv.setText(ruku);
            } else {
                viewHolder.rukuTv.setText("");
            }
            String lahuo = flightCheckInfoList.get(position).getDelTransWeight();
            if (lahuo != null && !lahuo.equals("")) {
                viewHolder.lahuoTv.setText(lahuo);
            } else {
                viewHolder.lahuoTv.setText("");
            }
            String restWeight = flightCheckInfoList.get(position).getUseableWeight();
            if (restWeight != null && !restWeight.equals("")) {
                viewHolder.restWeightTv.setText(restWeight);
            } else {
                viewHolder.restWeightTv.setText("");
            }

            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }

            // 每一项item点击事件
            viewHolder.guanliBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DomFlightInHomeActivity.this, DomFlightCheckSureActivity.class);
                    FlightCheckInfo flightCheckInfo = flightCheckInfoList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("domflightsure", flightCheckInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView fnoTv;
            TextView destTv;
            TextView yjqfTv;
            TextView rukuTv;
            TextView lahuoTv;
            TextView restWeightTv;
            Button guanliBtn;
            LinearLayout showLy;
        }
    }


    private String getXml(String fdate, String fno, String onlyWaitCheck, String onlyFlightOn) {
        return "<GNCFlightPlan>"
                + "<FDate>" + fdate + "</FDate>"
                + "<Fno>" + fno + "</Fno>"
                + " <OnlyWaitCheck>" + onlyWaitCheck + "</OnlyWaitCheck>"
                + "  <OnlyFlightOn>" + onlyFlightOn + "</OnlyFlightOn>"
                + "</GNCFlightPlan>";
    }
}
