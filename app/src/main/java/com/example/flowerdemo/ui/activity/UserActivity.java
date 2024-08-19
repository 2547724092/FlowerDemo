package com.example.flowerdemo.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.R;
import com.example.flowerdemo.ui.fragment.UserFlowersFragment;
import com.example.flowerdemo.ui.fragment.UserMainFragment;
import com.example.flowerdemo.ui.fragment.UserMineFragment;
import com.example.flowerdemo.util.KeyBoardUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Run
 * @date 2022/3/3
 * @description 用户模式Tap主页
 */
public class UserActivity extends BaseActivity {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    List<String> tabStrings = new ArrayList<>();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private UserMainFragment mainFragment;
    private UserFlowersFragment flowerFragment;
    private UserMineFragment mineFragment;
    private BroadcastReceiver broadcastReceiver;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mainFragment = new UserMainFragment();
        flowerFragment = new UserFlowersFragment();
        mineFragment = new UserMineFragment();

        viewPager = (ViewPager) findViewById(R.id.view_pager_user);
        tabLayout = (TabLayout) findViewById(R.id.tl_title_user);

        String user = getIntent().getStringExtra("mine_user");
        Bundle bundle = new Bundle();
        bundle.putString("mine_user", user);
        flowerFragment.setArguments(bundle);
        mineFragment.setArguments(bundle);

        fragments.add(mainFragment);
        fragments.add(flowerFragment);
        fragments.add(mineFragment);

        tabStrings.add("店铺介绍");
        tabStrings.add("全部鲜花");
        tabStrings.add("我");
        if (viewPager != null) {
            viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
                @Override
                public Fragment getItem(int position) {
                    return fragments.get(position);
                }

                @Override
                public int getCount() {
                    return fragments.size();
                }

                @Override
                public void destroyItem(ViewGroup container, int position, Object object) {
                    super.destroyItem(container, position, object);
                }

                @Nullable
                @Override
                public CharSequence getPageTitle(int position) {
                    return tabStrings.get(position);
                }
            });
        } else {
            throw new NullPointerException();
        }

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
    }

    private long firstTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {//如果两次按键时间间隔大于800毫秒，则不退出
                Toast.makeText(this, "再次滑动退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                finish();
                System.gc();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 点击空白处
     * 输入框消失
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取当前获得焦点的View
                View view = getCurrentFocus();
                //调用方法判断是否需要隐藏键盘
                KeyBoardUtils.hideKeyboard(ev, view, this);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
