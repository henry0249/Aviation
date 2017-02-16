package com.example.administrator.aviation.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * 选择时间
 */

public class ChoseTimeMethod {
    public void getCurrentTime(Activity activity, final EditText editText) {
        new DatePickerDialog(activity,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
            }
        },2016,12,20).show();
    }
}
