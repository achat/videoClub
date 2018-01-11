package com.videoclub.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.videoclub.R;

/**
 * Created by Kostas on 1/11/2018.
 */

public class MovieViewHolder extends RecyclerView.ViewHolder{

    public TextView movieTitle;
    public TextView movieCategory;

    public MovieViewHolder(View itemView) {
        super(itemView);
        movieTitle = itemView.findViewById(R.id.movie_title);
        movieCategory = itemView.findViewById(R.id.movie_category);
    }
}
