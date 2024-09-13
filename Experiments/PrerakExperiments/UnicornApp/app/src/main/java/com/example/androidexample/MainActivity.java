package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private ImageView Unicorn;     // define message textview variable
    private Button counterButton;     // define counter button variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        Unicorn = findViewById(R.id.unicorn);      // link to message textview in the Main activity XML

        /* click listener on counter button pressed */
        Unicorn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This runs after the animation finishes
                v.animate()
                        .scaleX(1.5f)
                        .scaleY(1.5f)
                        .rotation(360f)
                        .setDuration(1300)
                        .withEndAction(() -> {
                            // Intent to switch to Counter Activity after animation finishes
                            Intent intent = new Intent(MainActivity.this, CounterActivity.class);
                            startActivity(intent);
                        })
                        .start();
            }
        });
    }
}