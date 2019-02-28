package com.khayah.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.khayah.app.APIToolz;
import com.khayah.app.R;
import com.khayah.app.models.Station;
import com.khayah.app.models.User;
import com.khayah.app.util.CircleTransform;
import com.squareup.picasso.Picasso;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public MarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        return null;
    }

    @Override
    public View getInfoContents(Marker arg0) {

        LatLng latLng = arg0.getPosition();
        if(arg0.getTag() instanceof User) {
            User user = (User) arg0.getTag();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v =  inflater.inflate(R.layout.map_marker_info_window, null);
            TextView tvName = (TextView) v.findViewById(R.id.tv_name);
            TextView tvPhone = (TextView) v.findViewById(R.id.tv_phone);
            ImageView imgUser = (ImageView) v.findViewById(R.id.img_user);
            tvName.setText(user.getFirstName()+" "+user.getLastName() + " in Danger");
            tvPhone.setText("+"+ user.getPhone());
            if (user.getAvatar() != null) {
                Picasso.with(context).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                        + "/uploads/users/" + user.getAvatar()
                        : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                        .transform(new CircleTransform()).into(imgUser);
            } else {
                Picasso.with(context).load(R.drawable.girl).transform(new CircleTransform()).into(imgUser);
            }
            return v;
        } else {
            return null;
        }
    }
}