package com.example.administrator.aviation.ui.activity.domflightcheck;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.aviation.R;
import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;
import com.example.administrator.aviation.tool.AllCapTransformationMethod;
import com.example.administrator.aviation.tool.DateUtils;
import com.example.administrator.aviation.ui.base.NavBar;
import com.example.administrator.aviation.util.ChoseTimeMethod;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 订舱计划查询界面
 */

public class DomFlightCheckInSearchActivity extends Activity implements View.OnClickListener{
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.check_mawb_et)
    EditText checkMawbEt;
    @BindView(R.id.check_time_et)
    EditText checkTimeEt;
    @BindView(R.id.check_time_btn)
    ImageView checkTimeBtn;
    @BindView(R.id.check_daipi_cb)
    CheckBox checkDaipiCb;
    @BindView(R.id.check_gl_cb)
    CheckBox checkGlCb;
    @BindView(R.id.chaeck_search_btn)
    Button chaeckSearchBtn;
    @BindView(R.id.check_pb)
    ProgressBar checkPb;

    // 当前时间
    private String currentTime;

    private String showCheck;
    private String guolvCheck;
    private String fdate;
    private String fno;

    ChoseTimeMethod choseTimeMethod = new ChoseTimeMethod();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appflightcheckinsearch);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        NavBar navBar = new NavBar(this);
        navBar.hideRight();
        navBar.setTitle("订舱计划查询");

        checkMawbEt.setTransformationMethod(new AllCapTransformationMethod());
        currentTime = DateUtils.getTodayDateTime();
        checkTimeEt.setText(currentTime);
        chaeckSearchBtn.setOnClickListener(this);
        checkTimeBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chaeck_search_btn:
                checkPb.setVisibility(View.VISIBLE);
                if (checkDaipiCb.isChecked()) {
                    showCheck = "1";
                } else {
                    showCheck = "0";
                }
                if (checkGlCb.isChecked()) {
                    guolvCheck = "1";
                } else {
                    guolvCheck = "0";
                }
                fdate = checkTimeEt.getText().toString().trim();
                fno = checkMawbEt.getText().toString().trim();
                fno = fno.toUpperCase();
                final String xml = getXml(fdate, fno, showCheck, guolvCheck);
                Map<String, String> params = new HashMap<>();
                params.put("fltXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(DomFlightCheckInSearchActivity.this, HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_NAME,
                        HttpCommons.CGO_GET_DOM_FLIGHT_CHECK_IN_ACTION, params, new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject soapObject = (SoapObject) result;
                                String a =  soapObject.getProperty(0).toString();
                                Intent intent = new Intent(DomFlightCheckInSearchActivity.this, DomFlightCheckInActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("checkXml", a);
                                bundle.putString("searchXml", xml);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                checkPb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailed(String message) {
                                checkPb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                checkPb.setVisibility(View.GONE);
                            }
                        });

                break;

            // 选择日期
            case R.id.check_time_btn:
                choseTimeMethod.getCurrentTime(DomFlightCheckInSearchActivity.this, checkTimeEt);
                break;
        }
    }


    private String getXml(String fdate, String fno, String onlyWaitCheck, String onlyFlightOn) {
        String xml = new String("<GNCFlightPlan>"
                + "<FDate>" + fdate + "</FDate>"
                + "<Fno>" + fno + "</Fno>"
                + " <OnlyWaitCheck>" + onlyWaitCheck + "</OnlyWaitCheck>"
                + "  <OnlyFlightOn>" + onlyFlightOn + "</OnlyFlightOn>"
                + "</GNCFlightPlan>");

        return xml;
    }
}
