package com.sambhav2358.backgroundaudioplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

public class AudioPlayerActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getStringExtra("action")){
            case "s":
                if (AudioService.audioPlayer == null) {
                    AudioService.audioPlayer = MediaPlayer.create(context, R.raw.sample);
                }
                AudioService.audioPlayer.start();
                break;
            case "p":
                AudioService.audioPlayer.pause();
                break;
            case "r":
                if (AudioService.audioPlayer != null && AudioService.audioPlayer.isPlaying()) {
                    AudioService.audioPlayer.stop();
                    AudioService.audioPlayer = null;
                }
                break;
            default:
                Log.d("AudioPlayerActionReceiver", "onReceive: This case never comes");
        }
    }
}
