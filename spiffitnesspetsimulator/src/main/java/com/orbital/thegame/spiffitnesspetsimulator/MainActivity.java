package com.orbital.thegame.spiffitnesspetsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static WatchSpirit UserSpirit;

    public static final int EGG_REG = 10000;

    public static final int PIG_BABY_REG = 10001;
    public static final int PIG_ADULT_REG = 10002;

    public static final int PENGUIN_BABY_REG = 10003;
    public static final int PENGUIN_ADULT_REG = 10004;

    public static final int PANDA_BABY_REG = 10005;
    public static final int PANDA_ADULT_REG = 10006;

    private GoogleApiClient mGoogleApiClient;
    public static int affinityLevel = 0, affinityPoint = 0, register = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(AppIndex.API)
                .build();

        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClick();
            }
        });
    }

    /*private void handleFirstLaunch() {
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (settings.getBoolean("firstLaunch", true)) {
            Log.d("Settings", "First Launch Detected");
            UserSpirit = new WatchSpirit();
            //saveSpirit();

            settings.edit().putBoolean("firstLaunch", false).apply();
        } else {
            loadSpirit();
        }
    }

    public void saveSpirit(){
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);

        settings.edit().putInt("affinityPoint", 0).apply();
        settings.edit().putInt("affinityLevel", 0).apply();
        settings.edit().putInt("animation_happy", 0).apply();
        settings.edit().putInt("animation_idle", 0).apply();
    }

    public void loadSpirit(){
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        UserSpirit = new WatchSpirit();

        int affinityLevel = settings.getInt("affinityLevel", -99);
        int affinityPoint = settings.getInt("affinityPoint", -99);

        UserSpirit.setAffinityLevel(affinityLevel);
        UserSpirit.setAffinityPoint(affinityPoint);
    }*/

    private void handleOnClick(){
        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        affinityLevel += affinityPoint;
        affinityPoint = 0;

        //UserSpirit.setAffinityLevel(affinityLevel);
        //UserSpirit.setAffinityPoint(affinityPoint);

        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        sendData(affinityLevel, affinityPoint);
        //saveSpirit();
    }

    private void sendData(int affinityLevel, int affinityPoint){
        PutDataMapRequest req = PutDataMapRequest.create("/data");
        req.getDataMap().putInt("affinityLevel", affinityLevel);
        req.getDataMap().putInt("affinityPoint", affinityPoint);
        req.getDataMap().putLong("time", new Date().getTime());

        PutDataRequest putDataRequest = req.asPutDataRequest();
        putDataRequest.setUrgent();

        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(mGoogleApiClient,putDataRequest);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.e("AndroidWear", "Result sent");
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    protected void onResume(){
        super.onResume();
        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnectionSuspended (int cause){
        Log.e("AndroidWear", "CAUSE: "+cause);
    }

    @Override
    public void onConnectionFailed (@NonNull ConnectionResult result){
        Log.e("AndroidWear", "CONNECTION FAILED. REASON: " + result);
    }
}