package com.example.administrator.aviation.ui.activity.intjcgrbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.ChoseTimeMethod;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 国际出港日报表
 */

public class IntExportDayCarrierActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.declare_info_begin_time_et)
    EditText declareInfoBeginTimeEt;
    @BindView(R.id.declare_info_begin_time_btn)
    ImageView declareInfoBeginTimeBtn;
    @BindView(R.id.declare_info_search_btn)
    Button declareInfoSearchBtn;
    @BindView(R.id.declare_info_pb)
    ProgressBar declareInfoPb;

    // 获取当前时间
    private String begainTime;
    private String endTime;
    private String currentTime;
    private String xml;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintexportdaycarrier);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("国际出港日报表查询");
        navBar.hideRight();
        currentTime = DateUtils.getTodayDateTime();
        declareInfoBeginTimeEt.setText(currentTime);

        declareInfoBeginTimeBtn.setOnClickListener(this);

        declareInfoSearchBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.declare_info_begin_time_btn:
                choseTimeMethod.getCurrentTime(IntExportDayCarrierActivity.this, declareInfoBeginTimeEt);
                break;

            case R.id.declare_info_search_btn:
                declareInfoPb.setVisibility(View.VISIBLE);
                getEditText();
                xml = getXml(begainTime);
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(IntExportDayCarrierActivity.this, HttpCommons.CGO_GET_INT_EXPORT_DAY_REPORT_NAME,
                        HttpCommons.CGO_GET_INT_EXPORT_DAY_REPORT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
//                                Toast.makeText(IntExportDayCarrierActivity.this, xml, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(IntExportDayCarrierActivity.this, IntExportDayCarrierDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("intexportdaydetailxml", xmls);
                                intent.putExtras(bundle);
                                intent.putExtra("xml", xml);
                                startActivity(intent);
                                declareInfoPb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailed(String message) {
                                declareInfoPb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                declareInfoPb.setVisibility(View.GONE);
                            }
                        });
                break;

            default:
                break;
        }
    }

    private void getEditText() {
        begainTime = declareInfoBeginTimeEt.getText().toString().trim();
    }

    private String getXml(String begainTime) {
        String xml = new String("<GJCCarrierReport>"
                + "<StartDay>" + begainTime + "</StartDay>"
                + "</GJCCarrierReport>");
        return xml;
    }
}
