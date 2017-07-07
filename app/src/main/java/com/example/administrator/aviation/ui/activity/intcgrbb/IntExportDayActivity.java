package com.example.administrator.aviation.ui.activity.intcgrbb;

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

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 出港日报表查询界面
 */

public class IntExportDayActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.declare_info_begin_time_et)
    EditText declareInfoBeginTimeEt;
    @BindView(R.id.declare_info_begin_time_btn)
    ImageView declareInfoBeginTimeBtn;
    @BindView(R.id.declare_info_end_time_et)
    EditText declareInfoEndTimeEt;
    @BindView(R.id.declare_info_end_time_btn)
    ImageView declareInfoEndTimeBtn;
    @BindView(R.id.declare_info_search_btn)
    Button declareInfoSearchBtn;
    @BindView(R.id.declare_info_pb)
    ProgressBar declareInfoPb;

    // 获取当前时间
    private int countTime;
    private String begainTime;
    private String endTime;
    private String currentTime;
    private String xml;
    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intexportday_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("出港日报表查询");
        navBar.hideRight();

        currentTime = DateUtils.getTodayDateTime();
        declareInfoBeginTimeEt.setText(currentTime);
        declareInfoEndTimeEt.setText(currentTime);
        declareInfoBeginTimeBtn.setOnClickListener(this);
        declareInfoEndTimeBtn.setOnClickListener(this);
        declareInfoSearchBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.declare_info_begin_time_btn:
                choseTimeMethod.getCurrentTime(IntExportDayActivity.this, declareInfoBeginTimeEt);
                break;

            case R.id.declare_info_end_time_btn:
                choseTimeMethod.getCurrentTime(IntExportDayActivity.this, declareInfoEndTimeEt);
                break;

            case R.id.declare_info_search_btn:
                declareInfoPb.setVisibility(View.VISIBLE);
                try {
                    getEditText();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                if (countTime>3) {
//                    Toast.makeText(this, "时间差不能超过3天", Toast.LENGTH_LONG).show();
//                    declareInfoPb.setVisibility(View.GONE);
//                } else {
//
//                }
                xml = getXml(begainTime, endTime);
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(IntExportDayActivity.this, HttpCommons.CGO_GET_INT_EXPORT_REPORT_NAME,
                        HttpCommons.CGO_GET_INT_EXPORT_REPORT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xml = object.getProperty(0).toString();
//                                Toast.makeText(IntExportDayActivity.this, xml, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(IntExportDayActivity.this, IntExportDayDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("intexportdayxml", xml);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                declareInfoPb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailed(String message) {

                            }

                            @Override
                            public void onError() {

                            }
                        });
                break;

            default:
                break;
        }
    }

    private void getEditText() throws ParseException {
        begainTime = declareInfoBeginTimeEt.getText().toString().trim();
        endTime = declareInfoEndTimeEt.getText().toString().trim();
        countTime = DateUtils.daysBetween(begainTime, endTime);
    }

    private String getXml(String begainTime, String endTime) {
        String xml = new String("<GJCCarrierReport>"
                +"<StartDay>" +begainTime + "</StartDay>"
                +"<EndDay>" +begainTime + "</EndDay>"
                +"</GJCCarrierReport>");

        return xml;
    }
}
