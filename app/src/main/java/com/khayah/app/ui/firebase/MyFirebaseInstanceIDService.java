/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khayah.app.ui.firebase;

import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasServiceInjector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService  {

    private static final String TAG = "MyFirebaseIIDService";




    @Override
    public void onCreate() {
        //AndroidInjection.inject(this);
        super.onCreate();
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d(TAG, "Refreshed token: " + refreshedToken);
        //Log.e(TAG, "Refreshed token: " + refreshedToken);
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        /*mSharedPreferencesUserInfo = getSharedPreferences(CommonConstants.SHARE_PREFERENCE_USER_INFO, Context.MODE_PRIVATE);
        mEditorUserInfo = mSharedPreferencesUserInfo.edit();
        mEditorUserInfo.putString(CommonConstants.USER_FCM_TOKE, refreshedToken);
        mEditorUserInfo.commit();*/

        //mUserId = mSharedPreferencesUserInfo.getString(CommonConstants.USER_ID, null);
        //mRefreshToken = refreshedToken;

        Log.e(TAG,"Token>>"+ refreshedToken);

        //TODO this method should call after login on MainActivty or Homeactivity
        //sendRegistrationToServer(mUserId,mRefreshToken);
        /**
         * Persist token to third-party servers.
         * For Facebook PUSH Campaine Send Registration ID step 3 out of  3
         *
         */
        //AppEventsLogger.set

        /*mSharedPreferencesUserInfo =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
          preferences.edit().putString(CommonConstants.USER_FCM_TOKE, refreshedToken).apply();*/

    }
    // [END refresh_token]


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String userId, String token) {
        // TODO: Implement this method to send token to your app server.

        /*webservice.postUserFcmToken(userId, token).enqueue(new Callback<ApiResponse<GeneralResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<GeneralResponse>> call, Response<ApiResponse<GeneralResponse>> response) {
                //Log.e("Reply==","response>>"+ response.message().toString());
            }
            @Override
            public void onFailure(Call<ApiResponse<GeneralResponse>> call, Throwable t) {

            }
        });*/
    }

    //    implementation 'com.google.firebase:firebase-analytics:16.0.3'


}
