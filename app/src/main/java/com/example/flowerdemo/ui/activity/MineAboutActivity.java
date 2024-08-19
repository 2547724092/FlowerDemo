package com.example.flowerdemo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.R;

/**
 * @author Run
 * @date 2022/2/23
 * @description 用户模式：“我”-关于我们
 */
public class MineAboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_about);

        ImageView back = (ImageView) this.findViewById(R.id.iv_back);
        back.setOnClickListener(v -> finish());

        TextView about = (TextView) findViewById(R.id.tv_about);
        about.setText("" +
                "       对管理人员来讲，方便鲜花库存和鲜花订单的管理，减少鲜花管理" +
                "员的工作量并使其能更有效的管理" +
                "，实现了传统的鲜花管理工作的信息化建设。\n" +
                "\n" +
                "       对于消费者来讲，方便查询鲜花的名称、种类、价格、产地、" +
                "等，大幅度提高了消费者的购买体验和便捷度。");
    }
}
