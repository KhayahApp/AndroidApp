package com.khayah.app;

import com.khayah.app.models.ResponseToken;

public class Constant {
    public static ResponseToken token = null;
    public static String AccessToken = null;
    public static String settings = "settings";
    public static String version = "version";
    public static String aboutusKey = "aboutus";
    public static String settingsKey = "settings";
    public static String nearbyHeritage = "nearbyHeritage";
    public static String heritageType = "heritageType";
    public static String POST_DETAIL_ID = "post_id";


    //Firebase Message Type
    public static final String FCM_MESSAGE_USER_ID = "user_id";
    public static final String FCM_MESSAGE_TYPE ="type";
    public static final String MSG_DANGER = "danger";
    public static String FCM_COMMOM_TOPIC_FOR_USER = "Khayah_";
    public static String FCM_COMMOM_TOPIC_FOR_ALL = "Khayah_all";
    public static String FCM_COMMOM_TOPIC_FOR_WOMEN = "Khayah_women";
    public static String FCM_COMMOM_TOPIC_FOR_MEN = "Khayah_men";
    public static String FCM_COMMOM_TOPIC_FOR_LGBT = "Khayah_lgbt";
    public static String FCM_COMMOM_TOPIC_FOR_ = "Khayah";

    public static final String FCM_POST_IMAGE_URL = "image";
    public static final String FCM_POST_PERSON_IMG_URL = "avatar";
    public static final String FCM_TITLE = "title";
    public static final String FCM_MESSAGE_TEXT = "message";



    // Activity Request Codes
    public static final int RC_NEW_THREAD = 1;
    public static final int RC_NEW_THREAD_CAPTURE_IMAGE = 2;
    public static final int RC_NEW_THREAD_PICK_IMAGE = 3;
    public static final int RC_THREAD_COMMENT_CAPTURE_IMAGE = 4;
    public static final int RC_THREAD_COMMENT_PICK_IMAGE = 5;

    public static String PHONE = "phone";

}
