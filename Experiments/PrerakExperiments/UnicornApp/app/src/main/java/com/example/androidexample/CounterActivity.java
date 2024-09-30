package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CounterActivity extends AppCompatActivity {

    private Button backBtn;  // define back button variable
    private ImageView gifImageView;  // define GIF ImageView variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);


        gifImageView = findViewById(R.id.gifImageView);

        // Initialize the back button
        backBtn = findViewById(R.id.counter_back_btn);

        // When back button is pressed, switch back to MainActivity
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CounterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
