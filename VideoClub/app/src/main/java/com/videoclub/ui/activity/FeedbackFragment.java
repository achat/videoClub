package com.videoclub.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.videoclub.R;

public class FeedbackFragment extends DialogFragment implements View.OnClickListener {

    private OnFeedbackSubmitListener listener;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance() {
        return  new FeedbackFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        Button btnSubmit = view.findViewById(R.id.feedback_submit_button);
        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFeedbackSubmitListener) {
            listener = (OnFeedbackSubmitListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFeedbackSubmitListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onFeedbackSubmitted();
        }
        dismiss();
    }

    /**
     * Interface for routing action to the listener. In our case this is the {@link HomeActivity}
     */
    public interface OnFeedbackSubmitListener {
        void onFeedbackSubmitted();
    }
}
