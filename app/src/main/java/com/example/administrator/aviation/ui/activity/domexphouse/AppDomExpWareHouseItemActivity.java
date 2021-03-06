package com.example.administrator.aviation.ui.activity.domexphouse;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.example.administrator.aviation.model.house.PrepareceWhsInfo;
import com.example.administrator.aviation.model.house.WhsInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 显示house订单列表
 */

public class AppDomExpWareHouseItemActivity extends Activity{
    private String xml;
    private String searchXml;
    private ListView houseLv;
    private ProgressBar housePb;
    private TextView showTv;

    private List<WhsInfo> list;
    private HouseAdapter mHouseAdapter;

    private PullToRefreshView pullToRefreshView;


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
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.refresh_apphouse);

        // 下拉刷新
        pullToRefreshView.disableScroolUp();

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

        // 下拉刷新
        searchXml = getIntent().getStringExtra("xml");
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("whsXml",searchXml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppDomExpWareHouseItemActivity.this, HttpCommons.HOUSE_SEARCH_METHOD_NAME,
                        HttpCommons.HOUSE_SEARCH_METHOD_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                list = PrepareceWhsInfo.parseWhsInfoXml(xmls);
                                if (list != null && list.size() >= 1) {
                                    mHouseAdapter = new HouseAdapter(list, AppDomExpWareHouseItemActivity.this);
                                    houseLv.setAdapter(mHouseAdapter);
                                } else {
                                    showTv.setVisibility(View.VISIBLE);
                                }
                                pullToRefreshView.onHeaderRefreshComplete();

                            }

                            @Override
                            public void onFailed(String message) {
                                pullToRefreshView.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                pullToRefreshView.onHeaderRefreshComplete();
                            }
                        });

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
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.vs_ly);
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
            if (fdate != null && !fdate.equals("")) {
                viewHolder.fdateTv.setText(fdate);
            } else {
                viewHolder.fdateTv.setText("");
            }

            String fno = list.get(position).getFno();
            if (fno != null && !fno.equals("")) {
                viewHolder.fnoTv.setText(fno);
            } else {
                viewHolder.fnoTv.setText("");
            }

            String opdate = list.get(position).getOPDate();
            if (opdate != null && !opdate.equals("")) {
                viewHolder.opdateTv.setText(opdate);
            } else {
                viewHolder.opdateTv.setText("");
            }

            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

         class ViewHolder{
             TextView mavbTv;
             TextView fdateTv;
             TextView fnoTv;
             TextView opdateTv;
             LinearLayout showLy;
        }
    }

}
