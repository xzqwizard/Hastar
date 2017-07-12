package com.wizard.hastar.adapter.main_card_view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wizard.hastar.R;
import com.wizard.hastar.ui.HomePageActivity;
import com.wizard.hastar.ui.SettingActivity;
import com.wizard.hastar.ui.money_manager.activity.MoneyMainActivity;
import com.wizard.hastar.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jameson on 8/30/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    private List<Integer> mList = new ArrayList<>();
    private CardAdapterHelper mCardAdapterHelper = new CardAdapterHelper();
    private Activity activity;

    public CardAdapter(Activity activity, List<Integer> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_card_item, parent, false);
        mCardAdapterHelper.onCreateViewHolder(parent, itemView);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        mCardAdapterHelper.onBindViewHolder(holder.itemView, position, getItemCount());
        holder.mImageView.setImageResource(mList.get(position));
        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.displayShortToast(holder.mImageView.getContext(), "" + position);
                switch (position) {
                    case 0:
                        activity.startActivity(new Intent(activity, MoneyMainActivity.class));
                        break;
                    case 1:
                        activity.startActivity(new Intent(activity, HomePageActivity.class));
                        break;
                    case 2:
                        activity.startActivity(new Intent(activity, SettingActivity.class));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mImageView;

        public ViewHolder(final View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView);
        }

    }

}
