package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intimpcargoinfo.CargoInfoMessage;
import com.example.administrator.aviation.model.intimpcargoinfo.PrepareCargoInfoMessage;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 进港货站信息主界面
 */

public class AppIntimpCarGoInfoItemActivity extends Activity{
    // 用户信息
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

    private ListView impCarGoLv;

    private List<CargoInfoMessage> cargoInfoMessageList;
    private ImpCargoAdapter impCargoAdapter;
    private TextView nodateTv;
    private ProgressBar carGoPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimpcargoinfoitem);
        initView();

        cargoInfoMessageList = new ArrayList<>();
    }


    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("进港货站信息列表");
        navBar.hideRight();

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        impCarGoLv = (ListView) findViewById(R.id.impcargoinfo_lv);
        nodateTv = (TextView) findViewById(R.id.impcargoinfo_nodata_tv);
        carGoPb = (ProgressBar) findViewById(R.id.impcargoinfo_pb);

        // 进港货站信息列表进入详情页
        impCarGoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CargoInfoMessage cargoInfoMessage = (CargoInfoMessage) impCargoAdapter.getItem(position);
                String businessType = ((CargoInfoMessage) impCargoAdapter.getItem(position)).getBusinessType();
                String hawbID = ((CargoInfoMessage) impCargoAdapter.getItem(position)).getAwbID();
                Intent intent = new Intent(AppIntimpCarGoInfoItemActivity.this, AppIntimpCarGoInfoItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.IMP_CARGO_INFO_ITEM, cargoInfoMessage);
                bundle.putString(AviationCommons.IMP_CARGO_INFO_BUSINESSTYPE, businessType);
                bundle.putString(AviationCommons.IMP_CARGO_INFO_HAWBID, hawbID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 得到数据
        new GetImpCargoListAsyTask().execute();
    }

    // 得到进港货站信息
    private class GetImpCargoListAsyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // 进港货站信息传回的xml
            String cargoXml = getIntent().getStringExtra(AviationCommons.IMP_CARGO_INFO);
            cargoInfoMessageList = PrepareCargoInfoMessage.pullCargoInfoXml(cargoXml);
            return cargoXml;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            impCargoAdapter = new ImpCargoAdapter(AppIntimpCarGoInfoItemActivity.this, cargoInfoMessageList);
            impCarGoLv.setAdapter(impCargoAdapter);
            if (cargoInfoMessageList.size() <= 0) {
                nodateTv.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ImpCargoAdapter extends BaseAdapter {
        private Context context;
        private List<CargoInfoMessage> cargoInfoMessageList;
        public ImpCargoAdapter(Context context, List<CargoInfoMessage> cargoInfoMessageList) {
            this.context = context;
            this.cargoInfoMessageList = cargoInfoMessageList;
        }

        @Override
        public int getCount() {
            return cargoInfoMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return cargoInfoMessageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.impcargoinfo_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.impcargoinfo_mawb_tv);
                viewHolder.hnoTv = (TextView) convertView.findViewById(R.id.impcargoinfo_hno_tv);
                viewHolder.mftStatusTv = (TextView) convertView.findViewById(R.id.impcargoinfo_mftstatus_tv);
                viewHolder.tallyStatusTv = (TextView) convertView.findViewById(R.id.impcargoinfo_tallystatus_tv);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.impcargoinfo_checkbox);
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            Toast.makeText(AppIntimpCarGoInfoItemActivity.this, "选中了", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AppIntimpCarGoInfoItemActivity.this, "微操作", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mawb = cargoInfoMessageList.get(position).getMawb();
            if (!mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb);
            } else {
                viewHolder.mawbTv.setText("");
            }

            String hno = cargoInfoMessageList.get(position).getHno();
            if (!hno.equals("")) {
                viewHolder.hnoTv.setText(hno);
            } else {
                viewHolder.hnoTv.setText("");
            }

            String mftStatus = cargoInfoMessageList.get(position).getMftStatus();
            if (!mftStatus.equals("")) {
                viewHolder.mftStatusTv.setText(mftStatus);
            } else {
                viewHolder.mftStatusTv.setText("");
            }

            String tallyStatus = cargoInfoMessageList.get(position).getTallyStatus();
            if (!tallyStatus.equals("")) {
                viewHolder.tallyStatusTv.setText(tallyStatus);
            } else {
                viewHolder.tallyStatusTv.setText("");
            }
            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            TextView mawbTv;
            TextView hnoTv;
            TextView mftStatusTv;
            TextView tallyStatusTv;
        }
    }
}
