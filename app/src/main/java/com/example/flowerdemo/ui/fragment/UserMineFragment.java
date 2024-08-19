package com.example.flowerdemo.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.ui.activity.MineAboutActivity;
import com.example.flowerdemo.ui.activity.MineEdit;
import com.example.flowerdemo.ui.activity.MineUseActivity;
import com.example.flowerdemo.ui.activity.PicDetailActivity;
import com.example.flowerdemo.util.BitmapUtil;
import com.example.flowerdemo.util.CursorWindowUtil;

/**
 * @author Run
 * @date 2022/2/23
 * @description 用户模式-我
 */
public class UserMineFragment extends Fragment implements View.OnClickListener {

    private BroadcastReceiver myReceiver;
    private MyDatabaseHelper dbHelper;
    private ImageView userPic;
    private String user;
    byte[] bytePic;
    private String mKeyUSer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_mine, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RelativeLayout mRlChange = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_change);
        RelativeLayout mRlAbout = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_about);
        RelativeLayout mRlCall = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_call);
        RelativeLayout mRlUse = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_use);
        RelativeLayout mRlUpdate = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_update);
        RelativeLayout mRlExit = (RelativeLayout) getActivity().findViewById(R.id.rl_mine_exit);

        userPic = getActivity().findViewById(R.id.iv_logo);

        TextView mTvUser = (TextView) getActivity().findViewById(R.id.tv_mine_user);
        Bundle bundle = getArguments();
        assert bundle != null;
        user = (String) bundle.getString("mine_user");
        mTvUser.setText("尊敬的 " + user + " 用户您好");

        mRlChange.setOnClickListener(this);
        mRlAbout.setOnClickListener(this);
        mRlCall.setOnClickListener(this);
        mRlUse.setOnClickListener(this);
        mRlUpdate.setOnClickListener(this);
        mRlExit.setOnClickListener(this);
        userPic.setOnClickListener(this);

        lookPic();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_mine_change:
                Intent intent = new Intent(getActivity(), MineEdit.class);
                intent.putExtra("mine_user", user);
                startActivityForResult(intent, 1002);
                break;
            case R.id.rl_mine_about:
                startActivity(new Intent(getActivity(), MineAboutActivity.class));
                break;
            case R.id.rl_mine_call:
                Intent tell = new Intent(Intent.ACTION_DIAL);
                tell.setData(Uri.parse("tel:10086"));
                startActivity(tell);
                break;
            case R.id.rl_mine_use:
                startActivity(new Intent(getActivity(), MineUseActivity.class));
                break;
            case R.id.rl_mine_update:
                Toast.makeText(getActivity(), "当前为最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_mine_exit:
                getActivity().onBackPressed();
                break;
            case R.id.iv_logo:
                startActivity(new Intent(getActivity(), PicDetailActivity.class));
                break;
        }
    }

    @SuppressLint("Range")
    public void lookPic() {
        //个人头像显示
        dbHelper = new MyDatabaseHelper(getActivity(), "usersStore.db", null, 1);
        dbHelper.getWritableDatabase();
        mKeyUSer = "SELECT*FROM users where user_name like '%" + user + "%'";
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();

        Cursor cursor1 = db1.rawQuery(mKeyUSer, null);
        CursorWindowUtil.cw(cursor1);

        if (cursor1.moveToFirst()) {
            do {
                bytePic = cursor1.getBlob(cursor1.getColumnIndex("avatar"));
            } while (cursor1.moveToNext());
        }
        cursor1.close();
        if (bytePic != null) {
            //将字节数组转化为位图
            String picName = "flowers.jpg";
            String savePicPath = Environment.getExternalStorageDirectory().getPath();
            Bitmap imagebitmap = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
            BitmapUtil.saveBitmap(imagebitmap, picName, savePicPath);
            //将位图显示为图片
            if (imagebitmap != null) {
                Log.e("imagebitmap", "imagebitmap!=null");
                userPic.setImageBitmap(imagebitmap);
            } else {
                Log.e("imagebitmap", "imagebitmap==null");
            }
        } else {
            Log.e("imagebitmap", "bytePic==null");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1002 && resultCode == RESULT_OK) {
            getActivity().onBackPressed();
        }
    }
}
