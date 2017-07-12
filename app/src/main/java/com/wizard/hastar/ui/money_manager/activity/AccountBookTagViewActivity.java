package com.wizard.hastar.ui.money_manager.activity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.header.HeaderDesign;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.DrawerTagChooseGridViewAdapter;
import com.wizard.hastar.adapter.TagViewFragmentAdapter;
import com.wizard.hastar.base.BaseActivity;
import com.wizard.hastar.ui.money_manager.util.RecordManager;
import com.wizard.hastar.ui.money_manager.util.SettingManager;
import com.wizard.hastar.util.HaStarUtil;
import com.wizard.hastar.util.ToastUtil;
import com.wizard.hastar.widget.CustomSliderView;
import com.wizard.hastar.widget.MyGridView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountBookTagViewActivity extends BaseActivity {

    private MaterialViewPager mViewPager;

    private DrawerLayout mDrawer;
    private Toolbar toolbar;

    private TagViewFragmentAdapter tagModeAdapter = null;

    private Context mContext;

    private MyGridView myGridView;
    private DrawerTagChooseGridViewAdapter drawerTagChooseAdapter;

    private TextView userName;
    private TextView userEmail;

    private CircleImageView profileImage;

    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        RecordManager.getInstance(this);
        setContentView(R.layout.activity_account_book_tag_view);

        mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);

        View view = mViewPager.getRootView();
        TextView title = (TextView) view.findViewById(R.id.logo_white);
        title.setTypeface(HaStarUtil.typefaceLatoLight);
        title.setText(SettingManager.getInstance().getAccountBookName());

        mViewPager.getPagerTitleStrip().setTypeface(HaStarUtil.typefaceLatoLight, Typeface.NORMAL);

        setTitle("");

        toolbar = mViewPager.getToolbar();
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        userName = (TextView) findViewById(R.id.user_name);
        userEmail = (TextView) findViewById(R.id.user_email);
        userName.setTypeface(HaStarUtil.typefaceLatoRegular);
        userEmail.setTypeface(HaStarUtil.typefaceLatoLight);


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

        tagModeAdapter = new TagViewFragmentAdapter(getSupportFragmentManager());
        mViewPager.getViewPager().setOffscreenPageLimit(tagModeAdapter.getCount());
        mViewPager.getViewPager().setAdapter(tagModeAdapter);
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());

        mViewPager.clearAnimation();
        if (SettingManager.getInstance().getShowPicture()) {
            mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                @Override
                public HeaderDesign getHeaderDesign(int page) {
                    return HeaderDesign.fromColorAndUrl(
                            HaStarUtil.GetTagColor(RecordManager.TAGS.get(page).getId()),
                            HaStarUtil.GetTagUrl(RecordManager.TAGS.get(page).getId()));
                }
            });
        } else {
            mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
                @Override
                public HeaderDesign getHeaderDesign(int page) {
                    return HeaderDesign.fromColorAndDrawable(
                            HaStarUtil.GetTagColor(RecordManager.TAGS.get(page).getId()),
                            HaStarUtil.GetTagDrawable(-3));
                }
            });
        }

        myGridView = (MyGridView) mDrawer.findViewById(R.id.grid_view);
        drawerTagChooseAdapter = new DrawerTagChooseGridViewAdapter(mContext);
        myGridView.setAdapter(drawerTagChooseAdapter);

        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                YoYo.with(Techniques.Bounce).delay(0).duration(700).playOn(view);
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
    protected void onResume() {
        super.onResume();
        mDemoSlider.startAutoCycle();
    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
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

    private MaterialDialog progressDialog;

    public class LoadViews extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (progressDialog != null) progressDialog.cancel();
        }
    }

    private void loadLogo() {
        //TODO 加载头像
    }

}
