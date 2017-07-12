package com.wizard.hastar.ui.money_manager.activity;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dev.sacot41.scviewpager.DotsView;
import com.dev.sacot41.scviewpager.SCPositionAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimation;
import com.dev.sacot41.scviewpager.SCViewAnimationUtil;
import com.dev.sacot41.scviewpager.SCViewPager;
import com.dev.sacot41.scviewpager.SCViewPagerAdapter;
import com.wizard.hastar.MyApplication;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.PasswordChangeButtonGridViewAdapter;
import com.wizard.hastar.adapter.PasswordChangeFragmentAdapter;
import com.wizard.hastar.ui.money_manager.fragment.FragmentManager;
import com.wizard.hastar.ui.money_manager.util.SettingManager;
import com.wizard.hastar.util.HaStarUtil;
import com.wizard.hastar.util.ToastUtil;
import com.wizard.hastar.widget.MyGridView;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

public class ShowActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 5;

    private SCViewPager mViewPager;
    private SCViewPagerAdapter mPageAdapter;
    private DotsView mDotsView;

    private View toolbarLayout;

    private Context mContext;

    private MyGridView myGridView;
    private PasswordChangeButtonGridViewAdapter myGridViewAdapter;

    private static final int NEW_PASSWORD = 0;
    private static final int PASSWORD_AGAIN = 1;

    private int CURRENT_STATE = NEW_PASSWORD;

    private String newPassword = "";
    private String againPassword = "";

    private ViewPager viewPager;
    private PasswordChangeFragmentAdapter passwordAdapter;

    private TextView title;

    private float x1, y1, x2, y2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        mContext = this;

        title = (TextView) findViewById(R.id.title);
        HaStarUtil.init(mContext);
        title.setTypeface(HaStarUtil.typefaceLatoLight);
        title.setText(mContext.getResources().getString(R.string.app_name));

        mViewPager = (SCViewPager) findViewById(R.id.viewpager_main_activity);
        mDotsView = (DotsView) findViewById(R.id.dotsview_main);
        mDotsView.setDotRessource(R.drawable.dot_selected, R.drawable.dot_unselected);
        mDotsView.setNumberOfPage(NUM_PAGES);

        mPageAdapter = new SCViewPagerAdapter(getSupportFragmentManager());
        mPageAdapter.setNumberOfPage(NUM_PAGES);
        mPageAdapter.setFragmentBackgroundColor(R.color.my_blue);
        mViewPager.setAdapter(mPageAdapter);
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mDotsView.selectDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final Point size = SCViewAnimationUtil.getDisplaySize(this);

        int iconOffsetX = HaStarUtil.getInstance().dpToPx(28);
        int iconOffsetY = HaStarUtil.getInstance().dpToPx(28);

        SCViewAnimation sc0 = new SCViewAnimation(findViewById(R.id.icon_4));
        sc0.startToPosition(size.x / 4 - iconOffsetX, size.y * 2 / 7 - iconOffsetY);
        sc0.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        mViewPager.addAnimation(sc0);

        SCViewAnimation sc1 = new SCViewAnimation(findViewById(R.id.icon_11));
        sc1.startToPosition(size.x * 3 / 4 - iconOffsetX, size.y * 3 / 7 - iconOffsetY);
        sc1.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(sc1);

        SCViewAnimation sc2 = new SCViewAnimation(findViewById(R.id.icon_12));
        sc2.startToPosition(size.x / 4 - iconOffsetX, size.y * 4 / 7 - iconOffsetY);
        sc2.addPageAnimation(new SCPositionAnimation(this, 0, size.x, 0));
        mViewPager.addAnimation(sc2);

        SCViewAnimation sc3 = new SCViewAnimation(findViewById(R.id.icon_19));
        sc3.startToPosition(size.x * 3 / 4 - iconOffsetX, size.y * 5 / 7 - iconOffsetY);
        sc3.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y));
        mViewPager.addAnimation(sc3);

        ((TextView) findViewById(R.id.text_0)).setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc4 = new SCViewAnimation(findViewById(R.id.text_0));
        sc4.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        mViewPager.addAnimation(sc4);

        PieChartView pie = (PieChartView) findViewById(R.id.pie);
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < 5; ++i) {
            SliceValue sliceValue = new SliceValue((float) Math.random() * 30 + 15, ContextCompat.getColor(MyApplication.getAppContext(), R.color.white));
            values.add(sliceValue);
        }
        PieChartData pieData = new PieChartData(values);
        pieData.setHasLabels(false);
        pieData.setHasLabelsOnlyForSelected(false);
        pieData.setHasLabelsOutside(false);
        pieData.setHasCenterCircle(true);
        pie.setPieChartData(pieData);
        pie.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc5 = new SCViewAnimation(pie);
        sc5.startToPosition(size.x / 2, size.y / 9 - size.y);
        sc5.addPageAnimation(new SCPositionAnimation(this, 0, 0, size.y));
        sc5.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(sc5);

        LineChartView line = (LineChartView) findViewById(R.id.line);
        List<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < 1; ++i) {
            List<PointValue> pointValues = new ArrayList<PointValue>();

            pointValues.add(new PointValue(0, 50));
            pointValues.add(new PointValue(1, 100));
            pointValues.add(new PointValue(2, 20));
            pointValues.add(new PointValue(3, 0));
            pointValues.add(new PointValue(4, 10));
            pointValues.add(new PointValue(5, 15));
            pointValues.add(new PointValue(6, 40));
            pointValues.add(new PointValue(7, 60));
            pointValues.add(new PointValue(8, 100));

            Line aLine = new Line(pointValues);
            aLine.setColor(ContextCompat.getColor(MyApplication.getAppContext(), R.color.white));
            aLine.setShape(ValueShape.CIRCLE);
            aLine.setCubic(false);
            aLine.setFilled(false);
            aLine.setHasLabels(false);
            aLine.setHasLabelsOnlyForSelected(false);
            aLine.setHasLines(true);
            aLine.setHasPoints(true);
            lines.add(aLine);
        }
        LineChartData linedata = new LineChartData(lines);
        linedata.setBaseValue(Float.NEGATIVE_INFINITY);
        line.setLineChartData(linedata);
        line.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc6 = new SCViewAnimation(line);
        sc6.startToPosition(-size.x, null);
        sc6.addPageAnimation(new SCPositionAnimation(this, 0, size.x, 0));
        sc6.addPageAnimation(new SCPositionAnimation(this, 1, size.x, 0));
        mViewPager.addAnimation(sc6);

        ColumnChartView histogram = (ColumnChartView) findViewById(R.id.histogram);
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> subcolumnValues;
        for (int i = 0; i < 5; ++i) {
            subcolumnValues = new ArrayList<SubcolumnValue>();
            for (int j = 0; j < 1; ++j) {
                subcolumnValues.add(new SubcolumnValue((float) Math.random() * 50f + 5, ContextCompat.getColor(MyApplication.getAppContext(), R.color.white)));
            }
            Column column = new Column(subcolumnValues);
            column.setHasLabels(false);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }
        ColumnChartData histogramData = new ColumnChartData(columns);
        histogram.setColumnChartData(histogramData);
        histogram.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        SCViewAnimation sc7 = new SCViewAnimation(histogram);
        sc7.startToPosition(size.x / 2 - HaStarUtil.getInstance().dpToPx(140), size.y * 8 / 9 - HaStarUtil.getInstance().dpToPx(140) + size.y);
        sc7.addPageAnimation(new SCPositionAnimation(this, 0, 0, -size.y));
        sc7.addPageAnimation(new SCPositionAnimation(this, 1, 0, size.y));
        mViewPager.addAnimation(sc7);

        ((TextView) findViewById(R.id.text_1)).setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc8 = new SCViewAnimation(findViewById(R.id.text_1));
        sc8.startToPosition(size.x, null);
        sc8.addPageAnimation(new SCPositionAnimation(this, 0, -size.x, 0));
        sc8.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        mViewPager.addAnimation(sc8);

        SCViewAnimation sc9 = new SCViewAnimation(findViewById(R.id.cloud));
        sc9.startToPosition(size.x / 2 - HaStarUtil.getInstance().dpToPx(100) + size.x, size.y / 7);
        sc9.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        sc9.addPageAnimation(new SCPositionAnimation(this, 2, 0, size.y));
        mViewPager.addAnimation(sc9);

        SCViewAnimation sc10 = new SCViewAnimation(findViewById(R.id.mobile));
        sc10.startToPosition(size.x / 2 - size.x, size.y * 6 / 7 - HaStarUtil.getInstance().dpToPx(100));
        sc10.addPageAnimation(new SCPositionAnimation(this, 1, size.x, 0));
        sc10.addPageAnimation(new SCPositionAnimation(this, 2, 0, -size.y));
        mViewPager.addAnimation(sc10);

        ((TextView) findViewById(R.id.text_2)).setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc11 = new SCViewAnimation(findViewById(R.id.text_2));
        sc11.startToPosition(size.x, null);
        sc11.addPageAnimation(new SCPositionAnimation(this, 1, -size.x, 0));
        sc11.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        mViewPager.addAnimation(sc11);

        ImageView remind1 = (ImageView) findViewById(R.id.remind_1);
        remind1.getLayoutParams().width = size.x / 3;
        remind1.getLayoutParams().height = size.x / 3 * 653 / 320;
        SCViewAnimation sc12 = new SCViewAnimation(findViewById(R.id.remind_1));
        sc12.startToPosition(size.x / 2 - size.x, size.y / 11);
        sc12.addPageAnimation(new SCPositionAnimation(this, 2, size.x, 0));
        sc12.addPageAnimation(new SCPositionAnimation(this, 3, size.x, 0));
        mViewPager.addAnimation(sc12);

        ImageView remind2 = (ImageView) findViewById(R.id.remind_2);
        remind2.getLayoutParams().width = size.x / 3;
        remind2.getLayoutParams().height = size.x / 3 * 653 / 320;
        SCViewAnimation sc13 = new SCViewAnimation(findViewById(R.id.remind_2));
        sc13.startToPosition(size.x / 2 + size.x - size.x / 3, size.y * 10 / 11 - remind1.getLayoutParams().height);
        sc13.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        sc13.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(sc13);

        ((TextView) findViewById(R.id.text_3)).setTypeface(HaStarUtil.getInstance().typefaceLatoLight);
        SCViewAnimation sc14 = new SCViewAnimation(findViewById(R.id.text_3));
        sc14.startToPosition(size.x, null);
        sc14.addPageAnimation(new SCPositionAnimation(this, 2, -size.x, 0));
        sc14.addPageAnimation(new SCPositionAnimation(this, 3, -size.x, 0));
        mViewPager.addAnimation(sc14);

        toolbarLayout = findViewById(R.id.toolbar_layout);
        SCViewAnimation toolbarLayoutAnimation = new SCViewAnimation(toolbarLayout);
        toolbarLayoutAnimation.startToPosition(null, -size.y / 2);
        toolbarLayoutAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y / 2));
        mViewPager.addAnimation(toolbarLayoutAnimation);

        passwordAdapter = new PasswordChangeFragmentAdapter(
                getSupportFragmentManager());

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewPager.setScrollBarFadeDuration(700);
        }

        viewPager.setAdapter(passwordAdapter);

        myGridView = (MyGridView) findViewById(R.id.gridview);
        myGridViewAdapter = new PasswordChangeButtonGridViewAdapter(this);
        myGridView.setAdapter(myGridViewAdapter);

        myGridView.setOnItemClickListener(gridViewClickListener);
        myGridView.setOnItemLongClickListener(gridViewLongClickListener);

        myGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        myGridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        View lastChild = myGridView.getChildAt(myGridView.getChildCount() - 1);
                        RelativeLayout.LayoutParams relativeLayout = new RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom());
                        relativeLayout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        myGridView.setLayoutParams(relativeLayout);

                        DisplayMetrics displaymetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                        int height = displaymetrics.heightPixels;
                        RelativeLayout.LayoutParams viewPagerLayoutParams
                                = new RelativeLayout.LayoutParams(viewPager.getLayoutParams().width,
                                800);
                        viewPagerLayoutParams.topMargin
                                = getStatusBarHeight() + HaStarUtil.getToolBarHeight(mContext) / 2;
                        viewPager.setLayoutParams(viewPagerLayoutParams);
                    }
                });


        SCViewAnimation gridViewAnimation = new SCViewAnimation(myGridView);
        gridViewAnimation.startToPosition(null, size.y);
        gridViewAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, -size.y));
        mViewPager.addAnimation(gridViewAnimation);

        SCViewAnimation viewpagerAnimation = new SCViewAnimation(viewPager);
        viewpagerAnimation.startToPosition(null, -size.y);
        viewpagerAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y));
        mViewPager.addAnimation(viewpagerAnimation);

        View background = findViewById(R.id.background);
        SCViewAnimation backgroundAnimation = new SCViewAnimation(background);
        backgroundAnimation.startToPosition(null, -size.y - 100);
        backgroundAnimation.addPageAnimation(new SCPositionAnimation(this, 3, 0, size.y + 100));
        mViewPager.addAnimation(backgroundAnimation);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public void finish() {
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
        switch (CURRENT_STATE) {
            case NEW_PASSWORD:
                if (HaStarUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        newPassword = "";
                    } else {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(newPassword.length() - 1);
                        if (newPassword.length() != 0)
                            newPassword = newPassword.substring(0, newPassword.length() - 1);
                    }
                } else if (HaStarUtil.ClickButtonCommit(position)) {

                } else {
                    FragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(newPassword.length());
                    newPassword += HaStarUtil.BUTTONS[position];
                    if (newPassword.length() == 4) {
                        // finish the new password input
                        CURRENT_STATE = PASSWORD_AGAIN;
                        viewPager.setCurrentItem(PASSWORD_AGAIN, true);
                    }
                }
                break;
            case PASSWORD_AGAIN:
                if (HaStarUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        againPassword = "";
                    } else {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(againPassword.length() - 1);
                        if (againPassword.length() != 0)
                            againPassword = againPassword.substring(0, againPassword.length() - 1);
                    }
                } else if (HaStarUtil.ClickButtonCommit(position)) {

                } else {
                    FragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(againPassword.length());
                    againPassword += HaStarUtil.BUTTONS[position];
                    if (againPassword.length() == 4) {
                        // if the password again is equal to the new password
                        if (againPassword.equals(newPassword)) {
                            CURRENT_STATE = -1;
                            ToastUtil.displayShortToast(mContext, "这密码还不错");
                            SettingManager.getInstance().setPassword(newPassword);
                            SettingManager.getInstance().setFirstTime(false);
                            if (SettingManager.getInstance().getLoggenOn()) {
                                //TODO
                            }
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1000);
                        } else {
                            FragmentManager.passwordChangeFragment[CURRENT_STATE].clear(4);
                            FragmentManager.passwordChangeFragment[CURRENT_STATE - 1].init();
                            CURRENT_STATE = NEW_PASSWORD;
                            viewPager.setCurrentItem(NEW_PASSWORD, true);
                            newPassword = "";
                            againPassword = "";
                            ToastUtil.displayShortToast(mContext, "两次密码不一致！");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < 3; i++) {
            FragmentManager.passwordChangeFragment[i].onDestroy();
            FragmentManager.passwordChangeFragment[i] = null;
        }
        super.onDestroy();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
