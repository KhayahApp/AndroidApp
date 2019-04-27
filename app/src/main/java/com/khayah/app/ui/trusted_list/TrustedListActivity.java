package com.khayah.app.ui.trusted_list;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.khayah.app.APIToolz;
import com.khayah.app.Constant;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.FCMNetworkEngine;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Data;
import com.khayah.app.models.FCMRequest;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGroup;
import com.khayah.app.ui.adapter.NotificationAdapter;
import com.khayah.app.ui.adapter.TrustedListAdapter;
import com.khayah.app.ui.alarm.PersonActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrustedListActivity extends AppCompatActivity {
    private ListView lst_trusted;
    private List<UserGroup> trusted_list;
    private TrustedListAdapter mAdapter;
    private static final int CALLPHONE = 0;
    private static final int SENDFCMNOTI = 1;
    private static final int REMOVEUSER = 2;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trust_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.menu_trust_persons));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lst_trusted = (ListView) findViewById(R.id.lst_trusted);
        trusted_list = new ArrayList<>();
        mAdapter = new TrustedListAdapter(this, trusted_list);
        lst_trusted.setAdapter(mAdapter);
        lst_trusted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userActionChoice(trusted_list.get(position), position);
            }
        });
        user = (User) KhayahApp.getUser();
        NetworkEngine.getInstance().getUserGroups("user_id:equal:" + user.getId(), 1, 9).enqueue(new Callback<List<UserGroup>>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<UserGroup>> call, Response<List<UserGroup>> response) {
                if (response.isSuccessful()) {
                    trusted_list.addAll(response.body());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<UserGroup>> call, Throwable t) {

            }
        });
    }
    private void userActionChoice(UserGroup userGroup, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        CharSequence callPhone;
        CharSequence sendSms;
        CharSequence cr_remove;
        callPhone = getResources().getString(R.string.action_call);
        sendSms = getResources().getString(R.string.action_sms);
        cr_remove = getResources().getString(R.string.action_remove);

        builder.setCancelable(true).
                setItems(new CharSequence[]{callPhone, sendSms, cr_remove},
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CALLPHONE) {
                                    callPhone("+"+userGroup.getPhone());
                                } else if (i == SENDFCMNOTI) {
                                    //Send FCM Message by one
                                    sendFCMNoti(userGroup);
                                } else if (i == REMOVEUSER) {
                                    NetworkEngine.getInstance().deleteGroupUser(userGroup.getId()).enqueue(new Callback<String>() {
                                        @Override
                                        public void onResponse(Call<String> call, Response<String> response) {
                                            if (response.isSuccessful()) {
                                                trusted_list.remove(position);
                                                mAdapter.notifyDataSetChanged();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {

                                        }
                                    });

                                }
                            }
                        });
        builder.show();
    }

    private void sendFCMNoti(UserGroup group) {
        FCMRequest request = new FCMRequest();
        Data data = new Data();
        data.setTitle("Please help me, "+ group.getName());
        data.setMessage("I am in danger: " + user.getFirstName() + " "+user.getLastName());
        data.setUserId(user.getId());
        data.setImageUrl(APIToolz.getInstance().getHostAddress()
                + "/uploads/users/" + user.getAvatar());

        //Log.i("userImageFCMNOti","===>" + user.getAvatar());


        data.setType("danger");
        request.setTo("/topics/"+ Constant.FCM_COMMOM_TOPIC_FOR_USER+group.getPhone());
        request.setData(data);
        request.setSound("fire_alarm");
        FCMNetworkEngine.getInstance().postFCMNotification(request).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(TrustedListActivity.this,
                            "Sending...." + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    private void callPhone(String ph) {
        if (!TextUtils.isEmpty(ph)) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(ph))));
        }

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
