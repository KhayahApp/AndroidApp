package com.khayah.app.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;
import com.khayah.app.models.Lang;
import com.khayah.app.ui.adapter.LanguageAdapter;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.util.ArrayList;
import java.util.List;

public class LanguageActivity extends BaseAppCompatActivity {

    private TextView txt_title;
    private ListView lst_language;
    private LanguageAdapter langAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText(getResources().getString(R.string.choose_language));
        toolbar.setNavigationIcon(getIconicDrawable(CommunityMaterial.Icon.cmd_close.toString(), android.R.color.white, 16));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<Lang> languages = new ArrayList<>();
        languages.add(new Lang(R.drawable.ic_mm, "Myanmar", "mm"));
        languages.add(new Lang(R.drawable.ic_en, "English", "en"));

        lst_language = (ListView) findViewById(R.id.lst_language);

        langAdapter = new LanguageAdapter(this, languages);
        lst_language.setAdapter(langAdapter);
        lst_language.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("lang", new Gson().toJson(languages.get(position)));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        langAdapter.notifyDataSetChanged();


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
