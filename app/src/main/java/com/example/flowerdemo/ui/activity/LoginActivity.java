package com.example.flowerdemo.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.util.CursorWindowUtil;
import com.example.flowerdemo.util.KeyBoardUtils;

/**
 * @author Run
 * @date 2022/3/3
 * @description 登录页
 */
public class LoginActivity extends BaseActivity {
    private Button mLogin, mRegister;
    private EditText mEtUser, mEtPassword;
    private CheckBox mCbUser, mCbPassword;

    private SharedPreferences mCbPreferences;
    private String mSqlUser, mSqlPass;  //SQLite数据库中获取用户名密码
    private String mStrUser, mStrPass;  //将EditText用户名密码转换成String类型
    private String mKeyUSer;            //在数据库中搜索到的与输入的用户名相同的user_name
    private SharedPreferences.Editor editor;
    private MyDatabaseHelper dbHelper;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLogin = (Button) findViewById(R.id.bt_login);
        mRegister = (Button) findViewById(R.id.bt_register_login);

        mEtUser = (EditText) findViewById(R.id.et_phone);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mCbUser = (CheckBox) findViewById(R.id.cb_remember_phone);
        mCbPassword = (CheckBox) findViewById(R.id.cb_remember_password);

        mCbPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        editor = mCbPreferences.edit();
        dbHelper = new MyDatabaseHelper(LoginActivity.this, "usersStore.db", null, 1);
        dbHelper.getWritableDatabase();

        mStrUser = mCbPreferences.getString("userText", null);
        mStrPass = mCbPreferences.getString("passText", null);
        if (mStrUser == null) {
            mCbUser.setChecked(false);
        } else {
            mEtUser.setText(mStrUser);
            mCbUser.setChecked(true);
        }

        if (mStrPass == null) {
            mCbPassword.setChecked(false);
        } else {
            mEtPassword.setText(mStrPass);
            mCbPassword.setChecked(true);
        }

        mLogin.setOnClickListener(view -> {
            mStrUser = mEtUser.getText().toString().trim();
            mStrPass = mEtPassword.getText().toString().trim();
            mKeyUSer = "SELECT*FROM users where user_name like '%" + mStrUser + "%'";
            SQLiteDatabase db1 = dbHelper.getReadableDatabase();
            Cursor cursor1 = db1.rawQuery(mKeyUSer, null);
            CursorWindowUtil.cw(cursor1);
            if (cursor1.moveToFirst()) {
                do {
                    mSqlUser = cursor1.getString(cursor1.getColumnIndex("user_name"));
                    mSqlPass = cursor1.getString(cursor1.getColumnIndex("user_code"));
                } while (cursor1.moveToNext());
            }
            cursor1.close();
            if (TextUtils.isEmpty(mStrUser)) {
                Toast.makeText(LoginActivity.this, "账号不能为空！", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(mStrPass)) {
                Toast.makeText(LoginActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
            } else if (mStrUser.equals(mSqlUser) && mStrPass.equals(mSqlPass)) {
                Intent UserIntent = new Intent(LoginActivity.this, UserActivity.class);
                UserIntent.putExtra("mine_user", mSqlUser);
                startActivity(UserIntent);
                if (mCbUser.isChecked()) {
                    //如果用户选择了记住账号
                    //将用户输入的账号存入储存中，键为userTest
                    editor.putString("userText", mStrUser);
                } else {
                    //否则将账号清除
                    editor.remove("userText");
                }
                editor.commit();
                if (mCbPassword.isChecked()) {
                    //如果用户选择了记住密码
                    //将用户输入的密码存入储存中，键为passText
                    editor.putString("passText", mStrPass);
                } else {
                    //否则将端口清除
                    editor.remove("passText");
                }
                editor.commit();
            } else if (mStrUser.equals("admin") && mStrPass.equals("0000")) {
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                Intent AdIntent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(AdIntent);
                if (mCbUser.isChecked()) {
                    editor.putString("userText", mStrUser);
                } else {
                    editor.remove("userText");
                }
                editor.commit();
                if (mCbPassword.isChecked()) {
                    editor.putString("passText", mStrPass);
                } else {
                    editor.remove("passText");
                }
                editor.commit();
            } else {
                Toast.makeText(LoginActivity.this, "账号或密码输入有误，请更正后重新输入！", Toast.LENGTH_SHORT).show();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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


