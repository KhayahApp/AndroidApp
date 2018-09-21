package com.khayah.app.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.khayah.app.R;
import com.khayah.app.models.PersonUtils;
import com.khayah.app.models.Station;

import java.util.List;


public class DirectoryRecyclerAdapter extends RecyclerView.Adapter<DirectoryRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Object> lists;

    public DirectoryRecyclerAdapter(Context context, List<Object> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.directory_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(lists.get(position));

        Object directory = lists.get(position);
        if(directory instanceof Station) {
            Station station = (Station) directory;
            holder.txt_name.setText(station.getName());
            holder.txt_phone.setText(station.getHotlineNumbers());
            holder.txt_address.setText(station.getAddress());
        }



    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView txt_name;
        public TextView txt_phone;
        public TextView txt_address;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_name = (TextView) itemView.findViewById(R.id.txt_name);
            txt_phone = (TextView) itemView.findViewById(R.id.txt_phone);
            txt_address = (TextView) itemView.findViewById(R.id.txt_address);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PersonUtils cpu = (PersonUtils) view.getTag();

                    Toast.makeText(view.getContext(), cpu.getPersonName()+" is "+ cpu.getJobProfile(), Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

}
