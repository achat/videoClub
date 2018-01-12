package com.videoclub.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    }

    private void setupUi(){
        // Setup action bar.
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(R.string.title_activity_reservation);
        setSupportActionBar(toolbar);
        // Setup recycler view.
        RecyclerView recyclerView = findViewById(R.id.reservation_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set fixed size for better performance.
        recyclerView.setHasFixedSize(true);
        // Check and populate the movies in the background.
        new Thread(() -> {
            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get all the movies.
            List<Reservation> reservations = videoClubDatabase.reservationDao().getAllReservations();
            // We'll have to create a second list with the movie names. We could just save the
            // information to Reservation class, too.
            List<String> reservedMovieTitles = new ArrayList<>();
            // List to set the adapter.
            List<Reservation> userReservations = new ArrayList<>();
            // Choose only the current user's reservations.
            for (Reservation res: reservations) {
                if (res.getUserId() == HomeActivity.currentUser.getUserId()) {
                    userReservations.add(res);
                    String title = videoClubDatabase.movieDao().getMovieTitleById(res.getMovieId());
                    reservedMovieTitles.add(title);
                }
            }
            // Now populate the adapter.
            ReservationAdapter adapter = new ReservationAdapter(userReservations, reservedMovieTitles, ReservationActivity.this);
            recyclerView.setAdapter(adapter);
        }).start();
    }
}
