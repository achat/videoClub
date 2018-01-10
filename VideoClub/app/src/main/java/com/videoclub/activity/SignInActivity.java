package com.videoclub.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.videoclub.R;
import com.videoclub.database.VideoClubDatabase;
import com.videoclub.entity.User;

import java.util.List;

public class SignInActivity extends AppCompatActivity {


    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // First handle UI related operations.
        setupUi();
        // Initialize database in the background.
        new Thread(new Runnable() {
            @Override
            public void run() {
                VideoClubDatabase videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
                // First get all the available users.
                List<User> users = videoClubDatabase.userDao().getAllUsers();
                // If the list is not empty display the option to continue as the latest user.
                if (!users.isEmpty()) {
                    // Get the latest user.
                    User latestUser = users.get(users.size() - 1);
                    // Construct the string.
                    final String title = String.format(getString(R.string.sign_in_continue_text), latestUser.getName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Set the text.
                            btnContinue.setText(title);
                            // Make it visible.
                            btnContinue.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    private void setupUi() {
        // Hide app title from the toolbar.
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setDisplayShowTitleEnabled(false);

        // Get a reference to UI elements.
        btnContinue = findViewById(R.id.sign_in_continue_button);
    }
}


