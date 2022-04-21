package com.sambhav2358.backgroundaudioplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        super.onPause();
        startForegroundService(new Intent(this, AudioService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopService(new Intent(this, AudioService.class));
    }
}