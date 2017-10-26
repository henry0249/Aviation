package com.example.administrator.aviation.ui.activity.intimpcargoinfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoInfo;
import com.example.administrator.aviation.http.getintimpcargoinfo.HttpPrepareImpCargoShenBao;
import com.example.administrator.aviation.model.intimpcargoinfo.CargoInfoMessage;
import com.example.administrator.aviation.model.intimpcargoinfo.PrepareCargoInfoMessage;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.ui.dialog.LoadingDialog;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;
import com.example.administrator.aviation.util.PullToRefreshView;

import org.ksoap2.serialization.SoapObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 进港货站信息主界面
 */

public class AppIntimpCarGoInfoHomeActivity extends Activity implements View.OnClickListener{
    // 用户信息
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;

    private ListView impCarGoLv;

    private List<String> mawbList;
    private List<CargoInfoMessage> cargoInfoMessageList;
    private String mawb;

    private ImpCargoAdapter impCargoAdapter;
    private TextView nodateTv;
    private ProgressBar carGoPb;
    private Button shenbaoBtn;

    private PullToRefreshView pullToRefreshView;

    private Map<String, CargoInfoMessage> checkedDeclareMap;
    private Map<Integer, CargoInfoMessage> rearchIdMap;

    private String currentTime;

    private String xml;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intimpcargoinfoitem);
        initView();

        checkedDeclareMap = new HashMap<>();
        rearchIdMap = new HashMap<>();

        // 初始化xml的list
        mawbList = new ArrayList<>();

        cargoInfoMessageList = new ArrayList<>();
    }


    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("当天进港货站信息列表");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppIntimpCarGoInfoHomeActivity.this, AppIntimpCargoInfoActivity.class);
                startActivity(intent);
            }
        });

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        loadingDialog = new LoadingDialog(this);

        // 获取当前时间
        currentTime = DateUtils.getTodayDateTime();
        xml = HttpPrepareImpCargoInfo.getImpCargoXml("", "",currentTime, currentTime);

        impCarGoLv = (ListView) findViewById(R.id.impcargoinfo_lv);
        nodateTv = (TextView) findViewById(R.id.impcargoinfo_nodata_tv);
        carGoPb = (ProgressBar) findViewById(R.id.impcargoinfo_pb);

        shenbaoBtn = (Button) findViewById(R.id.impcargoinfo_zhixian_shenbao_btn);
        shenbaoBtn.setOnClickListener(this);

        pullToRefreshView = (PullToRefreshView) findViewById(R.id.go_refresh);
        pullToRefreshView.disableScroolUp();



        // 进港货站信息列表进入详情页
        impCarGoLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CargoInfoMessage cargoInfoMessage = (CargoInfoMessage) impCargoAdapter.getItem(position);
                String businessType = ((CargoInfoMessage) impCargoAdapter.getItem(position)).getBusinessType();
                String hawbID = ((CargoInfoMessage) impCargoAdapter.getItem(position)).getAwbID();
                Intent intent = new Intent(AppIntimpCarGoInfoHomeActivity.this, AppIntimpCarGoInfoItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.IMP_CARGO_INFO_ITEM, cargoInfoMessage);
                bundle.putString(AviationCommons.IMP_CARGO_INFO_BUSINESSTYPE, businessType);
                bundle.putString(AviationCommons.IMP_CARGO_INFO_HAWBID, hawbID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppIntimpCarGoInfoHomeActivity.this, HttpCommons.CGO_GET_INT_IMPORT_CARGO_INFOMATION_NAME,
                        HttpCommons.CGO_GET_INT_IMPORT_CARGO_INFOMATION_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                cargoInfoMessageList = PrepareCargoInfoMessage.pullCargoInfoXml(xmls);
                                if (cargoInfoMessageList != null && cargoInfoMessageList.size() >= 1) {
                                    impCargoAdapter = new ImpCargoAdapter(AppIntimpCarGoInfoHomeActivity.this, cargoInfoMessageList);
                                    impCarGoLv.setAdapter(impCargoAdapter);
                                } else {
                                    nodateTv.setVisibility(View.VISIBLE);
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

        // 首次进入页面加载数据
        loadingDialog.show();
        Map<String, String> params = new HashMap<>();
        params.put("awbXml", xml);
        params.put("ErrString", "");
        HttpRoot.getInstance().requstAync(AppIntimpCarGoInfoHomeActivity.this, HttpCommons.CGO_GET_INT_IMPORT_CARGO_INFOMATION_NAME,
                HttpCommons.CGO_GET_INT_IMPORT_CARGO_INFOMATION_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        cargoInfoMessageList = PrepareCargoInfoMessage.pullCargoInfoXml(xmls);
                        if (cargoInfoMessageList != null && cargoInfoMessageList.size() >= 1) {
                            impCargoAdapter = new ImpCargoAdapter(AppIntimpCarGoInfoHomeActivity.this, cargoInfoMessageList);
                            impCarGoLv.setAdapter(impCargoAdapter);
                        } else {
                            nodateTv.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 分单申报
            case R.id.impcargoinfo_zhixian_shenbao_btn:
                mawbList.clear();
                Set<Map.Entry<String, CargoInfoMessage>> entries = checkedDeclareMap.entrySet();
                for (Map.Entry<String, CargoInfoMessage> entry : entries) {
                    CargoInfoMessage cargoInfoMessage = checkedDeclareMap.get(entry.getKey());
                    mawb = cargoInfoMessage.getMawb();
                    mawbList.add(mawb);
                    new CarGoInfoAsyTask(mawb).execute();
                }
                if (mawbList.size() <= 0 ) {
                    Toast.makeText(AppIntimpCarGoInfoHomeActivity.this, "没有申报项",Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
                break;

            default:
                break;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            final CargoInfoMessage cargoInfoMessage = (CargoInfoMessage) getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.impcargoinfo_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mawbTv = (TextView) convertView.findViewById(R.id.impcargoinfo_mawb_tv);
                viewHolder.hnoTv = (TextView) convertView.findViewById(R.id.impcargoinfo_hno_tv);
                viewHolder.mftStatusTv = (TextView) convertView.findViewById(R.id.impcargoinfo_mftstatus_tv);
                viewHolder.tallyStatusTv = (TextView) convertView.findViewById(R.id.impcargoinfo_tallystatus_tv);
                viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.impcargoinfo_checkbox);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.awb_linear_layout);
                viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        if (isChecked) {
                            checkedDeclareMap.put(cargoInfoMessage.getMawb(), cargoInfoMessage);
                            rearchIdMap.put(position, cargoInfoMessage);
                        } else {
                            if (checkedDeclareMap.get(cargoInfoMessage.getMawb()) != null) {
                                checkedDeclareMap.remove(cargoInfoMessage.getMawb());
                            }
                            rearchIdMap.remove(position);
                        }
                    }
                });
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String mawb = cargoInfoMessageList.get(position).getMawb();
            if (mawb != null && !mawb.equals("")) {
                viewHolder.mawbTv.setText(mawb);
            } else {
                viewHolder.mawbTv.setText("");
            }

            String hno = cargoInfoMessageList.get(position).getHno();
            if (hno != null && !hno.equals("")) {
                viewHolder.hnoTv.setText(hno);
            } else {
                viewHolder.hnoTv.setText("");
            }

            String mftStatus = cargoInfoMessageList.get(position).getMftStatus();
            if (mftStatus != null && !mftStatus.equals("")) {
                viewHolder.mftStatusTv.setText(mftStatus);
            } else {
                viewHolder.mftStatusTv.setText("");
            }

            String tallyStatus = cargoInfoMessageList.get(position).getTallyStatus();
            if (tallyStatus != null && !tallyStatus.equals("")) {
                viewHolder.tallyStatusTv.setText(tallyStatus);
            } else {
                viewHolder.tallyStatusTv.setText("");
            }
            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            TextView mawbTv;
            TextView hnoTv;
            TextView mftStatusTv;
            TextView tallyStatusTv;
            LinearLayout showLy;
        }
    }

    // 分单申报异步任务
    private class CarGoInfoAsyTask extends AsyncTask<Void, Void, String> {
        String result = null;
        String mawb = null;

        public CarGoInfoAsyTask(String mawb) {
            this.mawb = mawb;
        }

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpPrepareImpCargoShenBao.shenBaoCargo(userBumen, userName, userPass, loginFlag, mawb);
            if (object == null) {
                ErrString = "服务器响应失败";
                return null;
            } else {
                result = object.getProperty(0).toString();
                if (result.equals("false")) {
                    ErrString = object.getProperty(1).toString();
                    return result;
                }
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (result == null && !ErrString.equals("")) {
                Toast.makeText(AppIntimpCarGoInfoHomeActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("")) {
                Toast.makeText(AppIntimpCarGoInfoHomeActivity.this, "单号:" + mawb + ErrString, Toast.LENGTH_LONG).show();
            }
        }
    }
}
