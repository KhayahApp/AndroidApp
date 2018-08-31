package com.khayah.app.ui.lawer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Lawer;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawer);

        ListView lstLawer = (ListView) findViewById(R.id.lst_lawer);
        getLawers();

    }

    private void getLawers() {
        NetworkEngine.getInstance().getLawers(1, 10).enqueue(new Callback<List<Lawer>>() {
            @Override
            public void onResponse(Call<List<Lawer>> call, Response<List<Lawer>> response) {
                Log.i("Hello","Hello Lawer List: "+response.body().size());
            }

            @Override
            public void onFailure(Call<List<Lawer>> call, Throwable t) {

            }
        });
    }
}
