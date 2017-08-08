package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

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
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpCGOExportOneKeyDeclare;
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpCGOMergerSubLineArrival;
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpGetIntOneKeyDeclare;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.model.intonekeydeclare.PrepareIntDeclare;
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
 * 预配及运抵列表界面
 */

public class AppIntOneKeyDeclarelHomeActivity extends Activity {
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    // list数据
    private List<Declare> declareList;
    private List<String> mawbList;
    private String mawb;
    private String rearchId;

    private DeclareAdapter declareAdapter;

    // 下拉刷新
    private PullToRefreshView pullToRefreshView;

    private ListView declareLv;
    private TextView nodateTv;
    private ProgressBar zhixianPb;

    private Map<String, Declare> checkedDeclareMap;
    private Map<Integer, Declare> rearchIdMap;

    // 一键申报
    private Button shenbaoBtn;

    // 支线合并
    private Button zhixianHebingBtn;

    private String xml;

    // 获取当前时间
    private String currentTime;

    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexponekeydeclareitem);
        initView();
        checkedDeclareMap = new HashMap<>();
        rearchIdMap = new HashMap<>();

        // 初始化xml的list
        mawbList = new ArrayList<>();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("预配与运抵信息");
        navBar.setRight(R.drawable.search);
        navBar.getRightImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppIntOneKeyDeclarelHomeActivity.this, AppIntExpOneKeyDeclareActivity.class);
                startActivity(intent);
            }
        });

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        loadingDialog = new LoadingDialog(this);

        // 申报
        shenbaoBtn = (Button) findViewById(R.id.shenbai_btn);
        zhixianHebingBtn = (Button) findViewById(R.id.zhixian_hebing_btn);
        zhixianPb = (ProgressBar) findViewById(R.id.zhixian_pb);

        // 得到查询的xml
        currentTime = DateUtils.getTodayDateTime();
        xml = HttpGetIntOneKeyDeclare.getDeclareDetailXml("", currentTime, currentTime);

        pullToRefreshView = (PullToRefreshView) findViewById(R.id.onkey_refresh);
        pullToRefreshView.disableScroolUp();

        declareLv = (ListView) findViewById(R.id.declare_lv);
        nodateTv = (TextView) findViewById(R.id.int_declare_nodata_tv);
        declareLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Declare declare = (Declare) declareAdapter.getItem(position);
                String mawb = ((Declare) declareAdapter.getItem(position)).getMawb();
                String rearchID = ((Declare) declareAdapter.getItem(position)).getRearchID();
                Intent intent = new Intent(AppIntOneKeyDeclarelHomeActivity.this, AppIntOneKeyDeclareItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.DECLARE_INFO, declare);
                bundle.putString(AviationCommons.DECLARE_MAWB, mawb);
                bundle.putString(AviationCommons.DECLARE_REARCHID, rearchID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // 下拉刷新
        pullToRefreshView.setOnHeaderRefreshListener(new PullToRefreshView.OnHeaderRefreshListener() {
            @Override
            public void onHeaderRefresh(PullToRefreshView view) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
                Map<String, String> params = new HashMap<>();
                params.put("dXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppIntOneKeyDeclarelHomeActivity.this, HttpCommons.GET_INT_EXPORT_ONE_KEY_DECLARE_NAME,
                        HttpCommons.GET_INT_EXPORT_ONE_KEY_DECLARE_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
                                declareList = PrepareIntDeclare.parseIntDeclareXml(xmls);
                                if (declareList != null && declareList.size() >= 1) {
                                    declareAdapter = new DeclareAdapter(declareList, AppIntOneKeyDeclarelHomeActivity.this);
                                    declareLv.setAdapter(declareAdapter);
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

        // 一键申报点击事件
        shenbaoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mawbList.clear();
                Set<Map.Entry<String, Declare>> entries = checkedDeclareMap.entrySet();
                for (Map.Entry<String, Declare> entry : entries) {
                    Declare declare = checkedDeclareMap.get(entry.getKey());
                    mawb = declare.getMawb();
                    mawbList.add(mawb);
                    new CgoExportOneKeyDeclareAsyTask(mawb).execute();
                }
                if (mawbList.size() <= 0 ) {
                    Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, "没有申报项",Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
            }
        });

        // 支线合并
        zhixianHebingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zhixianPb.setVisibility(View.VISIBLE);
                mawbList.clear();
                String xml = null;
                Set<Map.Entry<Integer, Declare>> entries = rearchIdMap.entrySet();
                for (Map.Entry<Integer, Declare> entry : entries) {
                    Declare declare = rearchIdMap.get(entry.getKey());
                    rearchId = declare.getRearchID();
                    mawbList.add(rearchId);
                    xml = getRearchID(mawbList);
                }
                if (xml ==  null ) {
                    Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, "请选择合并项",Toast.LENGTH_LONG).show();
                    zhixianPb.setVisibility(View.GONE);
                } else {
                    new ZhixianAsynck(xml).execute();
                }
            }
        });

        // 首次进入加载数据
        loadingDialog.show();

        Map<String, String> params = new HashMap<>();
        params.put("dXml", xml);
        params.put("ErrString", "");
        HttpRoot.getInstance().requstAync(AppIntOneKeyDeclarelHomeActivity.this, HttpCommons.GET_INT_EXPORT_ONE_KEY_DECLARE_NAME,
                HttpCommons.GET_INT_EXPORT_ONE_KEY_DECLARE_ACTION, params, new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        SoapObject object = (SoapObject) result;
                        String xmls = object.getProperty(0).toString();
                        declareList = PrepareIntDeclare.parseIntDeclareXml(xmls);
                        if (declareList != null && declareList.size() >= 1) {
                            declareAdapter = new DeclareAdapter(declareList, AppIntOneKeyDeclarelHomeActivity.this);
                            declareLv.setAdapter(declareAdapter);
                        } else {
                            nodateTv.setVisibility(View.VISIBLE);
                            // 没有数据显示，两个按钮不可点击并且变灰色
                            shenbaoBtn.setEnabled(false);
                            shenbaoBtn.setBackgroundColor(Color.parseColor("#e3e3e3"));
                            zhixianHebingBtn.setEnabled(false);
                            zhixianHebingBtn.setBackgroundColor(Color.parseColor("#e3e3e3"));
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

    private class DeclareAdapter extends BaseAdapter {
        private List<Declare> declareList;
        private Context context;

        public DeclareAdapter(List<Declare> declareList, Context context) {
            this.declareList = declareList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return declareList.size();
        }

        @Override
        public Object getItem(int position) {
            return declareList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Declare declare = (Declare) getItem(position);
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.declare_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.declareCheckBox = (CheckBox) convertView.findViewById(R.id.declare_checkbox);
                viewHolder.declareMawbTv = (TextView) convertView.findViewById(R.id.declare_mawb_tv);
                viewHolder.declareHnoTv = (TextView) convertView.findViewById(R.id.declare_hno_tv);
                viewHolder.declareCmdstatusTv = (TextView) convertView.findViewById(R.id.declare_cmdstatus_tv);
                viewHolder.declareMftstatusTv = (TextView) convertView.findViewById(R.id.declare_mftstatus_tv);
                viewHolder.declareArrivalstatusTv = (TextView) convertView.findViewById(R.id.declare_arrivalstatus_tv);
                viewHolder.showLy = (LinearLayout) convertView.findViewById(R.id.awb_linear_layout);
                viewHolder.declareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkedDeclareMap.put(declare.getMawb(), declare);
                            rearchIdMap.put(position, declare);
                        } else {
                            if (checkedDeclareMap.get(declare.getMawb()) != null) {
                                checkedDeclareMap.remove(declare.getMawb());
                            }
                            rearchIdMap.remove(position);
                        }
                    }
                });

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String declareMawb = declareList.get(position).getMawb();
            if (declareMawb != null && !declareMawb.equals("")) {
                viewHolder.declareMawbTv.setText(declareMawb);
            } else {
                viewHolder.declareMawbTv.setText("");
            }
            String declareHno = declareList.get(position).getHno();
            if (declareHno != null && !declareHno.equals("")) {
                viewHolder.declareHnoTv.setText(declareHno);
            } else {
                viewHolder.declareHnoTv.setText("");
            }
            String declareCmdstatus = declareList.get(position).getCMDStatus();
            if (declareCmdstatus != null && !declareCmdstatus.equals("")) {
                viewHolder.declareCmdstatusTv.setText(declareCmdstatus);
            } else {
                viewHolder.declareCmdstatusTv.setText("");
            }
            String declareMftstatus = declareList.get(position).getMftStatus();
            if (declareMftstatus != null && !declareMftstatus.equals("")) {
                viewHolder.declareMftstatusTv.setText(declareMftstatus);
            } else {
                viewHolder.declareMftstatusTv.setText("");
            }
            String declareArrivalstatus = declareList.get(position).getArrivalStatus();
            if (declareArrivalstatus != null && !declareArrivalstatus.equals("")) {
                viewHolder.declareArrivalstatusTv.setText(declareArrivalstatus);
            } else {
                viewHolder.declareArrivalstatusTv.setText("");
            }

            if (position % 2 == 0) {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                viewHolder.showLy.setBackgroundColor(Color.parseColor("#ebf5fe"));
            }
            return convertView;
        }

        class ViewHolder {
            CheckBox declareCheckBox;
            TextView declareMawbTv;
            TextView declareHnoTv;
            TextView declareCmdstatusTv;
            TextView declareMftstatusTv;
            TextView declareArrivalstatusTv;
            LinearLayout showLy;
        }
    }

    // 一键申报异步任务
    private class CgoExportOneKeyDeclareAsyTask extends AsyncTask<Void, Void, String> {
        String result = null;
        String mawb = null;

        public CgoExportOneKeyDeclareAsyTask(String mawb) {
            this.mawb = mawb;
        }

        @Override
        protected String doInBackground(Void... voids) {
                SoapObject object = HttpCGOExportOneKeyDeclare.cgoExportOneKeyDeclare(userBumen, userName, userPass, loginFlag, mawb);
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
                Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, "单号:" + mawb + ErrString, Toast.LENGTH_LONG).show();
            }
        }
    }

    // 支线合并异步任务
    private class ZhixianAsynck extends AsyncTask<Void, Void, String> {
        String xml = null;
        String result = null;
        public ZhixianAsynck(String xml) {
            this.xml = xml;
        }

        @Override
        protected String doInBackground(Void... voids) {
            SoapObject object = HttpCGOMergerSubLineArrival.cGOMergerSubLineArrival(userBumen, userName, userPass, loginFlag, xml);
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
                Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, ErrString, Toast.LENGTH_LONG).show();
                zhixianPb.setVisibility(View.GONE);
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, ErrString, Toast.LENGTH_LONG).show();
                zhixianPb.setVisibility(View.GONE);
            } else if (result.equals("true")) {
                Toast.makeText(AppIntOneKeyDeclarelHomeActivity.this, "合并成功", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AppIntOneKeyDeclarelHomeActivity.this, AppIntExpOneKeyDeclareActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


    // 支线合并的xml
    private String getRearchID(List<String> rearchId) {
        String pre = "<RearchInfo>";
        String after ="</RearchInfo>";
        String result = pre;
        for (String rearch :rearchId) {
            result += "<Rearch>"+ "<RearchID>"+rearch+"</RearchID>" + "</Rearch>";
        }
        result = result+after;
        return result;
    }

}
