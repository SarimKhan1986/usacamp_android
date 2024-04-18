package com.usacamp.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PowerButtonListenr extends BroadcastReceiver {
    private static final String TAG = "ActivityStatus";
    private static int countPowerOff = 0;
    private boolean screenOff;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(TAG, "onReceive: " + "I am in bad situation");
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            countPowerOff++;
            screenOff = true;
            Log.w(TAG, "onReceive: " + countPowerOff);
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {

            screenOff = false;

        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");

        }
        Intent i = new Intent(context, ScreenOnOffService.class);
        i.putExtra("screen_state", screenOff);
        context.startService(i);
    }


}
