package com.example.administrator.aviation.ui.activity.intexponekeydeclare;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.model.intonekeydeclare.Declare;
import com.example.administrator.aviation.model.intonekeydeclare.PrepareIntDeclare;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import java.util.List;

/**
 * 预配及运抵列表界面
 */

public class AppIntOneKeyDeclareItemActivity extends Activity{
    // list数据
    private List<Declare> declareList;

    private DeclareAdapter declareAdapter;

    private ListView declareLv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexponekeydeclareitem);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("预配与运抵信息");
        navBar.hideRight();

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
        public View getView(int position, View convertView, ViewGroup parent) {
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
                final Boolean ischecked = true;
                viewHolder.declareCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ischecked.equals(true)) {
                            Toast.makeText(AppIntOneKeyDeclareItemActivity.this, "显示", Toast.LENGTH_LONG).show();
                        } else if (ischecked.equals(false)){
                            Toast.makeText(AppIntOneKeyDeclareItemActivity.this, "没选中", Toast.LENGTH_LONG).show();
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
}
