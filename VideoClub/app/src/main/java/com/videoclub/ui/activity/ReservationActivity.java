package com.videoclub.ui.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.Reservation;
import com.videoclub.ui.adapters.ReservationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReservationActivity extends AppCompatActivity implements ReservationAdapter.ReservationListener {

    VideoClubDatabase videoClubDatabase;

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

    private void setupUi(){
        // Setup action bar.
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(R.string.title_activity_reservation);
        setSupportActionBar(toolbar);
        // Setup recycler view.
        RecyclerView recyclerView = findViewById(R.id.reservation_recycler);
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
