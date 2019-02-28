package com.khayah.app.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.khayah.app.Constant;
import com.khayah.app.R;

public class AboutUsFragment extends Fragment{

    // TODO: Rename and change types of parameters
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context mContext;
    private View view;

    private WebView mWebView;

    private TextView textViewTitle;

    // TODO: Rename and change types and number of parameters
    public static AboutUsFragment newInstance(String param1, String param2) {
        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public AboutUsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_about_us, container, false);

        // Setup toolbar
        /*Toolbar toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        //textViewTitle = view.findViewById(R.id.toolbar_title);

        mWebView = view.findViewById(R.id.wv_about_us);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(Constant.ABOUT_US);
        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }
        });

        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }


}
