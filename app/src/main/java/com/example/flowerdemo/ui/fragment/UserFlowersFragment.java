package com.example.flowerdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.list.FlowersData;
import com.example.flowerdemo.adapter.UserFlowersAdapter;
import com.example.flowerdemo.list.UserData;
import com.example.flowerdemo.ui.activity.FlowersDetailActivity;
import com.example.flowerdemo.util.ToastDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/5/15
 * 描述 : 用户模式鲜花列表
 */


public class UserFlowersFragment extends Fragment {

    private MyDatabaseHelper dbHelper;
    private ListView mLvAdFlowers;
    private UserFlowersAdapter adapter;
    private List<FlowersData> mListItems;
    private String name, price, user_name;
    private byte[] bytePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_flowers, container, false);
    }

    @SuppressLint("Range")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "flowersStore.db", null, 1);
        dbHelper.getWritableDatabase();

        Bundle bundle = getArguments();
        assert bundle != null;
        user_name = (String) bundle.getString("mine_user");

        mListItems = new ArrayList<>();
        adapter = new UserFlowersAdapter(mListItems, getActivity());

        mLvAdFlowers = (ListView) getActivity().findViewById(R.id.lv_user_flowers);

        String selectQuery = "SELECT*FROM flowers ";
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        Cursor cursor = db1.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                FlowersData mFlowersData = new FlowersData();
                name = cursor.getString(cursor.getColumnIndex("name"));
                price = cursor.getString(cursor.getColumnIndex("price"));
                bytePic = cursor.getBlob(cursor.getColumnIndex("pic"));

                mFlowersData.name = name;
                mFlowersData.price = price;
                mFlowersData.bytePic = bytePic;
                mListItems.add(mFlowersData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        mLvAdFlowers.setAdapter(adapter);

        mLvAdFlowers.setOnItemClickListener((parent, view, position, id) -> {
            FlowersData flowersData = mListItems.get(position);
            Log.i("输出输出", flowersData.getName());
            Intent intent = new Intent(getActivity(), FlowersDetailActivity.class);
            intent.putExtra("name", flowersData.getName());
            intent.putExtra("user_name", user_name);
            startActivity(intent);
        });
    }

}
