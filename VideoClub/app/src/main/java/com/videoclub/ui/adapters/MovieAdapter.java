package com.videoclub.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.videoclub.R;
import com.videoclub.data.database.entity.Movie;

import java.util.List;

/**
 * Created by Kostas on 1/11/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies){
        this.movies = movies;
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
        TextView txtTitle;
        ImageView imgThumbnail;

        MovieViewHolder(View itemView) {
            super(itemView);
            // Set a listener to the view.
            CardView cardView = (CardView) itemView;
            cardView.setOnClickListener(this);

            txtTitle = itemView.findViewById(R.id.movie_item_title);
            imgThumbnail = itemView.findViewById(R.id.movie_item_thumbnail);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
