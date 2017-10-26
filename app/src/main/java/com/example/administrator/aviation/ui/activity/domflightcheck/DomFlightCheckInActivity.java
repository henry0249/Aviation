package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.domjcgrbb.FlightCheckInfo;
import com.example.administrator.aviation.model.domjcgrbb.PrepareceFlightCheckInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订舱计划列表页
 */

public class DomFlightCheckInActivity extends Activity {
    @BindView(R.id.flight_check_nodata_tv)
    TextView flightCheckNodataTv;
    @BindView(R.id.flight_check_load_tv)
    TextView flightCheckLoadTv;
    @BindView(R.id.flight_check_deatil_pb)
    ProgressBar flightCheckDeatilPb;
    @BindView(R.id.dflight_check_listview)
    ListView dflightCheckListview;
    @BindView(R.id.search_flight_refresh)
    PullToRefreshView searchFlightRefresh;

    private String xml;

    // 下拉刷新的xml
    private String searchXml;

    private List<FlightCheckInfo> flightCheckInfoList;

    private  MyFlightCheckAdapter myFlightCheckAdapter;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.DOM_CHECK_INFO && flightCheckInfoList.size() >= 1) {
                flightCheckDeatilPb.setVisibility(View.GONE);
                myFlightCheckAdapter = new MyFlightCheckAdapter(DomFlightCheckInActivity.this, flightCheckInfoList);
                dflightCheckListview.setAdapter(myFlightCheckAdapter);
            } else {
                flightCheckDeatilPb.setVisibility(View.GONE);
                flightCheckNodataTv.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdomflightcheck);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("待确认航班计划列表");
        navBar.hideRight();

        searchFlightRefresh.disableScroolUp();

        // 得到查询界面的查询结果xml
        xml = getIntent().getStringExtra("checkXml");

        // 得到查询界面返回的查询xml数据
        searchXml = getIntent().getStringExtra("searchXml");

        new Thread() {
            @Override
            public void run() {
                super.run();
                flightCheckDeatilPb.setVisibility(View.VISIBLE);
                flightCheckNodataTv.setVisibility(View.GONE);
                flightCheckInfoList = PrepareceFlightCheckInfo.parseFlightCheckInfoInfoXml(xml);
                myHandler.sendEmptyMessage(AviationCommons.DOM_CHECK_INFO);

            }
        }.start();


        // 下拉刷新
        searchFlightRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("fltXml", searchXml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(DomFlightCheckInActivity.this, HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_NAME,
                        HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject soapObject = (SoapObject) result;

                                // 得到查询返回的xml数据（a 代表返回的String型xml 信息）
                                String a =  soapObject.getProperty(0).toString();

                                // 解析得到的返回数据信息
                                flightCheckInfoList = PrepareceFlightCheckInfo.parseFlightCheckInfoInfoXml(a);
                                myFlightCheckAdapter = new MyFlightCheckAdapter(DomFlightCheckInActivity.this, flightCheckInfoList);
                                dflightCheckListview.setAdapter(myFlightCheckAdapter);
                                searchFlightRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                searchFlightRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                searchFlightRefresh.onHeaderRefreshComplete();
                            }
                        });
            }
        });

        // listView点击事件
        dflightCheckListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FlightCheckInfo flightCheckInfo = myFlightCheckAdapter.getItem(position);
                Intent intent = new Intent(DomFlightCheckInActivity.this, DomFlightCheckInDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("domflight", flightCheckInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class MyFlightCheckAdapter extends BaseAdapter {
        private Context context;
        private List<FlightCheckInfo> flightCheckInfoList;

        public MyFlightCheckAdapter(Context context, List<FlightCheckInfo> flightCheckInfoList) {
            this.context = context;
            this.flightCheckInfoList = flightCheckInfoList;
        }

        @Override
        public int getCount() {
            return flightCheckInfoList.size();
        }

        @Override
        public FlightCheckInfo getItem(int position) {
            return flightCheckInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.flight_check_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.check_fno_tv);
                viewHolder.mudigangTv = (TextView) convertView.findViewById(R.id.check_mudigang_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.check_yjqf_tv);
                viewHolder.rukuTv = (TextView) convertView.findViewById(R.id.check_ruku_tv);
                viewHolder.lahuoTv = (TextView) convertView.findViewById(R.id.check_lahuo_tv);
                viewHolder.restWeightTv = (TextView) convertView.findViewById(R.id.check_rest_weight_tv);
                viewHolder.sureBtn = (Button) convertView.findViewById(R.id.check_sure_btn);
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
                viewHolder.mudigangTv.setText(mudigang);
            } else {
                viewHolder.mudigangTv.setText("");
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

            // 订舱管理
            viewHolder.sureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DomFlightCheckInActivity.this, DomFlightCheckSureActivity.class);
                    FlightCheckInfo flightCheckInfo = flightCheckInfoList.get(position);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("domflightsure", flightCheckInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

        class ViewHolder {
            TextView fnoTv;
            TextView mudigangTv;
            TextView yjqfTv;
            TextView rukuTv;
            TextView lahuoTv;
            TextView restWeightTv;
            Button sureBtn;
            LinearLayout showLy;
        }
    }

}
