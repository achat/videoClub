package com.videoclub.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.videoclub.data.database.dao.MovieDao;
import com.videoclub.data.database.dao.RatingDao;
import com.videoclub.data.database.dao.ReservationDao;
import com.videoclub.data.database.dao.UserDao;
import com.videoclub.data.database.entity.Movie;
import com.videoclub.data.database.entity.Rating;
import com.videoclub.data.database.entity.Reservation;
import com.videoclub.data.database.entity.User;

/**
 * Created by Kostas on 1/10/2018.
 */

@Database(entities = {User.class, Movie.class, Reservation.class, Rating.class}, version = 7)
public abstract class VideoClubDatabase extends RoomDatabase{

    private static VideoClubDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract ReservationDao reservationDao();
    public abstract MovieDao movieDao();
    public abstract RatingDao ratingDao();

    public static VideoClubDatabase getVideoClubDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), VideoClubDatabase.class, "video-club-database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
