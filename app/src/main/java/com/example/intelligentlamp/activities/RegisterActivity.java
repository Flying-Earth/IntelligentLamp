package com.example.intelligentlamp.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.R;
import com.example.intelligentlamp.classes.UserPassword;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
public class RegisterActivity extends AppCompatActivity {

    private String password;

    private String account;

    private String rePassword;

    private Bitmap bm = null;
    //resizeBitmap(bm,20,20);

    private byte[] headShot;
    private CircleImageView headImage;

    // 拍照存放本地文件名称
    public static final String IMAGE_FILE_NAME = "faceImage.jpg";
    // 裁剪后的文件名称
    public static final String IMAGE_FILE_NAME_TEMP = "faceImage_temp.jpg";

    private File file = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
    private Uri imageUri ;

    private Uri imageCropUri = Uri.fromFile(file);
    private Uri imageCropUri7 = FileProvider.getUriForFile(this, "com.your.package.name",file);;
    // 上传头像方式文字数据
    private String[] items = new String[] { "选择本地图片", "拍照" };

    // 请求码
    private static final int IMAGE_REQUEST_CODE = 0;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final int RESULT_REQUEST_CODE = 3;

    private boolean test = true;

    private EditText registerAccount;
    private EditText registerPassword;
    private EditText registerRePassword;
    private Context mContext;

    List<UserPassword> userPasswords = DataSupport.findAll(UserPassword.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button register = (Button)findViewById(R.id.register);
        Button back = (Button)findViewById(R.id.register_back);
        registerPassword = (EditText)findViewById(R.id.register_password);
        registerAccount = (EditText)findViewById(R.id.register_account);
        registerRePassword = (EditText)findViewById(R.id.register_rewrite_password);

        bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
        mContext = this;
        headImage=(CircleImageView)findViewById(R.id.register_image);
        Glide.with(mContext).load(R.drawable.icon_image).into(headImage);
//        bm = BitmapFactory.decodeResource(getResources(), R.drawable.icon_image);
//        headImage.setImageBitmap(bm);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = registerAccount.getText().toString();
                password = registerPassword.getText().toString();
                rePassword = registerRePassword.getText().toString();
                byte[] imageBytes = img(bm);
                test = true;
                if (account.equals("")){
                    Toast.makeText(RegisterActivity.this,"账户不能为空",Toast.LENGTH_SHORT).show();
                    test = false;
                } else if (password.equals("")){
                    Toast.makeText(RegisterActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                    test = false;
                }
                for (UserPassword userPassword : userPasswords){
                    if (account.equals(userPassword.getUser())){
                        Toast.makeText(RegisterActivity.this,"账户重复",Toast.LENGTH_SHORT).show();
                        test = false;
                    }
                }
                if (!password.equals(rePassword)){
                    Toast.makeText(RegisterActivity.this,"两次输入的密码不相同",Toast.LENGTH_SHORT).show();
                    test = false;
                }
                if (test){
                    UserPassword userPassword = new UserPassword();
                    userPassword.setUser(account);
                    userPassword.setPassword(password);;
                    userPassword.setHeadShot(imageBytes);
                    userPassword.save();
                    Log.i("Register", "image");
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        headImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(RegisterActivity.this,"选择照片",Toast.LENGTH_LONG).show();
                showDialog();
            }
        });


    }


    private void showDialog() {

        new AlertDialog.Builder(this).setTitle("设置头像").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        //从相册选择
                        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                                Manifest.permission.WRITE_APN_SETTINGS)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(RegisterActivity.this,new
                                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                        } else {
                            break;
                        }
                        Intent intent = new Intent(Intent.ACTION_PICK, null);
                        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(intent, IMAGE_REQUEST_CODE);
                        break;
                    case 1:
                        //拍照
                        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions(RegisterActivity.this,new
                            String[]{Manifest.permission.CAMERA},1);
                        } else {
                            break;
                        }
                        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        // 判断存储卡是否可以用，可用进行存储
                        if (hasSdcard()) {
                            if (Build.VERSION.SDK_INT < 24) {
                                intentFromCapture.putExtra("return-data", false);
                                imageUri = Uri.fromFile(file);
                            } else {
                                //兼容android7.0 使用共享文件的形式
                                if (!file.getParentFile().exists()){
                                    file.getParentFile().mkdirs();
                                }

                                String authorities = "com.your.package.name";
                                //检查是否有存储权限，以免崩溃
                                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        != PackageManager.PERMISSION_GRANTED) {
                                    //申请WRITE_EXTERNAL_STORAGE权限
//                                    Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                imageUri = FileProvider.getUriForFile(mContext, authorities, file);;
                            }
                            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            intentFromCapture.putExtra("noFaceDetection", true);
                        }
                        startActivityForResult(intentFromCapture, CAMERA_REQUEST_CODE);
                        break;
                 /*   case 2:
                        Intent intent1 = new Intent(RegisterActivity.this, UserActivity.class);

                        TextView userName = (TextView) findViewById(R.id.username);
                        //int ironImage = R.drawable.faceImage_temp;
                        intent1.putExtra(UserActivity.MUSIC_NAME, userName.getText());
                        // intent.putExtra(UserActivity.MUSIC_IMAGE_ID, ironImage);
                        startActivity(intent1);
                        break;*/
                }
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }
    private boolean hasSdcard() {
        // 判断Sdcard是否可用
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }
    @SuppressWarnings("static-access")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != this.RESULT_CANCELED) {
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    // 裁剪图片方法
                    startPhotoZoom(data.getData());
                    break;
                // 保存头像
                case CAMERA_REQUEST_CODE:
                    if (hasSdcard()) {
                        if (Build.VERSION.SDK_INT < 24)
                            startPhotoZoom(imageCropUri);
                        else startPhotoZoom(imageCropUri7);
                    } else {
                        Toast.makeText(this, "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();

                    }
                    break;
                // 裁剪之后，显示头像
                case RESULT_REQUEST_CODE:
                    if (data != null) {
                        setImageToView(data);
                    }
                    break;
            }
        }
    }

    /**
     *
     * 裁剪图片方法实现
     *
     */
    public void startPhotoZoom(Uri uri) {
        if (uri == null) {
            Log.i("tag", "========The uri is not exist.");
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        //裁剪之后，保存在裁剪文件中，关键

        if (Build.VERSION.SDK_INT < 24)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri);
        else intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCropUri7);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, RESULT_REQUEST_CODE);
    }

    /**
     * 保存裁剪之后的图片数据,并显示
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap bitmap = null;

        try {
            if (Build.VERSION.SDK_INT < 24)
                bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri));
            else bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageCropUri7));
            Log.d("size",bm.getByteCount()+"");
//            Glide.with(mContext).load(bm).into(headImage);
            headImage.setImageBitmap(bm);

            /*byte[]images=img(bitmap);
            for (UserPassword userPassword : userPasswords){
            //UserPassword userPassword = new UserPassword();
            userPassword.setHeadshot(images);
            userPassword.save();}*/


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (extras != null) {
           /* bitmap = extras.getParcelable("data");
            Log.d("size",bitmap.getByteCount()+"");


             ByteArrayOutputStream stream = new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
             byte[] b = stream.toByteArray(); // 将图片流以字符串形式存储下来
             UserPassword userPassword = new UserPassword();
             userPassword.setHeadshot(b);
             userPassword.save();*/
             /*
             * tp = new String(Base64Coder.encodeLines(b));
             * 这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了， 服务器处理的方法是服务器那边的事了，吼吼
             *
             * 如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换 为我们可以用的图片类型就OK啦...吼吼
             * Bitmap dBitmap = BitmapFactory.decodeFile(tp);
             * Drawable drawable = new BitmapDrawable(dBitmap);
             */
        }
    }

    private byte[]img(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private String getPicType(String picName){
        String[] s = picName.split(".");
        //数组长度
        return s[s.length-1];
    }


}
