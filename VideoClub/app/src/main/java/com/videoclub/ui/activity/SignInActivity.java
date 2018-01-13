package com.videoclub.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.videoclub.R;
import com.videoclub.data.database.VideoClubDatabase;
import com.videoclub.data.database.entity.User;
import com.videoclub.data.helpers.Constants;

import java.util.List;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    // Logging tag.
    private static final String TAG = SignInActivity.class.getSimpleName();
    // Database reference.
    private VideoClubDatabase videoClubDatabase;
    // UI elements.
    private Button btnContinue;
    private Button btnSignIn;
    private TextInputLayout inputLayout;
    private TextInputEditText inputText;
    // The username to pass to the rest of the application.
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // First handle UI related operations.
        setupUi();
        // Initialize database in the background.
        new Thread(() -> {
            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());
            // First get all the available users.
            List<User> users = videoClubDatabase.userDao().getAllUsers();
            // If the list is not empty display the option to continue as the latest user.
            if (!users.isEmpty()) {
                // Get the latest user.
                User latestUser = users.get(users.size() - 1);
                // Save the name.
                username = latestUser.getName();
                // Construct the string.
                final String title = String.format(getString(R.string.sign_in_continue_text), username);
                runOnUiThread(() -> {
                    // Set the text.
                    btnContinue.setText(title);
                    // Make it visible.
                    btnContinue.setVisibility(View.VISIBLE);
                });
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
        btnContinue.setOnClickListener(this);
        btnSignIn = findViewById(R.id.sign_in_button);
        btnSignIn.setOnClickListener(this);
        inputText = findViewById(R.id.sign_in_input_text);
        inputLayout = findViewById(R.id.sign_in_input_container);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                String userInput = inputText.getText().toString().trim();
                if (userInput.isEmpty() || userInput.length() < 4) {
                    // Display error.
                    inputLayout.setError(getString(R.string.sign_in_input_error));
                } else {
                    // Disable to avoid spam.
                    btnSignIn.setEnabled(false);
                    // Update the member variable.
                    username = userInput;
                    // Insert user to the database if not already.
                    insertUser(username);
                    // Show home page.
                    showHomeActivity();
                }
                break;
            case R.id.sign_in_continue_button:
                showHomeActivity();
                break;
            default:
                break;
        }
    }

    private void showHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        // Pass the username as an argument.
        intent.putExtra(Constants.ARG_CURRENT_USERNAME, username);
        // Launch the activity.
        startActivity(intent);
        // Exit, so the user cannot return with the back button.
        finish();
    }

    private void insertUser(final String username) {
        if (videoClubDatabase != null) {
            new Thread(() -> {
                // First check if the user already exists.
                User user = videoClubDatabase.userDao().getUser(username);
                if (user == null) {
                    // Create the user.
                    User newUser = new User();
                    newUser.setName(username);
                    // Issue the database command.
                    videoClubDatabase.userDao().insertUser(newUser);
                    Log.d(TAG, "User " + username + " added.");
                } else {
                    Log.d(TAG, "User already exists.");
                }
            }).start();
        }
    }
}


