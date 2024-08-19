package com.example.flowerdemo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.R;

/**
 * @author Run
 * @date 2022/2/23
 * @description 欢迎页
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_splash);
        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(3000);//使程序休眠
                    Intent it = new Intent(getApplicationContext(), LoginActivity.class);//启动Enter
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}