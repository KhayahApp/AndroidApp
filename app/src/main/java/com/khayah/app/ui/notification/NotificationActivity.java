package com.khayah.app.ui.notification;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.User;
import com.khayah.app.ui.adapter.NotificationAdapter;
import com.khayah.app.ui.alarm.PersonActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private ListView lst_noti;
    private List<FcmMessage> notifications;
    private NotificationAdapter mAdapter;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_notification));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lst_noti = (ListView) findViewById(R.id.lst_notification);
        notifications = new ArrayList<>();
        mAdapter = new NotificationAdapter(this, notifications);
        lst_noti.setAdapter(mAdapter);
        lst_noti.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Integer userId = notifications.get(position).getUser_id();
                startActivity(new Intent(getApplicationContext(), PersonActivity.class).putExtra("user_id",userId.toString()));
            }
        });
        user = (User) KhayahApp.getUser();
        NetworkEngine.getInstance().getNotifications("receivers:like:"+user.getPhone(),"id","desc",1,99).enqueue(new Callback<List<FcmMessage>>() {
            @Override
            public void onResponse(Call<List<FcmMessage>> call, Response<List<FcmMessage>> response) {
                if(response.isSuccessful()) {
                    notifications.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FcmMessage>> call, Throwable t) {

            }
        });

    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
