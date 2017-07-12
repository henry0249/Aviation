package com.example.administrator.aviation.ui.activity.intjcgrbb;

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
import com.example.administrator.aviation.model.intjcgrbb.IntExportDayInfo;
import com.example.administrator.aviation.model.intjcgrbb.PrepareIntExportDayInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 出港日报表详情
 */

public class IntExportDayCarrierDetailActivity extends Activity {
    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String xml;

    private List<IntExportDayInfo> intExportDayInfoList;
    private ExportDayAdapter exportDayAdapter;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.INT_E_DAT) {
                exportDayAdapter = new ExportDayAdapter(IntExportDayCarrierDetailActivity.this, intExportDayInfoList);
                edeclareLv.setAdapter(exportDayAdapter);
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
        setContentView(R.layout.activity_appexportdaydetail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("出港日报表列表");
        navBar.hideRight();

        xml = getIntent().getStringExtra("intexportdaydetailxml");

        new Thread() {
            @Override
            public void run() {
                super.run();
                intExportDayInfoList = PrepareIntExportDayInfo.pullExportDayInfoXml(xml);
                handler.sendEmptyMessage(AviationCommons.INT_E_DAT);
            }
        }.start();

        edeclareLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                IntExportDayInfo intExportDayInfo = (IntExportDayInfo) exportDayAdapter.getItem(position);
                String dates = intExportDayInfo.getFDate();
                String fnos = intExportDayInfo.getFno();
                Intent intent = new Intent(IntExportDayCarrierDetailActivity.this, IntExportDayManifestActivity.class);
                intent.putExtra("mdate", dates);
                intent.putExtra("mfno", fnos);
                startActivity(intent);
            }
        });
    }

    private class ExportDayAdapter extends BaseAdapter {
        private Context context;
        private List<IntExportDayInfo> intExportDayInfoList;
        public ExportDayAdapter(Context context, List<IntExportDayInfo> intExportDayInfoList) {
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
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.intexportdaydetail_item, viewGroup, false);
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.edeclare_info_fno_tv);
                viewHolder.sfgTv = (TextView) convertView.findViewById(R.id.edeclare_info_sf_tv);
                viewHolder.jtgTv = (TextView) convertView.findViewById(R.id.edeclare_info_jt_tv);
                viewHolder.mdgTv = (TextView) convertView.findViewById(R.id.edeclare_info_md_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.edeclare_info_pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.edeclare_info_weight_tv);
                viewHolder.volumeTv = (TextView) convertView.findViewById(R.id.edeclare_info_volume_tv);
                viewHolder.yjqfTv = (TextView) convertView.findViewById(R.id.edeclare_info_yj_tv);
                viewHolder.sjqfTv = (TextView) convertView.findViewById(R.id.edeclare_sj_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String fno = intExportDayInfoList.get(position).getFno();
            if (fno != null && !fno.equals("")) {
                viewHolder.fnoTv.setText(fno);
            } else {
                viewHolder.fnoTv.setText("");
            }
            String sfg = intExportDayInfoList.get(position).getFDep();
            if (sfg != null && !sfg.equals("")) {
                viewHolder.sfgTv.setText(sfg);
            } else {
                viewHolder.sfgTv.setText("");
            }
            String jtg = intExportDayInfoList.get(position).getJTZ();
            if (jtg != null && !jtg.equals("")) {
                viewHolder.jtgTv.setText(jtg);
            } else {
                viewHolder.jtgTv.setText("");
            }
            String mdg = intExportDayInfoList.get(position).getFDest();
            if (mdg != null && !mdg.equals("")) {
                viewHolder.mdgTv.setText(mdg);
            } else {
                viewHolder.mdgTv.setText("");
            }
            String pc = intExportDayInfoList.get(position).getPC();
            if (pc != null && !pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }
            String weight = intExportDayInfoList.get(position).getWeight();
            if (weight != null && !weight.equals("")) {
                viewHolder.weightTv.setText(weight);
            } else {
                viewHolder.weightTv.setText("");
            }
            String volume = intExportDayInfoList.get(position).getVolume();
            if (volume != null && !volume.equals("")) {
                viewHolder.volumeTv.setText(volume);
            } else {
                viewHolder.volumeTv.setText("");
            }
            String yjqf = intExportDayInfoList.get(position).getEstimatedTakeOff();
            if (yjqf != null && !yjqf.equals("")) {
                viewHolder.yjqfTv.setText(yjqf);
            } else {
                viewHolder.yjqfTv.setText("");
            }
            String sjqf = intExportDayInfoList.get(position).getActualTakeOff();
            if (sjqf != null && !sjqf.equals("")) {
                viewHolder.sjqfTv.setText(sjqf);
            } else {
                viewHolder.sjqfTv.setText("");
            }
            return convertView;
        }

        class ViewHolder{
            TextView fnoTv;
            TextView sfgTv;
            TextView mdgTv;
            TextView jtgTv;
            TextView pcTv;
            TextView weightTv;
            TextView volumeTv;
            TextView yjqfTv;
            TextView sjqfTv;
        }
    }
}
