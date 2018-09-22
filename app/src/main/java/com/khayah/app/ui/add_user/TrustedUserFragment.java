package com.khayah.app.ui.add_user;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.ybq.android.spinkit.SpinKitView;
import com.jh.circularlist.CircularAdapter;
import com.khayah.app.R;
import com.khayah.app.models.User;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrustedUserFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrustedUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrustedUserFragment extends Fragment implements Colors, EasyPermissions.PermissionCallbacks {
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
    private Menu menu;
    private SpinKitView spnkitView;
    private FrameLayout fyBell;
    private Button btnAlarm;
    private boolean bellFlag;
    private ArrayList<User> userList;

    private static final int CALLPHONE = 0;
    private static final int SENDSMS = 1;

    private static final int RC_ALL_PERMISSIONS = 2;
    private static final String[] REQUIRED_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE

    };
    private static MediaPlayer m;
    private int previousSelectedPosition = -1;
    private boolean isFirstAlarmOpen = true;
    private String userPhone;
    private final String call_phone_READ_PERMISSION = "android.permission.CALL_PHONE";
    boolean call_phone_PermissionAccepted = false;

    //Try request code between 1 to 255
    private static final int INITIAL_REQUEST = 1;
    private static final int CALL_REQUEST = INITIAL_REQUEST + 4;



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
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_trusted_user, container, false);

        //SpinkitViewMultiPulse
        spnkitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        btnAlarm = (Button)view.findViewById(R.id.alarm_btn);
        fyBell = (FrameLayout)view.findViewById(R.id.fy_bell);
        spnkitView.setVisibility(View.INVISIBLE);
        fyBell.setVisibility(View.INVISIBLE);


        // simple text item with numbers 0 ~ 9
        ArrayList<String> itemTitles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            itemTitles.add(String.valueOf(i) + "khin_sandar");

        }
        userList = new ArrayList<>();
        User user1 = new User(1, "Father", "09250111607", "http://www.hexatar.com/gallery/thumb/160419_044601_mc28acd3a32_avatar.png");
        User user2 = new User(2, "Mom", "09250111607", "http://www.hexatar.com/gallery/thumb/20151025_f562cfa4f8aa26.png");
        User user3 = new User(3, "John", "09250111607", "http://www.hexatar.com/gallery/thumb/151112_m2b4d0741e3.png");
        User user4 = new User(4, "Khin", "09250111607", "https://www.solespana.nl/wp-content/uploads/2017/11/Avatar1-300x300.png");
        User user5 = new User(5, "Su", "09250111607", "https://comigoo.be/wp-content/uploads/2016/02/Ilse-min.png");//https://img1.ak.crunchyroll.com/i/spire2/3aa39968e298c5c67151f526752194391524767340_large.jpg

        userList.add(user1);
        userList.add(user2);
        userList.add(user3);
        userList.add(user4);
        userList.add(user5);


        // usage sample
        final com.jh.circularlist.CircularListView circularListView = (com.jh.circularlist.CircularListView) view.findViewById(R.id.my_circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), userList, colors);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(130);//100 origin
        circularListView.setOnItemClickListener(new com.jh.circularlist.CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                /*Toast.makeText(mContext,
                        "view at index " + i + " is clicked!",
                        Toast.LENGTH_SHORT).show();
                */

                if (i <= 5) {
                    userActionChoice(userList.get(i).getPhone());
                    userPhone = userList.get(i).getPhone();
                } else {

                }
            }
        });

        /*btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!bellFlag) {
                    bellFlag = true;
                    spnkitView.setVisibility(View.VISIBLE);
                    sendnotificationtoUsers();
                    if (isFirstAlarmOpen) {
                        firstpermissionSound("Alarm");
                    } else {
                        if (m.isPlaying()) {
                            m.stop();
                            m.release();
                            m = new MediaPlayer();
                        }
                        isFirstAlarmOpen = true;
                    }
                } else {
                    spnkitView.setVisibility(View.INVISIBLE);
                    bellFlag = false;
                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }
                    //StopNotificationSend
                }
            }
        });*/

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.menu_bell, menu);
        this.menu = menu;
        this.menu.findItem(R.id.action_settings).setVisible(false);
        if (!bellFlag) {
            this.menu.findItem(R.id.action_bell).setIcon(getResources().getDrawable(R.drawable.ic_bell_icon));
        } else {
            this.menu.findItem(R.id.action_bell).setIcon(getResources().getDrawable(R.drawable.ic_check_white_18dp));

        }


    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (Build.VERSION.SDK_INT > 11) {
            menu.findItem(R.id.action_settings).setVisible(false);

            MenuItem bellItem = menu.findItem(R.id.action_bell);

            /*if (!bellFlag) {
                bellItem.setIcon(getResources().getDrawable(R.drawable.ic_bell_icon));
            } else {
                bellItem.setIcon(getResources().getDrawable(R.drawable.ic_check_white_18dp));
            }*/

        }
        //return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case R.id.action_bell:

                if (!bellFlag) {
                    //menu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.crop__ic_cancel));
                    menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.crop__ic_cancel));

                    bellFlag = true;
                    spnkitView.setVisibility(View.VISIBLE);
                    fyBell.setVisibility(View.VISIBLE);
                    sendnotificationtoUsers();

                    if (isFirstAlarmOpen) {
                        firstpermissionSound();
                        //tv.setText("Stop Alarm");
                    } else {
                        //tv.setText("Emergency Alarm");
                        //tv.setBackgroundColor(getResources().getColor(R.color.appWhite));
                        //tv.setTextColor(Color.BLACK);
                        /*if (m.isPlaying()) {
                            m.stop();
                            m.release();
                            m = new MediaPlayer();
                        }*/
                        playBeep();
                        isFirstAlarmOpen = false;

                    }

                } else {
                    menu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_bell_icon));
                    spnkitView.setVisibility(View.INVISIBLE);
                    fyBell.setVisibility(View.INVISIBLE);
                    bellFlag = false;
                    if (m.isPlaying()) {
                        m.stop();
                        m.release();
                        m = new MediaPlayer();
                    }
                    //StopNotificationSend
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendnotificationtoUsers() {

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

        private ArrayList<User> mItems;
        private LayoutInflater mInflater;
        private ArrayList<View> mItemViews;

        public CircularItemAdapter(LayoutInflater inflater, ArrayList<User> items, int[] colors) {
            this.mItemViews = new ArrayList<>();
            this.mItems = items;
            this.mInflater = inflater;

            for (int i = 0; i < userList.size(); i++) {
                View view = mInflater.inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                RoundedImageView imgView = (RoundedImageView) view.findViewById(R.id.item_icon);

                itemView.setText(userList.get(i).getUsername());
                itemView.setBackgroundColor(colors[i]);
                //imgView.setImageDrawable(R.drawable.ic_vector_lawyer_icon);


                Picasso.Builder builder = new Picasso.Builder(mContext);
                builder.downloader(new OkHttpDownloader(mContext));
                builder.build().load(userList.get(i).getAvatar())//dataList.get(position).getThumbnailUrl()
                        .placeholder((R.drawable.girl))
                        .error(R.drawable.ic_user_icon)
                        .into(imgView);
                mItemViews.add(view);
            }
            /*for (final String s : mItems) {
                View view = mInflater.inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                RoundedImageView imgView = (RoundedImageView) view.findViewById(R.id.item_icon);

                itemView.setText(s);
                itemView.setBackgroundColor(colors[mItems.indexOf(s)]);
                //imgView.setImageDrawable(R.drawable.ic_vector_lawyer_icon);

                Picasso.Builder builder = new Picasso.Builder(mContext);
                builder.downloader(new OkHttpDownloader(mContext));
                builder.build().load("https://img1.ak.crunchyroll.com/i/spire2/3aa39968e298c5c67151f526752194391524767340_large.jpg")//dataList.get(position).getThumbnailUrl()
                        .placeholder((R.drawable.boy))
                        .error(R.drawable.ic_vector_lawyer_icon)
                        .into(imgView);
                mItemViews.add(view);
            }*/
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

    //Choose Action
    private void userActionChoice(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence callPhone;
        CharSequence sendSms;
        callPhone = getResources().getString(R.string.action_call);
        sendSms = getResources().getString(R.string.action_sms);

        builder.setCancelable(true).
                setItems(new CharSequence[]{callPhone, sendSms},
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CALLPHONE) {
                                    userPhone = phone;
                                    callPhone(phone);

                                    } else if (i == SENDSMS) {
                                    Toast.makeText(mContext,
                                            "Sms Ph" + phone,
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
        builder.show();
    }

    @AfterPermissionGranted(RC_ALL_PERMISSIONS)
    private void firstpermissionSound() {
        if (hasAllPermissions()) { // For one permission EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_SMS)
            // Have permission, do the thing!
            //Toast.makeText(getActivity(), "TODO: MAP and GPS things", Toast.LENGTH_LONG).show();
            //TODO your job
            playBeep();
           /* if (task.equalsIgnoreCase("Alarm")) {
                playBeep();
            }*/

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
        if (EasyPermissions.somePermissionPermanentlyDenied(getActivity(), perms)) {
            new AppSettingsDialog.Builder(getActivity()).build().show();
        } else {
            playBeep();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        /*switch (requestCode) {
            case CALL_REQUEST:
                call_phone_PermissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (call_phone_PermissionAccepted) {
                    //img_tlg_ph_no.performClick();
                    if (userPhone != null) {
                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            String ph = userPhone;
                            callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                            startActivity(callIntent);
                            return;
                        }

                    }
                }
                break;
        }*/

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == getActivity().RESULT_OK)) {
            // When we are done cropping, display it in the ImageView.
            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            callPhone(userPhone);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private void callPhone(String ph) {
        if (!hasPermission(call_phone_READ_PERMISSION)) {
            //if no permission, request permission
            String[] perms = {call_phone_READ_PERMISSION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(perms, CALL_REQUEST);
            } else {
                if (ph != null) {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));

                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(callIntent);
                        //return;
                    }
                    startActivity(callIntent);

                }
            }

        } else {
            // Check if we were successful in obtaining the map.
            //Log.e("<<<tlgLeaderPhno else >>>", "===>" + tlgLeaderPhno);
            if (ph != null) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Uri.encode(ph)));
                startActivity(callIntent);
                return;

            }
        }
    }

    private boolean hasPermission(String permission) {

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            return (getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
        } else {
            return true;
        }


    }


}
