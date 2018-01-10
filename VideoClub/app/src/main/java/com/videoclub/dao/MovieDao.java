package com.videoclub.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.videoclub.entity.Movie;

import java.util.List;

/**
 * Created by Kostas on 1/10/2018.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    List<Movie> getAllMovies();

    @Insert
    void insertMovie(Movie movie);

    @Insert
    void insertAllMovies(Movie... movies);

    @Delete
    void deleteMovie(Movie movie);
}
