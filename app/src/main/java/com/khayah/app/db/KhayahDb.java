package com.khayah.app.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.khayah.app.vo.User;

@Database(entities = {User.class}, version = 1)
public abstract class KhayahDb extends RoomDatabase {

    public abstract UserDao userDao();
}
