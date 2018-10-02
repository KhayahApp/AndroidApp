package com.khayah.app.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khayah.app.R;
import com.khayah.app.ui.menu_record.RecordFragment;

public class ComingSoonFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private View view;

    // TODO: Rename and change types and number of parameters
    public static ComingSoonFragment newInstance(String param1, String param2) {
        ComingSoonFragment fragment = new ComingSoonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ComingSoonFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.coming_soon_fragment, container, false);

        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }
}
