package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    private TextView welcomeTextView;  // define welcome textview variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);  // link to Main activity XML

        /* Initialize the welcome text view */
        welcomeTextView = findViewById(R.id.welcome_textview);

        /* Get the username passed from the LoginApp activity */
        String username = getIntent().getStringExtra("USERNAME");

        /* Set the welcome message with the username */
        welcomeTextView.setText("Welcome " + username + "!");
    }
}
