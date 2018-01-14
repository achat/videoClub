package com.videoclub.data.helpers;

import com.videoclub.R;
import com.videoclub.data.database.entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class DummyItem {

    public static List<Movie> getDummyMovieList() {
        List<Movie> dummyMovies = new ArrayList<>();
        // Create dummy movies.
        Movie movie1 = new Movie();
        movie1.setName("Lord of the Rings");
        movie1.setCategory("Fantasy");
        movie1.setAvailable(1);
        movie1.setCopies(10);
        movie1.setPrice(15);
        movie1.setThumbnail(R.drawable.lotr_thumbnail);
        movie1.setRating((float)4.6);
        dummyMovies.add(movie1);

        Movie movie2 = new Movie();
        movie2.setName("Matrix");
        movie2.setCategory("Action/Fantasy");
        movie2.setAvailable(1);
        movie2.setCopies(10);
        movie2.setPrice(5);
        movie2.setThumbnail(R.drawable.matrix_thumbnail);
        movie2.setRating((float)4.2);
        dummyMovies.add(movie2);

        Movie movie3 = new Movie();
        movie3.setName("Matrix 2");
        movie3.setCategory("Action/Fantasy");
        movie3.setAvailable(1);
        movie3.setCopies(10);
        movie3.setPrice(15);
        movie3.setThumbnail(R.drawable.matrix_thumbnail);
        movie3.setRating((float)4.0);
        dummyMovies.add(movie3);

        Movie movie4 = new Movie();
        movie4.setName("Matrix 3");
        movie4.setCategory("Action/Fantasy");
        movie4.setAvailable(1);
        movie4.setCopies(15);
        movie4.setPrice(3);
        movie4.setThumbnail(R.drawable.matrix_thumbnail);
        movie4.setRating((float)3.9);
        dummyMovies.add(movie4);

	Movie movie5 = new Movie();
        movie5.setName("Η");
        movie5.setCategory("Fantasy");
        movie5.setAvailable(1);
        movie5.setCopies(10);
        movie5.setPrice(15);
        movie5.setThumbnail(R.drawable.inception_thumbnail);
        movie5.setRating((float)3.6);
        dummyMovies.add(movie5);

	Movie movie5 = new Movie();
        movie5.setName("Star Wars");
        movie5.setCategory("Fantasy");
        movie5.setAvailable(1);
        movie5.setCopies(10);
        movie5.setPrice(15);
        movie5.setThumbnail(R.drawable.inception_thumbnail);
        movie5.setRating((float)4.6);
        dummyMovies.add(movie5);

        Movie movie5 = new Movie();
        movie5.setName("X-men");
        movie5.setCategory("Fantasy");
        movie5.setAvailable(1);
        movie5.setCopies(10);
        movie5.setPrice(10);
        movie5.setThumbnail(R.drawable.inception_thumbnail);
        movie5.setRating((float)3.8);
        dummyMovies.add(movie5);

        return dummyMovies;
    }
}
