package com.example.administrator.aviation.ui.activity.intawbofprepare;

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
import com.example.administrator.aviation.http.getIntawbofprepare.HttpGetIntExportAwbOfPrepare;
import com.example.administrator.aviation.model.intawbprepare.Hawb;
import com.example.administrator.aviation.model.intawbprepare.MawbInfo;
import com.example.administrator.aviation.model.intawbprepare.PrepareIntAwbInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 获取国际预录入订单列表
 */

public class AppIntExpPrepareAWBActivity extends Activity{
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

    private List<MawbInfo> groupList;
    private ExpandableListView listView;
    private ExpandableAdapter expandableAdapter;
    private PullToRefreshView pullToRefreshView;

    private ProgressBar intawbProgressBar;
    private TextView intawbLoadTv;
    private TextView intawbNoneDataTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexpprepareawb);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle(R.string.int_prepare_title);
        navBar.showRight();
        navBar.setRight(R.drawable.add);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppIntExpPrepareAWBActivity.this, AppIntExpGroupAddActivity.class);
                startActivity(intent);
            }
        });

        listView = (ExpandableListView) findViewById(R.id.int_exp_listview);
        intawbProgressBar = (ProgressBar) findViewById(R.id.int_awb_pb);
        intawbLoadTv = (TextView) findViewById(R.id.int_awb_load_tv);
        intawbNoneDataTv = (TextView) findViewById(R.id.int_awb_house_load_tv);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.awb_refresh);
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        // 下拉刷新
        pullToRefreshView.disableScroolUp();
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Mawb", "");
                params.put("Hno", "");
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppIntExpPrepareAWBActivity.this, HttpCommons.GET_INT_METHOD_NAME,
                        HttpCommons.GET_INT_METHOD_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                groupList = PrepareIntAwbInfo.parseAwbInfoXml(xmls);
                                if (groupList != null && groupList.size() >= 1) {
                                    expandableAdapter = new ExpandableAdapter(AppIntExpPrepareAWBActivity.this);
                                    listView.setAdapter(expandableAdapter);
                                } else {
                                    intawbNoneDataTv.setVisibility(View.VISIBLE);
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

        new GetIntPrepareAsync().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetIntPrepareAsync().execute();
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

    // 得到数据的异步请求
    class GetIntPrepareAsync extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpGetIntExportAwbOfPrepare.getIntExportAwbOfPrepareInfo(userBumen, userName, userPass, loginFlag);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                String result = object.getProperty(0).toString();
                if (result.equals("anyType{}")) {
                    ErrString = object.getProperty(1).toString();
                    return result;
                } else {
                    result = object.getProperty(0).toString();
                    groupList = PrepareIntAwbInfo.parseAwbInfoXml(result);
                    return result;
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result == null && !ErrString.equals("")) {
                Toast.makeText(AppIntExpPrepareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
                intawbProgressBar.setVisibility(View.GONE);
                intawbLoadTv.setVisibility(View.GONE);
                intawbNoneDataTv.setVisibility(View.GONE);
            } else if (result.equals("anyType{}") && !ErrString.equals("")) {
                Toast.makeText(AppIntExpPrepareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
                intawbProgressBar.setVisibility(View.GONE);
                intawbLoadTv.setVisibility(View.GONE);
                intawbNoneDataTv.setVisibility(View.GONE);
            }else {
                if (groupList == null || groupList.size()<=0) {
                    intawbNoneDataTv.setVisibility(View.VISIBLE);
                    intawbProgressBar.setVisibility(View.GONE);
                    intawbLoadTv.setVisibility(View.GONE);
                } else {
                    expandableAdapter = new ExpandableAdapter(AppIntExpPrepareAWBActivity.this);
                    listView.setAdapter(expandableAdapter);
                    intawbProgressBar.setVisibility(View.GONE);
                    intawbLoadTv.setVisibility(View.GONE);
                    intawbNoneDataTv.setVisibility(View.GONE);
                }
            }
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
            GroupViewHolder groupViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.int_group_item, parent, false);
                groupViewHolder = new GroupViewHolder();
                groupViewHolder.groupMawbIdTv = (TextView) convertView.findViewById(R.id.int_group_mawb_tv);
                groupViewHolder.groupPc = (TextView) convertView.findViewById(R.id.int_group_pc_tv);
                groupViewHolder.groupWeight = (TextView) convertView.findViewById(R.id.int_group_weight_tv);
                groupViewHolder.groupVolume = (TextView) convertView.findViewById(R.id.int_group_volume_tv);
                groupViewHolder.groupImage = (ImageView) convertView.findViewById(R.id.int_group_imageview);
                groupViewHolder.groupDeatilTv = (TextView) convertView.findViewById(R.id.int_group_detail_tev);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
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

            groupViewHolder.groupDeatilTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AppIntExpPrepareAWBActivity.this, AppIntExpGroupActivity.class);
                    Bundle bundle = new Bundle();
                    MawbInfo mawbInfo = groupList.get(groupPosition);
                    bundle.putSerializable(AviationCommons.INT_GROUP_INFO, mawbInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            groupViewHolder.groupImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AppIntExpPrepareAWBActivity.this, AppIntExpGroupActivity.class);
                    Bundle bundle = new Bundle();
                    MawbInfo mawbInfo = groupList.get(groupPosition);
                    bundle.putSerializable(AviationCommons.INT_GROUP_INFO, mawbInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder childViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(activity).inflate(R.layout.int_child_group, parent, false);
                childViewHolder = new ChildViewHolder();
                childViewHolder.childText = (TextView) convertView.findViewById(R.id.int_child_hawbid_tv);
                childViewHolder.childPC = (TextView) convertView.findViewById(R.id.int_child_pc_tv);
                childViewHolder.childWeight = (TextView) convertView.findViewById(R.id.int_child_weight_tv);
                childViewHolder.childVolume = (TextView) convertView.findViewById(R.id.int_child_volume_tv);
                childViewHolder.childLayout = (LinearLayout) convertView.findViewById(R.id.int_child_layout);
                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
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
                    Intent intent = new Intent(AppIntExpPrepareAWBActivity.this, AppIntExpChildActivity.class);
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
            TextView groupPc;
            TextView groupWeight;
            TextView groupVolume;
            ImageView groupImage;
            TextView groupDeatilTv;
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
