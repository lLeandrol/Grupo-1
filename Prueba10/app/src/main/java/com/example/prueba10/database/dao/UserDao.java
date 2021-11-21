package com.example.prueba10.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.prueba10.database.model.User;
import com.example.prueba10.util.Constant;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Update
    int updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM " + Constant.TABLE_NAME_USER)
    List<User> getUser();

    @Query("SELECT * FROM " + Constant.TABLE_NAME_USER + " WHERE email = :email  and password = :password")
    User getUserLogin(String email, String password);



}
