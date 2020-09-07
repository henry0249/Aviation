package com.example.administrator.aviation.sys;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.administrator.aviation.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
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
    private static long lastClickTime;

    //region 1.是否有网络
    public static int getNetype(Context context) {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            netType = 2;
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        }
        return netType;
    }

    //endregion

    //region 2.获取activity
    public static Activity getActivityByContext(Context context){
        while(context instanceof ContextWrapper){
            if(context instanceof Activity){
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
    //endregion

    //region 3.遍历子控件
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

    //region 4.软键盘状态切换
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

    //region 5.隐藏软键盘
    public static void KeyBoardHide(Activity act,Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive() && act.getCurrentFocus()!=null){
            if (act.getCurrentFocus().getWindowToken()!=null) {
                imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    //endregion

    //region 6.隐藏软键盘,其中viewList 中需要放的是当前界面所有触发软键盘弹出的控件.
    public static void KeyBoardHideTwo(Context context,List<View> viewList) {
        if (viewList == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //endregion

    //region 7.计算ListView的高度
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

    //region 8.判断字符串是否是数字
    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }
    //endregion

    //region 9.浮点数转字符
    public static String DoubleToStr(Double n,int m){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumFractionDigits(m);
        String dou_str = nf.format(n);
        return  dou_str;
    }
    //endregion

    //region 10.转换平板号
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

    //region 11.判断首字符是否为字母
    public static boolean  CheckFirstLetter(String   fstrData)
    {
        char   c   =   fstrData.charAt(0);
        if(((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z')))
        {
            return   true;
        }else{
            return   false;
        }
    }
    //endregion

    //region 12.获取存储路径
    public static String getSDPath(Context context) {
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);// 判断sd卡是否存在
        if (sdCardExist) {
            return Environment.getExternalStorageDirectory().toString();// 获取根目录
        } else {
            return context.getCacheDir().getAbsolutePath(); // 获取内置内存卡目录
        }
    }
    //endregion

    //region 13.获取当前时间
    public static String getDateStr(String ti)  {
        try{
            SimpleDateFormat myFmt = new SimpleDateFormat(ti);
            Date currentTime = new Date();
            String dateString = myFmt.format(currentTime);
            return dateString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region 14.字符串转时间
    public static Date StringToDate(String str,String ti) {
        try {
            SimpleDateFormat myFmt = new SimpleDateFormat(ti);
            if (str != null && str.length() != 0) {
                Date a = myFmt.parse(str);
                return a;
            }
            return null;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region 15.判断字符串中是否包含中文
    /**
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        if (null != str && str != "") {
            Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
            Matcher m = p.matcher(str);
            if (m.find()) {
                return true;
            }
        }

        return false;
    }
    //endregion

    //region 16.二进制字符串BASE64编码
    public static String EncodeBase64(Object code)  {
        try {
            if (code!=null){
                if (code instanceof Blob) {
                    int size = (int) ((Blob) code).length();
                    byte[] msgContent = ((Blob) code).getBytes(1, size);
                    return Base64.encodeToString(msgContent, Base64.NO_WRAP);
                } else {
                    return Base64.encodeToString((byte[]) code, Base64.NO_WRAP);
                }
            }

            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region 17.BASE64编码转图片
    public static Bitmap DecodeBase64ToPic(String code){
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(code, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    //endregion

    //region 18.判断是否是身份证号
    public static boolean isIDCardNumber(String IDNumber) {
        if (IDNumber == null || "".equals(IDNumber)) {
            return false;
        }
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾


        boolean matches = IDNumber.matches(regularExpression);

        //判断第18位校验值
        if (matches) {

            if (IDNumber.length() == 18) {
                try {
                    char[] charArray = IDNumber.toCharArray();
                    //前十七位加权因子
                    int[] idCardWi = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
                    //这是除以11后，可能产生的11位余数对应的验证码
                    String[] idCardY = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
                    int sum = 0;
                    for (int i = 0; i < idCardWi.length; i++) {
                        int current = Integer.parseInt(String.valueOf(charArray[i]));
                        int count = current * idCardWi[i];
                        sum += count;
                    }
                    char idCardLast = charArray[17];
                    int idCardMod = sum % 11;
                    if (idCardY[idCardMod].toUpperCase().equals(String.valueOf(idCardLast).toUpperCase())) {
                        return true;
                    } else {
                        return false;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

        }
        return matches;
    }

    //endregion

    //region 19.单号赋值
    public static String[] getYunDanHao(String num){
        String[] strings = {"","",""};
        switch (num.length()){
            case  3:
                strings[0] = num;
                break;
            case  11:
                strings[0] = num.substring(0,3);
                strings[1] = num.substring(3);
                break;
            default:
                strings[1] = num;
        }


        return strings;
    }
    //endregion

    //region 20. 将文件转化为二进制的数据
    public static byte[] FileToByte(String inFile) {
        InputStream in = null;

        try {
            in = new FileInputStream(new File(inFile));
            int len = in.available();
            byte[] result = new byte[len];
            in.read(result);

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    //endregion

    //region 21.加载本地图片
    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    //endregion

    //region 20.是否点击过快
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    //endregion

    //region 21.对象转字符串
    public static String ObjectToString(Object obj) {
        try {
            String value1 = "";

            if (null != obj) {
                if (obj instanceof BigDecimal) {
                    value1 = String.format("%.2f", (BigDecimal) obj);
                } else if (obj instanceof Float) {
                    value1 = String.format("%.2f", (Float)obj);
                } else if (obj instanceof Double) {
                    value1 = String.format("%.2f", (Double) obj);
                }else if (obj instanceof Date) {
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    value1 = sdf.format((Date) obj);
                }else {
                    value1 = obj.toString();
                }

                return value1;
            }

            return "";
        }  catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    //endregion

    //region 22.判断相机是否可用
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            canUse = false;
        }
        if (canUse) {
            mCamera.release();
            mCamera = null;
        }
        return canUse;
    }
//endregion
}

