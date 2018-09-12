package com.khayah.app.ui.lawer;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khayah.app.R;
import com.khayah.app.models.PersonUtils;

import java.util.ArrayList;
import java.util.List;

public class LawerListFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private View view;

    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<PersonUtils> personUtilsList;


    public LawerListFragment() {
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
    public static LawerListFragment newInstance(String param1, String param2) {
        LawerListFragment fragment = new LawerListFragment();
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
        view = inflater.inflate(R.layout.fragment_lawer_list, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.rvContacts);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(mContext);

        recyclerView.setLayoutManager(layoutManager);

        personUtilsList = new ArrayList<>();

        //Adding Data into ArrayList
        personUtilsList.add(new PersonUtils("Todd Miller","Project Manager"));
        personUtilsList.add(new PersonUtils("Bradley Matthews","Senior Developer"));
        personUtilsList.add(new PersonUtils("Harley Gibson","Lead Developer"));
        personUtilsList.add(new PersonUtils("Gary Thompson","Lead Developer"));
        personUtilsList.add(new PersonUtils("Corey Williamson","UI/UX Developer"));
        personUtilsList.add(new PersonUtils("Samuel Jones","Front-End Developer"));
        personUtilsList.add(new PersonUtils("Michael Read","Backend Developer"));
        personUtilsList.add(new PersonUtils("Robert Phillips","Android Developer"));
        personUtilsList.add(new PersonUtils("Albert Stewart","Web Developer"));
        personUtilsList.add(new PersonUtils("Wayne Diaz","Junior Developer"));

        mAdapter = new CustomRecyclerAdapter(mContext, personUtilsList);

        recyclerView.setAdapter(mAdapter);

        return view;

    }
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }



}
