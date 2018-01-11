package com.videoclub.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.videoclub.viewholders.MovieViewHolder;
import com.videoclub.R;
import com.videoclub.entity.Movie;

import java.util.List;

/**
 * Created by Kostas on 1/11/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter{

    private List<Movie> movies;

    public MovieAdapter(List<Movie> movies){
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_rv_movies,parent, false);
        RecyclerView.ViewHolder row = new MovieViewHolder(view);
        return row;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) return;

        MovieViewHolder viewHolder = (MovieViewHolder) holder;

        viewHolder.movieTitle.setText(movies.get(position).getName());
        viewHolder.movieCategory.setText(movies.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
