package com.khayah.app.ui.lawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.Constant;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Station;
import com.khayah.app.models.User;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;
import com.khayah.app.ui.login.ProfileActivity;

import java.util.Locale;


public class DetailActivity extends BaseAppCompatActivity implements View.OnClickListener {


    private Integer mID;
    private Station mStation;
    private TextView txtStationName;
    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtDesc;
    private TextView txtType;
    private RelativeLayout ryCallNow;
    private RelativeLayout ryDirection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);

        android.support.v7.widget.Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_directory));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mID = bundle.getInt(Constant.POST_DETAIL_ID);
        }
        //Toast.makeText(getApplicationContext(),"ID"+ mID ,Toast.LENGTH_LONG).show();
        txtStationName = (TextView) findViewById(R.id.detail_text_name);
        txtPhone = (TextView) findViewById(R.id.detail_txt_ph);
        txtAddress = (TextView) findViewById(R.id.detail_txt_address);
        txtDesc = (TextView) findViewById(R.id.detail_txt_desc);
        txtType = (TextView) findViewById(R.id.detail_txt_type);
        ryCallNow = (RelativeLayout) findViewById(R.id.ry_call_now);
        ryDirection = (RelativeLayout) findViewById(R.id.ry_direction);

        ryCallNow.setOnClickListener(this::onClick);
        ryDirection.setOnClickListener(this::onClick);

        getDetailByID(mID);

    }

    private void getDetailByID(Integer id) {
        showLoading(true);
        NetworkEngine.getInstance().getstationDetail(id).enqueue(new Callback<Station>() {
            @Override
            public void onResponse(Call<Station> call, Response<Station> response) {
                showLoading(false);
                mStation = response.body();

                txtStationName.setText(mStation.getName());
                txtPhone.setText(mStation.getHotlineNumbers());
                txtAddress.setText(mStation.getAddress());
                txtType.setText(mStation.getType());
                txtDesc.setText(mStation.getDescription());


            }

            @Override
            public void onFailure(Call<Station> call, Throwable t) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }


    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        finish();
        return super.getParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        return true;
    }

    @Override
    public void onClick(View v) {

        //todo
        if (v == ryCallNow) {
            //Toast.makeText(this, "Onclick....", Toast.LENGTH_SHORT).show();
            callPhone("+"+txtPhone.getText().toString());
        }
        if (v == ryDirection ) {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%f,%f (%s)", mStation.getLatitude(), mStation.getLongitude(), mStation.getAddress());
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        }

    }

    private void callPhone(String ph) {
        if (!TextUtils.isEmpty(ph)) {
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(ph))));
        }

    }

}
