package com.khayah.app.ui.settings;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Feedback;
import com.khayah.app.models.User;
import com.khayah.app.util.CustomDialog;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HelpUsActivity extends BaseAppCompatActivity implements View.OnClickListener {
    private TextView txt_title;
    private LinearLayout layout_loading;
    private RatingBar ratings;
    private EditText edt_commemt;
    private Button btn_send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_us);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        btn_send = (Button)findViewById(R.id.btn_send);
        txt_title.setText(getResources().getString(R.string.help_us));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout_loading          = (LinearLayout) findViewById(R.id.layout_loading);
        ratings                 = (RatingBar) findViewById(R.id.ratings);
        edt_commemt             = (EditText) findViewById(R.id.edt_comment);

        btn_send.setOnClickListener(this);
    }

    private boolean checkField() {
        if(ratings.getRating() == 0){
            final CustomDialog dialog = new CustomDialog(this);
            dialog.setTitle(getResources().getString(R.string.required));
            dialog.setMessage(getResources().getString(R.string.invalid_rating_value));
            dialog.setOnClickPositiveListener(getResources().getString(R.string.str_yes_i), new CustomDialog.OnClickPositiveListener() {
                @Override
                public void onClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
        if(edt_commemt.getText().length() == 0){
            final CustomDialog dialog = new CustomDialog(this);
            dialog.setTitle(getResources().getString(R.string.required));
            dialog.setMessage(getResources().getString(R.string.invalid_comment_value));
            dialog.setOnClickPositiveListener(getResources().getString(R.string.str_yes_i), new CustomDialog.OnClickPositiveListener() {
                @Override
                public void onClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help_us, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_send).setIcon(getIconicDrawable(CommunityMaterial.Icon.cmd_send.toString(), android.R.color.white, 22));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_send){
            if(checkField()){
                layout_loading.setVisibility(View.VISIBLE);
                Feedback fb = new Feedback();
                fb.setUserId(((User) KhayahApp.getUser()).getId());
                fb.setRatings((double) ratings.getRating());
                fb.setComments(edt_commemt.getText().toString());
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    fb.setVersion(pInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                NetworkEngine.getInstance().postFeedback(fb).enqueue(new Callback<Feedback>() {

                    @Override
                    public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                        layout_loading.setVisibility(View.GONE);
                        if(response.isSuccessful()){
                            final CustomDialog dialog = new CustomDialog(HelpUsActivity.this);
                            dialog.setTitle(getResources().getString(R.string.success));
                            dialog.setMessageType(CustomDialog.Success);
                            dialog.setMessage(getResources().getString(R.string.success_feedback_msg));
                            dialog.setOnClickPositiveListener(getResources().getString(R.string.str_yes_i), new CustomDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Feedback> call, Throwable t) {

                    }

                });
            }
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {
        //todo
        if (v == btn_send) {
            if(checkField()){
                layout_loading.setVisibility(View.VISIBLE);
                Feedback fb = new Feedback();
                fb.setUserId(((User) KhayahApp.getUser()).getId());
                fb.setRatings((double) ratings.getRating());
                fb.setComments(edt_commemt.getText().toString());
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    fb.setVersion(pInfo.versionName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                NetworkEngine.getInstance().postFeedback(fb).enqueue(new Callback<Feedback>() {

                    @Override
                    public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                        layout_loading.setVisibility(View.GONE);
                        if(response.isSuccessful()){
                            final CustomDialog dialog = new CustomDialog(HelpUsActivity.this);
                            dialog.setTitle(getResources().getString(R.string.success));
                            dialog.setMessageType(CustomDialog.Success);
                            dialog.setMessage(getResources().getString(R.string.success_feedback_msg));
                            dialog.setOnClickPositiveListener(getResources().getString(R.string.str_yes_i), new CustomDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                            dialog.show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Feedback> call, Throwable t) {

                    }

                });
            }
        }

    }
}
