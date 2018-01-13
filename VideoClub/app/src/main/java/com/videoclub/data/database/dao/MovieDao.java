package com.videoclub.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.videoclub.data.database.entity.Movie;

import java.util.List;

/**
 * Created by Kostas on 1/10/2018.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie")
    List<Movie> getAllMovies();

    @Query("SELECT * FROM movie WHERE name=:movieTitle")
    Movie getMovie(String movieTitle);

    @Query("SELECT * FROM movie WHERE movieid=:movieId")
    Movie getMovieById(int movieId);

    @Insert
    void insertAllMovies(List<Movie> movies);

    @Update
    void updateMovie(Movie movie);
}
