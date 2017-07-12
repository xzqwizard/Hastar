package com.wizard.hastar.ui.money_manager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.wizard.hastar.R;
import com.wizard.hastar.adapter.ButtonGridViewAdapter;
import com.wizard.hastar.adapter.EditMoneyRemarkFragmentAdapter;
import com.wizard.hastar.adapter.TagChooseFragmentAdapter;
import com.wizard.hastar.base.BaseActivity;
import com.wizard.hastar.ui.money_manager.fragment.FragmentManager;
import com.wizard.hastar.ui.money_manager.fragment.TagChooseFragment;
import com.wizard.hastar.ui.money_manager.model.Record;
import com.wizard.hastar.ui.money_manager.util.RecordManager;
import com.wizard.hastar.util.HaStarUtil;
import com.wizard.hastar.util.ToastUtil;
import com.wizard.hastar.widget.MyGridView;
import com.wizard.hastar.widget.ScrollableViewPager;

import net.steamcrafted.materialiconlib.MaterialIconView;

public class EditRecordActivity extends BaseActivity
        implements TagChooseFragment.OnTagItemSelectedListener {

    private Context mContext;
    private boolean IS_CHANGED = false;
    private boolean FIRST_EDIT = true;
    private int position = -1;

    private ViewPager tagViewPager;
    private TagChooseFragmentAdapter tagAdapter;

    private ScrollableViewPager editViewPager;
    private EditMoneyRemarkFragmentAdapter editAdapter;

    private MyGridView myGridView;
    private ButtonGridViewAdapter myGridViewAdapter;

    private MaterialIconView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_record);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        mContext = this;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("POSITION");
            HaStarUtil.editRecordPosition = RecordManager.SELECTED_RECORDS.size() - 1 - position;
        } else {
            HaStarUtil.editRecordPosition = -1;
        }

// edit viewpager///////////////////////////////////////////////////////////////////////////////////
        editViewPager = (ScrollableViewPager) findViewById(R.id.edit_pager);
        editViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        editAdapter = new EditMoneyRemarkFragmentAdapter(
                getSupportFragmentManager(), FragmentManager.EDIT_RECORD_ACTIVITY_FRAGMENT);

        editViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 1) {
                    FragmentManager.editRecordActivityEditRemarkFragment.editRequestFocus();
                } else {
                    FragmentManager.editRecordActivityEditMoneyFragment.editRequestFocus();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        editViewPager.setAdapter(editAdapter);

// tag viewpager////////////////////////////////////////////////////////////////////////////////////
        tagViewPager = (ViewPager) findViewById(R.id.viewpager);
        tagViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (RecordManager.TAGS.size() % 8 == 0)
            tagAdapter = new TagChooseFragmentAdapter(getSupportFragmentManager(), RecordManager.TAGS.size() / 8);
        else
            tagAdapter = new TagChooseFragmentAdapter(getSupportFragmentManager(), RecordManager.TAGS.size() / 8 + 1);

        tagViewPager.setAdapter(tagAdapter);

        myGridView = (MyGridView) findViewById(R.id.gridview);
        myGridViewAdapter = new ButtonGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        myGridView.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom()));
                    }
                });

        back = (MaterialIconView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("IS_CHANGED", IS_CHANGED);
        intent.putExtra("POSITION", position);
        setResult(RESULT_OK, intent);

        HaStarUtil.editRecordPosition = -1;

        super.finish();
    }

    private AdapterView.OnItemClickListener gridViewClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(false, position);
        }
    };

    private AdapterView.OnItemLongClickListener gridViewLongClickListener
            = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            buttonClickOperation(true, position);
            return true;
        }
    };

    private void buttonClickOperation(boolean longClick, int position) {
        if (IS_CHANGED) {
            return;
        }
        if (FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString().equals("0")
                && !HaStarUtil.ClickButtonCommit(position)) {
            if (HaStarUtil.ClickButtonDelete(position)
                    || HaStarUtil.ClickButtonIsZero(position)) {

            } else {
                FragmentManager.editRecordActivityEditMoneyFragment.setNumberText(HaStarUtil.BUTTONS[position]);
            }
        } else {
            if (HaStarUtil.ClickButtonDelete(position)) {
                if (longClick) {
                    FragmentManager.editRecordActivityEditMoneyFragment.setNumberText("0");
                    FragmentManager.editRecordActivityEditMoneyFragment.setHelpText(" ");
                    FragmentManager.editRecordActivityEditMoneyFragment.setHelpText(
                            HaStarUtil.FLOATINGLABELS[FragmentManager.editRecordActivityEditMoneyFragment
                                    .getNumberText().toString().length()]);
                } else {
                    FragmentManager.editRecordActivityEditMoneyFragment.setNumberText(
                            FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString()
                                    .substring(0, FragmentManager.editRecordActivityEditMoneyFragment
                                            .getNumberText().toString().length() - 1));
                    if (FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString().length() == 0) {
                        FragmentManager.editRecordActivityEditMoneyFragment.setNumberText("0");
                        FragmentManager.editRecordActivityEditMoneyFragment.setHelpText(" ");
                    }
                }
            } else if (HaStarUtil.ClickButtonCommit(position)) {
                commit();
            } else {
                if (FIRST_EDIT) {
                    FragmentManager.editRecordActivityEditMoneyFragment.setNumberText(HaStarUtil.BUTTONS[position]);
                    FIRST_EDIT = false;
                } else {
                    FragmentManager.editRecordActivityEditMoneyFragment
                            .setNumberText(FragmentManager.editRecordActivityEditMoneyFragment
                                    .getNumberText().toString() + HaStarUtil.BUTTONS[position]);
                }
            }
        }
        FragmentManager.editRecordActivityEditMoneyFragment.setHelpText(HaStarUtil.FLOATINGLABELS[
                FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString().length()]);
    }

    private void commit() {
        if (FragmentManager.editRecordActivityEditMoneyFragment.getTagId() == -1) {
            ToastUtil.displayShortToast(mContext, "NO TAG");
        } else if (FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString().equals("0")) {
            ToastUtil.displayShortToast(mContext, "您花了多少钱");
        } else {
            Record coCoinRecord = new Record();
            coCoinRecord.set(RecordManager.SELECTED_RECORDS.get(RecordManager.getInstance(mContext).SELECTED_RECORDS.size() - 1 - position));
            coCoinRecord.setMoney(Float.valueOf(FragmentManager.editRecordActivityEditMoneyFragment.getNumberText().toString()));
            coCoinRecord.setTag(FragmentManager.editRecordActivityEditMoneyFragment.getTagId());
            coCoinRecord.setRemark(FragmentManager.editRecordActivityEditRemarkFragment.getRemark());
            long updateId = RecordManager.updateRecord(coCoinRecord);
            if (updateId == -1) {
                ToastUtil.displayShortToast(mContext, "额……保存失败了");
            } else {
                IS_CHANGED = true;
                RecordManager.SELECTED_RECORDS.set(RecordManager.getInstance(mContext).SELECTED_RECORDS.size() - 1 - position, coCoinRecord);
                for (int i = RecordManager.getInstance(mContext).RECORDS.size() - 1; i >= 0; i--) {
                    if (coCoinRecord.getId() == RecordManager.RECORDS.get(i).getId()) {
                        RecordManager.RECORDS.set(i, coCoinRecord);
                        break;
                    }
                }
                onBackPressed();
            }
        }
    }

    @Override
    public void onTagItemPicked(int position) {
        FragmentManager.editRecordActivityEditMoneyFragment.setTag(tagViewPager.getCurrentItem() * 8 + position + 2);
    }

    @Override
    public void onAnimationStart(int id) {

    }

    private float x1, x2, y1, y2;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (editViewPager.getCurrentItem() == 0
                        && HaStarUtil.isPointInsideView(x2, y2, editViewPager)
                        && HaStarUtil.GetScreenWidth(mContext) - x2 <= 60) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onDestroy() {
        for (int i = 0; i < FragmentManager.tagChooseFragments.size(); i++) {
            if (FragmentManager.tagChooseFragments.get(i) != null) {
                FragmentManager.tagChooseFragments.get(i).onDestroy();
                FragmentManager.tagChooseFragments.set(i, null);
            }
        }
        if (FragmentManager.editRecordActivityEditMoneyFragment != null) {
            FragmentManager.editRecordActivityEditMoneyFragment.onDestroy();
            FragmentManager.editRecordActivityEditMoneyFragment = null;
        }
        if (FragmentManager.editRecordActivityEditRemarkFragment != null) {
            FragmentManager.editRecordActivityEditRemarkFragment.onDestroy();
            FragmentManager.editRecordActivityEditRemarkFragment = null;
        }
        super.onDestroy();
    }
}
