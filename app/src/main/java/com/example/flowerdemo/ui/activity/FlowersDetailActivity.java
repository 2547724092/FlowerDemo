package com.example.flowerdemo.ui.activity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.Cons;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.adapter.CommentListAdapter;
import com.example.flowerdemo.adapter.UserFlowersAdapter;
import com.example.flowerdemo.list.CommentData;
import com.example.flowerdemo.util.CursorWindowUtil;
import com.example.flowerdemo.util.KeyBoardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/5/13
 * 描述 : 用户模式鲜花详情
 */

public class FlowersDetailActivity extends BaseActivity implements View.OnClickListener {

    private MyDatabaseHelper dbHelper, commentHelper;
    private TextView mTvName, mTvKind, mTvAddress, mTvPrice, mTvNum, mTvTotal, mTvBuy;
    private TextView mTvCommentUser, mTvCommentName, mTvCommentContent, mTvCommentSend;
    private ImageView mIvBack, mIvDetail, mIvSub, mIvAdd, mIvShare;
    private String mStrName, mStrNum, mStrTotal;
    private String mStrCommentUser, mStrCommentName, mStrCommentContent;
    private String mSqlName, mSqlKind, mSqlAddress, mSqlPrice;
    private String mSqlCommentUser, mSqlCommentName, mSqlCommentContent;
    private String user_name;
    private byte[] detailPic;
    private int mIntTotal, mIntPrice, mIntNum;
    private CommentListAdapter adapter;
    private List<CommentData> mListItems;
    private ListView mLvComment;
    private EditText mEdComment;
    private SQLiteDatabase dbComment, db1;
    private ProgressBar wait;
    private Bitmap imagebitmap;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers_detail);

        mStrName = getIntent().getStringExtra("name");
        user_name = getIntent().getStringExtra("user_name");

        mEdComment = (EditText) findViewById(R.id.et_comment);
        wait = (ProgressBar) findViewById(R.id.loading_process_dialog_progressBar);

        mTvName = (TextView) findViewById(R.id.tv_detail_name);
        mTvKind = (TextView) findViewById(R.id.tv_detail_kind);
        mTvAddress = (TextView) findViewById(R.id.tv_detail_address);
        mTvPrice = (TextView) findViewById(R.id.tv_detail_price);
        mTvNum = (TextView) findViewById(R.id.tv_detail_num);
        mTvTotal = (TextView) findViewById(R.id.tv_total_num);
        mTvBuy = (TextView) findViewById(R.id.tv_buy);
        mTvCommentSend = (TextView) findViewById(R.id.tv_comment_send);

        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mIvShare = (ImageView) findViewById(R.id.iv_share);
        mIvDetail = (ImageView) findViewById(R.id.iv_flowers_detail);
        mIvSub = (ImageView) findViewById(R.id.iv_num_sub);
        mIvAdd = (ImageView) findViewById(R.id.iv_add);

        mIvBack.setOnClickListener(this);
        mIvShare.setOnClickListener(this);
        mIvSub.setOnClickListener(this);
        mIvAdd.setOnClickListener(this);
        mTvBuy.setOnClickListener(this);
        mTvCommentSend.setOnClickListener(this);

        commentHelper = new MyDatabaseHelper(this, "commentStore.db", null, 1);
        dbHelper = new MyDatabaseHelper(this, "flowersStore.db", null, 1);

        mListItems = new ArrayList<>();
        adapter = new CommentListAdapter(mListItems, this);

        mLvComment = (ListView) findViewById(R.id.lv_comment_list);

        lookComment();

        //<editor-fold desc="查询鲜花详情">
        String selectQuery = "SELECT*FROM flowers where name like '%" + mStrName + "%'";
        db1 = dbHelper.getReadableDatabase();
        Cursor cursor = db1.rawQuery(selectQuery, null);
        CursorWindowUtil.cw(cursor);
        if (cursor.moveToFirst()) {
            do {
                mSqlName = cursor.getString(cursor.getColumnIndex("name"));
                mSqlKind = cursor.getString(cursor.getColumnIndex("kind"));
                mSqlAddress = cursor.getString(cursor.getColumnIndex("address"));
                mSqlPrice = cursor.getString(cursor.getColumnIndex("price"));
                detailPic = cursor.getBlob(cursor.getColumnIndex("pic"));
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (mStrName.equals(mSqlName)) {
            mTvName.setText(mSqlName);
            mTvKind.setText(mSqlKind);
            mTvAddress.setText(mSqlAddress);
            mTvPrice.setText("￥" + mSqlPrice);

            mStrNum = mTvNum.getText().toString();
            mStrTotal = mTvTotal.getText().toString();

            imagebitmap = BitmapFactory.decodeByteArray(detailPic, 0, detailPic.length);
            mIvDetail.setImageBitmap(imagebitmap);

            mIntNum = Integer.parseInt(mTvNum.getText().toString());
            mIntPrice = Integer.parseInt(mSqlPrice);
            mIntTotal = mIntNum * mIntPrice;
            mTvTotal.setText("￥" + mIntTotal);
        }
        //</editor-fold>

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_num_sub:
                int numSub = Integer.parseInt(mTvNum.getText().toString());
                if (numSub <= 1) {
                    Toast.makeText(this, "购买数量不能小于1", Toast.LENGTH_SHORT).show();
                } else {
                    mIntNum = numSub - 1;
                    String mSub = String.valueOf(mIntNum);
                    mTvNum.setText(mSub);
                    mIntTotal = mIntNum * mIntPrice;
                    mTvTotal.setText("￥" + mIntTotal);
                    mStrNum = mTvNum.getText().toString();
                    mStrTotal = mTvTotal.getText().toString();
                }
                break;
            case R.id.iv_add:
                int numAdd = Integer.parseInt(mTvNum.getText().toString());
                mIntNum = numAdd + 1;
                String mAdd = String.valueOf(mIntNum);
                mTvNum.setText(mAdd);
                mIntTotal = mIntNum * mIntPrice;
                mTvTotal.setText("￥" + mIntTotal);
                mStrNum = mTvNum.getText().toString();
                mStrTotal = mTvTotal.getText().toString();
                break;
            case R.id.tv_buy:
                mStrNum = mTvNum.getText().toString();
                mStrTotal = mTvTotal.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date date = new Date(System.currentTimeMillis());
                Log.i("当前时间：", simpleDateFormat.format(date));
                Intent intent = new Intent(this, PaySureDialogActivity.class);
                intent.putExtra("user_name", user_name);
                intent.putExtra("name", mStrName);
                intent.putExtra("num", mStrNum);
                intent.putExtra("total", mStrTotal);
                intent.putExtra("time", simpleDateFormat.format(date));
                startActivityForResult(intent, Cons.PAY_SURE);
                break;
            case R.id.tv_comment_send:
                wait.setVisibility(View.VISIBLE);
                mStrCommentContent = mEdComment.getText().toString();
                if (mStrCommentContent.equals("") || mStrCommentContent.isEmpty()) {
                    Toast.makeText(this, "点评内容不得为空", Toast.LENGTH_SHORT).show();
                } else {

                    dbComment = commentHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("user_name", user_name);
                    values.put("name", mStrName);
                    values.put("content", mStrCommentContent);
                    dbComment.insert("comment", null, values);
                    values.clear();

                    mEdComment.setText("");
                    adapter.notifyDataSetChanged();
                    mLvComment.setAdapter(adapter);
                    mListItems.clear();
                    new Handler().postDelayed(() -> {
                        lookComment();
                        wait.setVisibility(View.GONE);
                        Toast.makeText(this, "点评成功", Toast.LENGTH_SHORT).show();
                    }, 2000);

                }
                break;
            case R.id.iv_share:
                shareMsg("鲜花详情", mStrName, mStrName + mSqlPrice + "元/支", imagebitmap);
                break;
        }
    }

    public void shareImage(Context context, Bitmap bitmap, String title) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    /**
     * 分享功能
     *
     * @param activityTitle Activity的名字
     * @param msgTitle      消息标题
     * @param msgText       消息内容
     * @param bitmap        图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         Bitmap bitmap) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (bitmap == null) {
            intent.setType("text/plain"); // 纯文本
        } else {
            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Cons.PAY_SURE) {
            finish();
        }
    }

    @SuppressLint("Range")
    public void lookComment() {
        //<editor-fold desc="评论默认显示">
        String commentQuery = "SELECT*FROM comment where name like '%" + mStrName + "%'";
        dbComment = commentHelper.getReadableDatabase();
        Cursor cursorComment = dbComment.rawQuery(commentQuery, null);
        if (cursorComment.moveToFirst()) {
            do {
                mSqlCommentUser = cursorComment.getString(cursorComment.getColumnIndex("user_name"));
                mSqlCommentName = cursorComment.getString(cursorComment.getColumnIndex("name"));
                mSqlCommentContent = cursorComment.getString(cursorComment.getColumnIndex("content"));

                if (mStrName.equals(mSqlCommentName)) {
                    CommentData mCommentData = new CommentData();
                    mCommentData.user = mSqlCommentUser;
                    mCommentData.name = mSqlCommentName;
                    mCommentData.content = mSqlCommentContent;
                    mListItems.add(mCommentData);
                }
            } while (cursorComment.moveToNext());
        }
        cursorComment.close();
        mLvComment.setAdapter(adapter);
        //</editor-fold>
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