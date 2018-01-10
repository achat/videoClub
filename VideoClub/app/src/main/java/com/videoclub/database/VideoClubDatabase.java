package com.videoclub.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.videoclub.dao.UserDao;
import com.videoclub.entity.User;

/**
 * Created by Kostas on 1/10/2018.
 */

@Database(entities = {User.class}, version = 1)
public abstract class VideoClubDatabase extends RoomDatabase{

    private static VideoClubDatabase INSTANCE;

    public abstract UserDao userDao();

    public static VideoClubDatabase getVideoClubDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VideoClubDatabase.class, "video-club-database")
                .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
