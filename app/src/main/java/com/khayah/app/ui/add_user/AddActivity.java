package com.khayah.app.ui.add_user;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGroup;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddActivity extends BaseAppCompatActivity {

    private EditText edtName;
    private EditText edtPhone;
    private Button btnSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getIconicDrawable(CommunityMaterial.Icon.cmd_close.toString(), R.color.theme_primary, 16));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtName             = (EditText) findViewById(R.id.edt_name);
        edtPhone            = (EditText) findViewById(R.id.edt_phone_number);
        btnSave             = (Button) findViewById(R.id.btn_add_user);
        btnSave.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnSave) {
                if(checkValidate())
                    addUser();
            }
        }
    };

    private void addUser() {
        showLoading(true);
        UserGroup group = new UserGroup();
        group.setUserId(((User)KhayahApp.getUser()).getId());
        group.setName(edtName.getText().toString());
        group.setPhone(edtPhone.getText().toString());
        NetworkEngine.getInstance().addUser(group).enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                showLoading(false);
                if(response.isSuccessful()){
                    Intent data = new Intent();
                    data.putExtra("add_user", new Gson().toJson(response.body()));
                    setResult(RESULT_OK, data);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {

            }
        });
    }

    private boolean checkValidate(){
        if(edtName.getText().length() == 0){
            edtName.setError(getResources().getString(R.string.pls_enter_name));
            return false;
        }
        if(edtPhone.getText().length() == 0){
            edtPhone.setError(getResources().getString(R.string.pls_enter_phone));
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Intent getParentActivityIntent() {
        finish();
        return super.getParentActivityIntent();
    }
}
