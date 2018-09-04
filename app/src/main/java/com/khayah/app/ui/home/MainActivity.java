package com.khayah.app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.khayah.app.APIToolz;
import com.khayah.app.Constant;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.models.User;
import com.khayah.app.ui.alarm.AlarmMainfragment;
import com.khayah.app.ui.lawer.LawerActivity;
import com.khayah.app.ui.login.LoginActivity;
import com.khayah.app.ui.login.ProfileActivity;
import com.khayah.app.ui.map.NearbyMapFragment;
import com.khayah.app.ui.userlist.UserListFragment;
import com.khayah.app.util.CircleTransform;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,HasSupportFragmentInjector {

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    /**
     * The {@code FirebaseAnalytics} used to record screen views.
     */
    // [START declare_analytics]
    private FirebaseAnalytics mFirebaseAnalytics;
    // [END declare_analytics]

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(Constant.FCM_COMMOM_TOPIC_FOR_ALL);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        LinearLayout accountHeader = (LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.layout_header);
        ImageView accountImage = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.img_user);
        TextView accountName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_name);
        TextView accountEmail = (TextView) navigationView.getHeaderView(0).findViewById(R.id.txt_email);

        // Check already login
        if (KhayahApp.isLogin()) {
            User user = (User) KhayahApp.getUser();
            Picasso.with(this).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                    +"/uploads/users/"+user.getAvatar()
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
        } else {
            Picasso.with(this).load(R.drawable.girl).transform(new CircleTransform()).into(accountImage);
            accountHeader.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            });
        }

        //add this line to display menu1 when the activity is loaded
        displaySelectedScreen(R.id.nav_1);
    }

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
    }

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
                fragment = new AlarmMainfragment();
                break;
            case R.id.nav_2:
                //fragment = new Menu2();
                fragment = new NearbyMapFragment();
                break;
            case R.id.nav_3:
                //fragment = new Menu2();
                //fragment =  UserListFragment.newInstance();
                startActivity(new Intent(getApplicationContext(), LawerActivity.class));
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
}
