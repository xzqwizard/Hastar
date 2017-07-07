package com.wizard.hastar.ui.money_manager.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.TagChooseGridViewAdapter;
import com.wizard.hastar.ui.money_manager.util.RecordManager;
import com.wizard.hastar.widget.MyGridView;


/**
 * 作者：HastarWizard
 * on 2017-07-06 14:11
 * 邮箱：297155403@qq.com
 */

public class TagChooseFragment extends Fragment {

    public TagChooseGridViewAdapter getTagAdapter() {
        return tagAdapter;
    }

    public void setTagAdapter(TagChooseGridViewAdapter tagAdapter) {
        this.tagAdapter = tagAdapter;
    }

    private TagChooseGridViewAdapter tagAdapter;
    private int fragmentPosition;
    public MyGridView myGridView;

    Activity activity;

    static public TagChooseFragment newInstance(int position) {
        TagChooseFragment fragment = new TagChooseFragment();

        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity = (Activity)context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tag_choose, container, false);
        myGridView = (MyGridView)view.findViewById(R.id.gridview);

        fragmentPosition = getArguments().getInt("position");

        if (fragmentPosition >= FragmentManager.tagChooseFragments.size()) {
            while (fragmentPosition >= FragmentManager.tagChooseFragments.size()) {
                FragmentManager.tagChooseFragments.add(new TagChooseFragment());
            }
        }
        FragmentManager.tagChooseFragments.set(fragmentPosition, this);

        tagAdapter = new TagChooseGridViewAdapter(getActivity(), fragmentPosition);

        myGridView.setAdapter(tagAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ((OnTagItemSelectedListener)activity).onTagItemPicked(position);
                    ((OnTagItemSelectedListener)activity).onAnimationStart(RecordManager.TAGS.get(fragmentPosition * 8 + position + 2).getId());
                } catch (ClassCastException cce){
                    cce.printStackTrace();
                }
            }
        });
        return view;
    }

    public interface OnTagItemSelectedListener {
        void onTagItemPicked(int position);
        void onAnimationStart(int id);
    }

    public void updateTags() {
        ((BaseAdapter)myGridView.getAdapter()).notifyDataSetChanged();
        ((BaseAdapter)myGridView.getAdapter()).notifyDataSetInvalidated();
        myGridView.invalidateViews();
    }

}
