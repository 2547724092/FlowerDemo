package com.example.flowerdemo.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.util.BitmapUtil;
import com.example.flowerdemo.util.CursorWindowUtil;
import com.example.flowerdemo.util.KeyBoardUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Run
 * @date 2022/3/3
 * @description 注册页
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mPhone, mPassword, mSurePassword;
    private MyDatabaseHelper dbHelper;
    private ImageView userPic;
    private Button mRegister, mCancel;
    private RelativeLayout register, wait;
    private Bitmap bitmap = null;
    private Bitmap loadBitmap = null;
    private String imageUri, name1, mKeyUSer, mSqlUser, code1, code2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (savedInstanceState != null) {
            name1 = savedInstanceState.getString("name1");
            code1 = savedInstanceState.getString("code1");
            code2 = savedInstanceState.getString("code2");
            mPhone.setText(name1);
            mPassword.setText(code1);
            mSurePassword.setText(code2);
        }

        mPhone = findViewById(R.id.et_phone_register);
        mPassword = findViewById(R.id.et_password_register);
        mSurePassword = findViewById(R.id.et_password_sure_register);

        mRegister = findViewById(R.id.bt_register);
        mCancel = findViewById(R.id.bt_cancel_register);
        userPic = findViewById(R.id.iv_login_head);
        mRegister.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        userPic.setOnClickListener(this);

        register = findViewById(R.id.rl_register);
        wait = findViewById(R.id.rl_wait);
        register.setVisibility(View.VISIBLE);
        wait.setVisibility(View.GONE);

        dbHelper = new MyDatabaseHelper(RegisterActivity.this, "usersStore.db", null, 1);
        dbHelper.getWritableDatabase();

        imageUri = getIntent().getStringExtra("imageUri");
        if (imageUri != null) {
            /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(imageUri)));
                Log.i("宽高", bitmap.getWidth() + "*" + bitmap.getHeight());
                int width = (int) (BitmapUtil.checkHW(bitmap) * 0.9);
                loadBitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, width);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            userPic.setImageBitmap(loadBitmap);//显示到ImageView上
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_login_head:
                startActivity(new Intent(RegisterActivity.this, PicModeActivity.class));
                break;
            case R.id.bt_cancel_register:
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
                break;
            case R.id.bt_register:
                register.setVisibility(View.GONE);
                wait.setVisibility(View.VISIBLE);
                register();
                break;
        }
    }

    @SuppressLint("Range")
    public void register() {
        name1 = mPhone.getText().toString();
        code1 = mPassword.getText().toString();
        code2 = mSurePassword.getText().toString();

        if (name1.equals("") || code1.equals("") || code2.equals("") || !code1.equals(code2)) {
            Toast.makeText(RegisterActivity.this, "注册失败，请更正后重新注册", Toast.LENGTH_SHORT).show();
            register.setVisibility(View.VISIBLE);
            wait.setVisibility(View.GONE);
            mPhone.setText("");
            mPassword.setText("");
            mSurePassword.setText("");
            return;
        }

        mKeyUSer = "SELECT*FROM users where user_name like '%" + name1 + "%'";
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(mKeyUSer, null);
        CursorWindowUtil.cw(cursor1);
        if (cursor1.moveToFirst()) {
            do {
                mSqlUser = cursor1.getString(cursor1.getColumnIndex("user_name"));
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        if (name1.equals(mSqlUser)) {
            Toast.makeText(this, "该账号已被注册", Toast.LENGTH_LONG).show();
            register.setVisibility(View.VISIBLE);
            wait.setVisibility(View.GONE);
            return;
        }

        if (code1.length() <= 5 || code1.length() > 9) {
            Toast.makeText(this, "密码长度在6-9位", Toast.LENGTH_LONG).show();
            register.setVisibility(View.VISIBLE);
            wait.setVisibility(View.GONE);
            return;
        }

        myThread();
        imageUri = null;
        userPic.setImageResource(R.mipmap.ic_register);
    }

    public byte[] bitmapToBytes() {
        //将图片转化为位图
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_register);
        }
        //裁剪中间位置正方形
        int width = (int) (BitmapUtil.checkHW(bitmap) * 0.9);
        loadBitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, width);
        int size = loadBitmap.getWidth() * loadBitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            loadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        } catch (Exception e) {
        } finally {
            try {
                loadBitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public void myThread() {//创建子线程
        Thread myThread = new Thread(() -> {
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user_name", name1);
                values.put("user_code", code2);
                values.put("avatar", bitmapToBytes());//图片转为二进制
                db.insert("users", null, values);
                values.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        myThread.start();
        new Handler().postDelayed(() -> {
            Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            finish();
        }, 2000);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("name1", name1);
        outState.putString("code1", code1);
        outState.putString("code2", code2);
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