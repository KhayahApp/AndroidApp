package com.khayah.app.ui.menu_record;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.khayah.app.Constant;
import com.khayah.app.R;
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.ui.MatisseActivity;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;


public class RecordFragment extends Fragment implements EasyPermissions.PermissionCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String[] REQUIRED_READ_EXTERNAL_STORAGE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Context mContext;
    private View view;
    String[] titles = {
            "Audio",
            "Video",
            "Photo",

    };
    static final String[] MEDIA_ICON = new String[]{
            "Record", "Video", "Photo"};
    private static final int REQUEST_CODE_CHOOSE = 23;
    private PhotoUriAdapter mAdapter;
    private RecyclerView media_recyclerView;
    private FrameLayout audio_fy;

    public RecordFragment() {
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
    public static RecordFragment newInstance(String param1, String param2) {
        RecordFragment fragment = new RecordFragment();
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
        view = inflater.inflate(R.layout.fragment_record_horizontal, container, false);
        media_recyclerView = (RecyclerView) view.findViewById(R.id.img_display_recyclerview);
        audio_fy = (FrameLayout)view.findViewById(R.id.fy_aduio_record );

        MultiSnapRecyclerView firstRecyclerView = (MultiSnapRecyclerView) view.findViewById(R.id.first_recycler_view);
        SnapRecyclerRecordAdapter.RecyclerViewClickListener listener = (view, position) -> {
            Toast.makeText(getContext(), "Position " + position, Toast.LENGTH_SHORT).show();
            if(position == 0){
                //Audio Recording
                media_recyclerView.setVisibility(View.GONE);
                audio_fy.setVisibility(View.VISIBLE);

            }else if( position == 1){
                //Video Uploading
                media_recyclerView.setVisibility(View.VISIBLE);
                audio_fy.setVisibility(View.GONE);

                //handleCaptureImage();

            }else if( position == 2){
                //Photo Uploading
                media_recyclerView.setVisibility(View.VISIBLE);
                audio_fy.setVisibility(View.GONE);
                handlePickImage();

            }

        };
        SnapRecyclerRecordAdapter firstAdapter = new SnapRecyclerRecordAdapter(titles,listener);
        LinearLayoutManager firstManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        firstRecyclerView.setLayoutManager(firstManager);
        firstRecyclerView.setAdapter(firstAdapter);



        media_recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        media_recyclerView.setAdapter(mAdapter = new PhotoUriAdapter());


        return view;

    }

    private void pickImage() {
        Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG))
                .countable(true)
                .maxSelectable(1)
                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.gallery_image_thumbnail_size))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .forResult(Constant.RC_NEW_THREAD_PICK_IMAGE);
    }

    private void captureImage(){
        /*Intent intent = new Intent(getContext(), CameraKitAc.class);
        startActivityForResult(intent, Constant.RC_NEW_THREAD_CAPTURE_IMAGE);*/
    }



    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.RC_NEW_THREAD_CAPTURE_IMAGE) {
                //onPickImageResult();
            } else if (requestCode == Constant.RC_NEW_THREAD_PICK_IMAGE) {
                onPickImageResult(data);
            }
        }
    }
    @AfterPermissionGranted(Constant.RC_NEW_THREAD_PICK_IMAGE)
    private void handlePickImage() {
        if (hasReadExternalStoragePermission()) {
            pickImage();
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.app_name),
                    Constant.RC_NEW_THREAD_PICK_IMAGE,
                    REQUIRED_READ_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    private void onPickImageResult(Intent data){
        List<Uri> uriList = Matisse.obtainResult(data);

        mAdapter.setData(uriList,uriList.get(0).getPathSegments());
        Log.e("OnActivityResult ", String.valueOf(Matisse.obtainResult(data)));

    }
    private boolean hasReadExternalStoragePermission() {
        return EasyPermissions.hasPermissions(getContext(), REQUIRED_READ_EXTERNAL_STORAGE_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
     // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            pickImage();
        }
    }
}
