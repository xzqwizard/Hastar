package com.wizard.hastar.ui.money_manager.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.daimajia.slider.library.Tricks.FixedSpeedScroller;
import com.wizard.hastar.R;
import com.wizard.hastar.adapter.PasswordChangeButtonGridViewAdapter;
import com.wizard.hastar.adapter.PasswordChangeFragmentAdapter;
import com.wizard.hastar.base.BaseActivity;
import com.wizard.hastar.ui.money_manager.fragment.FragmentManager;
import com.wizard.hastar.ui.money_manager.util.SettingManager;
import com.wizard.hastar.util.CoCoinUtil;
import com.wizard.hastar.util.ToastUtil;
import com.wizard.hastar.widget.MyGridView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.lang.reflect.Field;


public class EditPasswordActivity extends BaseActivity {

    private Context mContext;

    private MyGridView myGridView;
    private PasswordChangeButtonGridViewAdapter myGridViewAdapter;

    private MaterialIconView back;

    private static final int VERIFY_STATE = 0;
    private static final int NEW_PASSWORD = 1;
    private static final int PASSWORD_AGAIN = 2;

    private int CURRENT_STATE = VERIFY_STATE;

    private String oldPassword = "";
    private String newPassword = "";
    private String againPassword = "";

    private ViewPager viewPager;
    private FragmentPagerAdapter adapter;


    private float x1, y1, x2, y2;

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        mContext = this;

        int currentapiVersion = Build.VERSION.SDK_INT;

        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            // Do something for lollipop and above versions
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.statusBarColor));
            }
        } else{
            // do something for phones running an SDK before lollipop
        }

        viewPager = (ViewPager)findViewById(R.id.viewpager);

        try {
            Interpolator sInterpolator = new AccelerateInterpolator();
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller
                    = new FixedSpeedScroller(viewPager.getContext(), sInterpolator);
            scroller.setFriction(1000);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }

        adapter = new PasswordChangeFragmentAdapter(getSupportFragmentManager());

        viewPager.setOffscreenPageLimit(3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            viewPager.setScrollBarFadeDuration(1000);
        }

        viewPager.setAdapter(adapter);

        myGridView = (MyGridView)findViewById(R.id.gridview);
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
                        myGridView.setLayoutParams(
                                new LinearLayout.LayoutParams(
                                        ViewGroup.LayoutParams.FILL_PARENT, lastChild.getBottom()));
                    }
                });

        back = (MaterialIconView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        title = (TextView)findViewById(R.id.title);
        title.setTypeface(CoCoinUtil.typefaceLatoLight);
        if (SettingManager.getInstance().getFirstTime()) {
            title.setText(mContext.getResources().getString(R.string.app_name));
        } else {
            title.setText(mContext.getResources().getString(R.string.change_password));
        }

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
    public void finish() {;
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
            case VERIFY_STATE:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        oldPassword = "";
                    } else {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(oldPassword.length() - 1);
                        if (oldPassword.length() != 0)
                            oldPassword = oldPassword.substring(0, oldPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    FragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(oldPassword.length());
                    oldPassword += CoCoinUtil.BUTTONS[position];
                    if (oldPassword.length() == 4) {
                        if (oldPassword.equals(SettingManager.getInstance().getPassword())) {
                            // old password correct
                            // notice that if the old password is correct,
                            // we won't go back to VERIFY_STATE any more
                            CURRENT_STATE = NEW_PASSWORD;
                            viewPager.setCurrentItem(NEW_PASSWORD, true);
                        } else {
                            // old password wrong
                            FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                    .clear(4);
                            ToastUtil.displayShortToast(mContext,"嗯……错了");
                            oldPassword = "";
                        }
                    }
                }
                break;
            case NEW_PASSWORD:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        newPassword = "";
                    } else {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(newPassword.length() - 1);
                        if (newPassword.length() != 0)
                            newPassword = newPassword.substring(0, newPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    FragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(newPassword.length());
                    newPassword += CoCoinUtil.BUTTONS[position];
                    if (newPassword.length() == 4) {
                        // finish the new password input
                        CURRENT_STATE = PASSWORD_AGAIN;
                        viewPager.setCurrentItem(PASSWORD_AGAIN, true);
                    }
                }
                break;
            case PASSWORD_AGAIN:
                if (CoCoinUtil.ClickButtonDelete(position)) {
                    if (longClick) {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE].init();
                        againPassword = "";
                    } else {
                        FragmentManager.passwordChangeFragment[CURRENT_STATE]
                                .clear(againPassword.length() - 1);
                        if (againPassword.length() != 0)
                            againPassword = againPassword.substring(0, againPassword.length() - 1);
                    }
                } else if (CoCoinUtil.ClickButtonCommit(position)) {

                } else {
                    FragmentManager.passwordChangeFragment[CURRENT_STATE]
                            .set(againPassword.length());
                    againPassword += CoCoinUtil.BUTTONS[position];
                    if (againPassword.length() == 4) {
                        // if the password again is equal to the new password
                        if (againPassword.equals(newPassword)) {
                            CURRENT_STATE = -1;
                            ToastUtil.displayShortToast(mContext,"这密码还不错");
                            SettingManager.getInstance().setPassword(newPassword);
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
                            ToastUtil.displayShortToast(mContext,"两次密码不一致！");
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = ev.getX();
                y1 = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(x1 - x2) > 20) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                x2 = ev.getX();
                y2 = ev.getY();
                if (Math.abs(x1 - x2) > 20) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        for (int i = 0; i < 3; i++) {
            FragmentManager.passwordChangeFragment[i].onDestroy();
            FragmentManager.passwordChangeFragment[i] = null;
        }
        super.onDestroy();
    }

}
