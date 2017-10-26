package com.example.administrator.aviation.ui.activity.domjcgrbb;

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
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.intjcgrbb.IntExportDayManifestInfo;
import com.example.administrator.aviation.model.intjcgrbb.PrepareIntExportDayMainfestInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国内出港日报表舱单信息
 */

public class DomImportDayManifestActivity extends Activity {

    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String endXml;
    private List<IntExportDayManifestInfo> intExportDayManifestInfoList;

    private  Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == AviationCommons.INT_E_DAY_MANIFEST) {
                IntEManifestAdapter intEManifestAdapter = new IntEManifestAdapter(DomImportDayManifestActivity.this, intExportDayManifestInfoList);
                edeclareLv.setAdapter(intEManifestAdapter);
                edeclarePb.setVisibility(View.GONE);
                if (intExportDayManifestInfoList.size() >= 1) {
                    intEdeclareNodataTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexportmanifest);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("进港舱单信息列表");
        navBar.hideRight();

        String fdate = getIntent().getStringExtra("imdate");
        String fno = getIntent().getStringExtra("imfno");

        String xml = getXml(fdate, fno);
        Map<String, String> params = new HashMap<>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        HttpRoot.getInstance().requstAync(DomImportDayManifestActivity.this, HttpCommons.CGO_GET_DOM_IMPORT_MANIFEST_NAME,
                HttpCommons.CGO_GET_DOM_IMPORT_MANIFEST_ACTION, params,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        endXml = object.getProperty(0).toString();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                intExportDayManifestInfoList = PrepareIntExportDayMainfestInfo.pullExportDayManifetInfoXml(endXml);
                                handler.sendEmptyMessage(AviationCommons.INT_E_DAY_MANIFEST);
                            }
                        }.start();

                    }

                    @Override
                    public void onFailed(String message) {
                        edeclarePb.setVisibility(View.GONE);
                        intEdeclareNodataTv.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        edeclarePb.setVisibility(View.GONE);
                        intEdeclareNodataTv.setVisibility(View.GONE);
                    }
                });

    }

    // 提交到服务器的xml
    private String getXml(String fdate, String fno) {
        return "<GNJCarrierReport>"
                + "<StartDay>" + fdate + "</StartDay>"
                + "<Fno>" + fno + "</Fno>"
                + "</GNJCarrierReport>";
    }

    // 出港舱单的adapter
    private class IntEManifestAdapter extends BaseAdapter {
        private Context context;
        private List<IntExportDayManifestInfo> intExportDayManifestInfoList;
        public IntEManifestAdapter(Context context, List<IntExportDayManifestInfo> intExportDayManifestInfoList) {
            this.context = context;
            this.intExportDayManifestInfoList = intExportDayManifestInfoList;
        }

        @Override
        public int getCount() {
            return intExportDayManifestInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return intExportDayManifestInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.intexportmanifest_item, viewGroup, false);
                viewHolder = new ViewHolder();
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.mawb_tv);
                viewHolder.dailidaimaTv = (TextView) convertView.findViewById(R.id.daili_daima_tv);
                viewHolder.dailiNameTv = (TextView) convertView.findViewById(R.id.dailimingcheng_tv);
                viewHolder.sfgTv = (TextView) convertView.findViewById(R.id.qiyungang_tv);
                viewHolder.mudigangTv = (TextView) convertView.findViewById(R.id.mudigang_tv);
                viewHolder.zongjianshuTv = (TextView) convertView.findViewById(R.id.zongjianshu_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.weiight_tv);
                viewHolder.volumeTv = (TextView) convertView.findViewById(R.id.volume_tv);
                viewHolder.goodsTv = (TextView) convertView.findViewById(R.id.goods_tv);
                viewHolder.isfgTv = (TextView) convertView.findViewById(R.id.textView10);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.isfgTv.setText("始发港:");
            String mawb = intExportDayManifestInfoList.get(position).getMawb();
            if (mawb != null && !mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb);
            } else {
                viewHolder.mawbTv.setText("");
            }
            String dailidaima = intExportDayManifestInfoList.get(position).getAgentCode();
            if (dailidaima != null && !dailidaima.equals("")) {
                viewHolder.dailidaimaTv.setText(dailidaima);
            } else {
                viewHolder.dailidaimaTv.setText("");
            }
            String dailiname = intExportDayManifestInfoList.get(position).getAgentName();
            if (dailiname != null && !dailiname.equals("")) {
                viewHolder.dailiNameTv.setText(dailiname);
            } else {
                viewHolder.dailiNameTv.setText("");
            }
            String sfg = intExportDayManifestInfoList.get(position).getOrigin();
            if (sfg != null && !sfg.equals("")) {
                viewHolder.sfgTv.setText(sfg);
            } else {
                viewHolder.sfgTv.setText("");
            }
            String mudigang = intExportDayManifestInfoList.get(position).getDest();
            if (mudigang != null && !mudigang.equals("")) {
                viewHolder.mudigangTv.setText(mudigang);
            } else {
                viewHolder.mudigangTv.setText("");
            }
            String zongjianshu = intExportDayManifestInfoList.get(position).getTotalPC();
            if (zongjianshu != null && !zongjianshu.equals("")) {
                viewHolder.zongjianshuTv.setText(zongjianshu);
            } else {
                viewHolder.zongjianshuTv.setText("");
            }
            String pc = intExportDayManifestInfoList.get(position).getPC();
            if (pc != null && !pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }
            String weight = intExportDayManifestInfoList.get(position).getWeight();
            if (weight != null && !weight.equals("")) {
                viewHolder.weightTv.setText(weight);
            } else {
                viewHolder.weightTv.setText("");
            }
            String volume = intExportDayManifestInfoList.get(position).getVolume();
            if (volume != null && !volume.equals("")) {
                viewHolder.volumeTv.setText(volume);
            } else {
                viewHolder.volumeTv.setText("");
            }
            String goods = intExportDayManifestInfoList.get(position).getGoods();
            if (goods != null && !goods.equals("")) {
                viewHolder.goodsTv.setText(goods);
            } else {
                viewHolder.goodsTv.setText("");
            }
            return convertView;
        }

        class ViewHolder{
            TextView mawbTv;
            TextView dailidaimaTv;
            TextView dailiNameTv;
            TextView sfgTv;
            TextView mudigangTv;
            TextView zongjianshuTv;
            TextView pcTv;
            TextView weightTv;
            TextView volumeTv;
            TextView goodsTv;
            TextView isfgTv;
        }
    }

}
