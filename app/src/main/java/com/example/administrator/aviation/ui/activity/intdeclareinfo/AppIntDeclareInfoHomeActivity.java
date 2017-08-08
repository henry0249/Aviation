package com.example.administrator.aviation.ui.activity.intdeclareinfo;

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
import com.example.administrator.aviation.http.getIntdeclareinfo.HttpIntDeclareInfo;
import com.example.administrator.aviation.model.intdeclareinfo.DeclareInfoMessage;
import com.example.administrator.aviation.model.intdeclareinfo.PrepareDeclareInfoMessage;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.administrator.aviation.R.id.progressBar;

/**
 * 国际货代联检查询
 */

public class AppIntDeclareInfoHomeActivity extends Activity {
    @BindView(R.id.gnjcg_tv)
    TextView gnjcgTv;
    @BindView(R.id.show_d_ly)
    LinearLayout showDLy;
    @BindView(R.id.declare_info_deatil_pb)
    ProgressBar declareInfoDeatilPb;
    @BindView(R.id.declare_info_show_tv)
    TextView declareInfoShowTv;
    @BindView(R.id.declare_info_load_tv)
    TextView declareInfoLoadTv;
    @BindView(R.id.declare_info_listview)
    ListView declareInfoListview;
    @BindView(R.id.declare_refresh)
    PullToRefreshView declareRefresh;

    // 获取当前时间
    private String currentTime;

    private String xml;
    private DeclareInfoAdapter declareInfoAdapter;
    private List<DeclareInfoMessage> declareInfoMessageList;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintdeclareinfo);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("当天联检状态信息");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppIntDeclareInfoHomeActivity.this, AppIntDeclareInfoSearchActivity.class);
                startActivity(intent);
            }
        });

        // 隐藏PB和没有数据信息
        declareInfoDeatilPb.setVisibility(View.GONE);
        declareInfoShowTv.setVisibility(View.GONE);

        loadingDialog = new LoadingDialog(this);

        declareRefresh.disableScroolUp();

        currentTime = DateUtils.getTodayDateTime();
        xml = HttpIntDeclareInfo.getIntDeclareInfoXml("", currentTime, currentTime);
        Map<String, String> params = new HashMap<>();
        params.put("dXml", xml);
        params.put("ErrString", "");
        loadingDialog.show();
        HttpRoot.getInstance().requstAync(AppIntDeclareInfoHomeActivity.this, HttpCommons.CGO_GET_EXPORT_DECLARE_INFO_OF_ALL_NAME,
                HttpCommons.CGO_GET_EXPORT_DECLARE_INFO_OF_ALL_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        declareInfoMessageList = PrepareDeclareInfoMessage.pullDeclareInfoXml(xmls);
                        if (declareInfoMessageList != null && declareInfoMessageList.size() >= 1) {
                            declareInfoAdapter = new DeclareInfoAdapter(AppIntDeclareInfoHomeActivity.this, declareInfoMessageList);
                            declareInfoListview.setAdapter(declareInfoAdapter);
                        } else {
                            declareInfoShowTv.setVisibility(View.VISIBLE);
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

        declareInfoListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DeclareInfoMessage declareInfoMessage = (DeclareInfoMessage) declareInfoAdapter.getItem(position);
                Intent intent = new Intent(AppIntDeclareInfoHomeActivity.this, AppIntDeclareInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.DECLAREINFO_DEATIL, declareInfoMessage);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 下拉刷新
        declareRefresh.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("dXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppIntDeclareInfoHomeActivity.this, HttpCommons.CGO_GET_EXPORT_DECLARE_INFO_OF_ALL_NAME,
                        HttpCommons.CGO_GET_EXPORT_DECLARE_INFO_OF_ALL_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                declareInfoMessageList = PrepareDeclareInfoMessage.pullDeclareInfoXml(xmls);
                                if (declareInfoMessageList != null && declareInfoMessageList.size() >= 1) {
                                    declareInfoAdapter = new DeclareInfoAdapter(AppIntDeclareInfoHomeActivity.this, declareInfoMessageList);
                                    declareInfoListview.setAdapter(declareInfoAdapter);
                                } else {
                                    declareInfoShowTv.setVisibility(View.VISIBLE);
                                }
                                declareRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                declareRefresh.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                declareRefresh.onHeaderRefreshComplete();
                            }
                        });
            }
        });

    }


    private class DeclareInfoAdapter extends BaseAdapter {
        private Context context;
        private List<DeclareInfoMessage> declareInfoMessageList;
        public DeclareInfoAdapter(Context context, List<DeclareInfoMessage> declareInfoMessageList) {
            this.context = context;
            this.declareInfoMessageList = declareInfoMessageList;
        }

        @Override
        public int getCount() {
            return declareInfoMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return declareInfoMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.declare_info_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.declare_info_mavb_tv);
                viewHolder.opDateTv = (TextView) convertView.findViewById(R.id.declare_info_opdate_tv);
                viewHolder.ciqstatusTv = (TextView) convertView.findViewById(R.id.declare_info_ciqstatus_tv);
                viewHolder.cmdstatusTv = (TextView) convertView.findViewById(R.id.declare_info_cmdstatus_tv);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.vs_ly);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mawb = declareInfoMessageList.get(position).getMawb();
            if (!mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb);
            } else {
                viewHolder.mawbTv.setText("");
            }

            String ciqstatus = declareInfoMessageList.get(position).getCIQStatus();
            if (!ciqstatus.equals("")) {
                viewHolder.ciqstatusTv.setText(ciqstatus);
            } else {
                viewHolder.ciqstatusTv.setText("");
            }

            String cmdstatus = declareInfoMessageList.get(position).getCMDStatus();
            if (!cmdstatus.equals("")) {
                viewHolder.cmdstatusTv.setText(cmdstatus);
            } else {
                viewHolder.cmdstatusTv.setText("");
            }

            String opDate = declareInfoMessageList.get(position).getOPDate();
            if (!opDate.equals("")) {
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

        class ViewHolder {
            // 主单号
            TextView mawbTv;

            // 商检指令
            TextView ciqstatusTv;

            // 海关指令
            TextView cmdstatusTv;

            // 入库日期
            TextView opDateTv;

            LinearLayout showLy;

        }
    }
}
