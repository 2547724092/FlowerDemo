package com.example.flowerdemo.ui.activity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.util.ToastDialogUtil;

/**
 * 作者 : Run
 * 日期 : 2022/5/16
 * 描述 : 二次确认支付
 */


public class PaySureDialogActivity extends BaseActivity {

    private String user_name, mStrName, mStrNum, mStrTotal, mStrTime;
    private TextView mTvUserName, mTvName, mTvNum, mTvTotal, mTvTime, mTvPay;
    private ImageView mIvClose;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paysure);
        mTvUserName = (TextView) this.findViewById(R.id.tv_pay_user);
        mTvName = (TextView) this.findViewById(R.id.tv_pay_name);
        mTvNum = (TextView) this.findViewById(R.id.tv_pay_num);
        mTvTotal = (TextView) this.findViewById(R.id.tv_pay_total);
        mTvTime = (TextView) this.findViewById(R.id.tv_pay_time);
        mTvPay = (TextView) this.findViewById(R.id.tv_pay_sure);
        mIvClose = (ImageView) this.findViewById(R.id.iv_close);

        user_name = getIntent().getStringExtra("user_name");
        mStrName = getIntent().getStringExtra("name");
        mStrNum = getIntent().getStringExtra("num");
        mStrTotal = getIntent().getStringExtra("total");
        mStrTime = getIntent().getStringExtra("time");

        mTvUserName.setText("用户名：" + user_name);
        mTvName.setText("名称：" + mStrName);
        mTvNum.setText("数量：" + mStrNum);
        mTvTotal.setText("总价：" + mStrTotal);
        mTvTime.setText("下单时间：" + mStrTime);

        mIvClose.setOnClickListener(view -> finish());
        mTvPay.setOnClickListener(view -> addItem(user_name, mStrName, mStrNum, mStrTotal, mStrTime));
    }


    public void addItem(String user_name, String name, String num, String total, String time) {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this, "listStore.db", null, 1);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_name", user_name);
        values.put("name", name);
        values.put("num", num);
        values.put("total", total);
        values.put("time", time);
        db.insert("list", null, values);
        Log.i("list", "添加成功");
        ToastDialogUtil.showToast(this, "支付成功");
        values.clear();
        setResult(RESULT_OK);
        finish();
    }
}
