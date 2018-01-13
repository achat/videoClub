package com.videoclub.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.Movie;
import com.videoclub.data.database.entity.Rating;
import com.videoclub.data.database.entity.Reservation;
import com.videoclub.data.helpers.Constants;

import java.util.List;

public class InfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = InfoActivity.class.getSimpleName();
    private VideoClubDatabase videoClubDatabase;

    private String movieTitle;
    private Movie movie;

    private Button btnReserve;
    private RatingBar ratingBar;

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
            TextView movieCategory = findViewById(R.id.info_movie_category);
            movieCategory.post(() -> movieCategory.setText(String.format(getString(R.string.info_movie_category), movie.getCategory(), movie.getCopies())));
            // Update database.
            videoClubDatabase.movieDao().updateMovie(movie);
            // Show confirmation message to the user.
            showMessage("Movie reserved.");
        }).start();
    }

    private void showMessage(String message) {
        runOnUiThread(() -> Toast.makeText(InfoActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void setupUi() {
        // Setup action bar.
        Toolbar toolbar = findViewById(R.id.app_toolbar);
        toolbar.setTitle(movieTitle);
        setSupportActionBar(toolbar);
        // Add click handler to reserve button.
        btnReserve = findViewById(R.id.info_movie_reserve_button);
        btnReserve.setOnClickListener(this);
        // Rating Bar
        ratingBar = findViewById(R.id.info_movie_rating);

        new Thread(() -> {
            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get the corresponding movie.
            movie = videoClubDatabase.movieDao().getMovie(movieTitle);
            // Initialize UI elements accordingly.
            ratingBar.setRating(movie.getRating());
            ratingBar.setOnRatingBarChangeListener((ratingBar, v, b) -> {
                // If this notification comes from the user...
                if (b) {
                   if (ratingBar.isIndicator()) {
                       showMessage("You have already rated this movie!");
                   } else {
                       saveNewRating(v);
                   }
                }
                // Else ignore...
            });
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
            checkIfUserRated();
        }).start();
    }

    private void checkIfUserRated() {
        // Check if the user has already rated this movie.
        List<Rating> ratings = videoClubDatabase.ratingDao().getAllRatings();
        for (Rating rate: ratings) {
            if (rate.getUserId() == HomeActivity.currentUser.getUserId() && rate.getMovieId() == movie.getMovieId()) {
                ratingBar.setIsIndicator(true);
                break;
            }
        }
    }

    private void saveNewRating(float rating) {
        // Calculate new rating (We don't count in the number of votes for simplicity).
        float newRating;
        float currentRating = movie.getRating();
        if (rating < currentRating) {
            newRating = currentRating - (currentRating - rating) * (float) Constants.USER_RATING_WEIGHT;
            // 0 is min.
            if (newRating < 0.0) newRating = (float) 0.0;
        } else if (rating > currentRating) {
            newRating = currentRating + (rating - currentRating) * (float) Constants.USER_RATING_WEIGHT;
            // 5 is max.
            if (newRating > 5.0) newRating = (float) 5.0;
        } else {
            createRating();
            ratingBar.setIsIndicator(true);
            // The rating is the same.
            showMessage(getString(R.string.info_movie_rating_thx));
            return;
        }
        createRating();
        // Set movie rating.
        movie.setRating(newRating);
        // Update UI.
        ratingBar.setRating(newRating);
        ratingBar.setIsIndicator(true);
        // Update database.
        new Thread(()-> videoClubDatabase.movieDao().updateMovie(movie)).start();
        // Show message to user.
        showMessage(getString(R.string.info_movie_rating_thx));
    }

    private void createRating() {
        // Save as object in database.
        Rating dbRating = new Rating();
        dbRating.setMovieId(movie.getMovieId());
        dbRating.setUserId(HomeActivity.currentUser.getUserId());
        new Thread(()-> videoClubDatabase.ratingDao().insertRating(dbRating)).start();
    }
}
