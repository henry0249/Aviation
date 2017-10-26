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
 * 航班动态出港
 */

public class FlightHomeOutFragment extends Fragment {
    @BindView(R.id.fragment_flight_out_lv)
    ListView fragmentFlightOutLv;
    @BindView(R.id.flight_out_refresh)
    PullToRefreshView flightOutRefresh;
    Unbinder unbinder;
    private View view;

    private FlightOutAdapter flightOutAdapter;

    private List<FlightMessage> flightMessages;
    private LoadingDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_flight_out, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        // 关闭上拉加载
        flightOutRefresh.disableScroolUp();
        dialog = new LoadingDialog(getContext());
        String currentTime = DateUtils.getTodayDateTime();
        String xml = getXml(currentTime, "", "P", "D", "", "", "E");
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
                        flightOutAdapter = new FlightOutAdapter(getActivity(), flightMessages);
                        fragmentFlightOutLv.setAdapter(flightOutAdapter);
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


        fragmentFlightOutLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                FlightMessage flightMessage = (FlightMessage) flightOutAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), FlightDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.FLIGHT_DETAIL, flightMessage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        // 下拉刷新
        flightOutRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                String currentTime = DateUtils.getTodayDateTime();
                String xml = getXml(currentTime, "", "P", "D", "", "", "E");
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
                                flightOutAdapter = new FlightOutAdapter(getActivity(), flightMessages);
                                fragmentFlightOutLv.setAdapter(flightOutAdapter);
                                flightOutRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                flightOutRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                flightOutRefresh.onHeaderRefreshComplete();
                            }

                        });
            }
        });
    }

    private class FlightOutAdapter extends BaseAdapter {
        private List<FlightMessage> list;
        private Context context;

        public FlightOutAdapter(Context context, List<FlightMessage> list) {
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
            // 优化adapter渲染速度
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.flight_out_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.flightOutLayout = (LinearLayout) convertView.findViewById(R.id.flight_out_layout);
                viewHolder.dateTv = (TextView) convertView.findViewById(R.id.flight_home_out_date_tv);
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.flight_home_out_fno_tv);
                viewHolder.sfgTv = (TextView) convertView.findViewById(R.id.flight_home_out_sfg_tv);
                viewHolder.mdgTv = (TextView) convertView.findViewById(R.id.flight_home_out_mdg_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.flight_home_out_yjqf_tv);
                viewHolder.sjqfTv = (TextView) convertView.findViewById(R.id.flight_home_out_sjqf_tv);
                viewHolder.stateTv = (TextView) convertView.findViewById(R.id.flight_home_out_state_tv);
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
                viewHolder.flightOutLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.flightOutLayout.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

        class ViewHolder {
            LinearLayout flightOutLayout;
            TextView dateTv;
            TextView fnoTv;
            TextView sfgTv;
            TextView mdgTv;
            TextView yjqfTv;
            TextView sjqfTv;
            TextView stateTv;
        }
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
