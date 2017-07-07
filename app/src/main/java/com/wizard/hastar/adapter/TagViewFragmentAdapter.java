package com.wizard.hastar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.wizard.hastar.MyApplication;
import com.wizard.hastar.ui.money_manager.fragment.TagViewFragment;
import com.wizard.hastar.ui.money_manager.util.RecordManager;
import com.wizard.hastar.util.CoCoinUtil;


/**
 * Created by 伟平 on 2015/10/20.
 */
public class TagViewFragmentAdapter extends FragmentStatePagerAdapter {

    public TagViewFragmentAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return TagViewFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return RecordManager.getInstance(MyApplication.getAppContext()).TAGS.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CoCoinUtil.GetTagName(
                RecordManager.getInstance(MyApplication.getAppContext()).TAGS.get(position % RecordManager.TAGS.size()).getId());
    }
}
