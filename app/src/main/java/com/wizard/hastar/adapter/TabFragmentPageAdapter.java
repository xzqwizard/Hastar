package com.wizard.hastar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
* 首页顶部tab导航适配器
* @author cwenling
* @version 1.0
* @since 2017/4/28 14:25
*/
public class TabFragmentPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;
    private List<String> titles;

    public TabFragmentPageAdapter(FragmentManager fm, ArrayList<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
