package com.example.administrator.aviation.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.administrator.aviation.R;
import com.example.administrator.aviation.util.AviationCommons;
import com.example.administrator.aviation.tool.recognize.view.FacadeView;
import com.example.administrator.aviation.ui.cgo.gnj.PickUpSignatureActivity;
import static com.example.administrator.aviation.sys.PublicFun.isFastDoubleClick;

public class RecognizeCardActivity extends Activity{
    //region 自定义变量
    private Context mContext;
    private Activity mAct;
    private NavBar navBar;
    private int takePic = 0;
    //endregion


    @BindView(R.id.RecognizeCard_btn_shutter)
    ImageButton btn_shutter;
    @BindView(R.id.RecognizeCard_facade)
    FacadeView facade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_card);
        mContext = RecognizeCardActivity.this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        navBar = new NavBar(this);
        navBar.setTitle("身份证识别");
        AviationCommons.MyDPI = mContext.getResources().getDisplayMetrics().density;

        int HH = (int)getIntent().getSerializableExtra("Height");
        facade.setTransParentRectWH(948,HH);

        File f = new File(AviationCommons.StoragePath + "/" + "card" +".jpg");
        if (f.exists()) {
            f.delete();
        }

        setListener();
    }

    private void setListener() {
        btn_shutter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFastDoubleClick()) {
                    return;
                }

                facade.takeRectPicture(true,AviationCommons.MyDPI);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        finish();
                    }
                },1000);


            }
        });
    }

    //region 句柄监听
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            return false;
        }
    });
    //endregion

    @Override
    public void finish() {
        Intent intent = new Intent();
        int num = 0;

        Integer req = (Integer) getIntent().getSerializableExtra("id");
        if (req == AviationCommons.PickUpSignatureActivity_REQUEST) {
            intent.setClass(RecognizeCardActivity.this, PickUpSignatureActivity.class);
            num = AviationCommons.PickUpSignatureActivity_RESULT;
        }

        setResult(num,intent);
        super.finish();
    }

}
