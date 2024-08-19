package com.example.flowerdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.adapter.UserListAdapter;
import com.example.flowerdemo.list.UserData;
import com.example.flowerdemo.util.CursorWindowUtil;
import com.example.flowerdemo.util.ToastDialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Run
 * @date 2022/3/5
 * @description 管理员模式用户管理页面
 */
public class AdminUserFragment extends Fragment {
    private MyDatabaseHelper dbHelper;
    private ListView mLvUser;
    private UserListAdapter adapter;
    byte[] bytePic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user, container, false);
        return view;
    }

    @SuppressLint("Range")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button mBtRetrieve = (Button) getActivity().findViewById(R.id.bt_ad_retrieve_user);
        Button mBtClear = (Button) getActivity().findViewById(R.id.bt_ad_clear_user);
        mLvUser = (ListView) getActivity().findViewById(R.id.lv_ad_user_info);

        List<UserData> mUserDataList = new ArrayList<>();
        adapter = new UserListAdapter(mUserDataList, getActivity());

        dbHelper = new MyDatabaseHelper(getActivity(), "usersStore.db", null, 1);
        dbHelper.getWritableDatabase();

        mBtRetrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name, user_code;
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                Cursor c = db.query("users", null, null, null, null, null, null);
                CursorWindowUtil.cw(c);
                if (c.moveToFirst()) {
                    do {
                        user_name = c.getString(c.getColumnIndex("user_name"));
                        user_code = c.getString(c.getColumnIndex("user_code"));
                        bytePic = c.getBlob(c.getColumnIndex("avatar"));

                        UserData mUserData = new UserData();
                        mUserData.name = user_name;
                        mUserData.password = user_code;
                        mUserData.bytePic = bytePic;
                        mUserDataList.add(mUserData);
                    } while (c.moveToNext());
                }
                c.close();
                if (mUserDataList.isEmpty() || adapter.isEmpty()) {
                    ToastDialogUtil.showToast(getActivity(), "暂无用户信息");
                } else {
                    mLvUser.setAdapter(adapter);
                }
            }
        });

        mBtClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter == null) {
                    Toast.makeText(getActivity(), "没有可清除的信息", Toast.LENGTH_SHORT).show();
                } else {
                    mLvUser.setAdapter(adapter);
                    mUserDataList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
