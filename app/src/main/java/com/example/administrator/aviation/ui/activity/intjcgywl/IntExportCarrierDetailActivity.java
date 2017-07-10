package com.example.administrator.aviation.ui.activity.intjcgywl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intjcgywl.IntExportCarrierInfo;
import com.example.administrator.aviation.model.intjcgywl.PrepareIntExportCarrierInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.edeclare_info_volume_tv;

/**
 * 国际出港日报表详情页
 */

public class IntExportCarrierDetailActivity extends Activity {
    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String type;

    private String xml;
    private List<IntExportCarrierInfo> intExportCarrierInfoList;
    private IntDayAdapter intDayAdapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.INT_EXPORT_DAY) {
                intDayAdapter = new IntDayAdapter(IntExportCarrierDetailActivity.this, intExportCarrierInfoList);
                edeclareLv.setAdapter(intDayAdapter);
                edeclarePb.setVisibility(View.GONE);
                if (intExportCarrierInfoList.size() >= 1) {
                    intEdeclareNodataTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intexportcarrier_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("国际出港业务量详情");
        navBar.hideRight();

        xml = getIntent().getStringExtra("intexportdayxml");
        type = getIntent().getStringExtra("type");
        new Thread() {
            @Override
            public void run() {
                super.run();
                intExportCarrierInfoList = PrepareIntExportCarrierInfo.pullExportCarrierInfoXml(xml);
                handler.sendEmptyMessage(AviationCommons.INT_EXPORT_DAY);
            }
        }.start();
    }

    private class IntDayAdapter extends BaseAdapter {
        private Context context;
        private List<IntExportCarrierInfo> intExportCarrierInfoList;
        public IntDayAdapter(Context context, List<IntExportCarrierInfo> intExportCarrierInfoList) {
            this.context = context;
            this.intExportCarrierInfoList = intExportCarrierInfoList;
        }

        @Override
        public int getCount() {
            return intExportCarrierInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return intExportCarrierInfoList.get(position);
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
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.edeclare_fno_tv);
                viewHolder.detTv = (TextView) convertView.findViewById(R.id.edeclare_info_dest_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.edeclare_info_pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.edeclare_info_weight_tv);
                viewHolder.volumeTv = (TextView) convertView.findViewById(edeclare_info_volume_tv);
                viewHolder.destLayout = (LinearLayout) convertView.findViewById(R.id.carrier_dest_layout);
                viewHolder.fnoLayout = (LinearLayout) convertView.findViewById(R.id.carrier_fno_layout);
                viewHolder.fdateLayout = (LinearLayout) convertView.findViewById(R.id.carrier_fdate_layout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (type!=null && type.equals("")) {
                viewHolder.destLayout.setVisibility(View.GONE);
                viewHolder.fdateLayout.setVisibility(View.GONE);
                viewHolder.fnoLayout.setVisibility(View.GONE);
            } else if (type != null && type.equals("DEST")) {
                viewHolder.fnoLayout.setVisibility(View.GONE);
                viewHolder.fdateLayout.setVisibility(View.GONE);
            } else if (type != null && type.equals("FNO")) {
                viewHolder.fdateLayout.setVisibility(View.GONE);
                viewHolder.destLayout.setVisibility(View.GONE);
            } else if (type != null && type.equals("DAY")) {
                viewHolder.fnoLayout.setVisibility(View.GONE);
                viewHolder.destLayout.setVisibility(View.GONE);
            }
            String carrier = intExportCarrierInfoList.get(position).getCarrier();
            if (carrier!=null && !carrier.equals("")) {
                viewHolder.carrierTv.setText(carrier);
            } else {
                viewHolder.carrierTv.setText("");
            }
            String fno = intExportCarrierInfoList.get(position).getFno();
            if (fno != null && !fno.equals("")) {
                viewHolder.fnoTv.setText(fno);
            } else {
                viewHolder.detTv.setText("");
            }
            String dest = intExportCarrierInfoList.get(position).getDest();
            if (dest != null && !dest.equals("")) {
                viewHolder.detTv.setText(dest);
            } else {
                viewHolder.detTv.setText("");
            }
            String fdate = intExportCarrierInfoList.get(position).getFDate();
            if (fdate != null && !fdate.equals("")) {
                viewHolder.fdateTv.setText(fdate);
            } else {
                viewHolder.fdateTv.setText("");
            }
            String pc = intExportCarrierInfoList.get(position).getPc();
            if (pc!=null && !pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }
            String weight = intExportCarrierInfoList.get(position).getWeight();
            if (weight != null && !weight.equals("")) {
                viewHolder.weightTv.setText(weight);
            } else {
                viewHolder.weightTv.setText("");
            }
            String volume = intExportCarrierInfoList.get(position).getVolume();
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
            TextView fnoTv;
            TextView detTv;
            LinearLayout fdateLayout;
            LinearLayout fnoLayout;
            LinearLayout destLayout;
            TextView pcTv;
            TextView weightTv;
            TextView volumeTv;
        }
    }
}
