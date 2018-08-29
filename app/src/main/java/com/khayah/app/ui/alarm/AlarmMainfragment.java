package com.khayah.app.ui.alarm;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;


import com.khayah.app.R;
import com.khayah.app.ui.adapter.ImageAdapter;
import com.khayah.app.ui.widget.WrappedGridView;
import com.khayah.app.vo.Categories;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;



public class AlarmMainfragment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private View view;
    private Context mContext;
    private WrappedGridView mgridView;
    private ArrayList<Categories> CategoriesModelList;
    static final String[] MOBILE_OS = new String[]{
            "Emergency Alarm", "Take Photo", "Voice Recording", "Video Recording"};

    private static final int RC_ALL_PERMISSIONS = 2;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE


    };

    private static MediaPlayer m;
    private int previousSelectedPosition = -1;

    private boolean isFirstAlarmOpen = true;

    private boolean isFirstCameraOpen = true;
    private boolean isFirstAudioOpen = true;
    private boolean isFirstVideoOpen = true;



    public static AlarmMainfragment newInstance() {
        AlarmMainfragment alarmMainfragment = new AlarmMainfragment();
        return alarmMainfragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {

        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_alarm_main, container, false);

        mgridView = view.findViewById(R.id.grid_view_cate);
        mgridView.setAdapter(new ImageAdapter(mContext, MOBILE_OS));
        mgridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(mContext, ((TextView) v.findViewById(R.id.txt_cate_name)).getText() + "/" + position, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, "Click again on item to stop function!", Toast.LENGTH_SHORT).show();
                /*if (position == 0) {
                    firstpermissionSound();
                }
                */
                // Get the selected item text
                //String selectedItem = parent.getItemAtPosition(position).toString();
                // Display the selected item text to app interface
                //tv_message.setText("Selected item : " + selectedItem);

                // Get the current selected view as a TextView
                TextView tv = (TextView) v.findViewById(R.id.txt_cate_name);
                tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                // Set the current selected item background color
                // Set the current selected item text color
                tv.setTextColor(Color.WHITE);


                switch (position) {
                    case 0:
                        if(isFirstAlarmOpen ) {
                            firstpermissionSound();
                            tv.setText("Stop Alarm");
                        }else {
                            tv.setText("Emergency Alarm");
                            tv.setBackgroundColor(getResources().getColor(R.color.appWhite));
                            tv.setTextColor(Color.BLACK);
                            if (m.isPlaying()) {
                                m.stop();
                                m.release();
                                m = new MediaPlayer();
                            }
                            isFirstAlarmOpen = true;

                        }

                        break;
                    case 1:
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        if(isFirstCameraOpen) {
                            tv.setText("Stop Camera");
                            isFirstCameraOpen = false;
                        }else {
                            tv.setText("Take Photo");
                            tv.setBackgroundColor(getResources().getColor(R.color.appWhite));
                            tv.setTextColor(Color.BLACK);
                            isFirstCameraOpen = true;

                        }

                        break;
                    case 2:
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        if(isFirstAudioOpen) {
                            tv.setText("Stop Recording");
                            isFirstAudioOpen = false;
                        }else {
                            tv.setText("Voice Recording");
                            tv.setBackgroundColor(getResources().getColor(R.color.appWhite));
                            tv.setTextColor(Color.BLACK);
                            isFirstAudioOpen = true;

                        }
                        break;
                    case 3:
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        if(isFirstVideoOpen) {
                            tv.setText("Stop Video");
                            isFirstVideoOpen = false;
                        }else {
                            tv.setText("Video Recording");
                            tv.setBackgroundColor(getResources().getColor(R.color.appWhite));
                            tv.setTextColor(Color.BLACK);
                            isFirstVideoOpen = true;

                        }
                        break;

                }

            }
        });

        return view;
    }

    public void playSound(Context context) throws IllegalArgumentException,
            SecurityException,
            IllegalStateException,
            IOException {

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        MediaPlayer mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setDataSource(context, soundUri);
        final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            // Uncomment the following line if you aim to play it repeatedly
            // mMediaPlayer.setLooping(true);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }
    }


    private void playBeep() {

        isFirstAlarmOpen = false;
        m = new MediaPlayer();
        try {
            if (m.isPlaying()) {
                m.stop();
                m.release();
                m = new MediaPlayer();
            }

            AssetFileDescriptor descriptor = mContext.getAssets().openFd("fire_alarm.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
            descriptor.close();

            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(true);
            m.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @AfterPermissionGranted(RC_ALL_PERMISSIONS)
    private void firstpermissionSound() {
        if (hasAllPermissions()) { // For one permission EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_SMS)
            // Have permission, do the thing!
            //Toast.makeText(getActivity(), "TODO: MAP and GPS things", Toast.LENGTH_LONG).show();
            //TODO your job

            playBeep();
        } else {
            // Request one permission
            //EasyPermissions.requestPermissions(this, getString(R.string.rationale_sms), RC_SMS_PERM, Manifest.permission.READ_SMS);
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_all_permissions),
                    RC_ALL_PERMISSIONS,
                    REQUIRED_PERMISSIONS);

        }
    }

    private boolean hasAllPermissions() {
        return EasyPermissions.hasPermissions(mContext, REQUIRED_PERMISSIONS);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
// (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(getActivity(), perms)) {
            new AppSettingsDialog.Builder(getActivity()).build().show();
        } else {
            //onLogin(mLoginType);
            //TODO your job
            /*try {
                //playSound(mContext);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
            playBeep();


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

}
