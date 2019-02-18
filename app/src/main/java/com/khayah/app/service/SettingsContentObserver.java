package com.khayah.app.service;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

import com.khayah.app.ui.home.MainActivity;


public class SettingsContentObserver extends ContentObserver {
    int previousVolume;
    Context context;
    int count = 0;
    public SettingsContentObserver(Context c, Handler handler) {
        super(handler);
        context=c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        int delta=previousVolume-currentVolume;
        if(delta>0)
        {
            count++;
            Log.i("Volume","Volume is decreased");
            previousVolume=currentVolume;
        }
        else if(delta<0)
        {
            count++;
            Log.i("Volume","Volume is increased");
            previousVolume=currentVolume;
        }

        if(count >= 2) {
            count = 0;
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("alert", true);
            context.startActivity(i);
        }
    }
}