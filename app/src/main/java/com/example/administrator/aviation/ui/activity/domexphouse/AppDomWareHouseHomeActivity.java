package com.example.administrator.aviation.ui.activity.domexphouse;

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
import com.example.administrator.aviation.http.house.HttpPrepareHouse;
import com.example.administrator.aviation.model.house.PrepareceWhsInfo;
import com.example.administrator.aviation.model.house.WhsInfo;
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

/**
 * 国内货代出港入库查询
 */

public class AppDomWareHouseHomeActivity extends Activity {
    @BindView(R.id.gnjcg_tv)
    TextView gnjcgTv;
    @BindView(R.id.show_d_ly)
    LinearLayout showDLy;
    @BindView(R.id.house_pb)
    ProgressBar housePb;
    @BindView(R.id.house_show_tv)
    TextView houseShowTv;
    @BindView(R.id.awb_load_tv)
    TextView awbLoadTv;
    @BindView(R.id.house_listview)
    ListView houseListview;
    @BindView(R.id.refresh_apphouse)
    PullToRefreshView refreshApphouse;


    private List<WhsInfo> list;
    private HouseAdapter mHouseAdapter;
    private String currentTime;
    private String xml;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apphouseitem);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("国内出港当天入库列表");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppDomWareHouseHomeActivity.this, AppDomExpWareHouseActivity.class);
                startActivity(intent);
            }
        });
        housePb.setVisibility(View.GONE);
        houseShowTv.setVisibility(View.GONE);
        loadingDialog = new LoadingDialog(this);

        refreshApphouse.disableScroolUp();

        currentTime = DateUtils.getTodayDateTime();
        xml = HttpPrepareHouse.getHouseXml("", currentTime, currentTime, "");
        Map<String, String> params = new HashMap<>();
        params.put("whsXml",xml);
        params.put("ErrString", "");
        loadingDialog.show();
        HttpRoot.getInstance().requstAync(AppDomWareHouseHomeActivity.this, HttpCommons.HOUSE_SEARCH_METHOD_NAME,
                HttpCommons.HOUSE_SEARCH_METHOD_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        list = PrepareceWhsInfo.parseWhsInfoXml(xmls);
                        if (list != null && list.size() >= 1) {
                            mHouseAdapter = new HouseAdapter(list, AppDomWareHouseHomeActivity.this);
                            houseListview.setAdapter(mHouseAdapter);
                        } else {
                            houseShowTv.setVisibility(View.VISIBLE);
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

        // listView单机跳转到详情界面
        houseListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WhsInfo whsInfo = mHouseAdapter.getItem(position);
                Intent intent = new Intent(AppDomWareHouseHomeActivity.this, AppDomExpWareHouseDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.HOUSE_ITEM_INFO, whsInfo);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        refreshApphouse.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                xml = HttpPrepareHouse.getHouseXml("", currentTime, currentTime, "");
                Map<String, String> params = new HashMap<>();
                params.put("whsXml",xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppDomWareHouseHomeActivity.this, HttpCommons.HOUSE_SEARCH_METHOD_NAME,
                        HttpCommons.HOUSE_SEARCH_METHOD_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                list = PrepareceWhsInfo.parseWhsInfoXml(xmls);
                                if (list != null && list.size() >= 1) {
                                    mHouseAdapter = new HouseAdapter(list, AppDomWareHouseHomeActivity.this);
                                    houseListview.setAdapter(mHouseAdapter);
                                } else {
                                    houseShowTv.setVisibility(View.VISIBLE);
                                }
                                refreshApphouse.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onFailed(String message) {
                                refreshApphouse.onHeaderRefreshComplete();
                            }

                            @Override
                            public void onError() {
                                refreshApphouse.onHeaderRefreshComplete();
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
