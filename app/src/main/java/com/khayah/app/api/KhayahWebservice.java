package com.khayah.app.api;

import android.arch.lifecycle.LiveData;

import com.khayah.app.vo.User;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface KhayahWebservice {

    @GET("users/{user_id}")
    LiveData<ApiResponse<User>>  getUser(@Path("user_id") int userId);

    @GET("users")
    LiveData<ApiResponse<List<User>>> getUsers();
}
