package com.khayah.app.ui.userprofile;

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
import com.khayah.app.databinding.UserProfileFragmentBinding;
import com.khayah.app.di.Injectable;
import com.khayah.app.util.AutoClearedValue;

import javax.inject.Inject;

public class UserProfileFragment extends Fragment implements Injectable {
    private static final String ARG_USER_ID = "arg.USER_ID";
    private UserProfileViewModel userProfileViewModel;
    private AutoClearedValue<UserProfileFragmentBinding> binding;
    private DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    public static UserProfileFragment newInstance(int userId) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        UserProfileFragmentBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment,
                container, false, dataBindingComponent);
        dataBinding.setRetryCallback(() -> userProfileViewModel.retry());
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int userId = getArguments().getInt(ARG_USER_ID);
        userProfileViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserProfileViewModel.class);
        userProfileViewModel.setUserId(userId);
        userProfileViewModel.getUser().observe(this, user -> {
            binding.get().setUser(user == null ? null : user.data);
            binding.get().setUserResource(user);
        });
    }


}
