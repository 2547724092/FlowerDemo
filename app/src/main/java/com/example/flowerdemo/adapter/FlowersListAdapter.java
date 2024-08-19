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
import com.example.flowerdemo.list.ListData;

import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 订单管理adapter
 */

public class FlowersListAdapter extends BaseAdapter {

    private final List<ListData> mListData;
    private final LayoutInflater inflater;

    public FlowersListAdapter(List<ListData> mListData, Context context) {
        this.mListData = mListData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_list, null);
        ListData mListData = (ListData) getItem(position);

        TextView tv_user = (TextView) view.findViewById(R.id.tv_list_user);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_list_name);
        TextView tv_num = (TextView) view.findViewById(R.id.tv_list_num);
        TextView tv_total = (TextView) view.findViewById(R.id.tv_list_total);
        TextView tv_time = (TextView) view.findViewById(R.id.tv_list_time);

        tv_user.setText("用户名：" + mListData.getUser());
        tv_name.setText("名称：" + mListData.getName());
        tv_num.setText("数量：" + mListData.getNum() );
        tv_total.setText("总价：" + mListData.getTotal());
        tv_time.setText("下单时间：" + mListData.getTime());

        return view;
    }
}
