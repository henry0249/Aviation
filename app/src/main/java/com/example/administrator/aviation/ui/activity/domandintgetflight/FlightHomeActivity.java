package com.example.administrator.aviation.ui.activity.domandintgetflight;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.intanddomflight.FlightMessage;
import com.example.administrator.aviation.model.intanddomflight.PrepareFlightMessage;
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
 * 航班动态主页面
 */

public class FlightHomeActivity extends Activity {
    @BindView(R.id.flight_nodata_tv)
    TextView flightNodataTv;
    @BindView(R.id.flight_home_lv)
    ListView flightHomeLv;
    @BindView(R.id.flight_home_pb)
    ProgressBar flightHomePb;
    @BindView(R.id.pull_refresh)
    PullToRefreshView pullRefresh;

    private String xml;
    // 查询界面传递来的刷新xml
    private String refreshXml;
    private String jcgLeixing;
    private List<FlightMessage> flightMessages;
    private FlightAdapter flightAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.FLIGHT_INFO_RQQUEST) {
                flightAdapter = new FlightAdapter(FlightHomeActivity.this, flightMessages);
                flightHomeLv.setAdapter(flightAdapter);
                flightHomePb.setVisibility(View.GONE);
                if (flightMessages.size() >= 1) {
                    flightNodataTv.setVisibility(View.GONE);
                }
            } else if (msg.what == AviationCommons.FLIGHT_REFERCH) {
                flightAdapter = new FlightAdapter(FlightHomeActivity.this, flightMessages);
                flightHomeLv.setAdapter(flightAdapter);
                flightHomePb.setVisibility(View.GONE);
                if (flightMessages.size() >= 1) {
                    flightNodataTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("航班动态列表");
        navBar.hideRight();

        pullRefresh.disableScroolUp();

        xml = getIntent().getStringExtra(AviationCommons.FLIGHT_INFO);
        refreshXml = getIntent().getStringExtra(AviationCommons.FLIGHT_XML);
        jcgLeixing = getIntent().getStringExtra("jcgleixing");

        new Thread() {
            @Override
            public void run() {
                super.run();
                flightMessages = PrepareFlightMessage.pullFlightXml(xml);
                handler.sendEmptyMessage(AviationCommons.FLIGHT_INFO_RQQUEST);
            }
        }.start();

        flightHomeLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FlightMessage flightMessage = (FlightMessage) flightAdapter.getItem(position);
                Intent intent = new Intent(FlightHomeActivity.this, FlightDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.FLIGHT_DETAIL, flightMessage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        // 下拉刷新
        pullRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("fltXml", refreshXml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(FlightHomeActivity.this, HttpCommons.CGO_GET_FLIGHT_NAME, HttpCommons.CGO_GET_FLIGHT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String a = object.getProperty(0).toString();
                                flightMessages = PrepareFlightMessage.pullFlightXml(a);
                                handler.sendEmptyMessage(AviationCommons.FLIGHT_REFERCH);
                                pullRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                flightHomePb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                flightHomePb.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }

    private class FlightAdapter extends BaseAdapter {
        private List<FlightMessage> list;
        private Context context;

        public FlightAdapter(Context context, List<FlightMessage> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.flight_home_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.dateTv = (TextView) convertView.findViewById(R.id.flight_home_date_tv);
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.flight_home_fno_tv);
                viewHolder.sfgTv = (TextView) convertView.findViewById(R.id.flight_home_sfg_tv);
                viewHolder.mdgTv = (TextView) convertView.findViewById(R.id.flight_home_mdg_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.flight_home_yjqf_tv);
                viewHolder.yjddTv = (TextView) convertView.findViewById(R.id.flight_home_yjdd_tv);
                viewHolder.sjqfTv = (TextView) convertView.findViewById(R.id.flight_home_sjqf_tv);
                viewHolder.sjddTv = (TextView) convertView.findViewById(R.id.flight_home_sjdd_tv);
                viewHolder.chugangLayout = (LinearLayout) convertView.findViewById(R.id.flight_home_chugang_layout);
                viewHolder.jingangLayout = (LinearLayout) convertView.findViewById(R.id.flight_home_jingang_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String fdate = list.get(position).getfDate();
            if (!fdate.equals("")) {
                viewHolder.dateTv.setText(fdate);
            } else {
                viewHolder.dateTv.setText("");
            }
            String fno = list.get(position).getFno();
            if (!fno.equals("")) {
                viewHolder.fnoTv.setText(fno);
            } else {
                viewHolder.fnoTv.setText("");
            }
            String sfg = list.get(position).getfDep();
            if (!sfg.equals("")) {
                viewHolder.sfgTv.setText(sfg);
            } else {
                viewHolder.sfgTv.setText("");
            }
            String mdg = list.get(position).getfDest();
            if (!mdg.equals("")) {
                viewHolder.mdgTv.setText(mdg);
            } else {
                viewHolder.mdgTv.setText("");
            }
            String yjqf = list.get(position).getEstimatedTakeOff();
            if (!yjqf.equals("")) {
                viewHolder.yjqfTv.setText(yjqf);
            } else {
                viewHolder.yjqfTv.setText("");
            }
            String sjqf = list.get(position).getActualTakeOff();
            if (!sjqf.equals("")) {
                viewHolder.sjqfTv.setText(sjqf);
            } else {
                viewHolder.sjqfTv.setText("");
            }
            String yjdd = list.get(position).getEstimatedArrival();
            if (!yjdd.equals("")) {
                viewHolder.yjddTv.setText(yjdd);
            } else {
                viewHolder.yjddTv.setText("");
            }
            String sjdd = list.get(position).getActualArrival();
            if (!sjdd.equals("")) {
                viewHolder.sjddTv.setText(sjdd);
            } else {
                viewHolder.sjddTv.setText("");
            }

            // 判断进出港
            if (jcgLeixing.equals("I")) {
                viewHolder.chugangLayout.setVisibility(View.GONE);
            } else if (jcgLeixing.equals("E")) {
                viewHolder.jingangLayout.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            TextView dateTv;
            TextView fnoTv;
            TextView sfgTv;
            TextView mdgTv;
            TextView yjqfTv;
            TextView sjqfTv;
            TextView yjddTv;
            TextView sjddTv;
            LinearLayout jingangLayout;
            LinearLayout chugangLayout;
        }
    }
}
