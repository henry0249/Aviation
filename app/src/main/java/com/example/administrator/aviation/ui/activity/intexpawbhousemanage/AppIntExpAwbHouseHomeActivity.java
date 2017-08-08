package com.example.administrator.aviation.ui.activity.intexpawbhousemanage;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.http.intExportawbofwarehouse.HttpIntawbPrepareHouse;
import com.example.administrator.aviation.model.intawbprepare.Hawb;
import com.example.administrator.aviation.model.intawbprepare.MawbInfo;
import com.example.administrator.aviation.model.intawbprepare.PrepareIntAwbInfo;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.activity.intawbofprepare.AppIntExpChildActivity;
import com.example.administrator.aviation.ui.activity.intawbofprepare.AppIntExpGroupActivity;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.administrator.aviation.R.id.int_group_detail_tev;

/**
 * 国际出港入库管理it
 */

public class AppIntExpAwbHouseHomeActivity extends Activity{

    private List<MawbInfo> groupList;
    private ExpandableListView listView;
    private ExpandableAdapter expandableAdapter;

    private ProgressBar intawbProgressBar;
    private TextView intawbLoadTv;

    // 提示没有数据信息
    private TextView showDataTv;

    private PullToRefreshView pullToRefreshView;

    private LoadingDialog loadingDialog;

    private String xml;
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexpprepareawb);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("当天出港入库信息");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppIntExpAwbHouseHomeActivity.this, AppIntExpAWBHouseManageActivity.class);
                startActivity(intent);
            }
        });


        listView = (ExpandableListView) findViewById(R.id.int_exp_listview);
        intawbProgressBar = (ProgressBar) findViewById(R.id.int_awb_pb);
        intawbLoadTv = (TextView) findViewById(R.id.int_awb_load_tv);
        showDataTv = (TextView) findViewById(R.id.int_awb_house_load_tv);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.awb_refresh);

        loadingDialog = new LoadingDialog(this);

        intawbLoadTv.setVisibility(View.GONE);
        intawbProgressBar.setVisibility(View.GONE);
        showDataTv.setVisibility(View.GONE);

        // 下拉刷新
        pullToRefreshView.disableScroolUp();
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<String, String>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppIntExpAwbHouseHomeActivity.this, HttpCommons.GET_INT_WARE_HOUSE_NAME,
                        HttpCommons.GET_INT_WARE_HOUSE_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                groupList = PrepareIntAwbInfo.parseAwbInfoXml(xmls);
                                if (groupList != null && groupList.size() >= 1) {
                                    expandableAdapter = new ExpandableAdapter(AppIntExpAwbHouseHomeActivity.this);
                                    listView.setAdapter(expandableAdapter);
                                } else {
                                    showDataTv.setVisibility(View.VISIBLE);
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


        // 首次进入加载数据
        currentTime = DateUtils.getTodayDateTime();
        xml = HttpIntawbPrepareHouse.getIntWareHouseXml("", currentTime, currentTime);
        Map<String, String> params = new HashMap<String, String>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        loadingDialog.show();
        HttpRoot.getInstance().requstAync(AppIntExpAwbHouseHomeActivity.this, HttpCommons.GET_INT_WARE_HOUSE_NAME,
                HttpCommons.GET_INT_WARE_HOUSE_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        groupList = PrepareIntAwbInfo.parseAwbInfoXml(xmls);
                        if (groupList != null && groupList.size() >= 1) {
                            expandableAdapter = new ExpandableAdapter(AppIntExpAwbHouseHomeActivity.this);
                            listView.setAdapter(expandableAdapter);
                        } else {
                            showDataTv.setVisibility(View.VISIBLE);
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

    }

    // 重新进入此界面重新从服务器获取数据（每次修改完数据信息调用此方法重新获取数据）
    @Override
    protected void onResume() {
        super.onResume();
        currentTime = DateUtils.getTodayDateTime();
        xml = HttpIntawbPrepareHouse.getIntWareHouseXml("", currentTime, currentTime);
        Map<String, String> params = new HashMap<String, String>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        loadingDialog.show();
        HttpRoot.getInstance().requstAync(AppIntExpAwbHouseHomeActivity.this, HttpCommons.GET_INT_WARE_HOUSE_NAME,
                HttpCommons.GET_INT_WARE_HOUSE_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        groupList = PrepareIntAwbInfo.parseAwbInfoXml(xmls);
                        if (groupList != null && groupList.size() >= 1) {
                            expandableAdapter = new ExpandableAdapter(AppIntExpAwbHouseHomeActivity.this);
                            listView.setAdapter(expandableAdapter);
                        } else {
                            showDataTv.setVisibility(View.VISIBLE);
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
    }

    // 回传参数(接收更新和添加订单传递过来的值更新列表)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 || requestCode == 4 || requestCode == 5 || requestCode == 6
                || requestCode == 7) {
            expandableAdapter.notifyDataSetChanged();
        }
    }


    public class ExpandableAdapter extends BaseExpandableListAdapter {
        Activity activity;
        public ExpandableAdapter (Activity activity) {
            this.activity = activity;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groupList.get(groupPosition).getHawb().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (groupList.get(groupPosition).getHawb() != null && groupList.get(groupPosition).getHawb().size() != 0) {
                return groupList.get(groupPosition).getHawb().size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            ExpandableAdapter.GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.int_group_item, parent, false);
                groupViewHolder = new ExpandableAdapter.GroupViewHolder();
                groupViewHolder.groupMawbIdTv = (TextView) convertView.findViewById(R.id.int_group_mawb_tv);
                groupViewHolder.groupDetailTv = (TextView) convertView.findViewById(int_group_detail_tev);
                groupViewHolder.groupPc = (TextView) convertView.findViewById(R.id.int_group_pc_tv);
                groupViewHolder.groupWeight = (TextView) convertView.findViewById(R.id.int_group_weight_tv);
                groupViewHolder.groupVolume = (TextView) convertView.findViewById(R.id.int_group_volume_tv);
                groupViewHolder.groupImage = (ImageView) convertView.findViewById(R.id.int_group_imageview);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (ExpandableAdapter.GroupViewHolder) convertView.getTag();
            }
            String mawb = groupList.get(groupPosition).getMawb();
            if (mawb != null && !mawb.equals("")) {
                groupViewHolder.groupMawbIdTv.setText(mawb);
            } else {
                groupViewHolder.groupMawbIdTv.setText("");
            }
            String pc = groupList.get(groupPosition).getPC();
            if (pc != null && !pc.equals("")) {
                groupViewHolder.groupPc.setText(pc);
            } else {
                groupViewHolder.groupPc.setText("");
            }
            String weight = groupList.get(groupPosition).getWeight();
            if (weight != null && !weight.equals("")) {
                groupViewHolder.groupWeight.setText(weight);
            } else {
                groupViewHolder.groupWeight.setText("");
            }
            String volume = groupList.get(groupPosition).getVolume();
            if (volume != null && !volume.equals("")) {
                groupViewHolder.groupVolume.setText(volume);
            } else {
                groupViewHolder.groupVolume.setText("");
            }
            groupViewHolder.groupDetailTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AppIntExpAwbHouseHomeActivity.this, AppIntExpGroupActivity.class);
                    Bundle bundle = new Bundle();
                    MawbInfo mawbInfo = groupList.get(groupPosition);
                    bundle.putSerializable(AviationCommons.INT_GROUP_INFO, mawbInfo);
                    bundle.putString(AviationCommons.HIDE_INT_AWB_UPDATE, "hide");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            groupViewHolder.groupImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AppIntExpAwbHouseHomeActivity.this, AppIntExpGroupActivity.class);
                    Bundle bundle = new Bundle();
                    MawbInfo mawbInfo = groupList.get(groupPosition);
                    bundle.putSerializable(AviationCommons.INT_GROUP_INFO, mawbInfo);
                    bundle.putString(AviationCommons.HIDE_INT_AWB_UPDATE, "hide");
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ExpandableAdapter.ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.int_child_group, parent, false);
                childViewHolder = new ExpandableAdapter.ChildViewHolder();
                childViewHolder.childText = (TextView) convertView.findViewById(R.id.int_child_hawbid_tv);
                childViewHolder.childPC = (TextView) convertView.findViewById(R.id.int_child_pc_tv);
                childViewHolder.childWeight = (TextView) convertView.findViewById(R.id.int_child_weight_tv);
                childViewHolder.childVolume = (TextView) convertView.findViewById(R.id.int_child_volume_tv);
                childViewHolder.childLayout = (LinearLayout) convertView.findViewById(R.id.int_child_layout);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ExpandableAdapter.ChildViewHolder) convertView.getTag();
            }
            if (!groupList.get(groupPosition).getHawb().get(childPosition).getHno().equals("")) {
                childViewHolder.childText.setText(groupList.get(groupPosition).getHawb().get(childPosition).getHno());

            } else {
                return null;
            }
            String pc = groupList.get(groupPosition).getHawb().get(childPosition).getPC();
            if (pc != null && !pc.equals("")) {
                childViewHolder.childPC.setText(pc);
            } else {
                childViewHolder.childPC.setText("");
            }
            String weight = groupList.get(groupPosition).getHawb().get(childPosition).getWeight();
            if (weight != null && !weight.equals("")) {
                childViewHolder.childWeight.setText(weight);
            } else {
                childViewHolder.childWeight.setText("");
            }
            String volume = groupList.get(groupPosition).getHawb().get(childPosition).getVolume();
            if (volume != null && !volume.equals("")) {
                childViewHolder.childVolume.setText(volume);
            } else {
                childViewHolder.childVolume.setText("");
            }

            childViewHolder.childLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AppIntExpAwbHouseHomeActivity.this, AppIntExpChildActivity.class);
                    Hawb hawb = groupList.get(groupPosition).getHawb().get(childPosition);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AviationCommons.INT_CHILD_INFO, hawb);
                    bundle.putString("mawbId", groupList.get(groupPosition).getMawbID());
                    bundle.putSerializable(AviationCommons.INT_GROUP_MAWB, groupList.get(groupPosition).getMawb());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            //如果子条目需要响应click事件,必需返回true
            return true;
        }
        class GroupViewHolder{
            TextView groupMawbIdTv;
            TextView groupDetailTv;
            TextView groupPc;
            TextView groupWeight;
            TextView groupVolume;
            ImageView groupImage;
        }
        class ChildViewHolder{
            TextView childText;
            TextView childPC;
            TextView childWeight;
            TextView childVolume;
            LinearLayout childLayout;
        }
    }

}
