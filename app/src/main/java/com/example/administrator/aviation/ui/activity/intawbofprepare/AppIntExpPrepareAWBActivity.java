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
import com.example.administrator.aviation.http.getIntawbofprepare.HttpGetIntExportAwbOfPrepare;
import com.example.administrator.aviation.model.intawbprepare.Hawb;
import com.example.administrator.aviation.model.intawbprepare.MawbInfo;
import com.example.administrator.aviation.model.intawbprepare.PrepareIntAwbInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

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
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

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
                if (result.equals("false")) {
                    ErrString = object.getProperty(2).toString();
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
            } else {
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
                groupViewHolder.groupImage = (ImageView) convertView.findViewById(R.id.int_group_imageview);
                groupViewHolder.groupDeatilTv = (TextView) convertView.findViewById(R.id.int_group_detail_tev);
                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }
            groupViewHolder.groupMawbIdTv.setText(groupList.get(groupPosition).getMawb());
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
            ImageView groupImage;
            TextView groupDeatilTv;
        }
        class ChildViewHolder{
            TextView childText;
            LinearLayout childLayout;
        }
    }
}
