package com.videoclub.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.videoclub.adapters.MovieAdapter;
import com.videoclub.R;
import com.videoclub.database.VideoClubDatabase;
import com.videoclub.entity.Movie;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {

    private VideoClubDatabase videoClubDatabase;
    private List<Movie> availableMovies;
    private boolean isMovieListEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.rv_movies);

        checkIfMovieListIsEmpty();

        if(isMovieListEmpty){
            insertDummyMovies();
            getAvailableMovies();
            MovieAdapter adapter = new MovieAdapter(availableMovies);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }else{
            getAvailableMovies();
            MovieAdapter adapter = new MovieAdapter(availableMovies);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }

    }

    private void insertDummyMovies(){
        ExecutorService executor = Executors.newFixedThreadPool(3);
        
        try {
            executor.submit(() -> {
                Movie movie1 = new Movie();
                movie1.setName("Lord of the Rings");
                movie1.setCategory("Fantasy");
                movie1.setAvailable(1);
                movie1.setCopies(10);
                movie1.setPrice(15);

                Movie movie2 = new Movie();
                movie2.setName("Matrix");
                movie2.setCategory("Fantasy");
                movie2.setAvailable(1);
                movie2.setCopies(10);
                movie2.setPrice(5);

                Movie movie3 = new Movie();
                movie3.setName("Matrix 2");
                movie3.setCategory("Fantasy");
                movie3.setAvailable(1);
                movie3.setCopies(10);
                movie3.setPrice(15);

                Movie movie4 = new Movie();
                movie4.setName("Matrix 3");
                movie4.setCategory("Fantasy");
                movie4.setAvailable(1);
                movie4.setCopies(15);
                movie4.setPrice(3);

                Movie movie5 = new Movie();
                movie5.setName("Inception");
                movie5.setCategory("Fantasy");
                movie5.setAvailable(1);
                movie5.setCopies(10);
                movie5.setPrice(15);

                videoClubDatabase.movieDao().insertMovie(movie1);
                videoClubDatabase.movieDao().insertMovie(movie2);
                videoClubDatabase.movieDao().insertMovie(movie3);
                videoClubDatabase.movieDao().insertMovie(movie4);
                videoClubDatabase.movieDao().insertMovie(movie5);
            });

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void checkIfMovieListIsEmpty(){
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            executor.submit(() -> {
                isMovieListEmpty = videoClubDatabase.movieDao().getAllMovies().isEmpty();
            });

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void getAvailableMovies(){
        ExecutorService executor = Executors.newFixedThreadPool(3);

        try {
            executor.submit(() -> {
                availableMovies = videoClubDatabase.movieDao().getAllMovies();
            });

            executor.shutdown();
            executor.awaitTermination(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
