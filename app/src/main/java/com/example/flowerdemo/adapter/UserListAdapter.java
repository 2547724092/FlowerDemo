package com.example.flowerdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flowerdemo.R;
import com.example.flowerdemo.list.UserData;

import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 用户管理adapter
 */

public class UserListAdapter extends BaseAdapter {

    private final List<UserData> mUserDataList;
    private final LayoutInflater inflater;

    public UserListAdapter(List<UserData> mUserDataList, Context context) {
        this.mUserDataList = mUserDataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mUserDataList == null ? 0 : mUserDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_user, null);
        UserData mUserData = (UserData) getItem(position);

        TextView tv_name = (TextView) view.findViewById(R.id.tv_name_title);
        TextView tv_password = (TextView) view.findViewById(R.id.tv_password_title);
        ImageView iv_photo = (ImageView) view.findViewById(R.id.iv_user_pic);

        tv_name.setText("用户名 : " + mUserData.getName());
        tv_password.setText("密   码 :  " + mUserData.getPassword());
        byte[] bytePic = mUserData.getBytePic();
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
        iv_photo.setImageBitmap(imageBitmap);

        return view;
    }
}
