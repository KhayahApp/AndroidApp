package com.khayah.app.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.khayah.app.R;
import com.khayah.app.ui.widget.SquareImageView;


public class ImageAdapter extends BaseAdapter {
    private Context context;
    private final String[] mobileValues;

    public ImageAdapter(Context context, String[] mobileValues) {
        this.context = context;
        this.mobileValues = mobileValues;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

        if (convertView == null) {

            gridView = new View(context);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.alarm_grid_item, null);

            // set value into textview
            TextView textView = (TextView) gridView.findViewById(R.id.txt_cate_name);
            textView.setText(mobileValues[position]);

            // set image based on selected text
            SquareImageView imageView = (SquareImageView) gridView.findViewById(R.id.img_cate);

            String mobile = mobileValues[position];

            if (mobile.equals("Emergency Alarm")) {
                imageView.setImageResource(R.drawable.ic_alarm);
            } else if (mobile.equals("Take Photo")) {
                imageView.setImageResource(R.drawable.ic_camera);
            } else if (mobile.equals("Voice Recording")) {
                imageView.setImageResource(R.drawable.ic_micro_phone);
            } else {
                imageView.setImageResource(R.drawable.ic_video);
            }

        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    @Override
    public int getCount() {
        return mobileValues.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}

