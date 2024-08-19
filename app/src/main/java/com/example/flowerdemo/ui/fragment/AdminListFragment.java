package com.example.flowerdemo.ui.fragment;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.adapter.AdminFlowersAdapter;
import com.example.flowerdemo.adapter.FlowersListAdapter;
import com.example.flowerdemo.list.FlowersData;
import com.example.flowerdemo.list.ListData;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者 : Run
 * 日期 : 2022/5/13
 * 描述 : 管理员模式订单查看
 */


public class AdminListFragment extends Fragment implements View.OnClickListener {

    private MyDatabaseHelper dbHelper;
    private EditText mEdUser, mEdName, mEdNum, mEdTotal, mEdTime;
    private Button mBtAdd, mBtUpdate, mBtRetrieve, mBtDelete, mBtClear;
    private String mStrUser, mStrName, mStrNum, mStrTotal, mStrTime;
    private ListView mLvList;
    private FlowersListAdapter adapter;
    private List<ListData> mListItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "listStore.db", null, 1);
        dbHelper.getWritableDatabase();

        mListItems = new ArrayList<>();
        adapter = new FlowersListAdapter(mListItems, getActivity());

        mEdUser = (EditText) getActivity().findViewById(R.id.et_list_user);
        mEdName = (EditText) getActivity().findViewById(R.id.et_list_name);
        mEdNum = (EditText) getActivity().findViewById(R.id.et_list_num);
        mEdTotal = (EditText) getActivity().findViewById(R.id.et_list_total);
        mEdTime = (EditText) getActivity().findViewById(R.id.et_list_time);

        mBtAdd = (Button) getActivity().findViewById(R.id.bt_list_add);
        mBtUpdate = (Button) getActivity().findViewById(R.id.bt_list_update);
        mBtRetrieve = (Button) getActivity().findViewById(R.id.bt_list_retrieve);
        mBtDelete = (Button) getActivity().findViewById(R.id.bt_list_delete);
        mBtClear = (Button) getActivity().findViewById(R.id.bt_list_clear);

        mBtAdd.setOnClickListener(this);
        mBtUpdate.setOnClickListener(this);
        mBtRetrieve.setOnClickListener(this);
        mBtDelete.setOnClickListener(this);
        mBtClear.setOnClickListener(this);

        mLvList = (ListView) getActivity().findViewById(R.id.lv_list);
    }

    @SuppressLint("Range")
    @Override
    public void onClick(View view) {
        mStrUser = mEdUser.getText().toString();
        mStrName = mEdName.getText().toString();
        mStrNum = mEdNum.getText().toString();
        mStrTotal = mEdTotal.getText().toString();
        mStrTime = mEdTime.getText().toString();

        switch (view.getId()) {

            //<editor-fold defaultstate="collapsed" desc="添加数据">
            case R.id.bt_list_add:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("user_name", mStrUser);
                values.put("name", mStrName);
                values.put("num", mStrNum);
                values.put("total", mStrTotal);
                values.put("time", mStrTime);
                if (mStrUser.equals("") || mStrName.equals("") || mStrNum.equals("") || mStrTotal.equals("") || mStrTime.equals("")) {
                    Toast.makeText(getActivity(), "添加失败，请输入完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    db.insert("list", null, values);
                    values.clear();
                    Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_SHORT).show();
                    mEdUser.setText("");
                    mEdName.setText("");
                    mEdNum.setText("");
                    mEdTotal.setText("");
                    mEdTime.setText("");
                }
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="更新数据">
            case R.id.bt_list_update:
                if (!TextUtils.isEmpty(mStrName)) {
                    db = dbHelper.getWritableDatabase();
                    values = new ContentValues();
                    mStrUser = mEdUser.getText().toString();
                    mStrName = mEdName.getText().toString();
                    mStrNum = mEdNum.getText().toString();
                    mStrTotal = mEdTotal.getText().toString();
                    mStrTime = mEdTime.getText().toString();
                    values.put("user_name", mStrUser);
                    values.put("name", mStrName);
                    values.put("num", mStrNum);
                    values.put("total", mStrTotal);
                    values.put("time", mStrTime);
                    // TODO: 2022/5/15 更新图片
                    if (mStrUser.equals("") || mStrName.equals("") || mStrNum.equals("") || mStrTotal.equals("") || mStrTime.equals("")) {
                        Toast.makeText(getActivity(), "更新失败，请输入完整信息", Toast.LENGTH_SHORT).show();
                    } else {
                        db.update("list", values, "name=?", new String[]{mStrName});
                        Toast.makeText(getActivity(), "更新" + mStrName + "成功", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入要更新的鲜花名称", Toast.LENGTH_SHORT).show();
                }
                break;
            //</editor-fold>

            // TODO: 2022/5/16 删除
            //<editor-fold defaultstate="collapsed" desc="删除数据">
            case R.id.bt_list_delete:
                if (!TextUtils.isEmpty(mStrUser)) {
                    db = dbHelper.getWritableDatabase();
                    mStrUser = mEdUser.getText().toString();
                    db.delete("list", "user_name=?", new String[]{mStrUser});
                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请输入要删除的订单", Toast.LENGTH_SHORT).show();
                }
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="查询数据">
            case R.id.bt_list_retrieve:
                String user_name, name, num, total, time;
                String key = mEdUser.getText().toString();
                String selectQuery = "SELECT*FROM list where user_name like '%" + key + "%' ";
                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                Cursor cursor = db1.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        user_name = cursor.getString(cursor.getColumnIndex("user_name"));
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        num = cursor.getString(cursor.getColumnIndex("num"));
                        total = cursor.getString(cursor.getColumnIndex("total"));
                        time = cursor.getString(cursor.getColumnIndex("time"));

                        ListData mListData = new ListData();
                        mListData.user = user_name;
                        mListData.name = name;
                        mListData.num = num;
                        mListData.total = total;
                        mListData.time = time;
                        mListItems.add(mListData);
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(getActivity(), "未查询到订单", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                mLvList.setAdapter(adapter);
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="清除列表">
            case R.id.bt_list_clear:
                if (adapter == null) {
                    Toast.makeText(getActivity(), "没有可清除的信息", Toast.LENGTH_SHORT).show();
                } else {
                    mLvList.setAdapter(adapter);
                    mListItems.clear();
                    adapter.notifyDataSetChanged();

                    mEdUser.setText("");
                    mEdName.setText("");
                    mEdNum.setText("");
                    mEdTotal.setText("");
                    mEdTime.setText("");
                }
                break;
            //</editor-fold>
        }
    }
}