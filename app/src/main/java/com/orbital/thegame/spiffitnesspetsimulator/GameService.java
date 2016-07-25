package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class GameService extends Service {
    public static Spirits UserSpirit;
    int stepCount;
    public static final int FACTOR = 200;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private final int mId = 1314520;

    public final static String TAG = "GoogleFitService";
    public final static String PTAG = "SharedPref";

    GoogleApiClient mGoogleWearClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (UserSpirit == null) {
            loadSpirits();
        }

        updateAffinityPoint();
        int affinityLevel = UserSpirit.getAffinityLevel();

        if (UserSpirit.evolveCheck(affinityLevel)) {
            String nameBefore, nameAfter, message;
            nameBefore = UserSpirit.getName();
            UserSpirit = UserSpirit.evolve(affinityLevel);
            nameAfter = UserSpirit.getName();

            if (UserSpirit.getRegister() == Spirits.PANDA_ADULT_REG
                    || UserSpirit.getRegister() == Spirits.PENGUIN_ADULT_REG
                    || UserSpirit.getRegister() == Spirits.PIG_ADULT_REG) {
                SharedPreferences settings = getSharedPreferences("GameSettings", 0);
                settings.edit().putBoolean("firstAdult", true).apply();
            } else {
                SharedPreferences settings = getSharedPreferences("GameSettings", 0);
                settings.edit().putBoolean("firstBaby", true).apply();
            }

            register();

            //NOTIFICATION FOR EVOLUTION
            message = "Your " + nameBefore + " has evolved to " + nameAfter + "!!";
            sendNotification(message);
            UserSpirit.setJustEvolved(true);
        }

        requestGoogleFitSync();

        if (UserSpirit.runCheck()) {
            String name = UserSpirit.getName();
            UserSpirit = UserSpirit.initialise();

            //NOTIFICATION FOR RUNNING AWAY
            String message = "Your " + name + " has ran away due to neglect.";
            sendNotification(message);
        }

        saveSpirits();
        requestWearConnection();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static final String TITLE = "SpiF";

    private void sendNotification(String message){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.watch_background);
        NotificationCompat.WearableExtender wearableExtender
                = new NotificationCompat.WearableExtender().setHintHideIcon(true).setBackground(bitmap);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(TITLE)
                .setContentText(message)
                .setAutoCancel(true)
                .extend(wearableExtender);

        Intent resultIntent = new Intent(this, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(getApplicationContext());
        mNotificationManager.notify(mId, mBuilder.build());
    }

    public static void updateAffinityPoint(){
        int affinityLevel = UserSpirit.getAffinityLevel();
        int stepCount = UserSpirit.getStepCount();
        int affinityPoint = stepCount/FACTOR - affinityLevel;

        /*Log.d("GAMESERVICE", "AffinityLevel: " + affinityLevel);
        Log.d("GAMESERVICE", "StepCount: " + stepCount);
        Log.d("GAMESERVICE", "AffinityPoint: " + affinityPoint);*/

        UserSpirit.setAffinityPoint(affinityPoint);
    }

    private void register(){
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        switch(UserSpirit.getRegister()){
            case Spirits.PIG_BABY_REG:
                settings.edit().putBoolean("pigBaby", true).apply();
                break;
            case Spirits.PENGUIN_BABY_REG:
                settings.edit().putBoolean("penguinBaby", true).apply();
                break;
            case Spirits.PANDA_BABY_REG:
                settings.edit().putBoolean("pandaBaby", true).apply();
                break;
            case Spirits.PIG_ADULT_REG:
                settings.edit().putBoolean("pigAdult", true).apply();
                break;
            case Spirits.PENGUIN_ADULT_REG:
                settings.edit().putBoolean("penguinAdult", true).apply();
                break;
            case Spirits.PANDA_ADULT_REG:
                settings.edit().putBoolean("pandaAdult", true).apply();
                break;
        }
    }

    public void requestGoogleFitSync() {
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(GoogleFitSync.STEP_COUNT));

        Log.e(TAG, "Executing requestFitConnection...");
        Intent service = new Intent(this, GoogleFitSync.class);
        service.putExtra(GoogleFitSync.TYPE_REQUEST_CONNECTION, GoogleFitSync.TYPE_TRUE);
        service.putExtra(GoogleFitSync.TYPE_GET_STEP_DATA, GoogleFitSync.TYPE_TRUE);
        service.putExtra("spiritStartTime", UserSpirit.getStartTime());
        service.putExtra("spiritEndTime", UserSpirit.getEndTime());
        startService(service);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Executing Connection onReceive");
            if (intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE) &&
                    intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE)) {
                Log.e(TAG, "Unable to Connect to Google Fit. Please start the application again.");
            }
            if (intent.hasExtra(GoogleFitSync.FIT_EXTRA_CONNECTION_MESSAGE)) {
                Log.d(TAG, "Fit connection successful - closing connect screen if it's open.");
                fitHandleConnection();
            }
            if (intent.hasExtra(GoogleFitSync.STEP_COUNT)) {
                stepCount = intent.getIntExtra(GoogleFitSync.STEP_COUNT, 0);
                //Log.e(TAG, "Broadcast Value: " + stepCount);
                UserSpirit.setStepCount(stepCount);
                saveSpirits();
            }
        }
    };

    private void fitHandleConnection() {
        Log.e(TAG, "Fit connected");
    }

    public void saveSpirits(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("stepCount", UserSpirit.getStepCount());
        editor.putInt("register", UserSpirit.getRegister());
        editor.putInt("affinityLevel", UserSpirit.getAffinityLevel());
        editor.putInt("affinityPoint", UserSpirit.getAffinityPoint());

        editor.putLong("startTime", UserSpirit.getStartTime());
        editor.putLong("endTime", UserSpirit.getEndTime());

        editor.apply();
        Log.d(PTAG, "Shared Preference saved");
    }

    private static final String WEAR = "AndroidWear";

    public void requestWearConnection(){
        mGoogleWearClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        Log.d(WEAR,"onConnected: "+bundle);
                        sendWearData();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        Log.d(WEAR, "onConnectionSuspended: " + i);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "onConnectionFailed: " + connectionResult);
                    }
                })
                .build();
        mGoogleWearClient.connect();

        sendWearData();
    }

    public void sendWearData(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int restoredRegister = prefs.getInt("register", -99999);
        int restoredAffinityLevel = prefs.getInt("affinityLevel", -99);
        int restoredStepCount = prefs.getInt("stepCount", -99);
        int restoredAffinityPoint = (restoredStepCount/FACTOR) - restoredAffinityLevel;

        PutDataMapRequest req = PutDataMapRequest.create("/data");
        req.getDataMap().putInt("register", restoredRegister);
        req.getDataMap().putInt("affinityLevel", restoredAffinityLevel);
        req.getDataMap().putInt("affinityPoint", restoredAffinityPoint);
        req.getDataMap().putInt("stepCount", restoredStepCount);
        req.getDataMap().putLong("time", new Date().getTime());

        PutDataRequest putDataRequest = req.asPutDataRequest();
        putDataRequest.setUrgent();

        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleWearClient,putDataRequest);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.e("AndroidWear", "Result sent");
            }
        });
    }

    public void loadSpirits(){
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        int restoredRegister = prefs.getInt("register", -99999);
        int restoredStepCount = prefs.getInt("stepCount", -99999);
        int restoredAffinityLevel = prefs.getInt("affinityLevel", -99);
        long restoredStartTime = prefs.getLong("startTime", -9999);
        long restoredEndTime = prefs.getLong("endTime", -9999);

        Log.d(PTAG, "" + restoredRegister);
        Log.d(PTAG, "" + restoredStepCount);
        Log.d(PTAG, "" + restoredStartTime);
        Log.d(PTAG, "" + restoredEndTime);

        switch(restoredRegister){
            case Spirits.EGG_REG:
                GameService.UserSpirit = new Egg(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "EGG load successful");
                break;
            case Spirits.PIG_BABY_REG:
                GameService.UserSpirit = new Pig_Baby(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PIG_BABY load successful");
                break;
            case Spirits.PENGUIN_BABY_REG:
                GameService.UserSpirit = new Penguin_Baby(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PENGUIN_BABY load successful");
                break;
            case Spirits.PANDA_BABY_REG:
                GameService.UserSpirit = new Panda_Baby(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PANDA_BABY load successful");
                break;
            case Spirits.PIG_ADULT_REG:
                GameService.UserSpirit = new Pig_Adult(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PIG_ADULT load successful");
                break;
            case Spirits.PENGUIN_ADULT_REG:
                GameService.UserSpirit = new Penguin_Adult(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PENGUIN_ADULT load successful");
                break;
            case Spirits.PANDA_ADULT_REG:
                GameService.UserSpirit = new Panda_Adult(restoredStepCount, restoredStartTime, restoredEndTime, restoredAffinityLevel);
                Log.d(PTAG, "PANDA_ADULT load successful");
                break;
            default:
                Log.d(PTAG, "No spirit detected");
        }
    }
}
