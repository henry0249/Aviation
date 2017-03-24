package com.example.administrator.aviation.util;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * 选择时间
 */

public class ChoseTimeMethod {
    private Calendar cal = Calendar.getInstance();
    //当前年
    private int year = cal.get(Calendar.YEAR);
    //当前月
    private int month = (cal.get(Calendar.MONTH));
    //当前月的第几天：即当前日
    private int day_of_month = cal.get(Calendar.DAY_OF_MONTH);
    public void getCurrentTime(Activity activity, final EditText editText) {

        new DatePickerDialog(activity,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(String.format("%d-%d-%d",year,monthOfYear+1,dayOfMonth));
            }
        },year,month,day_of_month).show();
    }
}
