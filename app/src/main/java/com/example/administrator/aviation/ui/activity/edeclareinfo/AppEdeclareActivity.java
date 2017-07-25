package com.example.administrator.aviation.ui.activity.edeclareinfo;

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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.edeclareinfo.EdeclareInfo;
import com.example.administrator.aviation.model.edeclareinfo.PrepareEdeclareInfo;
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
 * 国际承运人联检状态列表
 */

public class AppEdeclareActivity extends Activity {
    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;
    @BindView(R.id.pull_refresh_lj)
    PullToRefreshView pullRefreshLj;

    private String xml;
    private String fxml;
    private List<EdeclareInfo> edeclareInfoList;
    private EdeclareAdapter edeclareAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.EDECLARE_INFO_H) {
                edeclareAdapter = new EdeclareAdapter(AppEdeclareActivity.this, edeclareInfoList);
                edeclareLv.setAdapter(edeclareAdapter);
                edeclarePb.setVisibility(View.GONE);
                if (edeclareInfoList.size() >= 1) {
                    intEdeclareNodataTv.setVisibility(View.GONE);
                }
            } else if (msg.what == AviationCommons.INT_ES_DAT) {
                edeclareAdapter = new EdeclareAdapter(AppEdeclareActivity.this, edeclareInfoList);
                edeclareLv.setAdapter(edeclareAdapter);
                edeclarePb.setVisibility(View.GONE);
                if (edeclareInfoList.size() >= 1) {
                    intEdeclareNodataTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appedeclare);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("联检状态列表");
        navBar.hideRight();

        pullRefreshLj.disableScroolUp();

        xml = getIntent().getStringExtra(AviationCommons.EDECLARE_INFO);
        fxml = getIntent().getStringExtra("fxml");
        new Thread() {
            @Override
            public void run() {
                super.run();
                edeclareInfoList = PrepareEdeclareInfo.pullEDeclareInfoXml(xml);
                handler.sendEmptyMessage(AviationCommons.EDECLARE_INFO_H);
            }
        }.start();

        edeclareLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EdeclareInfo edeclareInfo = (EdeclareInfo) edeclareAdapter.getItem(position);
                Intent intent = new Intent(AppEdeclareActivity.this, AppEdeclareDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("edeclareInfo", edeclareInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 下拉刷新
        pullRefreshLj.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", fxml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppEdeclareActivity.this, HttpCommons.CGO_GET_EDECLARE_NAME, HttpCommons.CGO_GET_EDECLARE_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String a = object.getProperty(0).toString();
                                edeclareInfoList = PrepareEdeclareInfo.pullEDeclareInfoXml(a);
                                handler.sendEmptyMessage(AviationCommons.INT_ES_DAT);
                                pullRefreshLj.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                edeclarePb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                edeclarePb.setVisibility(View.GONE);
                            }
                        });

            }
        });

    }

    private class EdeclareAdapter extends BaseAdapter {
        private Context context;
        private List<EdeclareInfo> edeclareInfos;

        public EdeclareAdapter(Context context, List<EdeclareInfo> edeclareInfos) {
            this.context = context;
            this.edeclareInfos = edeclareInfos;
        }

        @Override
        public int getCount() {
            return edeclareInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return edeclareInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.edeclare_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.edeclare_info_mavb_tv);
                viewHolder.opDateTv = (TextView) convertView.findViewById(R.id.edeclare_info_papertime_tv);
                viewHolder.ciqstatusTv = (TextView) convertView.findViewById(R.id.edeclare_info_ciqstatus_tv);
                viewHolder.cmdstatusTv = (TextView) convertView.findViewById(R.id.edeclare_info_cmdstatus_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mawb = edeclareInfos.get(position).getMawb();
            if (mawb != null && !mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb);
            } else {
                viewHolder.mawbTv.setText("");
            }

            String ciqstatus = edeclareInfos.get(position).getCIQStatus();
            if (ciqstatus != null && !ciqstatus.equals("")) {
                viewHolder.ciqstatusTv.setText(ciqstatus);
            } else {
                viewHolder.ciqstatusTv.setText("");
            }

            String cmdstatus = edeclareInfos.get(position).getCMDStatus();
            if (cmdstatus != null && !cmdstatus.equals("")) {
                viewHolder.cmdstatusTv.setText(cmdstatus);
            } else {
                viewHolder.cmdstatusTv.setText("");
            }

            String opDate = edeclareInfos.get(position).getPaperTime();
            if (opDate != null && !opDate.equals("")) {
                viewHolder.opDateTv.setText(opDate);
            } else {
                viewHolder.opDateTv.setText("");
            }

            return convertView;
        }
    }

    class ViewHolder {
        // 主单号
        TextView mawbTv;

        // 商检指令
        TextView ciqstatusTv;

        // 海关指令
        TextView cmdstatusTv;

        // 交单日期
        TextView opDateTv;

    }
}
