/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khayah.app.ui.common;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.khayah.app.R;
import com.khayah.app.databinding.UserItemBinding;
import com.khayah.app.util.Objects;
import com.khayah.app.vo.User;

/**
 * A RecyclerView adapter for {@link User} class.
 */
public class UserListAdapter extends DataBoundListAdapter<User, UserItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final UserClickCallback UserClickCallback;

    public UserListAdapter(DataBindingComponent dataBindingComponent,
            UserClickCallback UserClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.UserClickCallback = UserClickCallback;
    }

    @Override
    protected UserItemBinding createBinding(ViewGroup parent) {
        UserItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.user_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            User User = binding.getUser();
            if (User != null && UserClickCallback != null) {
                UserClickCallback.onClick(User);
            }
        });
        return binding;
    }

    @Override
    protected void bind(UserItemBinding binding, User item) {
        binding.setUser(item);
    }

    @Override
    protected boolean areItemsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.getId(), newItem.getId()) &&
                Objects.equals(oldItem.getName(), newItem.getName());
    }

    @Override
    protected boolean areContentsTheSame(User oldItem, User newItem) {
        return Objects.equals(oldItem.getName(), newItem.getName()) &&
                oldItem.getLastName() == newItem.getLastName();
    }

    public interface UserClickCallback {
        void onClick(User User);
    }
}
