package com.example.demogame.utils;

import android.content.Context;
import android.media.MediaPlayer;

public class SoundManager {

    private static SoundManager instance;

    private MediaPlayer bgmPlayer;
    private MediaPlayer sfxPlayer;

    private int currentBgmResId = -1;

    private SoundManager() {}

    public static synchronized SoundManager getInstance() {
        if (instance == null) instance = new SoundManager();
        return instance;
    }

    public void playBgm(Context context, int resId, boolean loop) {
        if (bgmPlayer != null && currentBgmResId == resId) return;

        stopBgm();
        bgmPlayer = MediaPlayer.create(context.getApplicationContext(), resId);
        currentBgmResId = resId;

        if (bgmPlayer != null) {
            bgmPlayer.setLooping(loop);
            bgmPlayer.start();
        }
    }

    public void stopBgm() {
        if (bgmPlayer != null) {
            bgmPlayer.release();
            bgmPlayer = null;
            currentBgmResId = -1;
        }
    }

    public void playSfx(Context context, int resId) {
        stopSfx();
        sfxPlayer = MediaPlayer.create(context.getApplicationContext(), resId);
        if (sfxPlayer != null) sfxPlayer.start();
    }

    public void stopSfx() {
        if (sfxPlayer != null) {
            sfxPlayer.release();
            sfxPlayer = null;
        }
    }

    public void stopAll() {
        stopBgm();
        stopSfx();
    }
}

