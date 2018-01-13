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
import com.videoclub.data.helpers.Constants;

import java.util.List;

/**
 * Adapter for populating the recycler with reservations.
 */

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ResViewHolder> {

    private static final String TAG = ReservationAdapter.class.getSimpleName();
    private List<Reservation> reservationList;
    private ReservationListener reservationListener;

    public ReservationAdapter(List<Reservation> reservationList, ReservationListener reservationListener){
        this.reservationList = reservationList;
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
        holder.txtMovieTitle.setText(res.getMovieTitle());
        // Now set the button according to the reservation's status.
        if (res.getStatus() == Constants.RESERVATION_PAID) {
            holder.btnPrepay.setText(R.string.reservation_title_paid);
            holder.btnPrepay.setEnabled(false);
        } else {
            holder.btnPrepay.setText(R.string.reservation_title_unpaid);
        }
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public void removeReservation(Reservation reservation) {
        if (reservation != null) {
            // Remove from the list.
            reservationList.remove(reservation);
            // Update UI.
            notifyDataSetChanged();
        }
    }

    class ResViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView txtCount;
        TextView txtMovieTitle;
        Button btnPrepay;

        ResViewHolder(View itemView) {
            super(itemView);
            // Get references.
            txtCount = itemView.findViewById(R.id.reservation_count);
            txtMovieTitle = itemView.findViewById(R.id.reservation_movie_title);
            txtMovieTitle.setOnLongClickListener(this);
            btnPrepay = itemView.findViewById(R.id.reservation_prepay_button);
            // Set a listener to the prepay button.
            btnPrepay.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // Get the corresponding reservation.
            Reservation selectedReservation = reservationList.get(getAdapterPosition());
            // Change its status.
            selectedReservation.setStatus(Constants.RESERVATION_PAID);
            Log.d(TAG, "Reservation to prepay: " + selectedReservation.getMovieId());
            // Update UI item.
            btnPrepay.setText(R.string.reservation_title_paid);
            btnPrepay.setEnabled(false);
            // Inform the listener.
            reservationListener.onPrepayReservation(selectedReservation);
        }

        @Override
        public boolean onLongClick(View view) {
            // Get the corresponding reservation.
            Reservation selectedReservation = reservationList.get(getAdapterPosition());
            // Route to listener.
            reservationListener.onCancelReservation(selectedReservation);

            return true;
        }
    }

    /**
     * This interface is used to route action back to the activity when the user
     * selects an action upon the reservation.
     */
    public interface ReservationListener {
        void onPrepayReservation(Reservation reservation);

        void onCancelReservation(Reservation reservation);
    }
}
