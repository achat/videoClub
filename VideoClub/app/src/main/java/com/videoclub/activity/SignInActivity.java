package com.videoclub.activity;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.videoclub.R;
import com.videoclub.database.VideoClubDatabase;
import com.videoclub.entity.User;

import java.util.List;

public class SignInActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        // First handle UI related operations.
        setupUi();
        // Initialize database in the background.
        new DatabaseAsync().execute();
    }

    private void setupUi() {
        // Hide app title from the toolbar.
        ActionBar toolbar = getSupportActionBar();
        if (toolbar != null)
            toolbar.setDisplayShowTitleEnabled(false);


    }

    class DatabaseAsync extends AsyncTask<Void, Void, Void> {

        VideoClubDatabase videoClubDatabase;

        @Override
        protected Void doInBackground(Void... voids) {

            videoClubDatabase = VideoClubDatabase.getVideoClubDatabase(getApplicationContext());

            User user = new User();
            user.setName("Swag");

            videoClubDatabase.userDao().insertUser(user);

            List<User> users = videoClubDatabase.userDao().getAllUsers();
            System.out.println(users.get(0).getName());
            return null;
        }
    }
}


