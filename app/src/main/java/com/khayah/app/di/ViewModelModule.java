package com.khayah.app.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.khayah.app.ui.userlist.UserListViewModel;
import com.khayah.app.ui.userprofile.UserProfileViewModel;
import com.khayah.app.viewmodel.KhayahViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(UserProfileViewModel.class)
    abstract ViewModel bindUserProfileViewModel(UserProfileViewModel userProfileViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserListViewModel.class)
    abstract ViewModel bindUserListViewModel(UserListViewModel userListViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(KhayahViewModelFactory factory);
}
