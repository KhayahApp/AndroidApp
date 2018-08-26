package com.khayah.app.ui.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.khayah.app.APIToolz;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.mikepenz.iconics.view.IconicsImageView;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.User;
import com.khayah.app.util.CircleTransform;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseAppCompatActivity {

    private IconicsImageView img_user;
    private TextView txt_name;
    private TextView txt_email;
    private TextView txt_phone;
    private Button btn_change_password;
    private Button btn_edit_account;
    private User user;
    private TextView txt_points;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        img_user            = (IconicsImageView) findViewById(R.id.img_user);
        txt_name            = (TextView) findViewById(R.id.txt_name);
        txt_email           = (TextView) findViewById(R.id.txt_email);
        txt_phone           = (TextView) findViewById(R.id.txt_phone);
        txt_points          = (TextView) findViewById(R.id.txt_points);
        btn_change_password = (Button) findViewById(R.id.btn_change_password);
        btn_edit_account    = (Button) findViewById(R.id.btn_edit_account);

        img_user.setOnClickListener(onClickListener);
        btn_edit_account.setOnClickListener(onClickListener);
        btn_change_password.setOnClickListener(onClickListener);

        user = (User) KhayahApp.getUser();
        txt_name.setText(user.getFirstName() +" "+ user.getLastName());
        txt_email.setText(user.getEmail());
        txt_phone.setText(user.getPhone());
        if(user.getPoints() != null && user.getPoints() > 0) {
            txt_points.setText(user.getPoints().toString());
        }
        Picasso.with(ProfileActivity.this).load(R.drawable.boy).transform(new CircleTransform()).into(img_user);
        if(user.getAvatar() != null){
            Picasso.with(ProfileActivity.this).load(APIToolz.getInstance().getHostAddress()+
                    "/uploads/users/"+user.getAvatar()).transform(new CircleTransform()).into(img_user);
        }else{
            Picasso.with(ProfileActivity.this).load("https://graph.facebook.com/" + user.getFacebookId() +
                    "/picture?type=large").transform(new CircleTransform()).into(img_user);
        }

        if(isNetworkAvailable()) {
            getUser();
        }
    }

    private void getUser() {
        showLoading(true);
        NetworkEngine.getInstance().profile(user.getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showLoading(false);
                user = response.body();
                KhayahApp.updateUser(user);
                if(user.getPoints() != null && user.getPoints() > 0) {
                    txt_points.setText(user.getPoints().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void uploadImage(File file, RequestBody reqFile){
        showLoading(true);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(), reqFile);
        NetworkEngine.getInstance().uploadAvatar(user.getId(), body).enqueue(new retrofit2.Callback<User>() {
            @Override
            public void onResponse(Call<User> call, retrofit2.Response<User> response) {
                if(response.isSuccessful()){
                    KhayahApp.updateUser(response.body());
                    Picasso.with(ProfileActivity.this).load(APIToolz.getInstance().getHostAddress()+"/uploads/users/"+response.body().getAvatar()).transform(new CircleTransform()).into(img_user);
                    showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            Picasso.with(ProfileActivity.this).load(Crop.getOutput(result)).transform(new CircleTransform()).into(img_user);
            File file = new File(Crop.getOutput(result).getPath());
            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            uploadImage(file, reqFile);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v == img_user){
                Crop.pickImage(ProfileActivity.this);
            }

            if(v == btn_edit_account){
                Intent intent = new Intent(getApplicationContext(), EditAccountActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, v, "profile");
                startActivity(intent, options.toBundle());
            }

            if(v == btn_change_password){
                Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this, v, "profile");
                startActivity(intent, options.toBundle());
            }
        }
    };

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
