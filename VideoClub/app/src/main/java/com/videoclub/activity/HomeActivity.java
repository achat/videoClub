package com.videoclub.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.videoclub.adapters.MovieAdapter;
import com.videoclub.R;
import com.videoclub.database.VideoClubDatabase;
import com.videoclub.entity.Movie;
import com.videoclub.helpers.DummyItem;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    // Logging tag.
    private static final String TAG = HomeActivity.class.getSimpleName();

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupUi();
    }

    private void setupUi(){
        // Get a reference to the recycler view.
        recyclerView = findViewById(R.id.home_movies_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Set fixed size for better performance.
        recyclerView.setHasFixedSize(true);
        // Check and populate the movies in the background.
        new Thread(() -> {
            VideoClubDatabase videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // Get all the movies.
            List<Movie> list = videoClubDatabase.movieDao().getAllMovies();
            // Check for emptiness.
            if (list.isEmpty()) {
                Log.d(TAG, "No movies, populating with dummies.");
                // If empty populate with dummy content.
                list.addAll(DummyItem.getDummyMovieList());
                // Save them in the database too.
                videoClubDatabase.movieDao().insertAllMovies(list);
            }
            // Now populate the adapter.
            MovieAdapter adapter = new MovieAdapter(list);
            recyclerView.setAdapter(adapter);
        }).start();
    }
}
