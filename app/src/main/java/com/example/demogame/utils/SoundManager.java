package com.example.demogame.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;

    private SoundManager() {
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void playSound(Context context, int soundResId, boolean loop) {
        stopSound();
        try {
            mediaPlayer = MediaPlayer.create(context, soundResId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(loop);
                mediaPlayer.start();
            }
        } catch (Exception e) {
            // Sound file not found or error playing, continue without sound
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void pauseSound() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resumeSound() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}
