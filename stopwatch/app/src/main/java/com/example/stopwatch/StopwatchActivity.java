package com.example.stopwatch;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StopwatchActivity extends AppCompatActivity {

    public void OnAlarm (View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private Chronometer chronometer;
    long pausedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stopwatch);

        chronometer = findViewById(R.id.chronometer);

        Button start_btn = findViewById(R.id.start_btn);
        Button stop_btn = findViewById(R.id.stop_btn);
        Button reset_btn = findViewById(R.id.reset_btn);
        Button continue_btn = findViewById(R.id.continue_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.stop_btn).setVisibility(View.VISIBLE);

                long currentTime = SystemClock.elapsedRealtime();
                chronometer.setBase(currentTime);
                chronometer.start();

            }
        });

        stop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.stop_btn).setVisibility(View.INVISIBLE);
                findViewById(R.id.start_btn).setVisibility(View.INVISIBLE);
                findViewById(R.id.reset_btn).setVisibility(View.VISIBLE);
                findViewById(R.id.continue_btn).setVisibility(View.VISIBLE);

                pausedTime = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.stop();


            }
        });
        reset_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.reset_btn).setVisibility(View.INVISIBLE);
                findViewById(R.id.continue_btn).setVisibility(View.INVISIBLE);
                findViewById(R.id.start_btn).setVisibility(View.VISIBLE);


                chronometer.stop();
                long currentTime = SystemClock.elapsedRealtime();
                chronometer.setBase(currentTime);


            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pausedTime);
                chronometer.start();

                findViewById(R.id.reset_btn).setVisibility(View.INVISIBLE);
                findViewById(R.id.stop_btn).setVisibility(View.VISIBLE);



            }
        });

    }
}