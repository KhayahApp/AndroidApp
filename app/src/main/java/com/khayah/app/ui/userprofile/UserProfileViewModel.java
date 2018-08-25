package com.khayah.app.ui.userprofile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.text.TextUtils;

import com.khayah.app.repository.UserRepository;
import com.khayah.app.util.AbsentLiveData;
import com.khayah.app.util.Objects;
import com.khayah.app.vo.Resource;
import com.khayah.app.vo.User;

import javax.inject.Inject;

public class UserProfileViewModel extends ViewModel {
    private final MutableLiveData<Integer> userId;
    private LiveData<Resource<User>> user;

    @Inject
    public UserProfileViewModel(UserRepository userRepository) {
        userId = new MutableLiveData<>();
        user = Transformations.switchMap(userId, userId -> {
            if (userId <= 0) {
                return AbsentLiveData.create();
            } else {
                return userRepository.getUser(userId);
            }
        });
    }

    public void setUserId(int userId) {
        if (Objects.equals(this.userId.getValue(), userId)) {
            return;
        }
        this.userId.setValue(userId);
    }

    public LiveData<Resource<User>> getUser() {
        return user;
    }

    public void retry() {
        if (this.userId.getValue() != null) {
            this.userId.setValue(this.userId.getValue());
        }
    }
}
