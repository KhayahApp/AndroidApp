package com.khayah.app.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.Routing;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.khayah.app.APIToolz;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.Constant;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Error;
import com.khayah.app.models.UserGeo;
import com.khayah.app.models.UserGroup;
import com.khayah.app.service.PowerButtonReceiver;
import com.khayah.app.service.SettingsContentObserver;
import com.khayah.app.trace.G;
import com.khayah.app.ui.add_user.CircleListActivity;
import com.khayah.app.ui.add_user.TruestedUserListDialogFragment;
import com.khayah.app.ui.add_user.TrustedUserFragment;
import com.khayah.app.models.User;
import com.khayah.app.ui.alarm.AlarmMainfragment;
import com.khayah.app.ui.lawer.LawerActivity;
import com.khayah.app.ui.lawer.LawerListFragment;
import com.khayah.app.ui.login.LoginActivity;
import com.khayah.app.ui.login.ProfileActivity;
import com.khayah.app.ui.map.NearbyMapFragment;
import com.khayah.app.ui.menu_record.RecordFragment;
import com.khayah.app.ui.settings.HelpUsActivity;
import com.khayah.app.ui.settings.SettingsActivity;
import com.khayah.app.ui.userlist.UserListFragment;
import com.khayah.app.util.CircleTransform;
//import com.khayah.app.vo.User;
import com.khayah.app.util.DeviceUtil;
import com.khayah.app.util.GPSTracker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BaseAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    private View view;
    String mToken;
    private boolean mAlert = false;
    private LatLng currentLatLng;
    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private User user;

    // [END declare_analytics]

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mAlert = bundle.getBoolean("alert");
        }
        //tagTargetExplain(toolbar);

        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_COMMOM_TOPIC_FOR_ALL);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, fbCallback);

        /*LinearLayout accountHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.layout_header);
        RoundedImageView accountImage = (RoundedImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user);
        TextView accountName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        TextView accountEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_email);*/
        // Check already login
        LinearLayout accountHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.layout_header);
        RoundedImageView accountImage = (RoundedImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user);
        TextView accountName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        TextView accountEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_email);
        if (KhayahApp.isLogin()) {

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    mToken = instanceIdResult.getToken();
                    //Log.e("Token",mToken);
                }
            });

            user = (User) KhayahApp.getUser();
            if (mToken != null) {
                sendUserIDandfcmToken(user.getId(), DeviceUtil.getInstance(MainActivity.this).getID(), mToken);
            }

            Log.i("FCM User", "FCM User: " + Constant.FCM_COMMOM_TOPIC_FOR_USER + user.getPhone());
            FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_COMMOM_TOPIC_FOR_USER + user.getPhone());
            Picasso.with(this).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                    + "/uploads/users/" + user.getAvatar()
                    : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                    .transform(new CircleTransform()).into(accountImage);
            accountName.setText(user.getFirstName() + " " + user.getLastName());
            accountEmail.setText(user.getEmail());
            accountHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

                }
            });

            // Check for User is Activated
            NetworkEngine.getInstance().profile(user.getId()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getActive().equals(0)) {
                            Log.i("Active", "Active: " + response.body().getActive());
                            KhayahApp.logout();
                            closeAllActivities();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        } else {
                            Log.i("Active", "Active: " + response.body().getActive());
                            KhayahApp.login(response.body());
                        }
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            Picasso.with(this).load(R.drawable.girl).transform(new CircleTransform()).into(accountImage);
            accountHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
        }

        changeFontStyle(navigationView.getMenu());
        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_1);

        // add power button event
        /*IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        PowerButtonReceiver mPowerButtonReceiver = new PowerButtonReceiver(this);
        registerReceiver(mPowerButtonReceiver, filter);*/

        // add volume button event
        // SettingsContentObserver mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
        // getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
    }

    private FacebookCallback<Sharer.Result> fbCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onSuccess(Sharer.Result result) {

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    };


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank

        /*
                int id = item.getItemId();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        AlarmMainfragment alarmMainfragment = new AlarmMainfragment();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            //fragmentManager.beginTransaction().replace(R.id.content_frame, mainMaterialTab).commit();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        */


        return true;
    }

    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_1:
                fragment = TrustedUserFragment.newInstance(mAlert, null);
                break;
            case R.id.nav_2:
                fragment = new NearbyMapFragment();
                break;
            /*case R.id.nav_3:
                //fragment = new RecordFragment();
                fragment = new ComingSoonFragment();
                break;*/
            case R.id.nav_4:
                startActivity(new Intent(getApplicationContext(), LawerActivity.class));
                break;
            case R.id.nav_5:
                //fragment = new AboutUsFragment();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.ABOUT_US));
                startActivity(browserIntent);
                break;
            case R.id.nav_6:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                break;
            case R.id.nav_share:
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            //.setContentTitle("Hello, Khayar is with you!")
                            //.setContentDescription("You can add your trusted contacts for your safety connection!")
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()))
                            .setQuote("Available on Google Play Store")
                            .build();
                    shareDialog.show(linkContent);
                }
                break;
            case R.id.nav_send:
                startActivity(new Intent(getApplicationContext(), HelpUsActivity.class));
                break;

        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /*@Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return null;
    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void tagTargetExplain(Toolbar toolbar) {

        //toolbar.inflateMenu(R.menu.menu_test);
        final SpannableString sassyDesc = new SpannableString("It allows you to show more menu, you can choose!");
        sassyDesc.setSpan(new StyleSpan(Typeface.ITALIC), sassyDesc.length() - "choose".length(), sassyDesc.length(), 0);

        TapTargetView.showFor(this,

                TapTarget.forView(findViewById(R.id.nav_view), "Hello, Khayah is with you!", sassyDesc)
                        .cancelable(false)
                        .drawShadow(true)
                        .titleTextDimen(R.dimen.title_text_size)
                        .tintTarget(false), new TapTargetView.Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        // .. which evidently starts the sequence we defined earlier
                        //sequence.start();
                    }

                    @Override
                    public void onOuterCircleClick(TapTargetView view) {
                        super.onOuterCircleClick(view);
                        Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                        Log.d("TapTargetViewSample", "You dismissed me :(");
                    }
                });

    }

    private void sendUserIDandfcmToken(int userId, String deviceId, String token) {
        NetworkEngine.getInstance().addUserFcmTokenandDeviceID(userId, deviceId, token).enqueue(new Callback<UserGroup>() {
            @Override
            public void onResponse(Call<UserGroup> call, Response<UserGroup> response) {
                //Log.d("TapTargetViewSample", "You dismissed me :(");

            }

            @Override
            public void onFailure(Call<UserGroup> call, Throwable t) {
                //Log.d("TapTargetViewSample", "You dismissed me :(");


            }
        });
    }

}
