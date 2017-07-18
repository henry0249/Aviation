package com.example.administrator.aviation.ui.activity.edeclareinfo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国际承运人出港联检状态查询
 */

public class AppEDeclareInfoSearchActivity extends Activity implements View.OnClickListener {

    @BindView(R.id.edeclare_mawb_et)
    AutoCompleteTextView edeclareMawbEt;
    @BindView(R.id.edeclare_search_btn)
    Button edeclareSearchBtn;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String mawb;
    private ArrayAdapter<String> arr_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appedeclaresearch);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("联检状态查询");
        navBar.hideRight();

        edeclareSearchBtn.setOnClickListener(this);


        // 获取搜索记录文件内容
        SharedPreferences sp = getSharedPreferences("search_history", 0);
        String history = sp.getString("history", "");

        // 用逗号分割内容返回数组
        String[] history_arr = history.split(",");

        // 新建适配器，适配器数据为搜索历史文件内容
        arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, history_arr);

        // 保留前50条数据
        if (history_arr.length > 50) {
            String[] newArrays = new String[50];
            // 实现数组之间的复制
            System.arraycopy(history_arr, 0, newArrays, 0, 50);
            arr_adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, history_arr);
        }

        // 设置适配器
        edeclareMawbEt.setAdapter(arr_adapter);
        edeclareMawbEt.setDropDownHeight(500);
        edeclareMawbEt.setDropDownWidth(700);
        edeclareMawbEt.setThreshold(1);
        edeclareMawbEt.setCompletionHint("最近的5条记录");
        edeclareMawbEt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                AutoCompleteTextView view = (AutoCompleteTextView) v;
                if (hasFocus) {
                    view.showDropDown();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edeclare_search_btn:
                save();
                edeclarePb.setVisibility(View.VISIBLE);
                mawb = edeclareMawbEt.getText().toString().trim();
                String xml = getXml(mawb);
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(AppEDeclareInfoSearchActivity.this, HttpCommons.CGO_GET_EDECLARE_NAME,
                        HttpCommons.CGO_GET_EDECLARE_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String edeclare = object.getProperty(0).toString();
//                                Toast.makeText(AppEDeclareInfoSearchActivity.this, edeclare, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AppEDeclareInfoSearchActivity.this, AppEdeclareActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(AviationCommons.EDECLARE_INFO, edeclare);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                edeclarePb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailed(String message) {
                                edeclarePb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                edeclarePb.setVisibility(View.GONE);
                            }
                        });
                break;

            default:
                break;
        }
    }

    private String getXml(String mawb) {
        String xml = new String("<GJCCarrierReport>"
                + "<Mawb>" + mawb + "</Mawb>"
                + "</GJCCarrierReport>");
        return xml;
    }
    public void save() {
        // 获取搜索框信息
        String text = edeclareMawbEt.getText().toString();
        SharedPreferences mysp = getSharedPreferences("search_history", 0);
        String old_text = mysp.getString("history", "");

        // 利用StringBuilder.append新增内容，逗号便于读取内容时用逗号拆分开
        StringBuilder builder = new StringBuilder(old_text);
        builder.append(text + ",");

        // 判断搜索内容是否已经存在于历史文件，已存在则不重复添加
        if (!old_text.contains(text + ",")) {
            SharedPreferences.Editor myeditor = mysp.edit();
            myeditor.putString("history", builder.toString());
            myeditor.commit();
        }

    }
}
