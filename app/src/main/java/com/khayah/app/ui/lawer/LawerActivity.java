package com.khayah.app.ui.lawer;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Station;
import com.khayah.app.models.Type;
import com.khayah.app.ui.adapter.AbSpinnerAdapter;
import com.khayah.app.ui.adapter.DirectoryRecyclerAdapter;
import com.khayah.app.util.EndlessRecyclerOnScrollListener;
import com.khayah.app.util.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LawerActivity extends BaseAppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Object> directories;
    private DirectoryRecyclerAdapter mRVAdapter;
    private EndlessRecyclerOnScrollListener endlessRecyclerOnScrollListener;
    private boolean isLoading = false;
    private boolean isLoadMore = true;
    private int page = 1;
    private List<Type> types = new ArrayList<>();
    private String selectedType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_directory));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        types.add(new Type(R.drawable.ic_vector_court, "All", ""));
        types.add(new Type(R.drawable.ic_vector_court, "Court House", "courthouse"));
        types.add(new Type(R.drawable.ic_vector_police, "Police Station", "police"));
        types.add(new Type(R.drawable.ic_vector_hospital, "Hospital", "hospital"));

        recyclerView            = (RecyclerView) findViewById(R.id.recyclerView);
        mLayoutManager          = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        directories = new ArrayList<>();
        mRVAdapter   = new DirectoryRecyclerAdapter(this, directories);
        recyclerView.setAdapter(mRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });

        endlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(mLayoutManager, 6) {
            @Override
            public void onLoadMore(int current_page) {
                //get next batch of results of exists
                if(!isLoading && isLoadMore) {
                    page++;
                    getLawyers(page, selectedType);
                }
            }
        };
        recyclerView.addOnScrollListener(endlessRecyclerOnScrollListener);

        if(isNetworkAvailable()) {
            getLawyers(1, selectedType);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lawyer, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);

        AbSpinnerAdapter spinnerAdapter = new AbSpinnerAdapter(this, types);

        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                page = 1;
                isLoadMore = true;
                directories.clear();
                mRVAdapter.notifyDataSetChanged();
                endlessRecyclerOnScrollListener.reset(1, false);
                selectedType = types.get(i).getAlias();
                getLawyers(page, selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        return true;
    }

    private void getLawyers(int page, String type) {
        isLoading = true;
        this.page = page;
        NetworkEngine.getInstance().getStation("type:like:"+type,this.page, 10).enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                isLoading = false;
                if(response.body().size() > 0)directories.addAll(response.body());
                mRVAdapter.notifyDataSetChanged();
                if(response.body().size() == 10){
                    isLoadMore = true;
                }else{
                    isLoadMore = false;
                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
                isLoading = false;
            }
        });
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        finish();
        return super.getParentActivityIntent();
    }
}
