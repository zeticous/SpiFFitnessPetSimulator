package com.orbital.thegame.spiffitnesspetsimulator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OnBootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d("BOOT","ON BOOT DETECTED");
            Intent serviceIntent = new Intent(context, AlarmService.class);
            context.startService(serviceIntent);
        }
    }
}
