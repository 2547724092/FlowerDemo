package com.example.flowerdemo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.flowerdemo.R;
import com.example.flowerdemo.list.CommentData;
import com.example.flowerdemo.list.ListData;

import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/4/14
 * 描述 : 点评管理adapter
 */

public class CommentListAdapter extends BaseAdapter {

    private final List<CommentData> mCommentData;
    private final LayoutInflater inflater;

    public CommentListAdapter(List<CommentData> mCommentData, Context context) {
        this.mCommentData = mCommentData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mCommentData == null ? 0 : mCommentData.size();
    }

    @Override
    public Object getItem(int position) {
        return mCommentData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item_comment, null);
        CommentData mCommentData = (CommentData) getItem(position);

        TextView tv_user = (TextView) view.findViewById(R.id.tv_comment_user);
        TextView tv_name = (TextView) view.findViewById(R.id.tv_comment_name);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_comment_content);

        tv_user.setText("用户 " + mCommentData.getUser() + " 的评论：");
        tv_name.setText(mCommentData.getName());
        tv_content.setText(mCommentData.getContent());

        return view;
    }
}
