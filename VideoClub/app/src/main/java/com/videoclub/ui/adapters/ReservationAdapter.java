package com.videoclub.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.videoclub.R;
import com.videoclub.data.database.entity.Reservation;

import java.util.List;

/**
 * Adapter for populating the recycler with reservations.
 */

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ResViewHolder> {

    private static final String TAG = ReservationAdapter.class.getSimpleName();
    private List<Reservation> reservationList;
    private List<String> reservedMovies;
    private ReservationListener reservationListener;

    public ReservationAdapter(List<Reservation> reservationList, List<String> reservedMovies, ReservationListener reservationListener){
        this.reservationList = reservationList;
        this.reservedMovies = reservedMovies;
        this.reservationListener = reservationListener;
    }

    @Override
    public ResViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ResViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResViewHolder holder, int position) {
        Reservation res = reservationList.get(position);
        // Set numbering, start from 1.
        holder.txtCount.setText(String.valueOf(position + 1));
        // Set the movie title.
        holder.txtMovieTitle.setText(reservedMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    class ResViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtCount;
        TextView txtMovieTitle;
        Button btnPrepay;

        ResViewHolder(View itemView) {
            super(itemView);
            // Get references.
            txtCount = itemView.findViewById(R.id.reservation_count);
            txtMovieTitle = itemView.findViewById(R.id.reservation_movie_title);
            btnPrepay = itemView.findViewById(R.id.reservation_prepay_button);
            // Set a listener to the prepay button.
            btnPrepay.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the corresponding reservation.
            Reservation selectedReservation = reservationList.get(getAdapterPosition());
            Log.d(TAG, "Reservation to prepay: " + selectedReservation.getMovieId());
            // Inform the listener.
            reservationListener.onPrepayReservation(selectedReservation);
        }
    }

    /**
     * This interface is used to route action back to the activity when the user
     * selects an action upon the reservation.
     */
    public interface ReservationListener {
        void onPrepayReservation(Reservation reservation);
    }
}
