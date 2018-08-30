package com.khayah.app.ui.login;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.User;
import com.khayah.app.ui.home.MainActivity;
import com.khayah.app.util.CustomDialog;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseAppCompatActivity {

    private static final int REQUEST_PHONE_VERIFY_CODE = 1;
    private MaterialEditText edtEmail;
    private MaterialEditText edtPassword;
    private Button btnLogin;
    private LoginButton btnFacebook;
    private RelativeLayout btnLoginFacebook;
    private Button btnNewAccount;
    private CallbackManager callbackManager;
    private String verifyPhoneNumber;
    private JSONObject responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(getIconicDrawable(CommunityMaterial.Icon.cmd_close.toString(), R.color.theme_primary, 16));
        setSupportActionBar(toolbar);

        edtEmail 	        = (MaterialEditText) findViewById(R.id.edt_email);
        edtPassword	        = (MaterialEditText) findViewById(R.id.edt_password);
        btnLogin     	    = (Button)findViewById(R.id.btn_login);
        btnFacebook         = (LoginButton) findViewById(R.id.btn_facebook);
        btnLoginFacebook    = (RelativeLayout) findViewById(R.id.layout_login_facebook);
        btnNewAccount 	    = (Button)findViewById(R.id.btn_create);

        btnLogin.setOnClickListener(onclick);
        btnLoginFacebook.setOnClickListener(onclick);
        btnNewAccount.setOnClickListener(onclick);

        callbackManager = CallbackManager.Factory.create();
        btnFacebook.setReadPermissions(Arrays.asList("public_profile,email"));
        btnFacebook.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        if (response.getError() != null) {
                                        } else {
                                            responseData = object;
                                            verifyPhoneNumber();
                                        }
                                    }
                                });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,middle_name,last_name,email,gender,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

        printHashKey();
    }

    // To get SHA1 Hash key for Facebook App ID
    public void printHashKey() {
        try {
            // See SHA1 Hash Key in Log
            PackageInfo info = getPackageManager().getPackageInfo("com.khayah.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("FB KEY:","FB HashKey : "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {

        }
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(view == btnNewAccount){
                startActivity(new Intent(getApplication(), RegisterActivity.class));
            }
            if(view == btnLogin) {
                if(checkValidation()){
                    showLoading(true);
                    authenticate(edtEmail.getText().toString(), edtPassword.getText().toString());
                }
            }
            if(view == btnLoginFacebook){
                btnFacebook.performClick();
            }
        }
    };

    private void verifyPhoneNumber() {
        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        startActivityForResult(intent, REQUEST_PHONE_VERIFY_CODE);
    }

    private boolean checkValidation() {
        if(edtEmail.getText().length() == 0) {
            edtEmail.setError(getResources().getString(R.string.pls_enter_email));
            return false;
        }
        if(edtPassword.getText().length() == 0) {
            edtPassword.setError(getResources().getString(R.string.pls_enter_password));
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHONE_VERIFY_CODE) {
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            if (loginResult.getError() != null) {
            } else if (loginResult.wasCancelled()) {
            } else {
                showLoading(true);
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(final Account account) {
                        // Get phone number
                        PhoneNumber phoneNumber = account.getPhoneNumber();
                        verifyPhoneNumber = phoneNumber.getCountryCode()+phoneNumber.getPhoneNumber();
                        showLoading(true);
                        NetworkEngine.getInstance().checkUser("phone:equal:"+verifyPhoneNumber).enqueue(new Callback<List<User>>() {
                            @Override
                            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                if(response.isSuccessful()){
                                    if(response.body().size() == 0){
                                        register();
                                    }else{
                                        KhayahApp.login(response.body().get(0));
                                        //updateGcmDevice(response.body().get(0));
                                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<List<User>> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onError(final AccountKitError error) {
                        Log.e("Account Kit Error", "Account Kit Error:" + error.getUserFacingMessage());
                    }
                });
            }
        }
    }

    private void register() {
        final User user = new User();
        user.setFirstName(responseData.optString("first_name"));
        user.setLastName(responseData.optString("last_name"));
        user.setUsername(responseData.optString("first_name").toLowerCase()+responseData.optString("last_name").toLowerCase());
        user.setEmail(responseData.optString("email"));
        user.setPassword(responseData.optString("id"));
        user.setPasswordConfirmation(responseData.optString("id"));
        user.setPhone(verifyPhoneNumber);
        user.setFacebookId(responseData.optString("id"));
        user.setGender(responseData.optString("gender"));

        Log.i("User", new Gson().toJson(user));

        NetworkEngine.getInstance().register(user).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    authenticate(verifyPhoneNumber, user.getPassword());
                } else {
                    showLoading(false);
                    switch (response.code()) {
                        case 400:
                            String error = null;
                            try {
                                error = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final CustomDialog dialog = new CustomDialog(LoginActivity.this);
                            dialog.setTitle(getResources().getString(R.string.invalid_value));
                            dialog.setMessageType(CustomDialog.Error);
                            dialog.setMessage(error);
                            dialog.setOnClickPositiveListener(getResources().getString(R.string.try_again), new CustomDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void authenticate(String email, String password){
        NetworkEngine.getInstance().authenticate(email,password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showLoading(false);
                if(response.isSuccessful()) {
                    //updateGcmDevice(response.body());
                    KhayahApp.login(response.body());
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }else{
                    switch (response.code()) {
                        case 400:
                            String error = null;
                            try {
                                error = response.errorBody().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            final CustomDialog dialog = new CustomDialog(LoginActivity.this);
                            dialog.setTitle(getResources().getString(R.string.invalid_authenticate));
                            dialog.setMessageType(CustomDialog.Error);
                            dialog.setMessage(error);
                            dialog.setOnClickPositiveListener(getResources().getString(R.string.try_again), new CustomDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                            dialog.show();
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    /*private void updateGcmDevice(User user) {
        NetworkEngine.getInstance().updateGcmToken(DeviceUtil.getInstance(this).getID(), user.getId()).enqueue(new Callback<GcmToken>() {
            @Override
            public void onResponse(Call<GcmToken> call, Response<GcmToken> response) {

            }

            @Override
            public void onFailure(Call<GcmToken> call, Throwable t) {

            }
        });
    }*/

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        finish();
        return super.getSupportParentActivityIntent();
    }
}
