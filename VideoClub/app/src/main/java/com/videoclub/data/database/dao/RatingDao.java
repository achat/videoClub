package com.videoclub.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.videoclub.data.database.entity.Rating;

import java.util.List;

/**
 * DAO interface for accessing {@link Rating} entities.
 */
@Dao
public interface RatingDao {

    @Insert
    void insertRating(Rating rating);

    @Query("SELECT * FROM ratings")
    List<Rating> getAllRatings();
}
