package com.wizard.hastar.ui.money_manager.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.DrawerMonthViewRecyclerViewAdapter;
import com.wizard.hastar.adapter.MonthViewFragmentAdapter;
import com.wizard.hastar.base.BaseActivity;
import com.wizard.hastar.ui.money_manager.util.RecordManager;
import com.wizard.hastar.ui.money_manager.util.SettingManager;
import com.wizard.hastar.util.HaStarUtil;
import com.wizard.hastar.util.ToastUtil;
import com.wizard.hastar.widget.CustomSliderView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookMonthViewActivity extends BaseActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    private MonthViewFragmentAdapter monthModeAdapter = null;

    private Context mContext;

    private RecyclerView recyclerView;
    private DrawerMonthViewRecyclerViewAdapter drawerMonthViewRecyclerViewAdapter;

    private TextView userName;
    private TextView userEmail;

    private CircleImageView profileImage;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        setContentView(R.layout.activity_account_book_month_view);

        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userName.setTypeface(HaStarUtil.typefaceLatoRegular);
        userEmail.setTypeface(HaStarUtil.typefaceLatoLight);


        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        View view = mViewPager.getRootView();
        TextView title = (TextView) view.findViewById(R.id.logo_white);
        title.setTypeface(HaStarUtil.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(HaStarUtil.GetTypeface(), Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(false);
                actionBar.setHomeButtonEnabled(true);
            }
        }

        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.notifyHeaderChanged();
                }
            });
        }

        monthModeAdapter = new MonthViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(monthModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(monthModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        if (monthModeAdapter.getCount() == 1) {
            mViewPager.getPagerTitleStrip().setVisibility(View.INVISIBLE);
        }

        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                return HeaderDesign.fromColorAndDrawable(
                        HaStarUtil.GetTagColor(RecordManager.TAGS.get(page).getId()),
                        HaStarUtil.GetTagDrawable(-3)
                );
            }
        });

        recyclerView = (RecyclerView) mDrawer.findViewById(R.id.recycler_view);
        drawerMonthViewRecyclerViewAdapter = new DrawerMonthViewRecyclerViewAdapter(mContext);
        recyclerView.setAdapter(drawerMonthViewRecyclerViewAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        drawerMonthViewRecyclerViewAdapter.SetOnItemClickListener(new DrawerMonthViewRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mViewPager.getViewPager().setCurrentItem(position);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mDrawer.closeDrawers();
                    }
                }, 700);
            }
        });

        profileImage = (CircleImageView) mDrawer.findViewById(R.id.profile_image);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingManager.getInstance().getLoggenOn()) {
                    ToastUtil.displayShortToast(mContext, "请在设置界面更改头像");
                } else {
                    ToastUtil.displayShortToast(mContext, "请在设置界面登录或注册");
                }
            }
        });

        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        HashMap<String, Integer> urls = HaStarUtil.GetDrawerTopUrl();

        for (String name : urls.keySet()) {
            CustomSliderView customSliderView = new CustomSliderView(this);
            // initialize a SliderLayout
            customSliderView
                    .image(urls.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.addSlider(customSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));

        loadLogo();

    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onResume() {

        if (mDemoSlider != null) mDemoSlider.startAutoCycle();

        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MaterialViewPagerHelper.unregister(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    private void loadLogo() {
        //TODO加载头像
    }

}
