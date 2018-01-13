package com.videoclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.videoclub.R;
import com.videoclub.data.database.entity.Movie;

import java.util.List;

/**
 * Adapter class for populating the recycler with movies.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private static final String TAG = MovieAdapter.class.getSimpleName();
    private final List<Movie> movies;
    private final MovieSelectionListener movieSelectionListener;

    public MovieAdapter(List<Movie> movies, MovieSelectionListener movieSelectionListener){
        this.movies = movies;
        this.movieSelectionListener = movieSelectionListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item,parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.txtTitle.setText(movie.getName());
        holder.imgThumbnail.setImageResource(movie.getThumbnail());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView txtTitle;
        final ImageView imgThumbnail;

        MovieViewHolder(View itemView) {
            super(itemView);
            // Set a listener to the thumbnail.
            txtTitle = itemView.findViewById(R.id.movie_item_title);
            imgThumbnail = itemView.findViewById(R.id.movie_item_thumbnail);
            // Set a listener to the thumbnail.
            imgThumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the corresponding movie.
            Movie selectedMovie = movies.get(getAdapterPosition());
            Log.d(TAG, "Movie selected: " + selectedMovie.getName());
            // Inform the listener.
            movieSelectionListener.onMovieClicked(selectedMovie);
        }
    }

    /**
     * This interface is used to route action back to the activity when the user
     * clicks on a movie for more information.
     */
    public interface MovieSelectionListener {
        void onMovieClicked(Movie selectedMovie);
    }
}
