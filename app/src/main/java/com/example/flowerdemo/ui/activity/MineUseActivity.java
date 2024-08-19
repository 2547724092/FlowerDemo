package com.example.flowerdemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.R;

/**
 * @author Run
 * @date 2022/2/23
 * @description 用户模式：“我”-使用说明
 */
public class MineUseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_use);

        ImageView back = (ImageView) this.findViewById(R.id.iv_back);
        back.setOnClickListener(v -> finish());
    }
}