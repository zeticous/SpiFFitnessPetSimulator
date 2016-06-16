package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Qi Wei on 16/6/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private final String TAG = "AlarmReceiver";

    @Override
    public void onReceive (Context context, Intent intent){
        Log.d(TAG, "STARTED: onReceive");

        GameService reference = new GameService();
        int affinityLevel = GameService.UserSpirit.getAffinityLevel();

        reference.requestGoogleFitSync();
        GameService.UserSpirit.evolveCheck(affinityLevel);

        reference.saveSpirits();
    }

    public void setAlarm(Context context){
        Log.d(TAG, "STARTED: setAlarm");

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60, pendingIntent);
    }
}
