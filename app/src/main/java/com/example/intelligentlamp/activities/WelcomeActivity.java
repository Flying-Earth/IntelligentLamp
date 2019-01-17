package com.example.intelligentlamp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.intelligentlamp.R;


/**
 * Created by 76377 on 2017/10/28.
 */

public class WelcomeActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000; // 两秒后进入系统

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {

//                LitePal.initialize(context);

//                UserPassword userPassword = new UserPassword();
//                userPassword.setUser("admin");
//                userPassword.setPassword("123456");
//                userPassword.save();

                Intent Intent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(Intent);
                finish();
            }

        }, SPLASH_DISPLAY_LENGHT);

    }
}
