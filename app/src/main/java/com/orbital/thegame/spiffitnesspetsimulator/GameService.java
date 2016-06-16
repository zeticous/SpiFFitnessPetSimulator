package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class GameService extends Service {
    private final static String LOG = "GameService";
    public static Spirits UserSpirit;
    int stepCount;

    private JSONSerializer mSerializer;

    public final static String TAG = "GoogleFitService";
    boolean authInProgress = false;

    AlarmReceiver alarmManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "STARTED: onStartCommand");
        alarmManager = new AlarmReceiver();
        alarmManager.setAlarm(this);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void requestGoogleFitSync() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(GoogleFitSync.STEP_COUNT));

        Log.e(TAG, "Executing requestFitConnection...");
        Intent service = new Intent(this, GoogleFitSync.class);
        service.putExtra(GoogleFitSync.TYPE_REQUEST_CONNECTION, GoogleFitSync.TYPE_TRUE);
        service.putExtra(GoogleFitSync.TYPE_GET_STEP_DATA, GoogleFitSync.TYPE_TRUE);
        startService(service);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Executing Connection onReceive");
            if (intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE) &&
                    intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE)) {
                int errorCode = intent.getIntExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE, 0);
                PendingIntent pendingIntent = intent.getParcelableExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_INTENT);

                Intent resolution = new Intent();
                intent.putExtra("PENDING_INTENT", pendingIntent);
                intent.putExtra("ERROR_CODE", errorCode);
                intent.putExtra("REQUEST_RESOLUTION", true);
                Log.d(TAG, "Fit connection failed - opening connect screen.");

                sendBroadcast(resolution);
            }
            if (intent.hasExtra(GoogleFitSync.FIT_EXTRA_CONNECTION_MESSAGE)) {
                Log.d(TAG, "Fit connection successful - closing connect screen if it's open.");
                fitHandleConnection();
            }
            if (intent.hasExtra(GoogleFitSync.STEP_COUNT)) {
                stepCount = intent.getIntExtra(GoogleFitSync.STEP_COUNT, 0);
                Log.e(TAG, "Broadcast Value: " + stepCount);
                UserSpirit.setStepCount(stepCount);
            }
        }
    };

    private void fitHandleConnection() {
        Log.e(TAG, "Fit connected");
    }

    public void saveSpirits(){
        try{
            mSerializer.save(UserSpirit);
            Log.e("JSON", "Saved successfully");
        }catch(Exception e){
            Log.e("JSON", "Error Saving notes");
        }
    }
}
