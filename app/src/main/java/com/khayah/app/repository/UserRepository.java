package com.khayah.app.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.khayah.app.AppExecutors;
import com.khayah.app.api.ApiResponse;
import com.khayah.app.api.KhayahWebservice;
import com.khayah.app.db.UserDao;
import com.khayah.app.vo.Resource;
import com.khayah.app.vo.User;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private AppExecutors appExecutors;
    private UserDao userDao;
    private KhayahWebservice webservice;

    @Inject
    public UserRepository(AppExecutors appExecutors, UserDao userDao, KhayahWebservice webservice) {
        this.appExecutors = appExecutors;
        this.userDao = userDao;
        this.webservice = webservice;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        return new NetworkBoundResource<List<User>, List<User>>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull List<User> item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<User>> loadFromDb() {
                return userDao.loadUsers();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<List<User>>> createCall() {
                return webservice.getUsers();
            }
        }.asLiveData();
    }

    public LiveData<Resource<User>> getUser(int userId) {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User user) {
                userDao.insert(user);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.loadUser(userId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.getUser(userId);
            }
        }.asLiveData();
    }

}
