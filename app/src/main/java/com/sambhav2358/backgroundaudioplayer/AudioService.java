package com.sambhav2358.backgroundaudioplayer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Random;

public class AudioService extends Service {
    
    private final String TAG = "AudioService";
    private boolean isFirstTime = true;
    NotificationManager mNotificationManager;
    private final String CHANNEL_ID = "Channel_id";
    NotificationCompat.Builder timerNotificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID).setContentTitle(CHANNEL_ID);
    public static MediaPlayer audioPlayer;

    Handler handler;
    Runnable runnable;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: service started");
        startForeground(1, new NotificationCompat.Builder(this, createChannel()).setContentTitle("Music Player is playing").setPriority(NotificationManager.IMPORTANCE_HIGH).build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: service code started");

        audioPlayer = MediaPlayer.create(this, R.raw.sample);

        handler = new Handler();

        runnable = () -> {
            Log.d(TAG, "onStartCommand: updated again");
            updateNotification();
            handler.postDelayed(runnable, 1000);
        };

        handler.postDelayed(runnable, 1000);
        
        return START_STICKY;
    }

    @SuppressLint("NewApi")
    public void updateNotification() {
        if (isFirstTime) {
            Intent notificationIntent = new Intent(this, MainActivity.class);
            @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            timerNotificationBuilder.setContentTitle("Audio Controls")
                    .setOngoing(true)
                    .setContentIntent(pendingIntent)
                    .setOnlyAlertOnce(true)
                    .setOngoing(true)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true)
                    .addAction(createNotificationActionButton("START", "s"))
                    .addAction(createNotificationActionButton("PAUSE", "p"))
                    .addAction(createNotificationActionButton("RESTART", "r"))
                    .setOngoing(true);
            isFirstTime = false;
        }

        Log.d(TAG, "updateNotification: duration = " + audioPlayer.getDuration() + "    :::::::    " + "elapsed = " + audioPlayer.getCurrentPosition());

        timerNotificationBuilder.setProgress(audioPlayer.getDuration(), audioPlayer.getCurrentPosition(), false);

        startForeground(1, timerNotificationBuilder.build());
    }

    public NotificationCompat.Action createNotificationActionButton(String text, String actionName){

        Intent intent = new Intent(this, AudioPlayerActionReceiver.class).putExtra("action", actionName);
        @SuppressLint("InlinedApi") PendingIntent pendingIntent = PendingIntent.getBroadcast(this, new Random().nextInt(100), intent, PendingIntent.FLAG_MUTABLE);

        return new NotificationCompat.Action(0, text, pendingIntent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "STOPWATCH";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        mChannel.setName("Notifications");

        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }

        return CHANNEL_ID;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        audioPlayer.stop();
        audioPlayer.release();
        audioPlayer = null;

        stopSelf();
    }
}
