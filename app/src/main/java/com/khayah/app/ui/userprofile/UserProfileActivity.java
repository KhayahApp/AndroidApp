package com.khayah.app.ui.userprofile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.khayah.app.R;
import com.khayah.app.util.ActivityUtils;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class UserProfileActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private static final String EXTRA_USER_ID = "extra.USER_ID";
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    public static void start(Context context, int userId) {
        Intent starter = getStartIntent(context, userId);
        context.startActivity(starter);
    }

    @NonNull
    static Intent getStartIntent(Context context, int userId) {
        Intent starter = new Intent(context, UserProfileActivity.class);
        starter.putExtra(EXTRA_USER_ID, userId);
        return starter;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_activity);

        // Setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            ActivityUtils
                .addFragmentToActivity(getSupportFragmentManager(), UserProfileFragment.newInstance(getExtraUserId()), R.id.contentFrame);
        }
    }

    private int getExtraUserId() {
        return getIntent().getIntExtra(EXTRA_USER_ID, 0);
    }
}
