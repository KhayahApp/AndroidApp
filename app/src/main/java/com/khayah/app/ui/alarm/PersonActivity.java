package com.khayah.app.ui.alarm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khayah.app.APIToolz;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGeo;
import com.khayah.app.util.CircleTransform;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends BaseAppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private User user;
    private RoundedImageView imgProfile;
    private GoogleMap mMap;
    private LatLng currentLatLng;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("user_id");
            getUserInfo(Integer.parseInt(userId));
            setUpGoogleMap();
        }

        imgProfile = (RoundedImageView) findViewById(R.id.img_user);
    }

    private void getUserInfo(Integer id) {
        NetworkEngine.getInstance().profile(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    getSupportActionBar().setTitle(user.getFirstName() + " " + user.getLastName());
                    getSupportActionBar().setSubtitle(user.getPhone());
                    if (user.getAvatar() != null) {
                        Picasso.with(PersonActivity.this).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                                + "/uploads/users/" + user.getAvatar()
                                : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                                .transform(new CircleTransform()).into(imgProfile);
                    } else {
                        Picasso.with(PersonActivity.this).load(R.drawable.girl).transform(new CircleTransform()).into(imgProfile);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void getUserGeo(Integer id) {
        NetworkEngine.getInstance().getUserGeo("user_id:equal:"+id+"|type:equal:Danger_Track","id","desc", 1, 12).enqueue(new Callback<List<UserGeo>>() {
            @Override
            public void onResponse(Call<List<UserGeo>> call, Response<List<UserGeo>> response) {
                if(response.isSuccessful() && response.body().size() > 0) {

                    currentLatLng = new LatLng(response.body().get(0).getLat(), response.body().get(0).getLat());
                    MarkerOptions options = new MarkerOptions();
                    options.icon(bitmapDescriptorFromVector(PersonActivity.this, R.drawable.ic_vector_user));
                    options.position(currentLatLng);

                    if(mMap != null) {
                        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You Are Here!"));
                        mMap.addMarker(options);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(currentLatLng)
                                .zoom(13)
                                .build();

                        // Animate the change in camera view over 2 seconds
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),100, null);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserGeo>> call, Throwable t) {

            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void setUpGoogleMap() {
        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getUserGeo(Integer.parseInt(userId));
    }
}
