package com.wizard.hastar.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.TabFragmentPageAdapter;
import com.wizard.hastar.base.BaseActivity;
import com.wizard.hastar.util.SystemBarUtils;
import com.wizard.hastar.widget.RoundProgressBar;
import com.wizard.hastar.widget.behavior.AppBarLayoutOverScrollViewBehavior;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePageActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mIvHomepageBg;
    private Toolbar mToolBar;
    private ViewGroup mRlHomepagetitle;
    private AppBarLayout mAppBarLayout;

    private ViewGroup mLlHomepageCenterTitle;
    private RoundProgressBar mProgressBar;
    private ImageView mIvBack, mIvEdit;
    private TabLayout mTlHomePage;
    private ViewPager mVpHomePage;
    private CircleImageView mCvHomepageAvatar;
    private TextView mCvHomepageNickname;
    private LinearLayout mLlHomepageRelation;
    private CircleImageView mCvHomepageAvatarTitle;
    private TextView mTvHomepageNicknameTitle;

    private int lastState = 1;
    private List<String> titleList = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initViews();
        initListener();
        initStatus();
    }

    @Override
    protected void initData() {

    }


    private void initViews() {
        mVpHomePage = (ViewPager) findViewById(R.id.vp_homepage);
        mTlHomePage = (TabLayout) findViewById(R.id.tl_homepage);
        mIvBack = (ImageView) findViewById(R.id.iv_homepage_back);
        mIvEdit = (ImageView) findViewById(R.id.iv_homepage_edit);
        mRlHomepagetitle = (ViewGroup) findViewById(R.id.rl_homepage_title);
        mToolBar = (Toolbar) findViewById(R.id.toolbar_homepage);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.abl_homepage);
        mLlHomepageCenterTitle = (ViewGroup) findViewById(R.id.ll_homepage_centertitle);
        mProgressBar = (RoundProgressBar) findViewById(R.id.progressbar_homepage);
        mIvHomepageBg = (ImageView) findViewById(R.id.iv_homepage_bg);
        mCvHomepageNickname = (TextView) findViewById(R.id.cv_homepage_nickname);
        mLlHomepageRelation = (LinearLayout) findViewById(R.id.ll_homepage_relation);
        mCvHomepageAvatarTitle = (CircleImageView) findViewById(R.id.cv_homepage_avatar_title);
        mTvHomepageNicknameTitle = (TextView) findViewById(R.id.tv_homepage_nickname_title);
        mCvHomepageAvatar = (CircleImageView) findViewById(R.id.cv_homepage_avatar);

        titleList.add("全部");
        titleList.add("动态");
        titleList.add("产品");

        Fragment pageFragment1 = new HomePageAllFragment();
        Fragment pageFragment2 = new HomePageAllFragment();
        Fragment pageFragment3 = new HomePageAllFragment();
        fragments.add(pageFragment1);
        fragments.add(pageFragment2);
        fragments.add(pageFragment3);

        TabFragmentPageAdapter tabFragmentPageAdapter = new TabFragmentPageAdapter(getSupportFragmentManager(), fragments, titleList);
        mVpHomePage.setAdapter(tabFragmentPageAdapter);
        mTlHomePage.setupWithViewPager(mVpHomePage);
    }

    /**
     * 绑定事件
     */
    private void initListener() {

        mIvBack.setOnClickListener(this);
        mIvEdit.setOnClickListener(this);
        mLlHomepageRelation.setOnClickListener(this);
        mCvHomepageAvatar.setOnClickListener(this);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = Float.valueOf(Math.abs(verticalOffset)) / Float.valueOf(appBarLayout.getTotalScrollRange());
                if (mLlHomepageCenterTitle != null && mCvHomepageAvatar != null && mIvBack != null && mIvEdit != null) {
                    mLlHomepageCenterTitle.setAlpha(percent);
                    StatusBarUtil.setTranslucentForImageView(HomePageActivity.this, (int) (126f * percent), null);
                    if (percent == 0) {
                        groupChange(1f, 1);
                    } else if (percent == 1) {
                        if (mCvHomepageAvatar.getVisibility() != View.GONE) {
                            mCvHomepageAvatar.setVisibility(View.GONE);
                        }
                        groupChange(1f, 2);
                    } else {
                        //加上下面这个主要是防止展开时界面向上折叠出现问题
                        if (mCvHomepageAvatar.getVisibility() != View.VISIBLE) {
                            mCvHomepageAvatar.setVisibility(View.VISIBLE);
                        }
                        groupChange(percent, 0);
                    }
                }
            }
        });
        AppBarLayoutOverScrollViewBehavior myAppBarLayoutBehavoir = (AppBarLayoutOverScrollViewBehavior)
                ((CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams()).getBehavior();
        myAppBarLayoutBehavoir.setOnProgressChangeListener(new AppBarLayoutOverScrollViewBehavior.onProgressChangeListener() {
            @Override
            public void onProgressChange(float progress, boolean isRelease) {
                mProgressBar.setProgress((int) (progress * 360));
                if (progress == 1 && !mProgressBar.isSpinning && isRelease) {
                    //TODO 刷新viewpager里的fragment
                    //TODO 这样子有用么mVpHomePage.getAdapter().notifyDataSetChanged();
                }
                if (mIvEdit != null) {
                    if (progress == 0 && !mProgressBar.isSpinning) {
                        mIvEdit.setVisibility(View.VISIBLE);
                        mIvBack.setVisibility(View.VISIBLE);
                    } else if (progress > 0 && mIvBack.getVisibility() == View.VISIBLE && mIvEdit.getVisibility() == View.VISIBLE) {
                        mIvEdit.setVisibility(View.INVISIBLE);
                        mIvBack.setVisibility(View.INVISIBLE);

                    }
                }
            }
        });
    }

    /**
     * 初始化状态栏位置
     */
    private void initStatus() {
        //注意了，这里使用了第三方库 StatusBarUtil，目的是改变状态栏的alpha
        StatusBarUtil.setTransparentForImageView(HomePageActivity.this, null);
        //这里是重设我们的title布局的topMargin，StatusBarUtil提供了重设的方法，但是我们这里有两个布局
        //关于为什么不把Toolbar和@layout/home_page_head_title.xml放到一起，是因为需要Toolbar来占位，防止AppBarLayout折叠时将title顶出视野范围
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(HomePageActivity.this);
        CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) mRlHomepagetitle.getLayoutParams();
        lp1.topMargin = statusBarHeight;
        mRlHomepagetitle.setLayoutParams(lp1);
        CollapsingToolbarLayout.LayoutParams lp2 = (CollapsingToolbarLayout.LayoutParams) mToolBar.getLayoutParams();
        lp2.topMargin = statusBarHeight;
        mToolBar.setLayoutParams(lp2);
    }

    /**
     * @param alpha
     * @param state 0-正在变化 1展开 2 关闭
     */
    public void groupChange(float alpha, int state) {
        lastState = state;

        mIvBack.setAlpha(alpha);
        mIvEdit.setAlpha(alpha);
        switch (state) {
            case 1://完全展开 显示白色
                mIvBack.setImageResource(R.mipmap.back_left_white);
                mIvEdit.setImageResource(R.drawable.ic_edit_white);
                break;
            case 2://完全关闭 显示蓝色
                StatusBarUtil.setColor(HomePageActivity.this, getResources().getColor(R.color.white), 0);
                mIvBack.setImageResource(R.mipmap.back_left_blue);
                mIvEdit.setImageResource(R.drawable.ic_edit_blue);
                break;
            case 0://介于两种临界值之间 显示蓝色
                if (lastState != 0) {
                    mIvBack.setImageResource(R.mipmap.back_left_blue);
                    mIvEdit.setImageResource(R.drawable.ic_edit_blue);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_homepage_back:
                finish();
                break;
            default:
                break;
        }
    }


}
