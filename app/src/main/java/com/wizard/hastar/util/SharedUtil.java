package com.wizard.hastar.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences存储数据工具
 *
 * @author cwenling
 * @version 1.0
 * @since 2017/3/3 11:29
 */
public class SharedUtil {

    private static final String SHARED_PRE_NAME = "MyAndroidFrame";

    /**
     * 保存String类型
     *
     * @param context 上下文
     * @param key     key
     * @param val     value
     */
    public static void putString(Context context, String key, String val) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putString(key, val);
//		editor.apply();//异步提交
        editor.commit();//同步提交
    }

    /**
     * 保存boolean类型
     *
     * @param context 上下文
     * @param key     key
     * @param val     value
     */
    public static void putBoolean(Context context, String key, boolean val) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putBoolean(key, val);
        editor.commit();
    }

    /**
     * 保存int值
     *
     * @param context 上下文
     * @param key     key
     */
    public static void putInteger(Context context, String key, int val) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(key, val);
        editor.commit();
    }

    /**
     * 保存long值
     * @param context      上下文
     * @param key          key
     * @param val          value
     */
    public static void putLong(Context context, String key, long val) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putLong(key, val);
        editor.commit();
    }

    /**
     * 获取String值（出现异常或者值不存在时）defaultVal
     *
     * @param context    上下文
     * @param key        key
     * @param defaultVal 默认value
     */
    public static String getString(Context context, String key,
                                   String defaultVal) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key, defaultVal);
    }

    /**
     * 获取boolean值.默认（出现异常或者值不存在时）defaultVal
     *
     * @param context 上下文
     * @param key     key
     */
    public static boolean getBoolean(Context context, String key,
                                     boolean defaultVal) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultVal);
    }

    /**
     * 获取int值.默认（出现异常或者值不存在时）defaultVal
     *
     * @param context 上下文
     * @param key     key
     */
    public static int getInteger(Context context, String key,
                                 int defaultVal) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(key, defaultVal);
    }

    /**
     * 获取long值.默认（出现异常或者值不存在时）defaultVal
     *
     * @param context 上下文
     * @param key     key
     */
    public static long getLong(Context context, String key,
                                 long defaultVal) {
        SharedPreferences prefs = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE);
        return prefs.getLong(key, defaultVal);
    }

    /**
     * 删除指定key
     * @param context   上下文
     * @param key       key
     */
    public static void delete(Context context, String key){
        Editor editor = context.getSharedPreferences(
                SHARED_PRE_NAME, Context.MODE_PRIVATE).edit();
        editor.remove(key);
        editor.commit();
    }


}
