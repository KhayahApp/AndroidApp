package com.khayah.app.ui.home;
//https://android.jlelse.eu/right-way-to-create-splash-screen-on-android-e7f1709ba154

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.ui.login.LoginActivity;


public class SplashActivity extends BaseAppCompatActivity {

    private static final int SPLASH_TIME_OUT = 1000;
    private static final int PERMISSION_REQUEST = 100;
    private static final String CALL_PHONE = android.Manifest.permission.CALL_PHONE;
    private static final String RECEIVE_SMS = android.Manifest.permission.RECEIVE_SMS;
    private static final String ACCESS_FINE_LOCATION = android.Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION = android.Manifest.permission.ACCESS_COARSE_LOCATION;
    @TargetApi(Build.VERSION_CODES.M)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //startNextActivityWithDelay();
        if (!hasPermission(CALL_PHONE) || !hasPermission(RECEIVE_SMS) || !hasPermission(ACCESS_FINE_LOCATION) || !hasPermission(ACCESS_COARSE_LOCATION)) {
            requestPermissions(new String[]{CALL_PHONE, RECEIVE_SMS, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                    //startNextActivityWithDelay();
                }
            }
        }).start();

    }


    private void startNextActivityWithDelay() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(KhayahApp.isLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }


    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(300);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                finish();
                                //startNextActivityWithDelay();
                            }
                        }
                    }).start();
                }
            }
        }
    }



}
