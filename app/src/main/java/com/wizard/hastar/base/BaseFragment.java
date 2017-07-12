package com.wizard.hastar.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基础Fragment
 * @author cwenling
 * @version 1.0
 * @since 2017/4/28 11:06
 */
public abstract class BaseFragment extends Fragment{

    public View mLayoutView;

    /**
     * 初始化布局
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图
     */
    public abstract void initView();

    /**
     * 初始化视图
     */
    public abstract void initData();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null != mLayoutView) {
            //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
            ViewGroup parent = (ViewGroup) mLayoutView.getParent();
            if (null != parent) {
                parent.removeView(mLayoutView);
            }
        } else {
            //初始
            mLayoutView = inflater.inflate(getLayoutId(), null);
            initView();
            initData();
        }
        return mLayoutView;
    }
}
