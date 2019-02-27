package com.khayah.app.clients;


import com.khayah.app.models.CurrentLocation;
import com.khayah.app.models.FCMRequest;
import com.khayah.app.models.FcmMessage;
import com.khayah.app.models.Feedback;
import com.khayah.app.models.Lawer;
import com.khayah.app.models.RequestToken;
import com.khayah.app.models.ResponseToken;
import com.khayah.app.models.Station;
import com.khayah.app.models.User;
import com.khayah.app.models.UserGeo;
import com.khayah.app.models.UserGroup;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface INetworkEngine {

    @POST("/fcm/send")
    Call<Object> postFCMNotification(@Body FCMRequest request);

    @POST("/oauth/token")
    Call<ResponseToken> getAccessToken(@Body RequestToken token);

    @POST("/api/user")
    Call<String> register(@Body User user);
    @POST("/api/notification")
    Call<FcmMessage> sendNotification(@Body FcmMessage fcmMessage);

    @FormUrlEncoded
    @POST("/api/login")
    Call<User> authenticate(@Field("email") String email, @Field("password") String password);

    @GET("/api/user/{id}")
    Call<User> profile(@Path("id") Integer id);

    @GET("/api/user")
    Call<List<User>> checkUser(@Query("search") String search);

    @POST("/api/user/{id}")
    Call<User> editProfile(@Path("id") Integer id, @Body User user);

    @Multipart
    @POST("/api/avatar/{id}")
    Call<User> uploadAvatar(@Path("id") Integer id, @Part MultipartBody.Part image);

    @GET("/api/{model}/total")
    Call<Integer> getTotal(@Path("model") String model, @Query("search") String search);

    @POST("/api/usergroup")
    Call<UserGroup> addUser(@Body UserGroup user);

    @GET("/api/usergroup")
    Call<List<UserGroup>> getUserGroups(@Query("search") String search, @Query("page") Integer page, @Query("rows") Integer rows);

    @DELETE("/api/usergroup/{id}")
    Call<String> deleteGroupUser(@Path("id") Integer id);

    @GET("/api/lawer")
    Call<List<Lawer>> getLawers(@Query("page") Integer page, @Query("rows") Integer rows);

    @GET("/api/station")
    Call<List<Station>> getStation(@Query("search") String search, @Query("page") Integer page, @Query("rows") Integer rows);

    @GET("/api/station/{id}")
    Call<Station> getstationDetail(@Path("id") Integer id);

    @POST("/api/usergroup")
    Call<UserGroup> addUserFcmTokenandDeviceID(@Field("user_id") int user_id,@Field("device_id") String device_id,@Field("token") String token);

    @POST("/api/usergeo")
    Call<UserGeo> trackGeo(@Body UserGeo userGeo);

    @GET("/json")
    Call<CurrentLocation> getCurrentLocation();

    @POST("/api/feedback")
    Call<Feedback> postFeedback(@Body Feedback feedback);

}
