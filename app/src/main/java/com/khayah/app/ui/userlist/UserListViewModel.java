package com.khayah.app.ui.userlist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.khayah.app.repository.UserRepository;
import com.khayah.app.vo.Resource;
import com.khayah.app.vo.Status;
import com.khayah.app.vo.User;

import java.util.List;

import javax.inject.Inject;

public class UserListViewModel extends ViewModel {

    private LiveData<Resource<List<User>>> users;
    private UserRepository userRepository;

    @Inject
    public UserListViewModel(UserRepository userRepository) {
        users = userRepository.getUsers();
        this.userRepository = userRepository;
    }

    public LiveData<Resource<List<User>>> getUsers() {
        return users;
    }

    public void retry() {
        if (users != null && users.getValue() != null && users.getValue().status == Status.SUCCESS) {
            return;
        }
        users = this.userRepository.getUsers();
    }

}
