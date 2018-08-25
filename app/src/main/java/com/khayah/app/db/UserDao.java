package com.khayah.app.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.khayah.app.vo.User;

import java.util.List;


@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User... user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<User> item);

    @Query("SELECT * FROM User WHERE id = :userId")
    LiveData<User> loadUser(int userId);

    @Query("SELECT * FROM User")
    LiveData<List<User>> loadUsers();
}
