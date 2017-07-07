package com.example.administrator.aviation.ui.activity.intcgrbb;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intcgrbb.IntExportDayInfo;
import com.example.administrator.aviation.model.intcgrbb.PrepareIntExportDayInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.edeclare_info_volume_tv;

/**
 * 国际出港日报表详情页
 */

public class IntExportDayDetailActivity extends Activity {
    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String xml;
    private List<IntExportDayInfo> intExportDayInfoList;
    private IntDayAdapter intDayAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.INT_EXPORT_DAY) {
                intDayAdapter = new IntDayAdapter(IntExportDayDetailActivity.this, intExportDayInfoList);
                edeclareLv.setAdapter(intDayAdapter);
                edeclarePb.setVisibility(View.GONE);
                if (intExportDayInfoList.size() >= 1) {
                    intEdeclareNodataTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intexportday_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("国际出港日报表详情");
        navBar.hideRight();

        xml = getIntent().getStringExtra("intexportdayxml");
        new Thread() {
            @Override
            public void run() {
                super.run();
                intExportDayInfoList = PrepareIntExportDayInfo.pullExportDayInfoXml(xml);
                handler.sendEmptyMessage(AviationCommons.INT_EXPORT_DAY);
            }
        }.start();
    }

    private class IntDayAdapter extends BaseAdapter {
        private Context context;
        private List<IntExportDayInfo> intExportDayInfoList;
        public IntDayAdapter(Context context, List<IntExportDayInfo> intExportDayInfoList) {
            this.context = context;
            this.intExportDayInfoList = intExportDayInfoList;
        }

        @Override
        public int getCount() {
            return intExportDayInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return intExportDayInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.intexportday_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.carrierTv = (TextView) convertView.findViewById(R.id.edeclare_info_carrier_tv);
                viewHolder.fdateTv = (TextView) convertView.findViewById(R.id.edeclare_info_papertime_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.edeclare_info_pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.edeclare_info_weight_tv);
                viewHolder.volumeTv = (TextView) convertView.findViewById(edeclare_info_volume_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String carrier = intExportDayInfoList.get(position).getCarrier();
            if (carrier!=null && !carrier.equals("")) {
                viewHolder.carrierTv.setText(carrier);
            } else {
                viewHolder.carrierTv.setText("");
            }
            String fdate = intExportDayInfoList.get(position).getFDate();
            if (fdate != null && !fdate.equals("")) {
                viewHolder.fdateTv.setText(fdate);
            } else {
                viewHolder.fdateTv.setText("");
            }
            String pc = intExportDayInfoList.get(position).getPc();
            if (pc!=null && !pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }
            String weight = intExportDayInfoList.get(position).getWeight();
            if (weight != null && !weight.equals("")) {
                viewHolder.weightTv.setText("");
            } else {
                viewHolder.weightTv.setText("");
            }
            String volume = intExportDayInfoList.get(position).getVolume();
            if (volume != null && !volume.equals("")) {
                viewHolder.volumeTv.setText(volume);
            } else {
                viewHolder.volumeTv.setText("");
            }
            return convertView;
        }

        class ViewHolder {
            TextView carrierTv;
            TextView fdateTv;
            TextView pcTv;
            TextView weightTv;
            TextView volumeTv;
        }
    }
}
