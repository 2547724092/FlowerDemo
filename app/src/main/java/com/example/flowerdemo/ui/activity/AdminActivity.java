package com.example.flowerdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.example.flowerdemo.ui.fragment.AdminFlowersFragment;
import com.example.flowerdemo.ui.fragment.AdminListFragment;
import com.example.flowerdemo.ui.fragment.AdminUserFragment;
import com.example.flowerdemo.util.KeyBoardUtils;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Run
 * @date 2022/3/3
 * @description 管理员模式Tap主页
 */
public class AdminActivity extends BaseActivity {
    private ArrayList<Fragment> fragments = new ArrayList<>();
    List<String> tabStrings = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        AdminFlowersFragment flowersFragment = new AdminFlowersFragment();
        AdminListFragment listFragment = new AdminListFragment();
        AdminUserFragment userFragment = new AdminUserFragment();

        ViewPager viewPager = findViewById(R.id.view_pager);
        TabLayout tabLayout = findViewById(R.id.tl_title);

        fragments.add(flowersFragment);
        fragments.add(listFragment);
        fragments.add(userFragment);

        tabStrings.add("鲜花管理");
        tabStrings.add("订单管理");
        tabStrings.add("用户管理");
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
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {//获取当前获得焦点的View
            View view = getCurrentFocus();
            //调用方法判断是否需要隐藏键盘
            KeyBoardUtils.hideKeyboard(ev, view, this);
        }
        return super.dispatchTouchEvent(ev);
    }
}
