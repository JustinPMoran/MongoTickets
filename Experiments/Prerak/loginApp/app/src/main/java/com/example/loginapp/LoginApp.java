package com.example.loginapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the layout to this activity
        setContentView(R.layout.activity_main); // Make sure this matches your layout file name

        // Find the views by their ID
        EditText usernameEditText = findViewById(R.id.username);  // Replace with your actual ID
        EditText passwordEditText = findViewById(R.id.password);  // Replace with your actual ID
        Button loginButton = findViewById(R.id.login_button);     // Replace with your actual ID

        // Handle button click
        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Basic login logic
            if (username.equals("admin") && password.equals("password")) {
                Toast.makeText(LoginApp.this, "Login successful", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginApp.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
