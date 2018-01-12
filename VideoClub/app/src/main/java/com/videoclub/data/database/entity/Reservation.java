package com.videoclub.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Kostas on 1/10/2018.
 */

@Entity(
        tableName="reservation",
        primaryKeys={"reservationid", "movieid", "userid"},
        foreignKeys={
                @ForeignKey(
                        entity=Movie.class,
                        parentColumns="movieid",
                        childColumns="movieid",
                        onDelete=CASCADE),
                @ForeignKey(
                        entity=User.class,
                        parentColumns="userid",
                        childColumns="userid",
                        onDelete=CASCADE)}

)
public class Reservation {


    @ColumnInfo(name = "reservationid")
    private int reservationId;

    @ColumnInfo(name = "userid")
    private int userId;

    @ColumnInfo(name = "movieid")
    private int movieId;

    @ColumnInfo(name = "movie_title")
    private String movieTitle;

    @ColumnInfo(name = "status")
    private int status;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }
}
