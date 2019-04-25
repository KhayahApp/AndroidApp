package com.khayah.app.ui.alarm;

import android.Manifest;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.khayah.app.APIToolz;
import com.khayah.app.BaseAppCompatActivity;
import com.khayah.app.Constant;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Station;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGeo;
import com.khayah.app.ui.adapter.MarkerInfoWindowAdapter;
import com.khayah.app.ui.home.MainActivity;
import com.khayah.app.ui.lawer.DetailActivity;
import com.khayah.app.util.CircleTransform;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    private List<Station> stations = new ArrayList<>();
    private FloatingActionButton fab;
    private static final int INITIAL_REQUEST = 1;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 4;
    private final String call_phone_READ_PERMISSION = "android.permission.CALL_PHONE";

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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPhone("+"+user.getPhone());
            }
        });
        //imgProfile = (RoundedImageView) findViewById(R.id.img_user);
    }

    private void getUserInfo(Integer id) {
        NetworkEngine.getInstance().profile(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    getSupportActionBar().setTitle(user.getFirstName() + " " + user.getLastName());
                    //getSupportActionBar().setSubtitle("+"+user.getPhone());
                    /*if (user.getAvatar() != null) {
                        Picasso.with(PersonActivity.this).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                                + "/uploads/users/" + user.getAvatar()
                                : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                                .transform(new CircleTransform()).into(imgProfile);
                    } else {
                        Picasso.with(PersonActivity.this).load(R.drawable.girl).transform(new CircleTransform()).into(imgProfile);
                    }*/
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

                    currentLatLng = new LatLng(response.body().get(0).getLat(), response.body().get(0).getLng());
                    MarkerOptions options = new MarkerOptions();
                    options.icon(bitmapDescriptorFromVector(PersonActivity.this, R.drawable.ic_vector_user));
                    options.position(currentLatLng);
                    options.title(user.getFirstName()+" "+user.getLastName()+" in Danger");

                    if(mMap != null) {
                        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getApplicationContext());
                        mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        Marker dangerMarker = mMap.addMarker(options);
                        dangerMarker.setTag(user);
                        dangerMarker.showInfoWindow();

                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(currentLatLng)
                                .zoom(13)
                                .build();

                        // Animate the change in camera view over 2 seconds
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),100, null);

                        GradientDrawable d = new GradientDrawable();
                        d.setShape(GradientDrawable.OVAL);
                        d.setSize(500,500);
                        d.setColor(0x555751FF);
                        d.setStroke(5, Color.TRANSPARENT);

                        Bitmap bitmap = Bitmap.createBitmap(d.getIntrinsicWidth()
                                , d.getIntrinsicHeight()
                                , Bitmap.Config.ARGB_8888);

                        // Convert the drawable to bitmap
                        Canvas canvas = new Canvas(bitmap);
                        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        d.draw(canvas);

                        // Radius of the circle
                        final int radius = 1000;

                        // Add the circle to the map
                        final GroundOverlay circle = mMap.addGroundOverlay(new GroundOverlayOptions()
                                .position(currentLatLng, 2 * radius).image(BitmapDescriptorFactory.fromBitmap(bitmap)));


                        ValueAnimator valueAnimator = new ValueAnimator();
                        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
                        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
                        valueAnimator.setIntValues(0, radius);
                        valueAnimator.setDuration(1000);
                        valueAnimator.setEvaluator(new IntEvaluator());
                        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                float animatedFraction = valueAnimator.getAnimatedFraction();
                                circle.setDimensions(animatedFraction * radius * 2);
                            }
                        });

                        valueAnimator.start();
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
        getNearbyStation();
    }

    private boolean hasPermission(String permission) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }
    }

    private void callPhone(String ph) {
        if (!hasPermission(call_phone_READ_PERMISSION)) {
            //if no permission, request permission
            String[] perms = {call_phone_READ_PERMISSION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, CALL_REQUEST);
            } else {
                if (ph != null) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                        //return;
                    }
                    startActivity(callIntent);

                }
            }

        } else {
            // Check if we were successful in obtaining the map.
            //Log.e("<<<tlgLeaderPhno else >>>", "===>" + tlgLeaderPhno);
            if (ph != null) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                startActivity(callIntent);
                return;

            }
        }
    }

    private void getNearbyStation() {
        NetworkEngine.getInstance().getStation("",1, 999).enqueue(new Callback<List<Station>>() {
            @Override
            public void onResponse(Call<List<Station>> call, Response<List<Station>> response) {
                if(response.isSuccessful() && mMap != null) {
                    stations.clear();
                    stations.addAll(response.body());
                    if(stations.size() > 0) {

                        for(Station station: stations) {
                            if(station.getLatitude() != null && station.getLongitude() != null) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(station.getLatitude(), station.getLongitude()))
                                        .icon(bitmapDescriptorFromVector(PersonActivity.this,
                                                getMaker(station.getType())))).setTag(station);
                            }
                        }
                        mMap.setOnMarkerClickListener(onClickMarker);
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Station>> call, Throwable t) {
            }
        });
    }

    private GoogleMap.OnMarkerClickListener onClickMarker = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(marker.getTag() != null && marker.getTag() instanceof Station){
                Station station = (Station) marker.getTag();
                Intent i = new Intent(getApplicationContext(), DetailActivity.class);
                i.putExtra(Constant.POST_DETAIL_ID,station.getId());
                startActivity(i);
            }
            return false;
        }
    };

    private int getMaker(String type) {
        if(type != null) {
            switch (type) {
                case "hospital":
                    return R.drawable.ic_vector_hospital;
                case "police":
                    return R.drawable.ic_vector_police;
                case "courthouse":
                    return R.drawable.ic_vector_court;
            }
        }
        return R.drawable.ic_vector_court;
    }

    @Nullable
    @Override
    public Intent getSupportParentActivityIntent() {
        onBackPressed();
        closeAllActivities();
        return super.getSupportParentActivityIntent();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        super.onBackPressed();
    }
}
