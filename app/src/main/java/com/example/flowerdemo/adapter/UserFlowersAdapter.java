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
import com.example.flowerdemo.list.FlowersData;

import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 管理员模式鲜花adapter
 */

public class UserFlowersAdapter extends BaseAdapter {

    private final List<FlowersData> mFlowersData;
    private final LayoutInflater inflater;

    public UserFlowersAdapter(List<FlowersData> mFlowersData, Context context) {
        this.mFlowersData = mFlowersData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mFlowersData == null ? 0 : mFlowersData.size();
    }

    @Override
    public Object getItem(int position) {
        return mFlowersData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_user_flowers, null);
        FlowersData mFlowersData = (FlowersData) getItem(position);

        TextView tv_name = (TextView) view.findViewById(R.id.tv_user_name);
        TextView tv_price = (TextView) view.findViewById(R.id.tv_user_price);
        ImageView iv_photo = (ImageView) view.findViewById(R.id.iv_user_flowers);

        tv_name.setText("名称：" + mFlowersData.getName());
        tv_price.setText("价格：" + mFlowersData.getPrice() + "元/支");
        byte[] bytePic = mFlowersData.getBytePic();
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(bytePic, 0, bytePic.length);
        iv_photo.setImageBitmap(imageBitmap);
        return view;
    }
}
