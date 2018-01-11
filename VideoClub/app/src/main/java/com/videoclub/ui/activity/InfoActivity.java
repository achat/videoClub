package com.videoclub.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.Movie;
import com.videoclub.data.helpers.Constants;

import java.util.List;

public class InfoActivity extends AppCompatActivity {

    private String movieTitle;

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

    private void setupUi() {
        new Thread(() -> {
            VideoClubDatabase videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get the corresponding movie.
            Movie movie = videoClubDatabase.movieDao().getMovie(movieTitle);
            // Initialize UI elements accordingly.
            ImageView movieImage = findViewById(R.id.info_movie_image);
            movieImage.setImageResource(movie.getThumbnail());
            TextView movieCategory = findViewById(R.id.info_movie_category);
            movieCategory.setText(String.format(getString(R.string.info_movie_category), movie.getCategory()));
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

        }).start();
    }
}
