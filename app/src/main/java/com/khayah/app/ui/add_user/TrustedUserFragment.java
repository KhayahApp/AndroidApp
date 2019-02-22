package com.khayah.app.ui.add_user;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.jh.circularlist.CircularAdapter;
import com.khayah.app.APIToolz;
import com.khayah.app.Constant;
import com.khayah.app.KhayahApp;
import com.khayah.app.R;
import com.khayah.app.clients.FCMNetworkEngine;
import com.khayah.app.clients.NetworkEngine;
import com.khayah.app.models.Data;
import com.khayah.app.models.FCMRequest;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGroup;
import com.khayah.app.ui.add_user.sms.SmsActivity;
import com.khayah.app.ui.login.ProfileActivity;
import com.khayah.app.ui.login.RegisterActivity;
import com.khayah.app.util.CircleTransform;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private static final int ADD_USER_RESULT = 100;

    // TODO: Rename and change types of parameters
    private Boolean mParam1;
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
    private ArrayList<UserGroup> userList;

    private static final int CALLPHONE = 0;
    private static final int SENDSMS = 1;
    private static final int CUSTOMSENDSMS = 2;

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
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private RoundedImageView imgUser;
    private FrameLayout layoutUser;

    private FirebaseAnalytics mFirebaseAnalytics;
    private User user;
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
    public static TrustedUserFragment newInstance(Boolean param1, String param2) {
        TrustedUserFragment fragment = new TrustedUserFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity().getApplicationContext();
        view = inflater.inflate(R.layout.fragment_trusted_user, container, false);

        //Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);


        //SpinkitViewMultiPulse
        spnkitView = (SpinKitView) view.findViewById(R.id.spin_kit);
        btnAlarm = (Button) view.findViewById(R.id.alarm_btn);
        fyBell = (FrameLayout) view.findViewById(R.id.fy_bell);
        layoutUser = (FrameLayout) view.findViewById(R.id.layout_user);
        imgUser = (RoundedImageView) view.findViewById(R.id.img_user);
        spnkitView.setVisibility(View.INVISIBLE);
        fyBell.setVisibility(View.INVISIBLE);

        // Check already login
        if (KhayahApp.isLogin()) {
            user = (User) KhayahApp.getUser();
            Picasso.with(getActivity()).load(user.getAvatar() != null ? APIToolz.getInstance().getHostAddress()
                    + "/uploads/users/" + user.getAvatar()
                    : "https://graph.facebook.com/" + user.getFacebookId() + "/picture?type=large")
                    .transform(new CircleTransform()).into(imgUser);
            imgUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));

                }
            });

        } else {
            Picasso.with(getActivity()).load(R.drawable.girl).transform(new CircleTransform()).into(imgUser);
        }

        firstPermissionSound();
        userList = new ArrayList<>();
        getUserGroup();

        // usage sample
        final com.jh.circularlist.CircularListView circularListView = (com.jh.circularlist.CircularListView) view.findViewById(R.id.my_circular_list);
        adapter = new CircularItemAdapter(getLayoutInflater(), userList, colors);
        circularListView.setAdapter(adapter);
        circularListView.setRadius(130);//100 origin
        circularListView.setOnItemClickListener(new com.jh.circularlist.CircularTouchListener.CircularItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                if (i >= 0) {
                    userActionChoice(userList.get(i).getPhone());
                    userPhone = userList.get(i).getPhone();
                } else {

                }
            }
        });


        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        final FloatingActionButton actionA = (FloatingActionButton) view.findViewById(R.id.action_a);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), v, "profile");
                startActivityForResult(intent, ADD_USER_RESULT, options.toBundle());
            }
        });
        final FloatingActionButton actionB = (FloatingActionButton) view.findViewById(R.id.action_b);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userList.size() == 0) return;
                NetworkEngine.getInstance().deleteGroupUser(userList.get(0).getId()).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            userList.remove(0);
                            adapter.removeItemAt(0);
                            adapter.notifyItemChange();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });


            }
        });
        return view;

    }

    private void getUserGroup() {
        user = (User) KhayahApp.getUser();
        NetworkEngine.getInstance().getUserGroups("user_id:equal:" + user.getId(), 1, 9).enqueue(new Callback<List<UserGroup>>() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<UserGroup>> call, Response<List<UserGroup>> response) {
                if (response.isSuccessful()) {
                    if(response.body().size() == 0) tagTargetExplain(view);
                    for (UserGroup group : response.body()) {
                        userList.add(group);
                        View view_b = getLayoutInflater().inflate(R.layout.circle_ls_view_circular_item, null);
                        TextView itemView = (TextView) view_b.findViewById(R.id.bt_item);
                        itemView.setText(group.getName());
                        adapter.addItem(view_b);
                    }
                    if(mParam1 == true) {
                        playAlert();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<UserGroup>> call, Throwable t) {

            }
        });
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
    }

    
    private void playAlert() {

        bellFlag = true;
        spnkitView.setVisibility(View.VISIBLE);
        fyBell.setVisibility(View.VISIBLE);
        sendFCMNotification();
        sendNotificationtoUsers();
        playBeep();
    }
    
    private void unPlayAlert() {
        //StopNotificationSend
        spnkitView.setVisibility(View.INVISIBLE);
        fyBell.setVisibility(View.INVISIBLE);
        bellFlag = false;
        if (m.isPlaying()) {
            m.stop();
            m.release();
            m = new MediaPlayer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_bell:
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "action_bell");
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "bell");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "noti_button");
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

                if (!bellFlag) {
                    item.setIcon(getResources().getDrawable(R.drawable.ic_close_bell));
                    playAlert();
                } else {
                    item.setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_bell_icon));
                    unPlayAlert();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendFCMNotification() {
        if(userList != null && userList.size() > 0) {
            for (UserGroup group: userList) {
                FCMRequest request = new FCMRequest();
                Data data = new Data();
                data.setTitle("Please help me, "+ group.getName());
                data.setMessage("I am in danger: " + user.getFirstName() + " "+user.getLastName());
                request.setTo("/topics/"+ Constant.FCM_COMMOM_TOPIC_FOR_USER+group.getPhone());
                request.setData(data);
                FCMNetworkEngine.getInstance().postFCMNotification(request).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if(response.isSuccessful()) {
                            Toast.makeText(mContext,
                                    "Sending...." + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        }
    }

    private void sendNotificationtoUsers() {

        User user = (User) KhayahApp.getUser();
        final FcmMessage fcmMessage = new FcmMessage();
        fcmMessage.setTopicId("1");
        fcmMessage.setTitle("Khayah");
        fcmMessage.setMessage("I am in Danger."+ user.getUsername());
        fcmMessage.setAvatar(user.getAvatar());
        fcmMessage.setImage("");
        fcmMessage.setType("Khayah_all");
        fcmMessage.setFcmServerId("2");

        NetworkEngine.getInstance().sendNotification(fcmMessage).enqueue(new Callback<FcmMessage>() {
            @Override
            public void onResponse(Call<FcmMessage> call, Response<FcmMessage> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(mContext,
                            "Sending...." + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FcmMessage> call, Throwable t) {

                Toast.makeText(mContext,
                        "Fail noti...." + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    // you should extends CircularAdapter to add your custom item
    private class CircularItemAdapter extends CircularAdapter {

        private ArrayList<UserGroup> mItems;
        private LayoutInflater mInflater;
        private ArrayList<View> mItemViews;

        public CircularItemAdapter(LayoutInflater inflater, ArrayList<UserGroup> items, int[] colors) {
            this.mItemViews = new ArrayList<>();
            this.mItems = items;
            this.mInflater = inflater;

            for (int i = 0; i < userList.size(); i++) {
                View view = mInflater.inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view.findViewById(R.id.bt_item);
                RoundedImageView imgView = (RoundedImageView) view.findViewById(R.id.item_icon);

                itemView.setText(userList.get(i).getName());
                itemView.setBackgroundColor(colors[i]);
                //imgView.setImageDrawable(R.drawable.ic_vector_lawyer_icon);


                Picasso.Builder builder = new Picasso.Builder(mContext);
                builder.downloader(new OkHttpDownloader(mContext));
                builder.build().load("https://to_change_user_avatar")//dataList.get(position).getThumbnailUrl()
                        .placeholder((R.drawable.girl))
                        .error(R.drawable.ic_user_icon)
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

    //Choose Action
    private void userActionChoice(String phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        CharSequence callPhone;
        CharSequence sendSms;
        CharSequence customSms;
        callPhone = getResources().getString(R.string.action_call);
        sendSms = getResources().getString(R.string.action_sms);
        customSms = getResources().getString(R.string.action_sms_custon);

        builder.setCancelable(true).
                setItems(new CharSequence[]{callPhone, sendSms, customSms},
                        new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i == CALLPHONE) {
                                    userPhone = phone;
                                    callPhone(phone);

                                } else if (i == SENDSMS) {
                                    /*Toast.makeText(mContext,
                                            "Sms Ph" + phone,
                                            Toast.LENGTH_SHORT).show();*/
                                    if (checkForSmsPermission()) {
                                        Toast.makeText(mContext,
                                                "Sending...." + phone,
                                                Toast.LENGTH_SHORT).show();
                                        smsSendMessage(phone, "Help! I am in Danger!");
                                    }

                                } else if (i == CUSTOMSENDSMS) {
                                    Toast.makeText(mContext,
                                            "Ph " + phone,
                                            Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(mContext, SmsActivity.class));
                                    Intent intent = new Intent(mContext, SmsActivity.class);
                                    intent.putExtra(Constant.PHONE, phone);
                                    startActivity(intent);

                                }
                            }
                        });
        builder.show();
    }

    @AfterPermissionGranted(RC_ALL_PERMISSIONS)
    private void firstPermissionSound() {
        if (!hasAllPermissions()) {
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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_USER_RESULT) {
            if (resultCode == getActivity().RESULT_OK) {
                UserGroup addUser = new Gson().fromJson(data.getStringExtra("add_user"), UserGroup.class);
                userList.add(addUser);
                View view_b = getLayoutInflater().inflate(R.layout.circle_ls_view_circular_item, null);
                TextView itemView = (TextView) view_b.findViewById(R.id.bt_item);
                itemView.setText(addUser.getName());
                adapter.addItem(view_b);
            }
        } else {
            if ((resultCode == getActivity().RESULT_OK)) {
                // When we are done cropping, display it in the ImageView.
                // For API >= 23 we need to check specifically that we have permissions to read external storage,
                // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
                callPhone(userPhone);
            }
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

    /*SMS permission and code*/

    /**
     * Checks whether the app has SMS permission.
     */
    private boolean checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(mContext,
                Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
            return false;
        } else {
            // Permission already granted. Enable the SMS button.
            //enableSmsButton();
            return true;
        }
    }

    public void smsSendMessage(String phone, String sms) {
        // Set the destination phone number to the string in editText.
        String destinationAddress = phone;
        // Find the sms_message view.
        // Get the text of the sms message.
        String smsMessage = sms;
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Check for permission first.
        checkForSmsPermission();
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(destinationAddress, scAddress, smsMessage,
                sentIntent, deliveryIntent);
    }

    /*
    * Tag Target Explaination*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void tagTargetExplain( View view){

        // You don't always need a sequence, and for that there's a single time tap target
        final SpannableString spannedDesc = new SpannableString("You can add your trusted contacts for your safety connection!");
        spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(getActivity(), TapTarget.forView(view.findViewById(R.id.multiple_actions), "Hello, Khayah is with you!", spannedDesc)
                .cancelable(true)
                .drawShadow(true)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                //sequence.start();
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                //Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }

}
