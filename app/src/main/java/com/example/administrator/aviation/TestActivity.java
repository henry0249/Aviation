package com.example.administrator.aviation;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.aviation.http.HttpCommons;
import com.example.administrator.aviation.http.HttpRoot;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 59573 on 2017/6/29.
 */

public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void requestTest(View view) {
        Map<String,String> params = new HashMap<>();
        String xml = "<IntImportHawb><HawbID>0</HawbID><Mawb>04381272494</Mawb> <Hno>234145</Hno> <BusinessType>ANR</BusinessType> <PC>12</PC> <Weight>55</Weight>  <Volume>55</Volume><Goods>VBBB</Goods> <Remark></Remark></IntImportHawb>";
        params.put("hawbXml",xml);
        params.put("ErrString","");
        HttpRoot.getInstance().requstAync(this, HttpCommons.CGO_ADD_INT_IMPORT_HAWB_NAME, HttpCommons.CGO_ADD_INT_IMPORT_HAWB_ACTION, params,
                new HttpRoot.CallBack() {
                    @Override
                    public void onSucess(Object result) {
                        Toast.makeText(TestActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(String message) {
                        Toast.makeText(TestActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Toast.makeText(TestActivity.this, "网络连接异常", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
