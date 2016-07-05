package com.orbital.thegame.spiffitnesspetsimulator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity implements DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    public static final int EGG_REG = 10000;

    public static final int PIG_BABY_REG = 10001;
    public static final int PIG_ADULT_REG = 10002;

    public static final int PENGUIN_BABY_REG = 10003;
    public static final int PENGUIN_ADULT_REG = 10004;

    public static final int PANDA_BABY_REG = 10005;
    public static final int PANDA_ADULT_REG = 10006;

    private GoogleApiClient mGoogleApiClient;
    private static int register, affinityPoint, affinityLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        loadImage();
        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClick();
            }
        });
    }

    private void loadImage(){
        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        switch(register){
            case EGG_REG:
                sprite.setImageResource(R.drawable.watch_egg_idle1);

        }
    }

    private void handleOnClick(){
        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        affinityLevel += affinityPoint;
        affinityPoint = 0;

        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        sendData(affinityLevel, affinityPoint);
    }

    private void sendData(int affinityLevel, int affinityPoint){
        PutDataMapRequest req = PutDataMapRequest.create("/data");
        req.getDataMap().putInt("affinityLevel", affinityLevel);
        req.getDataMap().putInt("affinityPoint", affinityPoint);

        PutDataRequest putDataRequest = req.asPutDataRequest();
        putDataRequest.setUrgent();

        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(mGoogleApiClient,putDataRequest);

        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(@NonNull DataApi.DataItemResult dataItemResult) {
                Log.e("AndroidWear", "Result sent");
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.DataApi.removeListener(mGoogleApiClient, this);
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        for (DataEvent event : dataEvents){
            if (event.getType() == DataEvent.TYPE_CHANGED){
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/dataStream")==0){
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    setUpData(dataMap);
                }
            }
            else if (event.getType() == DataEvent.TYPE_DELETED){
                Log.e("AndroidWear", "DataEvent DELETED");
            }
        }
    }

    private void setUpData(DataMap dataMap){
        register = dataMap.getInt("register");
        affinityLevel = dataMap.getInt("affinityLevel");
        affinityPoint = dataMap.getInt("affinityPoint");
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