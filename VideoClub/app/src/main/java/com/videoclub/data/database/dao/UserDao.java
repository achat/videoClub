package com.videoclub.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.videoclub.data.database.entity.User;

import java.util.List;

/**
 * Class for accessing all the user-related operations.
 */

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUsers();

    @Query("SELECT * FROM user WHERE name=:userName")
    User getUser(String userName);
    @Insert
    void insertUser(User user);
}
