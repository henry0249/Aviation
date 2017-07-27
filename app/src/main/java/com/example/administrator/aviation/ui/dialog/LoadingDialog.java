package com.example.administrator.aviation.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
 import com.example.administrator.aviation.R;


public class LoadingDialog extends Dialog {
    TextView tvMessage;
    private String message;

    public LoadingDialog(Context context) {
        super(context, R.style.loadingDialogStyle);
        this.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        tvMessage = (TextView) findViewById(R.id.tv_message);
    }

    @Override
    public void show() {
        if (tvMessage != null && !TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }
        super.show();
    }

    /**
     * 重新设置信息
     * @param message
     */
    public void reSetMsg(String message){
        tvMessage.setText(message);
        tvMessage.postInvalidate();
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
