package com.khayah.app.ui.menu_record;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.khayah.app.R;
import com.makeramen.roundedimageview.RoundedImageView;


public class SnapRecyclerRecordAdapter extends RecyclerView.Adapter<SnapRecyclerRecordAdapter.ViewHolder> {

    private String[] titles;

    private RecyclerViewClickListener mListener;

    /*SnapRecyclerRecordAdapter(RecyclerViewClickListener listener) {
    }*/

    public SnapRecyclerRecordAdapter(String[] titles, RecyclerViewClickListener listener) {
        this.titles = titles;
        this.mListener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_record_horizontal, viewGroup, false);
        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String title = titles[position];
        holder.title.setText(title);

        String function = titles[position];

        if (function.equals("Audio")) {
            holder.img.setImageResource(R.mipmap.ic_mircor_phone);
        } else if (function.equals("Video")) {
            holder.img.setImageResource(R.mipmap.ic_video);
        } else if (function.equals("Photo")) {
            holder.img.setImageResource(R.mipmap.ic_take_photo);
        }



    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView title;
        private RoundedImageView img;
        private RecyclerViewClickListener mListener;

        ViewHolder(final View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            mListener = listener;
            itemView.setOnClickListener(this);

            this.title = (TextView) itemView.findViewById(R.id.title);
            this.img = (RoundedImageView)itemView.findViewById(R.id.icon);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());

        }
    }

    public interface RecyclerViewClickListener {

        void onClick(View view, int position);
    }
}