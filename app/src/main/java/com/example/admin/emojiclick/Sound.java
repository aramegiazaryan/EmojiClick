package com.example.admin.emojiclick;


import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;

public class Sound {

    private Context context;
    private MediaPlayer soundClick;
    private MediaPlayer soundEffect;
    private MediaPlayer soundGameOver;
    private boolean flagSoundClick = true;
    private boolean flagSoundEffect = true;
    private boolean flagSoundGameOver = true;
    private static boolean flagMute = false;

    public Sound(Context context) {
        this.context = context;
    }

    public void setFlagMute(boolean flagMute) {
        this.flagMute = flagMute;
    }

    public MediaPlayer getSoundClick() {
        if (flagSoundClick) {
            soundClick = MediaPlayer.create(context, R.raw.stapling);
            flagSoundClick = false;
        }
        if (flagMute) {
            return soundClick;
        } else {

            soundClick.start();
            return soundClick;
        }
    }

    public MediaPlayer getSoundEffect() {
        if (flagSoundEffect) {
            soundEffect = MediaPlayer.create(context, R.raw.soundeffect);
            flagSoundEffect = false;
        }
        if (flagMute) {
            return soundClick;
        } else {
            soundEffect.start();
            return soundEffect;
        }
    }

    public MediaPlayer getSoundGameOver() {
        if (flagSoundGameOver) {
            soundGameOver = MediaPlayer.create(context, R.raw.kukushka);
            flagSoundGameOver = false;
        }
        if (flagMute) {
            return soundClick;
        } else {
            soundGameOver.start();
            return soundGameOver;
        }
    }
}
