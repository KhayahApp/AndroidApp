package com.khayah.app.service;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.khayah.app.ui.home.MainActivity;

public class PowerButtonReceiver extends BroadcastReceiver {
    static int countPowerOff = 0;
    private Activity activity = null;

    public PowerButtonReceiver(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.v("onReceive", "Power button is pressed.");

        Toast.makeText(context, countPowerOff +" Power button clicked", Toast.LENGTH_LONG).show();

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            countPowerOff++;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (countPowerOff > 2) {
                Intent i = new Intent(activity, MainActivity.class);
                i.putExtra("alert", true);
                activity.startActivity(i);
            }
        }

    }
}