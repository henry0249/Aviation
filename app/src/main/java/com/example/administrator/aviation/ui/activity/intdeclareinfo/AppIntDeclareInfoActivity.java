package com.example.administrator.aviation.ui.activity.intdeclareinfo;

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
import com.example.administrator.aviation.model.intdeclareinfo.DeclareInfoMessage;
import com.example.administrator.aviation.model.intdeclareinfo.PrepareDeclareInfoMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

/**
 * 联检状态主页显示
 */

public class AppIntDeclareInfoActivity extends Activity{
    private List<DeclareInfoMessage> declareInfoMessageList;
    private ListView declareInfoLv;
    private TextView showTv;
    private ProgressBar progressBar;

    private String xml;
    private DeclareInfoAdapter declareInfoAdapter;

    // 接收消息隐藏加载图标
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AviationCommons.INT_DECLARE_INFO) {
                declareInfoAdapter = new DeclareInfoAdapter(AppIntDeclareInfoActivity.this, declareInfoMessageList);
                declareInfoLv.setAdapter(declareInfoAdapter);
                progressBar.setVisibility(View.GONE);
                if (declareInfoMessageList.size() >= 1) {
                    showTv.setVisibility(View.GONE);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintdeclareinfo);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("联检状态信息");
        navBar.hideRight();
        declareInfoLv = (ListView) findViewById(R.id.declare_info_listview);
        showTv = (TextView) findViewById(R.id.declare_info_show_tv);
        progressBar = (ProgressBar) findViewById(R.id.declare_info_deatil_pb);

        new Thread() {
            @Override
            public void run() {
                super.run();
                xml = getIntent().getStringExtra(AviationCommons.DECLARE_INFO_DEATIL);
                declareInfoMessageList = PrepareDeclareInfoMessage.pullDeclareInfoXml(xml);
                mHandler.sendEmptyMessage(AviationCommons.INT_DECLARE_INFO);
            }
        }.start();

        declareInfoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                DeclareInfoMessage declareInfoMessage = (DeclareInfoMessage) declareInfoAdapter.getItem(position);
                Intent intent = new Intent(AppIntDeclareInfoActivity.this, AppIntDeclareInfoDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.DECLAREINFO_DEATIL, declareInfoMessage);
                intent.putExtras(bundle);
                startActivity(intent);
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

        }
    }
}
