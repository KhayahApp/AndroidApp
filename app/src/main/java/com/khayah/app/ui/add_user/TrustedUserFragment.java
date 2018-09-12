package com.khayah.app.ui.add_user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.jh.circularlist.CircularAdapter;
import com.khayah.app.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrustedUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrustedUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrustedUserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private View view;
    private CircularItemAdapter adapter;

    public TrustedUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrustedUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrustedUserFragment newInstance(String param1, String param2) {
        TrustedUserFragment fragment = new TrustedUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_trusted_user, container, false);


        // simple text item with numbers 0 ~ 9
        ArrayList<String> itemTitles = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            itemTitles.add(String.valueOf(i) + "khin_sandar");
        }


        // usage sample
        final com.jh.circularlist.CircularListView circularListView = (com.jh.circularlist.CircularListView) view.findViewById(R.id.my_circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), itemTitles);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(130);//100 origin
        circularListView.setOnItemClickListener(new com.jh.circularlist.CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                Toast.makeText(mContext,
                        "view at index " + i + " is clicked!",
                        Toast.LENGTH_SHORT).show();

                if (i == 1) {


                }
            }
        });


        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        final FloatingActionButton actionA = (FloatingActionButton) view.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionA.setTitle("Add User");

                View view_b = getLayoutInflater().inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view_b.findViewById(R.id.bt_item);
                itemView.setText(String.valueOf(adapter.getCount() + 1) + "new_user");
                adapter.addItem(view_b);
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton) view.findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setTitle("Remove User");
                adapter.removeItemAt(0);


            }
        });

        return view;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*///     Caused by: java.lang.RuntimeException: com.khayah.app.ui.home.MainActivity@b1ac501 must implement OnFragmentInteractionListener

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    // you should extends CircularAdapter to add your custom item
    private class CircularItemAdapter extends CircularAdapter {

        private ArrayList<String> mItems;
        private LayoutInflater mInflater;
        private ArrayList<View> mItemViews;

        public CircularItemAdapter(LayoutInflater inflater, ArrayList<String> items) {
            this.mItemViews = new ArrayList<>();
            this.mItems = items;
            this.mInflater = inflater;

            for (final String s : mItems) {
                View view = mInflater.inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                RoundedImageView imgView = (RoundedImageView) view.findViewById(R.id.item_icon);

                itemView.setText(s);
                //imgView.setImageDrawable(R.drawable.ic_vector_lawyer_icon);


                Picasso.Builder builder = new Picasso.Builder(mContext);
                builder.downloader(new OkHttpDownloader(mContext));
                builder.build().load("https://img1.ak.crunchyroll.com/i/spire2/3aa39968e298c5c67151f526752194391524767340_large.jpg")//dataList.get(position).getThumbnailUrl()
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
            if (mItemViews.size() > 0) {
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
