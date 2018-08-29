package com.khayah.app.ui.home;
//https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.khayah.app.R;
import com.khayah.app.ui.login.LoginActivity;


public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 400;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startNextActivityWithDelay();

    }


    private void startNextActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }


}
