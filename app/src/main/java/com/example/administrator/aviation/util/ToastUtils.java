package com.example.administrator.aviation.util;

import android.content.Context;
import android.widget.Toast;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * toast弹出
 * 作用：防止用户多次点击一个按钮，多次弹出提示
 */

public class ToastUtils {
    private static String oldMsg;
    private static long time;
    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration) {
        if (!msg.equals(oldMsg)) { // 当显示的内容不一样时，即断定为不是同一个Toast
            mToast = Toast.makeText(context, msg, duration);
            mToast.show();
            time = System.currentTimeMillis();
        } else {
            // 显示内容一样时，只有间隔时间大于2秒时才显示
            if (System.currentTimeMillis() - time > 2000) {
                mToast = Toast.makeText(context, msg, duration);
                mToast.show();
                time = System.currentTimeMillis();
            }
        }
        oldMsg = msg;
    }

    public static void hideToast() {
        try {
            if (mToast != null) {
                mToast.cancel();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
