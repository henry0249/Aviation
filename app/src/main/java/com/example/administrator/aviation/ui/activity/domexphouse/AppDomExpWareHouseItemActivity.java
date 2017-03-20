package com.example.administrator.aviation.ui.activity.domexphouse;

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
import com.example.administrator.aviation.model.house.PrepareceWhsInfo;
import com.example.administrator.aviation.model.house.WhsInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

/**
 * 显示house订单列表
 */

public class AppDomExpWareHouseItemActivity extends Activity{
    private String xml;
    private ListView houseLv;
    private ProgressBar housePb;
    private TextView showTv;

    private List<WhsInfo> list;
    private HouseAdapter mHouseAdapter;


    // 接收消息隐藏加载图标
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == AviationCommons.HOUSE_HANDLER) {
                housePb.setVisibility(View.GONE);
                if (list.size() >= 1) {
                    showTv.setVisibility(View.GONE);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphouseitem);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle(R.string.house_guonei_ruku_detail);
        houseLv = (ListView) findViewById(R.id.house_listview);
        housePb = (ProgressBar) findViewById(R.id.house_pb);
        showTv = (TextView) findViewById(R.id.house_show_tv);

        // 解析house的xml数据
        new Thread() {
            @Override
            public void run() {
                xml = getIntent().getStringExtra("houseXml");
                list = PrepareceWhsInfo.parseWhsInfoXml(xml);
                mHouseAdapter = new HouseAdapter(list, AppDomExpWareHouseItemActivity.this);
                houseLv.setAdapter(mHouseAdapter);
                mHandler.sendEmptyMessage(AviationCommons.HOUSE_HANDLER);
            }
        }.start();

        // listView单机跳转到详情界面
        houseLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WhsInfo whsInfo = mHouseAdapter.getItem(position);
                Intent intent = new Intent(AppDomExpWareHouseItemActivity.this, AppDomExpWareHouseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.HOUSE_ITEM_INFO, whsInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    private class HouseAdapter extends BaseAdapter {
        private List<WhsInfo> list;
        private Context context;
        public HouseAdapter(List<WhsInfo> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public WhsInfo getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.house_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mavbTv = (TextView) convertView.findViewById(R.id.house_mavb_tv);
                viewHolder.fdateTv = (TextView) convertView.findViewById(R.id.house_fdate_tv);
                viewHolder.fnoTv = (TextView) convertView.findViewById(R.id.house_fno_tv);
                viewHolder.opdateTv = (TextView) convertView.findViewById(R.id.house_opdate_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mavb = list.get(position).getMawb();
            if (mavb==null || mavb.equals("")) {
                viewHolder.mavbTv.setText("无数据");
            } else {
                viewHolder.mavbTv.setText(mavb);
            }

            String fdate = list.get(position).getFdate();
            viewHolder.fdateTv.setText(fdate);

            String fno = list.get(position).getFno();
            viewHolder.fnoTv.setText(fno);

            String opdate = list.get(position).getOPDate();
            viewHolder.opdateTv.setText(opdate);
            return convertView;
        }

         class ViewHolder{
             TextView mavbTv;
             TextView fdateTv;
             TextView fnoTv;
             TextView opdateTv;
        }
    }

}
