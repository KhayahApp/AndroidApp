package com.khayah.app.ui.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.khayah.app.Constant;
import com.khayah.app.R;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Station;
import com.khayah.app.ui.lawer.DetailActivity;
import com.khayah.app.ui.lawer.LawerActivity;
import com.khayah.app.util.GPSTracker;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NearbyMapFragment extends Fragment implements EasyPermissions.PermissionCallbacks, OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private static final int RC_ALL_PERMISSIONS = 2;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };/*Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,*/
    // Create a stroke pattern of a gap followed by a dash.
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(20);
    private static final List<PatternItem> PATTERN_POLYGON_ALPHA = Arrays.asList(GAP, DOT);
    private Context mContext;
    private View view;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private LatLng currentLatLng;
    private GPSTracker gpsTracker;

    //For Map Routing
    private LatLng routeTo;
    private ArrayList<Object> polylines;
    private Routing.TravelMode[] routeTypes= new Routing.TravelMode[4];
    private int currentRouteType = 0;
    private List<Station> stations = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_maps, container, false);
        //firt time permission
        firstpermissionRequestgoogleMap();
        return view;
    }

    @AfterPermissionGranted(RC_ALL_PERMISSIONS)
    private void firstpermissionRequestgoogleMap() {
        if (hasAllPermissions()) { // For one permission EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_SMS)
            // Have permission, do the thing!
            //Toast.makeText(getActivity(), "TODO: MAP and GPS things", Toast.LENGTH_LONG).show();
            //TODO your job
            setUpGoogleMap();

        } else {
            // Request one permission
            //EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms), RC_SMS_PERM, Manifest.permission.READ_SMS);
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_all_permissions),
                    RC_ALL_PERMISSIONS,
                    REQUIRED_PERMISSIONS);

        }
    }

    private boolean hasAllPermissions() {
        return EasyPermissions.hasPermissions(mContext, REQUIRED_PERMISSIONS);
    }


    private void setUpGoogleMap() {


        if (mMap == null) {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                EasyPermissions.requestPermissions(
                        this,
                        getString(R.string.rationale_all_permissions),
                        RC_ALL_PERMISSIONS,
                        REQUIRED_PERMISSIONS);
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            if (mMap != null) {
                getNearbyStation();
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
                                        .icon(bitmapDescriptorFromVector(getActivity(),
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

    private GoogleMap.OnMarkerClickListener onClickMarker = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            if(marker.getTag() != null && marker.getTag() instanceof Station){
                Station station = (Station) marker.getTag();
                Toast.makeText(getActivity(), "Clicked: "+ station.getName(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(mContext, DetailActivity.class);
                i.putExtra(Constant.POST_DETAIL_ID,station.getId());
                startActivity(i);
            }
            return false;
        }
    };


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(getActivity(), perms)) {
            new AppSettingsDialog.Builder(getActivity()).build().show();
        } else {
            //onLogin(mLoginType);
            //TODO your job
            setUpGoogleMap();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getActivity(), "onMapReady", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        enableMyLocation();
        getNearbyStation();
    }
    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_all_permissions),
                    RC_ALL_PERMISSIONS,
                    REQUIRED_PERMISSIONS);
            return;
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            //mMap.setMyLocationEnabled(true);
            getNearbyStation();
            gpsTracker = new GPSTracker(mContext);
            if(gpsTracker.canGetLocation() && gpsTracker.getLongitude() > 0 && gpsTracker.getLongitude() > 0) {
                currentLatLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
                //Toast.makeText(mContext, "Current Lat Lng" + currentLatLng, Toast.LENGTH_SHORT).show();
                goToCurrentLocation();

                routeTypes[0] = Routing.TravelMode.DRIVING;
                routeTypes[1] = Routing.TravelMode.TRANSIT;
                routeTypes[2] = Routing.TravelMode.WALKING;
                routeTypes[3] = Routing.TravelMode.BIKING;

                routeTo = new LatLng(16.849610, 96.117740);
                getRoute(routeTypes[currentRouteType], routeTo);
            }
        }
    }

    private void goToCurrentLocation() {
        MarkerOptions options = new MarkerOptions();
        options.icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_vector_user));
        options.position(currentLatLng);


        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("You Are Here!"));
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(currentLatLng)
                .zoom(13)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),100, null);


        // Add a circle in Current Location
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(currentLatLng)
                .radius(5000)
                .strokeWidth(4)
                .strokeColor(getResources().getColor(R.color.theme_primary))
                .fillColor(getResources().getColor(R.color.theme_primary_light)));

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(mContext, "onMyLocationButtonClick" + currentLatLng, Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(mContext, "onMyLocationClick" + currentLatLng, Toast.LENGTH_SHORT).show();


    }

    //TODO Map Routing Method
    private void getRoute(Routing.TravelMode travelMode, LatLng toRoute) {
        Routing routing = new Routing.Builder()
                .travelMode(travelMode)
                .withListener(new RoutingListener() {
                    @Override
                    public void onRoutingFailure(RouteException e) {
                        //showLoading(false);
                        Toast.makeText(mContext, "This routing is not available.", Toast.LENGTH_LONG).show();
                        Log.e("Routing error0","0===>" +e.getMessage());
                        Log.e("Routing error1","1===>" +e.getStatusCode());
                        Log.e("Routing error2","2===>" +e.toString());
                    }

                    @Override
                    public void onRoutingStart() {

                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
                        //showLoading(false);
                        mMap.clear();
                        goToCurrentLocation();
                        mMap.addMarker(new MarkerOptions()
                                .position(routeTo) //new LatLng(building.getLat(), building.getLng())
                                .icon(bitmapDescriptorFromVector(mContext, R.drawable.ic_vector_lawyer)));

                        //.icon(bitmapDescriptorFromVector(getActivity(), getMaker(building.getType())))).setTag(building);
                        /*for(Building building: stations) {

                        }
                        if(slidingPanel.getPanelState() != SlidingUpPanelLayout.PanelState.COLLAPSED) {
                            recyclerView.smoothScrollToPosition(0);
                            slidingPanel.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                        }*/
                        //mMap.setOnMarkerClickListener(onClickMarker);

                        polylines = new ArrayList<>();
                        //add route(s) to the map.
                        double distance = 0.0;
                        for (int i = 0; i < routes.size(); i++) {
                            PolylineOptions polyOptions = new PolylineOptions();
                            polyOptions.pattern(PATTERN_POLYGON_ALPHA);
                            polyOptions.color(getResources().getColor(R.color.theme_primary));
                            polyOptions.width(20);
                            polyOptions.addAll(routes.get(i).getPoints());
                            Polyline polyline = mMap.addPolyline(polyOptions);
                            polylines.add(polyline);
                            distance += routes.get(i).getDistanceValue();
                        }
                    }

                    @Override
                    public void onRoutingCancelled() {

                    }
                })
                .waypoints(currentLatLng, toRoute)
                .build();
        routing.execute();
    }


}
