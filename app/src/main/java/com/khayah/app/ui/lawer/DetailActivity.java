package com.khayah.app.ui.lawer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;


public class DetailActivity extends BaseAppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_directory));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            Integer station_id = bundle.getInt("id");
        }

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
}
