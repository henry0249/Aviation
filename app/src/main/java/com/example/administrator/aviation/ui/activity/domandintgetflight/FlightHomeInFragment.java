package com.example.administrator.aviation.ui.activity.domandintgetflight;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.intanddomflight.FlightMessage;
import com.example.administrator.aviation.model.intanddomflight.PrepareFlightMessage;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 航班动态进港数据
 */

public class FlightHomeInFragment extends Fragment {
    @BindView(R.id.fragment_flight_in_lv)
    ListView fragmentFlightInLv;
    Unbinder unbinder;
    @BindView(R.id.flight_in_refresh)
    PullToRefreshView flightInRefresh;
    private View view;

    private  FlightInAdapter flightInAdapter;

    private List<FlightMessage> flightMessages;
    private LoadingDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flight_in, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        // 关闭上拉加载
        flightInRefresh.disableScroolUp();
        dialog = new LoadingDialog(getContext());
        String currentTime = DateUtils.getTodayDateTime();
        String xml = getXml(currentTime, "", "P", "D", "", "", "I");
        Map<String, String> params = new HashMap<>();
        params.put("fltXml", xml);
        params.put("ErrString", "");
        dialog.show();
        HttpRoot.getInstance().requstAync(getActivity(), HttpCommons.CGO_GET_FLIGHT_NAME, HttpCommons.CGO_GET_FLIGHT_ACTION, params,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String a = object.getProperty(0).toString();
                        flightMessages = PrepareFlightMessage.pullFlightXml(a);
                        flightInAdapter = new FlightInAdapter(getActivity(), flightMessages);
                        fragmentFlightInLv.setAdapter(flightInAdapter);
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailed(String message) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        dialog.dismiss();
                    }

                });


        fragmentFlightInLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FlightMessage flightMessage = (FlightMessage) flightInAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), FlightDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.FLIGHT_DETAIL, flightMessage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        // 下拉刷新
        flightInRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                String currentTime = DateUtils.getTodayDateTime();
                String xml = getXml(currentTime, "", "P", "D", "", "", "I");
                Map<String, String> params = new HashMap<>();
                params.put("fltXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(getActivity(), HttpCommons.CGO_GET_FLIGHT_NAME, HttpCommons.CGO_GET_FLIGHT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String a = object.getProperty(0).toString();
                                flightMessages = PrepareFlightMessage.pullFlightXml(a);
                                flightInAdapter = new FlightInAdapter(getActivity(), flightMessages);
                                fragmentFlightInLv.setAdapter(flightInAdapter);
                                flightInRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                flightInRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                flightInRefresh.onHeaderRefreshComplete();
                            }

                        });
            }
        });
    }



    private class FlightInAdapter extends BaseAdapter {
        private List<FlightMessage> list;
        private Context context;

        public FlightInAdapter(Context context, List<FlightMessage> list) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.flight_in_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.flightInLayout = (LinearLayout) convertView.findViewById(R.id.flight_in_layout);
                viewHolder.dateTv = (TextView) convertView.findViewById(R.id.flight_home_in_date_tv);
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.flight_home_in_fno_tv);
                viewHolder.sfgTv = (TextView) convertView.findViewById(R.id.flight_home_in_sfg_tv);
                viewHolder.mdgTv = (TextView) convertView.findViewById(R.id.flight_home_in_mdg_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.flight_home_in_yjqf_tv);
                viewHolder.sjqfTv = (TextView) convertView.findViewById(R.id.flight_home_in_sjqf_tv);
                viewHolder.stateTv = (TextView) convertView.findViewById(R.id.flight_home_in_state_tv);
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
            String yjqf = list.get(position).getEstimatedArrival();
            if (!yjqf.equals("")) {
                viewHolder.yjqfTv.setText(yjqf);
            } else {
                viewHolder.yjqfTv.setText("");
            }
            String sjqf = list.get(position).getActualArrival();
            if (!sjqf.equals("")) {
                viewHolder.sjqfTv.setText(sjqf);
            } else {
                viewHolder.sjqfTv.setText("");
            }

            String state = list.get(position).getFlightStatus();
            if (state != null && !state.equals("")) {
                if (state.equals("D") || state.equals("X")) {
                    viewHolder.stateTv.setTextColor(Color.parseColor("#fe3500"));
                } else {
                    viewHolder.stateTv.setTextColor(Color.parseColor("#26c219"));
                }
                state = AviationNoteConvert.statusCntoEn(state);
                viewHolder.stateTv.setText(state);
            } else {
                viewHolder.stateTv.setText("");
            }

            if (position % 2 == 0) {
                viewHolder.flightInLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.flightInLayout.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

        class ViewHolder {
            LinearLayout flightInLayout;
            TextView dateTv;
            TextView fnoTv;
            TextView sfgTv;
            TextView mdgTv;
            TextView yjqfTv;
            TextView sjqfTv;
            TextView stateTv;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private String getXml(String riqi, String hangbhao, String hangbanleixing, String quyuleixing,
                          String qiyungang, String mudigang, String jinchugangleixing) {
        String xml = new String("<FLTFlight>"
                + "<FDate>" + riqi + "</FDate>"
                + "<Fno>" + hangbhao + "</Fno>"
                + " <ServiceType>" + hangbanleixing + "</ServiceType>"
                + " <CountryType>" + quyuleixing + "</CountryType>"
                + " <FDep>" + qiyungang + "</FDep>"
                + " <FDest>" + mudigang + "</FDest>"
                + "  <IEFlag>" + jinchugangleixing + "</IEFlag>"
                + "</FLTFlight>");

        return xml;
    }
}
