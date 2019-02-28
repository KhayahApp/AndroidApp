package com.khayah.app.ui.settings;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;
import com.khayah.app.models.Lang;
import com.khayah.app.ui.home.MainActivity;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Settings;

import java.util.ArrayList;

public class SettingsActivity extends BaseAppCompatActivity {

    private static final int LANGUAGE = 1;
    private TextView txt_title;
    private RadioGroup rdo_fonts;
    private RadioButton rdo_default;
    private RadioButton rdo_zawgyi;
    private RadioButton rdo_unicode;
    private String selectedFont;
    private LinearLayout layout_language;
    private TextView txt_language;
    private String selectedLang;
    private TextView txt_version;
    private boolean isCached;
    private boolean isNoti = true;
    private ArrayList<Lang> languages;
    private CheckBox chk_sound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_title = (TextView) toolbar.findViewById(R.id.txt_title);
        txt_title.setText(getResources().getString(R.string.settings));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rdo_fonts = (RadioGroup) findViewById(R.id.rdo_fonts);
        rdo_default = (RadioButton) findViewById(R.id.rdo_default);
        rdo_zawgyi = (RadioButton) findViewById(R.id.rdo_zawgyi);
        rdo_unicode = (RadioButton) findViewById(R.id.rdo_unicode);
        chk_sound = (CheckBox) findViewById(R.id.chk_sound);
        Boolean soundOn = StorageDriver.getInstance().selectFrom("soundFlag");
        if(soundOn != null) chk_sound.setChecked(soundOn);
        chk_sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                StorageDriver.getInstance().saveTo("soundFlag", isChecked);
            }
        });

        languages = new ArrayList<>();
        languages.add(new Lang(R.drawable.ic_mm, "Myanmar", "mm"));
        languages.add(new Lang(R.drawable.ic_en, "English", "en"));

        layout_language = (LinearLayout) findViewById(R.id.layout_language);
        txt_language = (TextView) findViewById(R.id.txt_language);
        for (Lang lang: languages){
            if(lang.getShortName().equals(getSettings().getLocale())){
                txt_language.setText(lang.getName());
            }
        }
        layout_language.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SettingsActivity.this, v, "profile");
                Intent intent = new Intent(getApplicationContext(), LanguageActivity.class);
                startActivityForResult(intent, LANGUAGE, options.toBundle());
            }
        });

        if(getSettings().getFontStyle().equals("zawgyi")){
            rdo_zawgyi.setChecked(true);
            selectedFont = "zawgyi";
        }else if(getSettings().getFontStyle().equals("myanmar3")) {
            rdo_unicode.setChecked(true);
            selectedFont = "myanmar3";
        }else{
            rdo_default.setChecked(true);
            selectedFont = "default";
        }

        rdo_fonts.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.rdo_default){
                    selectedFont = "default";
                }
                if(checkedId == R.id.rdo_zawgyi){
                    selectedFont = "zawgyi";
                }
                if(checkedId == R.id.rdo_unicode){
                    selectedFont = "myanmar3";
                }
            }
        });


        txt_version = (TextView) findViewById(R.id.txt_version);
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            txt_version.setText("v"+pInfo.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LANGUAGE) {
            if(resultCode == RESULT_OK) {
                Lang resultLang = new Gson().fromJson(data.getStringExtra("lang"), Lang.class);
                selectedLang = resultLang.getShortName();
                for (Lang lang: languages){
                    if(lang.getShortName().equals(resultLang.getShortName())){
                        txt_language.setText(lang.getName());
                    }
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem mItem = menu.findItem(R.id.action_save);
        LinearLayout rootView = (LinearLayout) mItem.getActionView();
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(mItem);
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_save){
            save();
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        Settings settings = getSettings();
        if(selectedLang != null){
            settings.setLocale(selectedLang);
        }
        settings.setFontStyle(selectedFont);
        settings.setCached(isCached);
        settings.setNoti(isNoti);
        settings.save();
        closeAllActivities();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        save();
        super.onBackPressed();
    }
}
