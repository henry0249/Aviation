package com.example.administrator.aviation.ui.activity.domandintgetflight;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.AviationCommons;
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
 * 航班动态查询界面
 */

public class FlightActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.flight_timeet)
    EditText flightTimeEt;
    @BindView(R.id.flightchosetime_btn)
    ImageView flightchosetimeBtn;
    @BindView(R.id.flight_hangbanhaoet)
    EditText flightHangbanhaoEt;
    @BindView(R.id.flight_hangbanleixinget)
    EditText flightHangbanleixingEt;
    @BindView(R.id.flighthangbanleixing_sp)
    Spinner flighthangbanleixingSp;
    @BindView(R.id.flight_quyuleixinget)
    EditText flightQuyuleixingEt;
    @BindView(R.id.flight_quyuleixing_sp)
    Spinner flightQuyuleixingSp;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.flight_qiyunganget)
    EditText flightQiyunganget;
    @BindView(R.id.flight_mudiganget)
    EditText flightMudiganget;
    @BindView(R.id.flight_jinchuganget)
    EditText flightJinchuganget;
    @BindView(R.id.flight_jinchugangleixing_sp)
    Spinner flightJinchugangleixingSp;
    @BindView(R.id.flight_search_btn)
    Button flightSearchBtn;
    @BindView(R.id.flight_pb)
    ProgressBar flightPb;

    private String hbTime;
    private String hbHao;
    private String hbLexing;
    private String qyLexing;
    private String qyGang;
    private String mdGang;
    private String jcgLexing;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    // 航班类型
    private ArrayAdapter<String> flighthangbanleixingSpAdapter;
    private List<String> flighthangbanleixingSpList;

    // 区域类型
    private ArrayAdapter<String> flightQuyuleixingSpAdapter;
    private List<String> flightQuyuleixingSpList;

    // 进出港类型
    private ArrayAdapter<String> flightJinchugangleixingSpAdapter;
    private List<String> flightJinchugangleixingSpList;

    // 获取当前时间
    private String currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchflight);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.setTitle("航班动态查询");
        navBar.hideRight();

        // 选择时间点击事件
        flightchosetimeBtn.setOnClickListener(this);

        // 查询按钮点击事件
        flightSearchBtn.setOnClickListener(this);

        // 给默认值
        currentTime = DateUtils.getTodayDateTime();
        flightTimeEt.setText(currentTime);


        flighthangbanleixingSpList = new ArrayList<>();

        flighthangbanleixingSpList.add("客机");
        flighthangbanleixingSpList.add("货机");
        flighthangbanleixingSpList.add("卡车");
        flighthangbanleixingSpList.add("");
        flighthangbanleixingSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flighthangbanleixingSpList);
        flighthangbanleixingSpAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        flighthangbanleixingSp.setAdapter(flighthangbanleixingSpAdapter);
        flighthangbanleixingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                flightHangbanleixingEt.setText(flighthangbanleixingSpAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        flightQuyuleixingSpList = new ArrayList<>();
        flightQuyuleixingSpList.add("国内");
        flightQuyuleixingSpList.add("国际");
        flightQuyuleixingSpList.add("");
        flightQuyuleixingSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flightQuyuleixingSpList);
        flightQuyuleixingSpAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        flightQuyuleixingSp.setAdapter(flightQuyuleixingSpAdapter);
        flightQuyuleixingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                flightQuyuleixingEt.setText(flightQuyuleixingSpAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        flightJinchugangleixingSpList = new ArrayList<>();
        flightJinchugangleixingSpList.add("出港");
        flightJinchugangleixingSpList.add("进港");
        flightJinchugangleixingSpList.add("");
        flightJinchugangleixingSpAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, flightJinchugangleixingSpList);
        flightJinchugangleixingSpAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        flightJinchugangleixingSp.setAdapter(flightJinchugangleixingSpAdapter);
        flightJinchugangleixingSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                flightJinchuganget.setText(flightJinchugangleixingSpAdapter.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//
//        flightHangbanleixingEt.setText("客机");
//        flightQuyuleixingEt.setText("国内");
//        flightJinchuganget.setText("出港");

    }

    // 获取输入框的值并且拼接xml
    private void getEditTextValues() {
        hbTime = flightTimeEt.getText().toString().trim();
        hbHao = flightHangbanhaoEt.getText().toString().trim();
        hbLexing = flightHangbanleixingEt.getText().toString().trim();
        if (!hbLexing.equals("")) {
            hbLexing = AviationNoteConvert.cNtoEn(hbLexing);
        }
        qyLexing = flightQuyuleixingEt.getText().toString().trim();
        if (!qyLexing.equals("")) {
            qyLexing = AviationNoteConvert.cNtoEn(qyLexing);
        }
        qyGang = flightQiyunganget.getText().toString().trim();
        mdGang = flightMudiganget.getText().toString().trim();
        jcgLexing = flightJinchuganget.getText().toString().trim();
        if (!jcgLexing.equals("")) {
            jcgLexing = AviationNoteConvert.cNtoEn(jcgLexing);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flightchosetime_btn:
                choseTimeMethod.getCurrentTime(FlightActivity.this, flightTimeEt);
                break;

            case R.id.flight_search_btn:
                flightPb.setVisibility(View.VISIBLE);
                getEditTextValues();
//                if (hbHao.equals("")) {
//                    Toast.makeText(FlightActivity.this, "请输入航班号", Toast.LENGTH_SHORT).show();
//                } else {
                    String xml = getXml(hbTime,hbHao, hbLexing, qyLexing, qyGang, mdGang, jcgLexing);
                    Map<String, String> params = new HashMap<>();
                    params.put("fltXml", xml);
                    params.put("ErrString", "");
                    HttpRoot.getInstance().requstAync(FlightActivity.this, HttpCommons.CGO_GET_FLIGHT_NAME, HttpCommons.CGO_GET_FLIGHT_ACTION, params,
                            new HttpRoot.CallBack() {
                                @Override
                                public void onSucess(Object result) {
                                    SoapObject object = (SoapObject) result;
                                    String a = object.getProperty(0).toString();
//                                    Toast.makeText(FlightActivity.this, a, Toast.LENGTH_SHORT).show();
                                    flightPb.setVisibility(View.GONE);
                                    Intent intent = new Intent(FlightActivity.this, FlightHomeActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString(AviationCommons.FLIGHT_INFO, a);
                                    intent.putExtras(bundle);
                                    intent.putExtra("jcgleixing", jcgLexing);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailed(String message) {
                                    flightPb.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    flightPb.setVisibility(View.GONE);
                                }
                            });
//                }
                break;

            default:
                break;
        }
    }

    private String getXml(String riqi, String hangbhao, String hangbanleixing, String quyuleixing,
                          String qiyungang, String mudigang, String jinchugangleixing) {
        String xml = new String("<FLTFlight>"
                + "<FDate>" + riqi + "</FDate>"
                + "<Fno>" + hangbhao + "</Fno>"
                + " <ServiceType>" + hangbanleixing + "</ServiceType>"
                + " <CountryType>" + quyuleixing + "</CountryType>"
                + " <FDep>" + qiyungang + "</FDep>"
                + " <FDest>" + mudigang + "</FDest>"
                + "  <IEFlag>" + jinchugangleixing + "</IEFlag>"
                + "</FLTFlight>");

        return xml;
    }
}
