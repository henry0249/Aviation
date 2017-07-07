package com.example.administrator.aviation.ui.activity.edeclareinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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
    EditText edeclareMawbEt;
    @BindView(R.id.edeclare_search_btn)
    Button edeclareSearchBtn;
    @BindView(R.id.edeclare_pb)
    ProgressBar edeclarePb;

    private String mawb;

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edeclare_search_btn:
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

    private String getXml(String mawb) {
        String xml = new String("<GJCCarrierReport>"
        +"<Mawb>" +mawb + "</Mawb>"
        +"</GJCCarrierReport>");
        return xml;
    }
}
