package com.example.administrator.aviation.ui.activity.edeclareinfo;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.model.edeclareinfo.EdeclareInfo;
import com.example.administrator.aviation.model.edeclareinfo.PrepareEdeclareInfo;
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
 * 首次进入国际承运人联检状态查询界面显示页面
 */

public class AppEdeclareHomeActivity extends Activity {
    @BindView(R.id.int_edeclare_nodata_tv)
    TextView intEdeclareNodataTv;
    @BindView(R.id.edeclare_lv)
    ListView edeclareLv;
    @BindView(R.id.pull_refresh_lj)
    PullToRefreshView pullRefreshLj;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private List<EdeclareInfo> edeclareInfoList;
    private EdeclareAdapter edeclareAdapter;

    private LoadingDialog loadingDialog;

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
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppEdeclareHomeActivity.this, AppEDeclareInfoSearchActivity.class);
                startActivity(intent);
            }
        });

        // 关闭上拉刷新
        pullRefreshLj.disableScroolUp();

        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();

        // 不显示pb和没有数据
        edeclarePb.setVisibility(View.GONE);
        intEdeclareNodataTv.setVisibility(View.GONE);
        String xml = getXml("", "0");
        Map<String, String> params = new HashMap<>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        HttpRoot.getInstance().requstAync(AppEdeclareHomeActivity.this, HttpCommons.CGO_GET_EDECLARE_NAME,
                HttpCommons.CGO_GET_EDECLARE_ACTION, params,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String edeclare = object.getProperty(0).toString();
                        edeclareInfoList = PrepareEdeclareInfo.pullEDeclareInfoXml(edeclare);
                        edeclareAdapter = new EdeclareAdapter(AppEdeclareHomeActivity.this, edeclareInfoList);
                        edeclareLv.setAdapter(edeclareAdapter);
                        if (edeclareInfoList.size() >= 1) {
                            intEdeclareNodataTv.setVisibility(View.GONE);
                        }
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

        // 列表项点击事件
        edeclareLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                EdeclareInfo edeclareInfo = (EdeclareInfo) edeclareAdapter.getItem(position);
                Intent intent = new Intent(AppEdeclareHomeActivity.this, AppEdeclareDetailActivity.class);
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
                String xml = getXml("", "0");
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppEdeclareHomeActivity.this, HttpCommons.CGO_GET_EDECLARE_NAME,
                        HttpCommons.CGO_GET_EDECLARE_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String edeclare = object.getProperty(0).toString();
                                edeclareInfoList = PrepareEdeclareInfo.pullEDeclareInfoXml(edeclare);
                                edeclareAdapter = new EdeclareAdapter(AppEdeclareHomeActivity.this, edeclareInfoList);
                                edeclareLv.setAdapter(edeclareAdapter);
                                if (edeclareInfoList.size() >= 1) {
                                    intEdeclareNodataTv.setVisibility(View.GONE);
                                }
                                pullRefreshLj.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                pullRefreshLj.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                pullRefreshLj.onHeaderRefreshComplete();
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
                viewHolder.nameTv = (TextView) convertView.findViewById(R.id.name_tv);
                viewHolder.opDateTv = (TextView) convertView.findViewById(R.id.edeclare_info_papertime_tv);
                viewHolder.ciqstatusTv = (TextView) convertView.findViewById(R.id.edeclare_info_ciqstatus_tv);
                viewHolder.cmdstatusTv = (TextView) convertView.findViewById(R.id.edeclare_info_cmdstatus_tv);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.flight_in_layout);
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

            String name = edeclareInfos.get(position).getAgentName();
            if (name != null && !name.equals("")) {
                viewHolder.nameTv.setText(name);
            } else {
                viewHolder.nameTv.setText("");
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

            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }

            return convertView;
        }
    }

    class ViewHolder {
        // 主单号
        TextView mawbTv;

        // 公司名称
        TextView nameTv;

        // 商检指令
        TextView ciqstatusTv;

        // 海关指令
        TextView cmdstatusTv;

        // 交单日期
        TextView opDateTv;

        LinearLayout showLy;

    }

    // 获取查询的xml
    private String getXml(String mawb, String sffangxing) {
        return "<GJCCarrierReport>"
                + "<Mawb>" + mawb + "</Mawb>"
                + "<RELStatus>" + sffangxing + "</RELStatus>"
                + "</GJCCarrierReport>";
    }
}
