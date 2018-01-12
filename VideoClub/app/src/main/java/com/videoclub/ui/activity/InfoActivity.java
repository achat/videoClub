package com.videoclub.ui.activity;

import android.arch.persistence.room.ColumnInfo;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.Movie;
import com.videoclub.data.database.entity.Reservation;
import com.videoclub.data.helpers.Constants;

import java.util.List;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = InfoActivity.class.getSimpleName();
    private VideoClubDatabase videoClubDatabase;

    private String movieTitle;
    private Movie movie;

    private Button btnReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Intent intent = getIntent();
        if (intent != null) {
            movieTitle = intent.getStringExtra(Constants.EXTRA_MOVIE_TITLE);
        }

        setupUi();
    }

    @Override
    public void onClick(View view) {
        new Thread(() -> {
            // Check if the user has already reserved this movie.
            List<Reservation> reservations = videoClubDatabase.reservationDao().getAllReservations();
            for (Reservation res: reservations) {
                if (res.getUserId() == HomeActivity.currentUser.getUserId()
                        && res.getMovieId() == movie.getMovieId()) {
                    showMessage("You already reserved this movie.");
                    return;
                }
            }
            Reservation reservation = new Reservation();
            reservation.setMovieId(movie.getMovieId());
            reservation.setMovieTitle(movieTitle);
            reservation.setStatus(Constants.RESERVATION_UNPAID);
            reservation.setUserId(HomeActivity.currentUser.getUserId());

            videoClubDatabase.reservationDao().insertReservation(reservation);
            // We know that it is available so don't be scared to decrease.
            movie.setCopies(movie.getCopies() - 1);
            // Make this movie unavailable if no copies are left.
            if (movie.getCopies() == 0) {
                Log.d(TAG, "No copies of " + movieTitle + " left.");
                movie.setAvailable(0);
            } else {
                Log.d(TAG, "Copies of " + movieTitle + " left: " + movie.getCopies());
            }
            // Update database.
            videoClubDatabase.movieDao().updateMovie(movie);
            // Show confirmation message to the user.
            showMessage("Movie reserved.");
        }).start();
    }

    private void showMessage(String message) {
        runOnUiThread(() -> {
            Toast.makeText(InfoActivity.this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void setupUi() {
        // Setup action bar.
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(movieTitle);
        setSupportActionBar(toolbar);
        // Add click handler to reserve button.
        btnReserve = findViewById(R.id.info_movie_reserve_button);
        btnReserve.setOnClickListener(this);

        new Thread(() -> {
            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get the corresponding movie.
            movie = videoClubDatabase.movieDao().getMovie(movieTitle);
            // Initialize UI elements accordingly.
            ImageView movieImage = findViewById(R.id.info_movie_image);
            movieImage.setImageResource(movie.getThumbnail());
            TextView movieCategory = findViewById(R.id.info_movie_category);
            movieCategory.setText(String.format(getString(R.string.info_movie_category), movie.getCategory(), movie.getCopies()));
            TextView movieDescription = findViewById(R.id.info_movie_description);
            switch (movie.getMovieId()) {
                case 1:
                    movieDescription.setText(R.string.lotr_description);
                    break;
                case 2:
                    movieDescription.setText(R.string.matrix_description);
                    break;
                case 3:
                    movieDescription.setText(R.string.matrix_2_description);
                    break;
                case 4:
                    movieDescription.setText(R.string.matrix_3_description);
                    break;
                case 5:
                    movieDescription.setText(R.string.inception_description);
                    break;
                default:
                    break;
            }
            // Disable reserve button if not available.
            if (movie.getAvailable() != 1) {
                btnReserve.setEnabled(false);
            }
        }).start();
    }
}
