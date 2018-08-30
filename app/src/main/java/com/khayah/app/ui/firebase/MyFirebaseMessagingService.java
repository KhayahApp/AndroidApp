/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khayah.app.ui.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.khayah.app.R;
import com.khayah.app.ui.home.MainActivity;
import com.khayah.app.ui.home.SplashActivity;


import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;


import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String ADMIN_CHANNEL_ID = "fcm_default_channel";
    private NotificationManager notificationManager;
    private SharedPreferences mSharedPreferencesUserInfo;

    private String mStrOpen, mStrClose;

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Timber.e(TAG, "From: %s" + remoteMessage.getFrom());
        // Check if message contains a data payload.
        /*mSharedPreferencesUserInfo = getSharedPreferences(CommonConstants.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        boolean willShowNoti = mSharedPreferencesUserInfo.getBoolean(CommonConstants.WILL_SHOW_NOTIFICATION, true);
*/
        boolean willShowNoti = true;
        if (willShowNoti) {


            if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {
                Timber.e(TAG, "Message data payload:scheduleJob %s" + remoteMessage.getData());
                //06-18 14:43:36.794 32692-1573/greenway_myanmar.org E/MyFirebaseMsgService: Message data payload: {post_image_url=https://docs.centroida.co/wp-content/uploads/2017/05/notification.png, person_image_url=https://docs.centroida.co/wp-content/uploads/2017/05/notification.png, ads_image_url=https://docs.centroida.co/wp-content/uploads/2017/05/notification.png, post_id=postID123456, type=news, title=I'd tell you a chemistry joke2, message=but I know I wouldn't get a reaction2}
                /*
                if ( Check if data needs to be processed by long running job  true) {
                */
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                /*scheduleJob();
                Bundle data = new Bundle();
                for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                    data.putString(entry.getKey(), entry.getValue());
                }*/
                sendNotification(remoteMessage);

            } else {
                // Handle message within 10 seconds
                //handleNow();
                // Check if message contains a notification payload.
                if (remoteMessage.getNotification() != null) {
                    sendNotificationByDashboardShortTask(remoteMessage);
                }
            }
        } else {
            Timber.e("Close Notification %s", "" + willShowNoti);
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
    }
    // [END receive_message]

    /**
     * Schedule a job using FirebaseJobDispatcher.
     */
    private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param remoteMessage FCM message body received.
     */
    private void sendNotificationByDashboardShortTask(RemoteMessage remoteMessage) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        //You should use an actual ID instead
        int notificationId = new Random().nextInt(60000);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Big Text Style
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(remoteMessage.getNotification().getBody());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_notification_icon)
                        .setAutoCancel(true)
                        .setStyle(bigTextStyle)
                        //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setSound(defaultSoundUri)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(notificationId /* ID of notification */, notification);

    }


    private void sendNotification(RemoteMessage remoteMessage) {


        //You should use an actual ID instead
        Random random = new Random();
        int notificationId = random.nextInt(9999 - 1000) + 1000;
        //int notificationId = new Random().nextInt(60000);
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        //RemoteViews customview = new RemoteViews(getPackageName(), R.layout.custom_notifiction_collasped_view);
        //RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.greenway_notification_large);


        //Here messageBody means Message text from Firebase Dashboard
        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0) {



                /*if (type.equalsIgnoreCase(CommonConstants.FCM_MESSAGE_TYPE_SURVEY)) {


                    // adding action to left button
                    Intent leftIntent = new Intent(this, NotificationIntentService.class);
                    leftIntent.setAction("survey_left");

                    leftIntent.putExtra(NotificationIntentService.KEY_MSG_TO_SERVICE_SURVEY_ID, Integer.parseInt(remoteMessage.getData().get(CommonConstants.FCM_SURVEY_ID)));
                    leftIntent.putExtra(NotificationIntentService.KEY_MSG_NOTIFICATION_ID, notificationId);
                    sendBroadcast(leftIntent);

                    notificationLayoutExpanded.setOnClickPendingIntent(R.id.noti_open_button, PendingIntent.getService(this, 0, leftIntent, PendingIntent.FLAG_UPDATE_CURRENT));
                    // adding action to right button
                    Intent rightIntent = new Intent(this, NotificationIntentService.class);
                    rightIntent.setAction("survey_right");
                    rightIntent.putExtra(NotificationIntentService.KEY_MSG_NOTIFICATION_ID, notificationId);
                    sendBroadcast(rightIntent);

                    notificationLayoutExpanded.setOnClickPendingIntent(R.id.noti_close_button, PendingIntent.getService(this, 1, rightIntent, PendingIntent.FLAG_UPDATE_CURRENT));

                    //TODO to open Survey by survey Id  from direct notibar
                    Intent survey_intent = new Intent(this, SurveyMainActivity.class);
                    survey_intent.putExtra("extra.SURVEY_ID", Integer.parseInt(remoteMessage.getData().get(CommonConstants.FCM_SURVEY_ID)));
                    survey_intent.putExtra(SurveyMainActivity.EXTRA_NOTIFICATION_ID, notificationId);

                    PendingIntent survey_pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, survey_intent, PendingIntent.FLAG_ONE_SHOT);

                    //Button
                    NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.drawable.ic_notification_reply, mStrOpen, survey_pendingIntent).build();
                    NotificationCompat.Builder notificationBuilder =
                            new NotificationCompat.Builder(this, channelId)
                                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                                    .setAutoCancel(true)
                                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                    .setCustomContentView(customview)
                                    .setContent(customview)
                                    .addAction(action)
                                    .setCustomBigContentView(notificationLayoutExpanded)
                                    .setSound(defaultSoundUri)
                                    .setContentIntent(survey_pendingIntent);
                    Notification notification = notificationBuilder.build();
                    notification.flags |= Notification.FLAG_AUTO_CANCEL;
                    notification.defaults |= Notification.DEFAULT_SOUND;
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                    notificationManager.notify(notificationId *//* ID of notification *//*, notification);


                    return;

                } else if (type.equalsIgnoreCase(CommonConstants.FCM_MESSAGE_TYPE_PUSH)) {//Direct Push Message Type push_message

                    Intent homeIntent = new Intent(this, NotificationReceivedActivity.class);
                    //No error when there is no data or null in remoteMessage.getData().get(CommonConstants.FCM_TITLE)
                    FcmMessage fcmMessage = new FcmMessage(remoteMessage.getData().get(CommonConstants.FCM_TITLE)
                            , remoteMessage.getData().get(CommonConstants.FCM_MESSAGE_TEXT)
                            , remoteMessage.getData().get(CommonConstants.FCM_PERSON_IMAGE_URL)
                            , remoteMessage.getData().get(CommonConstants.FCM_POST_IMAGE_URL)
                            , remoteMessage.getData().get(CommonConstants.FCM_ADS_IMAGE_URL)
                            , remoteMessage.getData().get(CommonConstants.FCM_POST_ID)
                            , remoteMessage.getData().get(CommonConstants.FCM_CAT_PARENT_ID)
                            , notificationId);
                    //fcmMessage = new Gson().fromJson(remoteMessage.getData().get(FcmMessage.class),FcmMessage.class);
                    homeIntent.putExtra(CommonConstants.FCM_JSON_MESSAGE, new Gson().toJson(fcmMessage));
                    startActivity(homeIntent);
                    return;
                }*/
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels();
        }


    }

   /* private void sendNotificationByPostType(PendingIntent pintent) {
        Random random = new Random();
        int notificationId = random.nextInt(9999 - 1000) + 1000;
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        RemoteViews customview = new RemoteViews(getPackageName(), R.layout.custom_notifiction_collasped_view);
        RemoteViews notificationLayoutExpanded = new RemoteViews(getPackageName(), R.layout.greenway_notification_large);


    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels() {
        CharSequence adminChannelName = getString(R.string.notifications_admin_channel_name);
        String adminChannelDescription = getString(R.string.notifications_admin_channel_description);

        NotificationChannel adminChannel;
        adminChannel = new NotificationChannel(ADMIN_CHANNEL_ID, adminChannelName, NotificationManager.IMPORTANCE_LOW);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

    /*
     *
     *  Code for converting message to image
     *
     */
    /*public static Bitmap textAsBitmap(Context context, String messageText, float textSize, int textColor) {
        String fontName = context.getString(R.string.noti_font_mymm);
        Typeface font = Typeface.createFromAsset(context.getAssets(), String.format("fonts/%s.ttf", fontName));
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(messageText) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 0.5f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(messageText, 0, baseline, paint);
        return image;
    }*/


    /*
     *
     *  Code for converting url to image
     *
     */
    /*public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }*/


}
