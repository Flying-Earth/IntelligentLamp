package com.example.intelligentlamp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.intelligentlamp.R;
import com.example.intelligentlamp.classes.UserPassword;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private Button register;

    private Context mContext;

    private Thread thread;

    private CheckBox rememberPass;

    private CircleImageView headImage;

    private boolean isStop = false;

    private byte[] headShot = null;

    final String SP_KEY_USERNAME = "username";
    final String SP_KEY_PASSWORD = "password";
    final String SP_KEY_DATETIME = "datetime";

    final long DAY = 1 * 1000 * 60 * 60 * 24;

    List<UserPassword> userPasswords = DataSupport.findAll(UserPassword.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = (EditText) findViewById(R.id.login_account);
        passwordEdit = (EditText) findViewById(R.id.login_password);
        rememberPass = (CheckBox) findViewById(R.id.remember_pass);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        mContext = this;
        verifyStoragePermissions(this);
        headImage = (CircleImageView) findViewById(R.id.login_image);
//        Glide.with(mContext).load(R.drawable.icon_image).into(headImage);
//        boolean isRemember = pref.getBoolean("remember_pass", false);
//        if (isRemember) {
//            String account = pref.getString("account", "");
//            String password = pref.getString("password", "");
//            accountEdit.setText(account);
//            passwordEdit.setText(password);
//            rememberPass.setChecked(true);
//        }
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        String userName = sp.getString(SP_KEY_USERNAME, "");
        String password = sp.getString(SP_KEY_PASSWORD, "");
        long ts = sp.getLong(SP_KEY_DATETIME, 0);

        if (System.currentTimeMillis() - ts <= DAY) {
            accountEdit.setText(userName);
            if (rememberPass.isChecked()) {
                passwordEdit.setText(password);
            }
        }

        for (UserPassword userPassword : userPasswords) {
            if (accountEdit.getText().toString().equals(userPassword.getUser())) {
                Glide.with(mContext).load(userPassword.getHeadShot()).error(R.drawable.icon_image).into(headImage);
            }
        }

//        handler =new Handler();
//        handler.post(runnable);
//        thread = new Thread(runnable);
//        thread.start();

        accountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (UserPassword userPassword : userPasswords) {
                    if (accountEdit.getText().toString().equals(userPassword.getUser())) {
                       Glide.with(mContext).load(userPassword.getHeadShot()).error(R.drawable.icon_image).into(headImage);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (UserPassword userPassword : userPasswords) {
                    if (accountEdit.getText().toString().equals(userPassword.getUser())) {
//                        Glide.with(mContext).load(userPassword.getHeadShot()).error(R.drawable.icon_image).into(headImage);
                        if (passwordEdit.getText().toString().equals(userPassword.getPassword())) {
                            if (rememberPass.isChecked()) {
                                SharedPreferences sp = getPreferences(MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString(SP_KEY_USERNAME, accountEdit.getText().toString());
                                editor.putString(SP_KEY_PASSWORD, passwordEdit.getText().toString());
                                editor.putLong(SP_KEY_DATETIME, System.currentTimeMillis());
                                editor.apply();
                            }
//                Toast.makeText(mContext, "已登录", Toast.LENGTH_SHORT).show();
                            isStop = true;
                            MainActivity.actionStart(LoginActivity.this, userPassword.getUser());
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "密码错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    //Toast.makeText(LoginActivity.this, "不存在该账户", Toast.LENGTH_SHORT).show();
                }

//                String account = accountEdit.getText().toString();
//                String password = passwordEdit.getText().toString();
//                if (account.equals("admin") && password.equals("123456")) {
//                    editor = pref.edit();
//                    if (rememberPass.isChecked()) {
//                        editor.putBoolean("remember_passed", true);
//                        editor.putString("account", account);
//                        editor.putString("password", password);
//                    } else {
//                        editor.clear();
//                    }
//                    editor.apply();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                }

//                Toast.makeText(mContext, "已登录", Toast.LENGTH_SHORT).show();
//                else{
//                    String account = accountEdit.getText().toString();
//                    String password = passwordEdit.getText().toString();
//                    if (account.equals("admin") && password.equals("123456")) {
////                        editor = pref.edit();
//                        if (rememberPass.isChecked()){
//                            SharedPreferences sp = getPreferences(MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sp.edit();
//                            editor.putString(SP_KEY_USERNAME, "admin");
//                            editor.putString(SP_KEY_PASSWORD, "123456");
//                            editor.putLong(SP_KEY_DATETIME, System.currentTimeMillis());
//                            editor.apply();
//                        }
////                        if (rememberPass.isChecked()) {
////                            editor.putBoolean("remember_passed", true);
////                            editor.putString("account", account);
////                            editor.putString("password", password);
////                        } else {
////                            editor.clear();
////                        }
////                        editor.apply();
//                        MainActivity.actionStart(LoginActivity.this, "admin");
//                        finish();
//                    } else if (test == 0){
//                        Toast.makeText(LoginActivity.this, "账户或密码错误",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    test = 0;
//                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}

