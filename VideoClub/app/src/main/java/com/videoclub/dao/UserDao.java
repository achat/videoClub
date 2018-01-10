package com.videoclub.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.videoclub.entity.User;

import java.util.List;

/**
 * Created by Kostas on 1/10/2018.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Insert
    void insertUser(User user);

    @Insert
    void insertAllUsers(User... users);

    @Delete
    void deleteUser(User user);

    @Query("DELETE FROM user")
    void deleteAll();
}
