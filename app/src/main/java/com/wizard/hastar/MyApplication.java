package com.wizard.hastar;

import android.app.Application;
import android.content.Context;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;

/**
 * 作者：HastarWizard
 * on 2017-07-06 14:11
 * 邮箱：297155403@qq.com
 */

public class MyApplication extends Application {
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;
        // 必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回
        BGASwipeBackManager.getInstance().init(this);
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}

