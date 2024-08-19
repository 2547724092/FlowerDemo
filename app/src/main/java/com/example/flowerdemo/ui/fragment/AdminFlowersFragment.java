package com.example.flowerdemo.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.flowerdemo.Cons;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.adapter.AdminFlowersAdapter;
import com.example.flowerdemo.list.FlowersData;
import com.example.flowerdemo.util.BitmapUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 作者 : Run
 * 日期 : 2022/5/13
 * 描述 : 管理员模式鲜花页面
 */


public class AdminFlowersFragment extends Fragment implements View.OnClickListener {

    private Bitmap bitmap, loadbitmap;
    private Bitmap loadBitmap = null;
    private MyDatabaseHelper dbHelper;
    private EditText mEdName, mEdKind, mEdPrice, mEdAddress;
    private Button mBtAdd, mBtUpdate, mBtRetrieve, mBtDelete, mBtClear;
    private String mStrName, mStrKind, mStrPrice, mStrAddress;
    private ListView mLvAdFlowers;
    private TextView mTvSelect;
    private AdminFlowersAdapter adapter;
    private ImageView imageView;
    private List<FlowersData> mListItems;
    int amount = 0;
    byte[] bytePic;
    private Uri mImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_flowers, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new MyDatabaseHelper(getActivity(), "flowersStore.db", null, 1);
        dbHelper.getWritableDatabase();

        mListItems = new ArrayList<>();
        adapter = new AdminFlowersAdapter(mListItems, getActivity());

        mEdName = (EditText) getActivity().findViewById(R.id.et_ad_name);
        mEdKind = (EditText) getActivity().findViewById(R.id.et_ad_kind);
        mEdPrice = (EditText) getActivity().findViewById(R.id.et_ad_price);
        mEdAddress = (EditText) getActivity().findViewById(R.id.et_ad_address);

        mBtAdd = (Button) getActivity().findViewById(R.id.bt_ad_add);
        mBtUpdate = (Button) getActivity().findViewById(R.id.bt_ad_update);
        mBtRetrieve = (Button) getActivity().findViewById(R.id.bt_ad_retrieve);
        mBtDelete = (Button) getActivity().findViewById(R.id.bt_ad_delete);
        mBtClear = (Button) getActivity().findViewById(R.id.bt_ad_clear);

        mTvSelect = (TextView) getActivity().findViewById(R.id.tv_select_change);

        mBtAdd.setOnClickListener(this);
        mBtUpdate.setOnClickListener(this);
        mBtRetrieve.setOnClickListener(this);
        mBtDelete.setOnClickListener(this);
        mBtClear.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);

        imageView = (ImageView) getActivity().findViewById(R.id.iv_flowers);
        mLvAdFlowers = (ListView) getActivity().findViewById(R.id.lv_ad_flowers_info);

    }

    @SuppressLint("Range")
    @Override
    public void onClick(View view) {
        mStrName = mEdName.getText().toString();
        mStrKind = mEdKind.getText().toString();
        mStrPrice = mEdPrice.getText().toString();
        mStrAddress = mEdAddress.getText().toString();
        switch (view.getId()) {

            //<editor-fold desc="打开相册">
            case R.id.tv_select_change:
                requestPermissino();
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="添加数据">
            case R.id.bt_ad_add:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                //<editor-fold defaultstate="collapsed" desc="首次添加数据">
                Cursor c = db.rawQuery("select * from flowers", null);
                amount = c.getCount();
                if (amount == 0) {
                    Log.i("添加数据", "flowers表为空");

                    addItem("百合花", "百合", "浙江", "120", R.mipmap.ic_baihe);
                    addItem("玫瑰花", "蔷薇", "平阴", "100", R.mipmap.ic_rose);
                    addItem("康乃馨", "石竹", "日本", "100", R.mipmap.ic_kangnaixin);
                }
                //</editor-fold>
                values.put("name", mStrName);
                values.put("kind", mStrKind);
                values.put("price", mStrPrice);
                values.put("address", mStrAddress);
                values.put("pic", bitmapToBytes());
                // TODO: 2022/5/15 添加照片
                if (mStrName.equals("") || mStrKind.equals("") || mStrPrice.equals("") || mStrAddress.equals("")) {
                    Toast.makeText(getActivity(), "添加失败，请输入完整信息", Toast.LENGTH_SHORT).show();
                } else {
                    db.insert("flowers", null, values);
                    values.clear();
                    Toast.makeText(getActivity(), mStrName + "添加成功", Toast.LENGTH_SHORT).show();
                    mEdName.setText("");
                    mEdKind.setText("");
                    mEdPrice.setText("");
                    mEdAddress.setText("");
                    imageView.setImageResource(R.mipmap.ic_logo);
                }
                Log.i("非首次添加数据", "flowers表不为空");
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="更新数据">
            case R.id.bt_ad_update:
                if (!TextUtils.isEmpty(mStrName)) {
                    db = dbHelper.getWritableDatabase();
                    values = new ContentValues();
                    mStrName = mEdName.getText().toString();
                    mStrKind = mEdKind.getText().toString();
                    mStrPrice = mEdPrice.getText().toString();
                    mStrAddress = mEdAddress.getText().toString();
                    values.put("kind", mStrKind);
                    values.put("price", mStrPrice);
                    values.put("address", mStrAddress);
                    values.put("pic", bitmapToBytes());
                    // TODO: 2022/5/15 更新图片
                    db.update("flowers", values, "name=?", new String[]{mStrName});
                    Toast.makeText(getActivity(), "更新" + mStrName + "成功", Toast.LENGTH_SHORT).show();
                    imageView.setImageResource(R.mipmap.ic_logo);
                } else {
                    Toast.makeText(getActivity(), "请输入要更新的鲜花名称", Toast.LENGTH_SHORT).show();
                }
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="删除数据">
            case R.id.bt_ad_delete:
                if (!TextUtils.isEmpty(mStrName)) {
                    db = dbHelper.getWritableDatabase();
                    mStrName = mEdName.getText().toString();
                    db.delete("flowers", "name=?", new String[]{mStrName});
                    Toast.makeText(getActivity(), "删除" + mStrName + "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请输入要删除的鲜花", Toast.LENGTH_SHORT).show();
                }
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="查询数据">
            case R.id.bt_ad_retrieve:
                String name, kind, price, address;
                String key = mEdName.getText().toString();
                String selectQuery = "SELECT*FROM flowers where name like '%" + key + "%' ";
                SQLiteDatabase db1 = dbHelper.getReadableDatabase();
                Cursor cursor = db1.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        name = cursor.getString(cursor.getColumnIndex("name"));
                        kind = cursor.getString(cursor.getColumnIndex("kind"));
                        price = cursor.getString(cursor.getColumnIndex("price"));
                        address = cursor.getString(cursor.getColumnIndex("address"));
                        bytePic = cursor.getBlob(cursor.getColumnIndex("pic"));

                        FlowersData mFlowersData = new FlowersData();
                        mFlowersData.name = name;
                        mFlowersData.kind = kind;
                        mFlowersData.price = price;
                        mFlowersData.address = address;
                        mFlowersData.bytePic = bytePic;
                        mListItems.add(mFlowersData);
                    } while (cursor.moveToNext());
                } else {
                    Toast.makeText(getActivity(), "未查询到该鲜花", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
                mLvAdFlowers.setAdapter(adapter);
                break;
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="清除列表">
            case R.id.bt_ad_clear:
                if (adapter == null) {
                    Toast.makeText(getActivity(), "没有可清除的信息", Toast.LENGTH_SHORT).show();
                } else {
                    mLvAdFlowers.setAdapter(adapter);
                    mListItems.clear();
                    adapter.notifyDataSetChanged();

                    mEdName.setText("");
                    mEdKind.setText("");
                    mEdPrice.setText("");
                    mEdAddress.setText("");
                    imageView.setImageResource(R.mipmap.ic_logo);
                }
                break;
            //</editor-fold>
        }
    }

    private void requestPermissino() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            startActivityForResult(choosePhoto(), Cons.CHOOSE_PHOTO_AD);
        }
    }

    public static Intent choosePhoto() {
        if (Build.VERSION.SDK_INT >= 30) {// Android 11 (API level 30)
            return new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            return Intent.createChooser(intent, null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(choosePhoto(), Cons.CHOOSE_PHOTO_AD);
                } else {
                    Toast.makeText(getActivity(), "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Cons.CHOOSE_PHOTO_AD && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 19) {
                //4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];  //解析出数字格式的id
                mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public downloads"), Long.valueOf(docId));
                mImageUri = contentUri;
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            mImageUri = uri;
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            mImageUri = uri;
        }
        lookPic();
    }

    public void lookPic() {
        if (mImageUri != null) {
            /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
            try {
                bitmap = BitmapFactory.decodeStream(Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(mImageUri));
                Log.i("宽高", bitmap.getWidth() + "*" + bitmap.getHeight());
                int width = (int) (BitmapUtil.checkHW(bitmap) * 0.9);
                loadbitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, width);
            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            imageView.setImageBitmap(loadbitmap);//显示到ImageView上
        }
    }

    public byte[] bitmapToBytes() {
        //将图片转化为位图
        int size = loadbitmap.getWidth() * loadbitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            loadbitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imagedata = baos.toByteArray();
            return imagedata;
        } catch (Exception e) {
        } finally {
            try {
                loadbitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }

    public void addItem(String name, String kind, String address, String price, int id) {

        byte[] imagedata = new byte[0];
        if (id == 0) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        } else {
            bitmap = BitmapFactory.decodeResource(getResources(), id);
        }
        //裁剪中间位置正方形
        int width = (int) (BitmapUtil.checkHW(bitmap) * 0.9);
        loadBitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, width);
        int size = loadBitmap.getWidth() * loadBitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            loadBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            //将字节数组输出流转化为字节数组byte[]
            imagedata = baos.toByteArray();
        } catch (Exception e) {
        } finally {
            try {
                loadBitmap.recycle();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("kind", kind);
        values.put("price", price);
        values.put("address", address);
        values.put("pic", imagedata);
        db.insert("flowers", null, values);
        Log.i("flowers", "添加成功");
        values.clear();
    }
}
