package com.example.administrator.aviation.ui.activity.domjcgrbb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
 * 国内进港日报表
 */

public class DomImportDayCarrierActivity extends Activity implements View.OnClickListener{
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
        navBar.setTitle("国内进港日报表查询");
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
                choseTimeMethod.getCurrentTime(DomImportDayCarrierActivity.this, declareInfoBeginTimeEt);
                break;

            case R.id.declare_info_search_btn:
                declareInfoPb.setVisibility(View.VISIBLE);
                getEditText();
                xml = getXml(begainTime);
                Map<String, String> params = new HashMap<>();
                params.put("awbXml", xml);
                params.put("ErrString", "");
                HttpRoot.getInstance().requstAync(DomImportDayCarrierActivity.this, HttpCommons.CGO_GET_DOM_IMPORT_DAY_REPORT_NAME,
                        HttpCommons.CGO_GET_DOM_IMPORT_DAY_REPORT_ACTION, params,
                        new HttpRoot.CallBack() {
                            @Override
                            public void onSucess(Object result) {
                                SoapObject object = (SoapObject) result;
                                String xml = object.getProperty(0).toString();
//                                Toast.makeText(IntImportDayCarrierActivity.this, xml, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DomImportDayCarrierActivity.this, DomImportDayCarrierDetailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("intimportdaydetailxml", xml);
                                intent.putExtras(bundle);
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
        String xml = new String("<GNJCarrierReport>"
                + "<StartDay>" + begainTime + "</StartDay>"
                + "</GNJCarrierReport>");
        return xml;
    }
}
