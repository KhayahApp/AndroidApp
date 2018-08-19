package com.khayah.app.ui.userlist;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.khayah.app.R;
import com.khayah.app.binding.FragmentDataBindingComponent;
import com.khayah.app.databinding.UserListFragmentBinding;
import com.khayah.app.di.Injectable;
import com.khayah.app.ui.common.UserListAdapter;
import com.khayah.app.ui.userprofile.UserProfileActivity;
import com.khayah.app.util.AutoClearedValue;

import javax.inject.Inject;

public class UserListFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private UserListViewModel userListViewModel;
    AutoClearedValue<UserListFragmentBinding> binding;
    private AutoClearedValue<UserListAdapter> adapter;

    public static UserListFragment newInstance() {
        UserListFragment fragment = new UserListFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserListFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.user_list_fragment,
                container, false, dataBindingComponent);
        dataBinding.setRetryCallback(() -> userListViewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userListViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserListViewModel.class);
        UserListAdapter rvAdapter = new UserListAdapter(dataBindingComponent,
                user -> UserProfileActivity.start(getContext(), user.getId()));
        binding.get().userList.setAdapter(rvAdapter);
        this.adapter = new AutoClearedValue<>(this, rvAdapter);
        initUserList();
    }

    private void initUserList() {
        userListViewModel.getUsers().observe(this, listResource -> {
            // no null checks for adapter.get() since LiveData guarantees that we'll not receive
            // the event if fragment is now show.
            if (listResource == null) {
                adapter.get().replace(null);
            } else {
                adapter.get().replace(listResource.data);
            }
        });
    }
}
