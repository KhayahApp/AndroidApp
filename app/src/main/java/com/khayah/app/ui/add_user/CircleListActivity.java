package com.khayah.app.ui.add_user;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jh.circularlist.CircularAdapter;
import com.jh.circularlist.CircularListView;
import com.khayah.app.R;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CircleListActivity extends AppCompatActivity {

    private CircularItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_circle_ls_main);

        // simple text item with numbers 0 ~ 9
        ArrayList<String> itemTitles = new ArrayList<>();
        for(int i = 0 ; i < 6 ; i ++){
            itemTitles.add(String.valueOf(i));
        }



        // usage sample
        final com.jh.circularlist.CircularListView circularListView = (com.jh.circularlist.CircularListView) findViewById(R.id.my_circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), itemTitles);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(130);//100 origin
        circularListView.setOnItemClickListener(new com.jh.circularlist.CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(CircleListActivity.this,
                        "view at index " + i + " is clicked!",
                        Toast.LENGTH_SHORT).show();
            }
        });
       /* circularListView.setOnItemClickListener(new CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int index) {
                Toast.makeText(CircleListActivity.this,
                        "view at index " + index + " is clicked!",
                        Toast.LENGTH_SHORT).show();
            }
        });*/


        // remove item example
        Button btRemoveItem = (Button) findViewById(R.id.bt_remove_item);
        btRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeItemAt(0);
            }
        });


        // add item example
        Button btAddItem = (Button) findViewById(R.id.bt_add_item);
        btAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                itemView.setText(String.valueOf(adapter.getCount() + 1));
                adapter.addItem(view);
            }
        });


        // remove item example
        Button btEnlargeRadius = (Button) findViewById(R.id.bt_enlarge_radius);
        btEnlargeRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularListView.setRadius(circularListView.radius += 15);
            }
        });


        // remove item example
        Button btReduceRadius = (Button) findViewById(R.id.bt_reduce_radius);
        btReduceRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circularListView.setRadius(circularListView.radius -= 15);
            }
        });
    }



    // you should extends CircularAdapter to add your custom item
    private class CircularItemAdapter extends CircularAdapter {

        private ArrayList<String> mItems;
        private LayoutInflater mInflater;
        private ArrayList<View> mItemViews;

        public CircularItemAdapter(LayoutInflater inflater, ArrayList<String> items){
            this.mItemViews = new ArrayList<>();
            this.mItems = items;
            this.mInflater = inflater;

            for(final String s : mItems){
                View view = mInflater.inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                IconicsImageView imgView = (IconicsImageView) view.findViewById(R.id.item_icon);

                itemView.setText(s);
                //imgView.setImageDrawable(R.drawable.ic_vector_lawyer_icon);


                Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                builder.downloader(new OkHttpDownloader(getApplicationContext()));
                builder.build().load("https://greenwaymyanmar.com/uploads/avatars/1526990999_5b040897e3804.png")//dataList.get(position).getThumbnailUrl()
                        .placeholder((R.drawable.boy))
                        .error(R.drawable.ic_vector_lawyer_icon)
                        .into(imgView);
                mItemViews.add(view);
            }
        }

        @Override
        public ArrayList<View> getAllViews() {
            return mItemViews;
        }

        @Override
        public int getCount() {
            return mItemViews.size();
        }

        @Override
        public View getItemAt(int i) {
            return mItemViews.get(i);
        }

        @Override
        public void removeItemAt(int i) {
            if(mItemViews.size() > 0) {
                mItemViews.remove(i);
                notifyItemChange();
            }
        }

        @Override
        public void addItem(View view) {
            mItemViews.add(view);
            notifyItemChange();
        }
    }

}
