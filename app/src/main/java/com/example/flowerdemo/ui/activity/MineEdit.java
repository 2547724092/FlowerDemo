package com.example.flowerdemo.ui.activity;

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
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.flowerdemo.BaseActivity;
import com.example.flowerdemo.MyDatabaseHelper;
import com.example.flowerdemo.R;
import com.example.flowerdemo.util.BitmapUtil;
import com.example.flowerdemo.util.CursorWindowUtil;
import com.example.flowerdemo.util.KeyBoardUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MineEdit extends BaseActivity implements View.OnClickListener {

    private Button ok, no;
    private TextView mTvCamera, mTvSelect;
    private EditText user, passOld, passNew;
    private Uri mImageUri;
    private Bitmap bitmap, loadbitmap;
    private ImageView imageView;
    private String mStrUser, mStrOld, mStrNew, mSqlUser, mSqlPass, mStrKey, mStrOldUser;
    private MyDatabaseHelper dbHelper;
    private RelativeLayout progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_edit);
        progress = findViewById(R.id.rl_wait);
        progress.setVisibility(View.GONE);

        dbHelper = new MyDatabaseHelper(MineEdit.this, "usersStore.db", null, 1);

        mStrOldUser = getIntent().getStringExtra("mine_user");

        mTvCamera = (TextView) this.findViewById(R.id.tv_camera_change);
        mTvSelect = (TextView) this.findViewById(R.id.tv_select_change);
        ok = (Button) this.findViewById(R.id.bt_change);
        no = (Button) this.findViewById(R.id.bt_cancel_change);
        mTvCamera.setOnClickListener(this);
        mTvSelect.setOnClickListener(this);
        ok.setOnClickListener(this);
        no.setOnClickListener(this);

        user = (EditText) this.findViewById(R.id.et_phone_new);
        passOld = (EditText) this.findViewById(R.id.et_password_old);
        passNew = (EditText) this.findViewById(R.id.et_password_new);
        imageView = findViewById(R.id.iv_change_head);

        File image = new File(Environment.getExternalStorageDirectory().getPath() + "/flowers.jpg");
        Glide.with(this).load(image.getPath()).apply(mOptions).into(imageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_camera_change:
                takePhoto();
                break;
            case R.id.tv_select_change:
                requestPermissino();
                break;
            case R.id.bt_change:
                save();
                break;
            case R.id.bt_cancel_change:
                finish();
        }
    }

    private void takePhoto() {
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//打开相机的Intent
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {//这句作用是如果没有相机则该应用不会闪退，要是不加这句则当系统没有相机应用的时候该应用会闪退
            File imageFile = createImageFile();//创建用来保存照片的文件
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= 24) {
                    /*7.0以上要通过FileProvider将File转化为Uri*/
                    mImageUri = FileProvider.getUriForFile(this, "com.example.flowersdemo", imageFile);
                } else {
                    /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
                    mImageUri = Uri.fromFile(imageFile);
                }
                takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePhotoIntent, 11);//打开相机
            }
        }
    }

    /**
     * 创建用来存储图片的文件，以时间来命名就不会产生命名冲突
     *
     * @return 创建的图片文件
     */
    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private void requestPermissino() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            startActivityForResult(choosePhoto(), 12);
        }
    }

    public static final Intent choosePhoto() {
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
                    startActivityForResult(choosePhoto(), 12);
                } else {
                    Toast.makeText(this, "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == RESULT_OK) {
            lookPic();
        }
        if (requestCode == 12 && resultCode == RESULT_OK) {
            if (Build.VERSION.SDK_INT >= 19) {
                //4.4及以上系统使用这个方法处理图片
                handleImageOnKitKat(data);
            }
        }
    }

    private void handleImageOnKitKat(Intent data) {
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
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

    @SuppressLint("Range")
    public void save() {
        /*判断三者不为空
         * 判断原密码与数据库相等
         * 判断原密码与新密码不同
         * 判断新密码6-9位
         * */
        mStrUser = user.getText().toString();
        mStrOld = passOld.getText().toString();
        mStrNew = passNew.getText().toString();

        mStrKey = "SELECT*FROM users where user_name like '%" + mStrOldUser + "%'";
        SQLiteDatabase db1 = dbHelper.getReadableDatabase();
        Cursor cursor1 = db1.rawQuery(mStrKey, null);
        CursorWindowUtil.cw(cursor1);
        if (cursor1.moveToFirst()) {
            do {
                mSqlUser = cursor1.getString(cursor1.getColumnIndex("user_name"));
                mSqlPass = cursor1.getString(cursor1.getColumnIndex("user_code"));
            } while (cursor1.moveToNext());
        }
        cursor1.close();

        if (!mStrOld.equals(mSqlPass)) {
            Toast.makeText(this, "原密码有误，请更正后重新输入", Toast.LENGTH_SHORT).show();
            passOld.setText(null);
            passNew.setText(null);
            return;
        }

        if (mStrNew.length() <= 5 || mStrNew.length() > 9) {
            Toast.makeText(this, "密码长度在6-9位", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mStrNew.equals(mSqlPass)) {
            Toast.makeText(this, "新密码不能与原密码相同，请重新输入", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(MineEdit.this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", (dialog, which) -> {
                    progress.setVisibility(View.VISIBLE);
                    progress.bringToFront();
                    myThread();
                })
                .setNegativeButton("返回", (dialog, which) -> {
                }).show();
    }

    public void myThread() {//创建子线程
        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("user_name", mStrUser);
                    values.put("user_code", mStrNew);
                    values.put("avatar", bitmapToBytes());//图片转为二进制
                    db.update("users", values, "user_name=?", new String[]{mStrOldUser});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
        new Handler().postDelayed(() -> {
            Toast.makeText(MineEdit.this, "修改成功，请重新登录", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();
        }, 2000);

    }

    public void lookPic() {
        if (mImageUri != null) {
            /*如果拍照成功，将Uri用BitmapFactory的decodeStream方法转为Bitmap*/
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mImageUri));
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
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_register);
        }
        //裁剪中间位置正方形
        int width = (int) (BitmapUtil.checkHW(bitmap) * 0.9);
        loadbitmap = BitmapUtil.centerSquareScaleBitmap(bitmap, width);
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

    private RequestOptions mOptions = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true);

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