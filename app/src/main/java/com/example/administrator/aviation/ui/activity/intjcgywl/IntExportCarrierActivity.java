package com.example.administrator.aviation.ui.activity.intjcgywl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationNoteConvert;
import com.example.administrator.aviation.util.ChoseTimeMethod;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 出港业务量查询界面
 */

public class IntExportCarrierActivity extends Activity implements View.OnClickListener {
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
    @BindView(R.id.export_carrier_et)
    EditText exportCarrierEt;
    @BindView(R.id.export_carrier_sp)
    Spinner exportCarrierSp;

    // 获取当前时间
    private String reportType;
    private String begainTime;
    private String endTime;
    private String currentTime;
    private String xml;

    // 进出港类型
    private ArrayAdapter<String> reportTypeSpAdapter;
    private List<String> reportTypeSpList;
    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intexportcarrier_search);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("出港业务量查询");
        navBar.hideRight();

        currentTime = DateUtils.getTodayDateTime();
        declareInfoBeginTimeEt.setText(currentTime);
        declareInfoEndTimeEt.setText(currentTime);
        declareInfoBeginTimeBtn.setOnClickListener(this);
        declareInfoEndTimeBtn.setOnClickListener(this);
        declareInfoSearchBtn.setOnClickListener(this);

        reportTypeSpList = new ArrayList<>();
        reportTypeSpList.add("业务量");
        reportTypeSpList.add("目的港");
        reportTypeSpList.add("航班号");
        reportTypeSpList.add("日");
        reportTypeSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reportTypeSpList);
        reportTypeSpAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        exportCarrierSp.setAdapter(reportTypeSpAdapter);
        exportCarrierSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                exportCarrierEt.setText(reportTypeSpAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.declare_info_begin_time_btn:
                choseTimeMethod.getCurrentTime(IntExportCarrierActivity.this, declareInfoBeginTimeEt);
                break;

            case R.id.declare_info_end_time_btn:
                choseTimeMethod.getCurrentTime(IntExportCarrierActivity.this, declareInfoEndTimeEt);
                break;

            case R.id.declare_info_search_btn:
                declareInfoPb.setVisibility(View.VISIBLE);
                getEditText();
                xml = getXml(begainTime, endTime, reportType);
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(IntExportCarrierActivity.this, HttpCommons.CGO_GET_INT_EXPORT_REPORT_NAME,
                        HttpCommons.CGO_GET_INT_EXPORT_REPORT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xmls = object.getProperty(0).toString();
//                                Toast.makeText(IntExportCarrierActivity.this, xml, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(IntExportCarrierActivity.this, IntExportCarrierDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("intexportdayxml", xmls);
                                bundle.putString("type", reportType);
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
        reportType = exportCarrierEt.getText().toString().trim();
        reportType = AviationNoteConvert.cNtoEn(reportType);
        begainTime = declareInfoBeginTimeEt.getText().toString().trim();
        endTime = declareInfoEndTimeEt.getText().toString().trim();
    }

    private String getXml(String begainTime, String endTime, String reportType) {
        String xml = new String("<GJCCarrierReport>"
                +"<ReportType>" + reportType + "</ReportType>"
                + "<StartDay>" + begainTime + "</StartDay>"
                + "<EndDay>" + endTime + "</EndDay>"
                + "</GJCCarrierReport>");
        return xml;
    }
}
