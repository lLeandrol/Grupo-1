package com.example.prueba10.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.prueba10.database.dao.UserDao;
import com.example.prueba10.database.model.User;
import com.example.prueba10.util.Constant;

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    public static UserDatabase userDB;

    public static UserDatabase getInstance(Context context) {
        if (userDB == null) {
            userDB = buildDatabaseInstance(context);
        }
        return userDB;
    }

    public static UserDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context, UserDatabase.class, Constant.DB_USER).
                allowMainThreadQueries().build();
    }
}
