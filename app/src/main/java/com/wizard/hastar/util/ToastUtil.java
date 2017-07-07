package com.wizard.hastar.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
* 吐司对话框工具类
* @author cwenling
* @version 1.0
* @since 2017/3/3 11:52
*/
public class ToastUtil {

    /**
     * 显示长吐司对话框，
     * @param context 上下文
     * @param text    值
     */
    public static void displayLongToast(Context context, String text) {
        displayToast(context, text, Toast.LENGTH_LONG);
    }

    /**
     * 显示短长吐司对话框，
     * @param context 上下文
     * @param text    值
     */
    public static void displayShortToast(Context context, String text) {
        displayToast(context, text, Toast.LENGTH_SHORT);
    }

    /**
     * 显示吐司对话框
     * @param context  上下文
     * @param text     值
     * @param time     时间
     */
    private static void displayToast(Context context, String text, int time) {
        if(!TextUtils.isEmpty(text)) {
            Toast.makeText(context, text, time).show();
        }
    }
}
