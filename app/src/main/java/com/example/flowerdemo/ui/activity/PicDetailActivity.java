package com.example.flowerdemo.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.R;

import java.io.File;

/**
 * 作者 : Run
 * 日期 : 2022/4/15
 * 描述 : 查看大图
 */


public class PicDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_detail);

        ImageView detailImage = findViewById(R.id.iv_detail);
        LinearLayout back = findViewById(R.id.ll_back);
        back.setOnClickListener(view -> finish());

        File imageFile = new File(Environment.getExternalStorageDirectory().getPath() + "/flowers.jpg");
        Glide.with(this).load(imageFile).apply(mOptions).into(detailImage);

    }

    private RequestOptions mOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true);
}
