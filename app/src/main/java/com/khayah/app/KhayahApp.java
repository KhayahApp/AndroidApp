package com.khayah.app;

import android.app.Activity;
import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.khayah.app.di.AppInjector;
import com.khayah.app.util.StorageDriver;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;

public class KhayahApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    public static String PACKAGE_NAME;
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        AppInjector.init(this);
        APIToolz apiToolz = APIToolz.getInstance();
        apiToolz.setClientId(3);
        apiToolz.setClientSecret("wKKJzMZ6rRCJjJREWeqpB4yeQn1bKr5meD4PlhKq");
        apiToolz.setHostAddress("https://khayarapp.com");
        apiToolz.setStoragePath(getFilesDir().getAbsolutePath());
        APIToolzTokenManager.getInstance().getAccessToken();
        PACKAGE_NAME = getApplicationContext().getPackageName();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    public static boolean isLogin(){
        Object user = StorageDriver.getInstance().selectFrom("loginUser");
        if(user != null){
            return true;
        }
        return false;
    }

    public static void login(Object user){
        StorageDriver.getInstance().saveTo("loginUser", user);
    }

    public static void updateUser(Object user){
        StorageDriver.getInstance().saveTo("loginUser", user);
    }

    public static Object getUser(){
        if(isLogin()){
            return StorageDriver.getInstance().selectFrom("loginUser");
        }
        return null;
    }

    public static void logout(){
        if(isLogin()){
            StorageDriver.getInstance().destroy("loginUser");
        }
    }
}
