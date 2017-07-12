package com.wizard.hastar.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.wizard.hastar.R;
import com.wizard.hastar.ui.money_manager.fragment.ReportViewFragment;
import com.wizard.hastar.util.HaStarUtil;

import java.util.ArrayList;

/**
 * Created by Weiping on 2016/1/30.
 */
public class ReportTagAdapter extends BaseAdapter {

    private ArrayList<double[]> tagExpense;

    public ReportTagAdapter(ArrayList<double[]> tagExpense) {
        this.tagExpense = tagExpense;
    }

    @Override
    public int getCount() {
        return min(tagExpense.size() - 1, ReportViewFragment.MAX_TAG_EXPENSE);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_tag, null);

        ImageView icon = (ImageView)convertView.findViewById(R.id.icon);
        TextView name = (TextView)convertView.findViewById(R.id.tag_name);
        TextView expense = (TextView)convertView.findViewById(R.id.tag_expense);
        TextView records = (TextView)convertView.findViewById(R.id.tag_sum);

        name.setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        expense.setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        records.setTypeface(HaStarUtil.getInstance().typefaceLatoLight);

        icon.setImageDrawable(HaStarUtil.getInstance().GetTagIconDrawable((int)tagExpense.get(position + 1)[2]));
        name.setText(HaStarUtil.getInstance().GetTagName((int)tagExpense.get(position + 1)[2]) + HaStarUtil.getInstance().GetPurePercentString(tagExpense.get(position + 1)[1] * 100));
        expense.setText(HaStarUtil.getInstance().GetInMoney((int)tagExpense.get(position + 1)[0]));
        records.setText(HaStarUtil.getInstance().GetInRecords((int)tagExpense.get(position + 1)[3]));

        return convertView;
    }

    private int min(int a, int b) {
        return (a < b ? a : b);
    }
}
