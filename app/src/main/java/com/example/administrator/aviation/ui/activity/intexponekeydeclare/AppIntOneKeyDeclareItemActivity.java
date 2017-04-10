package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.getintexportonekeydeclare.HttpCGOExportOneKeyDeclare;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.model.intonekeydeclare.PrepareIntDeclare;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.util.PreferenceUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 预配及运抵列表界面
 */

public class AppIntOneKeyDeclareItemActivity extends Activity {
    private String ErrString = "";
    private String userBumen;
    private String userName;
    private String userPass;
    private String loginFlag;
    // list数据
    private List<Declare> declareList;
    private List<String> mawbList;

    private DeclareAdapter declareAdapter;

    private ListView declareLv;
    private TextView nodateTv;

    private Map<String, Declare> checkedDeclareMap;

    // 一键申报
    private Button shenbaoBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexponekeydeclareitem);
        initView();
        checkedDeclareMap = new HashMap<>();

        // 初始化xml的list
        mawbList = new ArrayList<>();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("预配与运抵信息");
        navBar.hideRight();

        // 获得用户信息
        userBumen = PreferenceUtils.getUserBumen(this);
        userName = PreferenceUtils.getUserName(this);
        userPass = PreferenceUtils.getUserPass(this);
        loginFlag = PreferenceUtils.getLoginFlag(this);

        // 测试
        shenbaoBtn = (Button) findViewById(R.id.shenbai_btn);

        declareLv = (ListView) findViewById(R.id.declare_lv);
        nodateTv = (TextView) findViewById(R.id.int_declare_nodata_tv);
        declareLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Declare declare = (Declare) declareAdapter.getItem(position);
                Intent intent = new Intent(AppIntOneKeyDeclareItemActivity.this, AppIntOneKeyDeclareItemDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(AviationCommons.DECLARE_INFO, declare);
                intent.putExtras(bundle);
                startActivity(intent);
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
                    String mawb = declare.getMawb();
                    mawbList.add(mawb);
                    new CgoExportOneKeyDeclareAsyTask(mawb).execute();
                }
                finish();
            }
        });

        // 得到list数据
        new GetDeclarListAsyTask().execute();

    }

    // 得到预配与运抵信息
    private class GetDeclarListAsyTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            // 预配与运抵界面传回的xml
            String declareXml = getIntent().getStringExtra(AviationCommons.INT_ONEKEY_DECLARE);
            declareList = PrepareIntDeclare.parseIntDeclareXml(declareXml);
            return declareXml;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            declareAdapter = new DeclareAdapter(declareList, AppIntOneKeyDeclareItemActivity.this);
            declareLv.setAdapter(declareAdapter);
            if (declareList.size() <= 0) {
                nodateTv.setVisibility(View.VISIBLE);
            }
        }
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
                viewHolder.declareCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            checkedDeclareMap.put(declare.getMawb(), declare);
                        } else {
                            if (checkedDeclareMap.get(declare.getMawb()) != null) {
                                checkedDeclareMap.remove(declare.getMawb());
                            }
                        }
                    }
                });

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String declareMawb = declareList.get(position).getMawb();
            if (declareMawb != null) {
                viewHolder.declareMawbTv.setText(declareMawb);
            } else {
                viewHolder.declareMawbTv.setText("");
            }
            String declareHno = declareList.get(position).getHno();
            if (declareHno != null) {
                viewHolder.declareHnoTv.setText(declareHno);
            } else {
                viewHolder.declareHnoTv.setText("");
            }
            String declareCmdstatus = declareList.get(position).getCMDStatus();
            if (declareCmdstatus != null) {
                viewHolder.declareCmdstatusTv.setText(declareCmdstatus);
            } else {
                viewHolder.declareCmdstatusTv.setText("");
            }
            String declareMftstatus = declareList.get(position).getMftStatus();
            if (declareMftstatus != null) {
                viewHolder.declareMftstatusTv.setText(declareMftstatus);
            } else {
                viewHolder.declareMftstatusTv.setText("");
            }
            String declareArrivalstatus = declareList.get(position).getArrivalStatus();
            if (declareArrivalstatus != null) {
                viewHolder.declareArrivalstatusTv.setText(declareArrivalstatus);
            } else {
                viewHolder.declareArrivalstatusTv.setText("");
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
        }
    }

    private String getHouseXml(List<String> Mawb) {
       String pre = "<DomExportWarehouse>"
               +"<whsInfo>";
        String after = "</whsInfo>"
                +"</DomExportWarehouse>";
        String result = pre;
        for (String mawb:Mawb) {
            result = result + "<Mwab>"+mawb+"</Mawb>";
        }
        result = result+after;
        return result;
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
                Toast.makeText(AppIntOneKeyDeclareItemActivity.this, ErrString, Toast.LENGTH_LONG).show();
            } else if (result.equals("false") && !ErrString.equals("") ) {
                Toast.makeText(AppIntOneKeyDeclareItemActivity.this, ErrString, Toast.LENGTH_LONG).show();
            }
        }
    }
}
