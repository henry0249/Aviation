package com.example.administrator.aviation.ui.activity.prepareawb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWB;
import com.example.administrator.aviation.http.prepareawb.HttpPrepareAWBDelete;
import com.example.administrator.aviation.model.prepareawb.MawbInfo;
import com.example.administrator.aviation.model.prepareawb.PrepareceAwbInfo;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

/**
 * AppDomExpPrePareAWB功能模块
 */

public class AppDomExpPrePareAWBActivity extends Activity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

    private ListView awbListView;
    private ProgressBar awbProgressBar;
    private TextView awbLoadTv;
    private AwbAdapter awbAdapter;

    private List<MawbInfo> list;
    private String mawb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appdomexpprepareawb);
        initView();
    }
    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.showRight();
        navBar.setRight(R.drawable.add);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AppDomExpPrePareAWBActivity.this, AwbAddActivity.class);
                startActivity(intent);
            }
        });
        navBar.setTitle(R.string.awb_detail);
        awbListView = (ListView) findViewById(R.id.awb_listview);
        awbProgressBar = (ProgressBar) findViewById(R.id.awb_pb);
        awbLoadTv = (TextView) findViewById(R.id.awb_load_tv);
        awbListView.setOnItemClickListener(this);

        // 长按点击删除
        awbListView.setOnItemLongClickListener(this);

        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        new GetPrepareAWB().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetPrepareAWB().execute();
    }

    // 回传参数(接收更新和添加订单传递过来的值更新列表)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 || requestCode == 1) {
            awbAdapter.notifyDataSetChanged();
        }
    }

    // 将list中的每一个点击项的值传递到订单详情界面
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        MawbInfo mawbInfo = awbAdapter.getItem(position);
        Intent intent = new Intent(this, AwbDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AviationCommons.AWB_ITEM_INFO, mawbInfo);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    // 长按每一项实现删除
    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
        mawb = list.get(position).getMawb();
        AlertDialog.Builder builder = new AlertDialog.Builder(AppDomExpPrePareAWBActivity.this);
        builder.setTitle("删除订单")
                .setMessage("确定删除订单号为:"+mawb+"订单吗?")
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new DeletePrepareAWB(position).execute();
                    }
                })
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        builder.create().show();
        return true;
    }

    public class AwbAdapter extends BaseAdapter{
        private List<MawbInfo> awbInfoList;
        private Context context;

        public AwbAdapter(List<MawbInfo> awbInfoList, Context context) {
            this.awbInfoList = awbInfoList;
            this.context = context;
        }
        @Override
        public int getCount() {
            return awbInfoList.size();
        }

        @Override
        public MawbInfo getItem(int position) {
            return awbInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.awb_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.awbLayout = (LinearLayout) convertView.findViewById(R.id.awb_linear_layout);
                viewHolder.mavbTv = (TextView) convertView.findViewById(R.id.awb_mavb_tv);
                viewHolder.destTv = (TextView) convertView.findViewById(R.id.awb_dest_tv);
                viewHolder.pcTv = (TextView) convertView.findViewById(R.id.awb_pc_tv);
                viewHolder.weightTv = (TextView) convertView.findViewById(R.id.awb_weight_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 设置偶数项灰色
//            if (position % 2 == 0) {
//                viewHolder.awbLayout.setBackgroundColor(Color.parseColor("#FFC4C1C1"));
//            } else {
//                viewHolder.awbLayout.setBackgroundColor(Color.WHITE);
//            }

            String mawb = awbInfoList.get(position).getMawb();
            if (!mawb.equals("")) {
                viewHolder.mavbTv.setText(mawb);
            } else {
                viewHolder.mavbTv.setText("");
            }

            // 如果dest1为空就显示dest2
            String dest1 = awbInfoList.get(position).getDest1();
            String dest2 = awbInfoList.get(position).getDest2();
            if (!dest2.equals("")) {
                viewHolder.destTv.setText(dest2);
            } else {
                viewHolder.destTv.setText(dest1);
            }

            // 件数
            String pc = awbInfoList.get(position).getPC();
            if (!pc.equals("")) {
                viewHolder.pcTv.setText(pc);
            } else {
                viewHolder.pcTv.setText("");
            }

            // 重量
            String weight = awbInfoList.get(position).getWeight();
            if (!weight.equals("")) {
                viewHolder.weightTv.setText(weight);
            } else {
                viewHolder.weightTv.setText("");
            }
            return convertView;
        }

        class ViewHolder {
            LinearLayout awbLayout;
            TextView mavbTv;
            TextView destTv;
            TextView pcTv;
            TextView weightTv;
        }
    }

    // 得到数据列表的异步任务
    class GetPrepareAWB extends AsyncTask<Object, Object, String> {
        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareAWB.getAWBInfo(userBumen, userName, userPass, loginFlag);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                String result = object.getProperty(0).toString();
                if (result.equals("False")) {
                    ErrString = object.getProperty(2).toString();
                    return result;
                } else {
                    result = object.getProperty(0).toString();
                    list = PrepareceAwbInfo.parseAwbInfoXml(result);
                    return result;
                }
            }
        }

        @Override
        protected void onPostExecute(String request) {
            if (request == null && !ErrString.equals("")) {
                Toast.makeText(AppDomExpPrePareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("False") && !ErrString.equals("") ) {
                Toast.makeText(AppDomExpPrePareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else {
                if (list == null) {
                    Toast.makeText(AppDomExpPrePareAWBActivity.this, "没有数据", Toast.LENGTH_LONG).show();
                } else {
                    // 加载数据
                    awbAdapter = new AwbAdapter(list, AppDomExpPrePareAWBActivity.this);
                    awbListView.setAdapter(awbAdapter);
                    awbProgressBar.setVisibility(View.GONE);
                    awbLoadTv.setVisibility(View.GONE);
                }
            }
            super.onPostExecute(request);
        }
    }

    // 删除一条订单的异步任务
    class DeletePrepareAWB extends AsyncTask<Object, Object, String> {
        private int position;
        public DeletePrepareAWB(int position) {
            this.position = position;
        }

        @Override
        protected String doInBackground(Object... objects) {
            SoapObject object = HttpPrepareAWBDelete.deleteAWBdetail(userBumen, userName, userPass, loginFlag, mawb);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                String result = object.getProperty(0).toString();
                if (result.equals("false")) {
                    ErrString = object.getProperty(1).toString();
                    return result;
                } else {
                    result = object.getProperty(0).toString();
                    return result;
                }
            }
        }

        @Override
        protected void onPostExecute(String request) {
            if (request == null && !ErrString.equals("")) {
                Toast.makeText(AppDomExpPrePareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (request.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppDomExpPrePareAWBActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else {
                list.remove(position);
                awbAdapter.notifyDataSetChanged();
                Toast.makeText(AppDomExpPrePareAWBActivity.this, "删除成功"+ErrString, Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(request);
        }
    }
}
