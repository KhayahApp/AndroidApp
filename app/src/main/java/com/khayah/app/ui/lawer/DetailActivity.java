package com.khayah.app.ui.lawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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


public class DetailActivity extends BaseAppCompatActivity {


    private Integer mID;
    private Station mStation;
    private TextView txtStationName;
    private TextView txtPhone;
    private TextView txtAddress;
    private TextView txtDesc;
    private TextView txtType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_directory));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

    }

    private void init(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            mID = bundle.getInt(Constant.POST_DETAIL_ID);
        }
        //Toast.makeText(getApplicationContext(),"ID"+ mID ,Toast.LENGTH_LONG).show();
        txtStationName = (TextView)findViewById(R.id.detail_text_name);
        txtPhone = (TextView)findViewById(R.id.detail_txt_ph);
        txtAddress = (TextView)findViewById(R.id.detail_txt_address);
        txtDesc = (TextView)findViewById(R.id.detail_txt_desc);
        txtType = (TextView)findViewById(R.id.detail_txt_type);
        getDetailByID(mID);

    }

    private void getDetailByID(Integer id){
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
}
