package com.videoclub.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.videoclub.entity.Reservation;

import java.util.List;

/**
 * Created by Kostas on 1/11/2018.
 */

@Dao
public interface ReservationDao {

    @Query("SELECT * FROM reservation")
    List<Reservation> getAllReservations();

    @Insert
    void insertReservation(Reservation reservation);

    @Insert
    void insertAllReservations(Reservation... reservation);

    @Delete
    void deleteReservation(Reservation reservation);
}
