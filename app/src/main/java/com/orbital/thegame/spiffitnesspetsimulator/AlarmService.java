package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class AlarmService extends Service {
    private final static String LOG = "GameService";

    public final static String ALARM_RECEIVE = "com.orbital.thegame.spiffitnesspetsimulator.gameservice.alarmreceiver";
    private IntentFilter intentFilter = new IntentFilter(ALARM_RECEIVE);
    public static final String TITLE = "SpiF";
    private String message = "SpiF is running.";
    private Notification notification;

    AlarmReceiver alarmManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "STARTED: onStartCommand");
        alarmManager = new AlarmReceiver();
        registerReceiver(alarmManager, intentFilter);
        alarmManager.setAlarm(this);

       notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(TITLE)
                .setContentText(message)
                .build();

        startForeground(233223, notification);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class AlarmReceiver extends BroadcastReceiver {
        private final String TAG = "AlarmReceiver";

        @Override
        public void onReceive (Context context, Intent intent){
            startGameService();
        }

        public void setAlarm(Context context){
            Log.d(TAG, "STARTED: setAlarm");

            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent (ALARM_RECEIVE);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,intent,0);
            alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*10, pendingIntent);
        }
    }

    private void startGameService(){
        Intent service = new Intent(this, GameService.class);
        startService(service);
    }
}
