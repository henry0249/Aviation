package com.example.administrator.aviation.sys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.administrator.aviation.R;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/2/7.
 */
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//             佛祖保佑       永无BUG     永不修改                //
//                                                                //
//       佛曰:                                                    //
//               写字楼里写字间，写字间里程序员；                 //
//               程序人员写程序，又拿程序换酒钱。                 //
//               酒醒只在网上坐，酒醉还来网下眠；                 //
//               酒醉酒醒日复日，网上网下年复年。                 //
//               但愿老死电脑间，不愿鞠躬老板前；                 //
//               奔驰宝马贵者趣，公交自行程序员。                 //
//               别人笑我太疯癫，我笑他人看不穿；                 //
//               不见满街漂亮妹，哪个归得程序员？                 //
////////////////////////////////////////////////////////////////////
public class PublicFun {

    //region 遍历子控件
    public static void ElementSwitch (ViewGroup viewGroup, boolean bool) {
        if (viewGroup == null) {
            return;
        }

        if(viewGroup instanceof ViewGroup) {
            LinkedList<ViewGroup> queue = new LinkedList<ViewGroup>();
            queue.add(viewGroup);
            while(!queue.isEmpty()) {
                ViewGroup current = queue.removeFirst();
                current.setEnabled(bool);

                for(int i = 0; i < current.getChildCount(); i ++) {
                    if(current.getChildAt(i) instanceof ViewGroup) {
                        queue.addLast((ViewGroup) current.getChildAt(i));
                    }else {
                        View view = current.getChildAt(i);
                        view.setEnabled(bool);
                        if (view instanceof Button) {
                            Button newBtn = (Button) view;
                            if (bool) {
                                newBtn.setBackgroundResource(R.drawable.button_selector);
                            } else {
                                newBtn.setBackgroundColor(Color.parseColor("#979797"));
                            }
                        }
                    }
                }
            }
        }else {
            viewGroup.setEnabled(bool);
        }
    }
    //endregion

    //region 软键盘状态切换
    public static void KeyBoardSwitch(Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);

        }
    }
    //endregion

    //region 隐藏软键盘
    public static void KeyBoardHide(Activity act,Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() && act.getCurrentFocus()!=null){
            if (act.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    //endregion

    //region 计算ListView的高度
    public static int CalcListHeigh(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return 0;
        }
        View mView = mAdapter.getView(0, null, listView);
        mView.measure(0, 0);
        int res = mView.getMeasuredHeight() + listView.getDividerHeight();
        return res;
    }
    //endregion

    //region 判断字符串是否是数字
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    //endregion

    //region 浮点数转字符
    public static String DoubleToStr(Double n,int m){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(m);
        String dou_str = nf.format(n);
        return  dou_str;
    }
    //endregion

    //region 转换平板号
    public static String getPinBanHao(String x){
        String result = "";
        String first = x.substring(0,1);

        if (isNumeric(first)){
            if (x.length() == 1) {
                result = "00" + x;
            }else if (x.length() == 2) {
                result = "0" + x;
            } else {
                result = x;
            }
        }else if (x.length() > 1){
            result = first.toUpperCase();
            String two = x.substring(1);
            if (two.length() == 1){
                result += "000" + two;
            }else if (two.length() == 2) {
                result += "00" + two;
            }else if (two.length() == 3) {
                result += "0" + two;
            }else {
                result = x;
            }
        } else {
            result = x;
        }

        return result;
    }
    //endregion

}

