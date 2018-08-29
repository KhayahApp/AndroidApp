package com.khayah.app.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.accountkit.AccountKit;
import com.facebook.login.LoginManager;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.gson.Gson;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.Constant;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.ui.adapter.SearchAdapter;
import com.khayah.app.ui.alarm.AlarmMainfragment;
import com.khayah.app.util.StorageDriver;
import com.khayah.app.vo.Building;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.squareup.picasso.Picasso;

/*import org.myanmarcultureheritage.adapters.SearchAdapter;
import org.myanmarcultureheritage.clients.NetworkEngine;
import org.myanmarcultureheritage.fragments.HeritageTypeFragment;
import org.myanmarcultureheritage.fragments.NearbyHeritageFragment;
import org.myanmarcultureheritage.models.Building;
import org.myanmarcultureheritage.models.GcmToken;
import org.myanmarcultureheritage.models.User;
import org.myanmarcultureheritage.models.Version;
import org.myanmarcultureheritage.services.GCMRegistrationIntentService;
import org.myanmarcultureheritage.utils.CircleTransform;
import org.myanmarcultureheritage.utils.DeviceUtil;
import org.myanmarcultureheritage.utils.StorageDriver;
import org.myanmarcultureheritage.views.CustomDialog;*/

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrawerMainActivity extends BaseAppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private MaterialSearchView searchView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private SearchAdapter mSearchAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.darker_gray));
        toolbar.setTitle(String.format(getResources().getString(R.string.search), getResources().getString(R.string.menu_1)));
        setSupportActionBar(toolbar);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareDialog.registerCallback(callbackManager, fbCallback);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_1);

        //LinearLayout accountHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.layout_header);
        ImageView accountImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user);
        TextView accountName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        TextView accountEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_email);

        // Check already login
        /*if (BaseApplication.isLogin()) {
            User user = (User) BaseApplication.getUser();
            Picasso.with(this).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                    +"/uploads/users/"+user.getAvatar()
                    : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                    .transform(new CircleTransform()).into(accountImage);
            accountName.setText(user.getFirstName() + " " + user.getLastName());
            accountEmail.setText(user.getEmail());
            navigationView.getMenu().findItem(R.id.nav_sign_in_out).setTitle(getResources().getString(R.string.logout));
            accountHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
            });
        } else {
            Picasso.with(this).load(R.drawable.boy).transform(new CircleTransform()).into(accountImage);
            accountHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
        }*/

        changeFontStyle(navigationView.getMenu());

        // Adding menu for fragment;
        List<MenuItem> menus = new ArrayList<>();
        menus.add(navigationView.getMenu().findItem(R.id.nav_1));
        menus.add(navigationView.getMenu().findItem(R.id.nav_2));
        /*menus.add(navigationView.getMenu().findItem(R.id.nav_menu_3));
        menus.add(navigationView.getMenu().findItem(R.id.nav_menu_4));
        menus.add(navigationView.getMenu().findItem(R.id.nav_menu_5));
        menus.add(navigationView.getMenu().findItem(R.id.nav_menu_6));
        menus.add(navigationView.getMenu().findItem(R.id.nav_menu_7));*/

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(false); //or false

        //searchView.setSearchLayoutHeight(toolbar.getLayoutParams().height);
        //searchView.isShowDefaultSuggestion(true);

        viewPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), menus);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if(position > 0) {
                    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            Toast.makeText(DrawerMainActivity.this, "Please choose suggestion heritage", Toast.LENGTH_LONG).show();
                            return true;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            searchHeritage(newText, position);
                            return false;
                        }
                    });
                    searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class)
                                    .putExtra("building", new Gson().toJson(parent.getAdapter().getItem(position))));
                        }
                    });
                    if(isNetworkAvailable()) {
                        searchHeritage("", position);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if(isNetworkAvailable()) {
            getCheckVersion();
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {

                //When the broadcast received
                //We are sending the broadcast from GCMRegistrationIntentService

                @Override
                public void onReceive(Context context, Intent intent) {
                    //If the broadcast has received with success
                    //that means device is registered successfully
                    /*if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_SUCCESS)){
                        //Getting the registration token from the intent
                        String token = intent.getStringExtra("token");
                        GcmToken gcm = new GcmToken();
                        gcm.setId(DeviceUtil.getInstance(DrawerMainActivity.this).getID());
                        gcm.setToken(token);
                        if(BaseApplication.isLogin()) {
                            User user = (User) BaseApplication.getUser();
                            gcm.setUserId(user.getId());
                        }


                        NetworkEngine.getInstance().postGcmToken(gcm).enqueue(new Callback<GcmToken>() {
                            @Override
                            public void onResponse(Call<GcmToken> call, Response<GcmToken> response) {

                            }

                            @Override
                            public void onFailure(Call<GcmToken> call, Throwable t) {

                            }
                        });

                        //if the intent is not with success then displaying error messages
                    } else if(intent.getAction().equals(GCMRegistrationIntentService.REGISTRATION_ERROR)){

                    }*/
                }
            };

            //Checking play service is available or not
            int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

            //if play service is not available
            if(ConnectionResult.SUCCESS != resultCode) {
                //If play service is supported but not installed
                if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                    //Displaying message that play service is not installed
                    Log.e("Gcm","Google play service is not install/enabled in this device!");
                    GooglePlayServicesUtil.showErrorNotification(resultCode, getApplicationContext());
                } else {
                    Log.e("Gcm","This device does not support for Google Play Service!");
                }

                //If play service is available
            } else {
                //Starting intent to register device
                /*Intent intent = new Intent(this, GCMRegistrationIntentService.class);
                startService(intent);*/
            }
        }
    }

    private void getCheckVersion() {
        /*NetworkEngine.getInstance().getVersion(1, 1).enqueue(new Callback<List<Version>>() {
            @Override
            public void onResponse(Call<List<Version>> call, Response<List<Version>> response) {
                if(response.isSuccessful() && response.body().size() > 0) {
                    StorageDriver.getInstance().saveTo(Constant.version, response.body().get(0));
                    if(isUpdateAvailable()) {
                        final CustomDialog dialog = new CustomDialog(DrawerMainActivity.this);
                        dialog.setTitle(getResources().getString(R.string.version_update_title));
                        dialog.setMessage(getResources().getString(R.string.version_update_message));
                        dialog.setOnClickPositiveListener(getResources().getString(R.string.str_yes_i), new CustomDialog.OnClickPositiveListener() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                Version version = StorageDriver.getInstance().selectFrom(Constant.version);
                                intent.setData(Uri.parse(version.getLink()));
                                startActivity(intent);
                            }
                        });
                        dialog.show();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Version>> call, Throwable t) {

            }
        });*/
    }

    private void searchHeritage(String keyword, int position) {
        String search = getSettings().getLocale().equals("mm") ? "name_mm:like:"+keyword : "name:like:"+keyword;
        /*NetworkEngine.getInstance().getBuildings(search+"|building_type_id:equal:"+position,1, 12).enqueue(new Callback<List<Building>>() {
            @Override
            public void onResponse(Call<List<Building>> call, Response<List<Building>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() > 0) {
                        mSearchAdapter = new SearchAdapter(DrawerMainActivity.this, response.body());
                        searchView.setAdapter(mSearchAdapter);
                        mSearchAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Building>> call, Throwable t) {

            }
        });*/
    }


    //Registering receiver on activity resume
    @Override
    protected void onResume() {
        super.onResume();
        /*LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_ERROR));*/
    }


    //Un registering receiver on activity paused
    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu_main, menu);
        menu.findItem(R.id.action_search).setIcon(getIconicDrawable(CommunityMaterial.Icon.cmd_magnify.toString(),android.R.color.darker_gray, 20));
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_1) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(0);
        }
        if (item.getItemId() == R.id.nav_2) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(1);
        }
        /*if (item.getItemId() == R.id.nav_menu_3) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(2);
        }
        if (item.getItemId() == R.id.nav_menu_4) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(3);
        }
        if (item.getItemId() == R.id.nav_menu_5) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(4);
        }
        if (item.getItemId() == R.id.nav_menu_6) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(5);
        }
        if (item.getItemId() == R.id.nav_menu_7) {
            getSupportActionBar().setTitle(String.format(getResources().getString(R.string.search), item.getTitle()));
            viewPager.setCurrentItem(6);
        }
        if (item.getItemId() == R.id.nav_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        if (item.getItemId() == R.id.nav_call_us) {
            String number = "tel: +959250111607";
            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(number));
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            startActivity(callIntent);
        }
        if (item.getItemId() == R.id.nav_help_us) {
            if(BaseApplication.isLogin())
                startActivity(new Intent(getApplicationContext(), HelpUsActivity.class));
            else
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        if (item.getItemId() == R.id.nav_about_us) {
            startActivity(new Intent(getApplicationContext(), AboutusActivity.class));
        }
        if (item.getItemId() == R.id.nav_share) {
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=org.myanmarcultureheritage"))
                        .build();
                shareDialog.show(linkContent);
            }

        }
        if (item.getItemId() == R.id.nav_sign_in_out) {
            if (BaseApplication.isLogin()) {
                LoginManager.getInstance().logOut();
                AccountKit.logOut();
                BaseApplication.logout();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                closeAllActivities();
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class PagerAdapter extends FragmentPagerAdapter {

        private List<MenuItem> list = new ArrayList<>();

        public PagerAdapter(FragmentManager fm, List<MenuItem> list) {
            super(fm);
            this.list = list;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return list.get(position).getTitle();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return AlarmMainfragment.newInstance();//NearbyHeritageFragment.newInstance(position);
            }else{
                return null;//HeritageTypeFragment.newInstance(position);
            }
        }

    }


}
