package com.example.loginapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Signup extends AppCompatActivity {

    private EditText usernameEditText;  // define username edittext variable
    private EditText passwordEditText;  // define password edittext variable
    private EditText confirmEditText;   // define confirm edittext variable
    private Button loginButton;         // define login button variable
    private Button signupButton;        // define signup button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /* initialize UI elements */
        usernameEditText = findViewById(R.id.username);  // link to username edtext in the Signup activity XML
        passwordEditText = findViewById(R.id.password);  // link to password edtext in the Signup activity XML
        confirmEditText = findViewById(R.id.confirm_password);    // link to confirm edtext in the Signup activity XML
        loginButton = findViewById(R.id.login_button);    // link to login button in the Signup activity XML
        signupButton = findViewById(R.id.signup_button);  // link to signup button in the Signup activity XML

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(Signup.this, LoginApp.class);
                startActivity(intent);  // go to LoginActivity
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* grab strings from user inputs */
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String confirm = confirmEditText.getText().toString();

                if (password.equals(confirm)){
                    Toast.makeText(getApplicationContext(), "Signing up", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}