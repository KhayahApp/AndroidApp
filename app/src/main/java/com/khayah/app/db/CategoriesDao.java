package com.khayah.app.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.khayah.app.vo.Categories;

import java.util.List;


@Dao
public interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Categories... categories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<Categories> item);

    @Query("SELECT * FROM Categories WHERE id = :userId")
    LiveData<Categories> loadUser(int userId);

    @Query("SELECT * FROM Categories")
    LiveData<List<Categories>> loadUsers();
}
