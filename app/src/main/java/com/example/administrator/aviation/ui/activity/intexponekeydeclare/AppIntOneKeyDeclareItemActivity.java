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

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.model.intonekeydeclare.PrepareIntDeclare;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 预配及运抵列表界面
 */

public class AppIntOneKeyDeclareItemActivity extends Activity {
    // list数据
    private List<Declare> declareList;
    private List<String> mawbList;

    private DeclareAdapter declareAdapter;

    private ListView declareLv;
    private Map<String, Declare> checkedDeclareMap;

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

        // 测试
        Button button = (Button) findViewById(R.id.shenbai_btn);

        declareLv = (ListView) findViewById(R.id.declare_lv);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mawbList.clear();
                Set<Map.Entry<String, Declare>> entries = checkedDeclareMap.entrySet();
                for (Map.Entry<String, Declare> entry : entries) {
                    Declare declare = checkedDeclareMap.get(entry.getKey());
                    String a = declare.getMawb();
                    mawbList.add(a);
                }
                String xml = getHouseXml(mawbList);
                Log.d("tag", xml);
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
                            String str = null;
                            Set<Map.Entry<String, Declare>> entries = checkedDeclareMap.entrySet();
                            for (Map.Entry<String, Declare> entry : entries) {
                                Declare declare = checkedDeclareMap.get(entry.getKey());
                                String a = declare.getMawb();
                                str += "," + a;
                                Log.d("abc", str);
                            }
                        } else {
                            if (checkedDeclareMap.get(declare.getMawb()) != null) {
                                checkedDeclareMap.remove(declare.getMawb());
                                Set<Map.Entry<String, Declare>> entries = checkedDeclareMap.entrySet();
                                for (Map.Entry<String, Declare> entry : entries) {
                                    Declare declare = checkedDeclareMap.get(entry.getKey());
                                    String a = declare.getMawb();
                                    if (a.equals("") && checkedDeclareMap.size() == 0) {
                                        Log.d("abc", "没有数据了");
                                    } else {
                                        Log.d("abc", a);
                                    }
                                }
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
}
