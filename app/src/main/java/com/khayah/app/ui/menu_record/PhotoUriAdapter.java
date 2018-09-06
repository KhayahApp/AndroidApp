package com.khayah.app.ui.menu_record;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.khayah.app.R;

import java.util.List;

public class PhotoUriAdapter extends RecyclerView.Adapter<PhotoUriAdapter.UriViewHolder> {

    private List<Uri> mUris;
    private List<String> mPaths;

    void setData(List<Uri> uris, List<String> paths) {//
        mUris = uris;
        mPaths = paths;
        notifyDataSetChanged();
    }

    @Override
    public UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UriViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_photo_uri_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UriViewHolder holder, int position) {
        /*holder.mUri.setText(mUris.get(position).toString());
        holder.mPath.setText(mPaths.get(position));

        holder.mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        holder.mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);*/

        Bitmap bitmap= BitmapFactory.decodeFile(mPaths.get(position));
        if (bitmap != null) {
            holder.mImg.setImageBitmap(bitmap);

        }
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    static class UriViewHolder extends RecyclerView.ViewHolder {

        private TextView mUri;
        private TextView mPath;
        private ImageView mImg;

        UriViewHolder(View contentView) {
            super(contentView);
            mUri = (TextView) contentView.findViewById(R.id.uri);
            mPath = (TextView) contentView.findViewById(R.id.path);
            mImg =contentView.findViewById(R.id.img_display);
        }
    }
}
