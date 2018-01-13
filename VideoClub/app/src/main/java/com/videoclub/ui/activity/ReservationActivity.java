package com.videoclub.ui.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.Movie;
import com.videoclub.data.database.entity.Reservation;
import com.videoclub.data.helpers.Constants;
import com.videoclub.ui.adapters.ReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity implements ReservationAdapter.ReservationListener {

    private VideoClubDatabase videoClubDatabase;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        setupUi();
    }

    @Override
    public void onPrepayReservation(Reservation reservation) {
        new Thread(() -> {
            videoClubDatabase.reservationDao().updateReservation(reservation);
        }).start();

        Snackbar.make(findViewById(R.id.reservation_container), R.string.reservation_prepay_message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onCancelReservation(Reservation reservation) {
        // Show an alert dialog for confirmation.
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // Set title
        alertDialogBuilder.setTitle(R.string.reservation_cancel_title);
        alertDialogBuilder
                .setMessage(R.string.reservation_cancel_msg)
                .setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    // Update the database.
                    new Thread(() -> {
                        // First update the copies of the movie.
                        Movie movie = videoClubDatabase.movieDao().getMovieById(reservation.getMovieId());
                        movie.setCopies(movie.getCopies() + 1);
                        videoClubDatabase.movieDao().updateMovie(movie);
                        // Remove the reservation.
                        videoClubDatabase.reservationDao().deleteReservation(reservation);
                    }).start();
                    // Update the adapter.
                    ReservationAdapter adapter = (ReservationAdapter) recyclerView.getAdapter();
                    adapter.removeReservation(reservation);
                    // Show the corresponding message.
                    if (reservation.getStatus() == Constants.RESERVATION_PAID) {
                        Snackbar.make(findViewById(R.id.reservation_container), R.string.reservation_cancelled_paid, Snackbar.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(findViewById(R.id.reservation_container), R.string.reservation_cancelled_unpaid, Snackbar.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(android.R.string.no, (dialog, id) -> {
                    dialog.cancel();
                });

        // Create and show the alert dialog.
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void setupUi(){
        // Setup action bar.
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(R.string.title_activity_reservation);
        setSupportActionBar(toolbar);
        // Setup recycler view.
        recyclerView = findViewById(R.id.reservation_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        // Set fixed size for better performance.
        recyclerView.setHasFixedSize(true);
        // Check and populate the movies in the background.
        new Thread(() -> {
            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get all the movies.
            List<Reservation> reservations = videoClubDatabase.reservationDao().getAllReservations();
            // List to set the adapter.
            List<Reservation> userReservations = new ArrayList<>();
            // Choose only the current user's reservations.
            for (Reservation res: reservations) {
                if (res.getUserId() == HomeActivity.currentUser.getUserId()) {
                    userReservations.add(res);
                }
            }
            // Now populate the adapter.
            ReservationAdapter adapter = new ReservationAdapter(userReservations, ReservationActivity.this);
            recyclerView.setAdapter(adapter);
        }).start();
    }
}
