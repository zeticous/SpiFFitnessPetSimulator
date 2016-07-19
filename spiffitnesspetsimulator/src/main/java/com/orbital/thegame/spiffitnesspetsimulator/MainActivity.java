package com.orbital.thegame.spiffitnesspetsimulator;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;

public class MainActivity extends WearableActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final int FACTOR = 200;
    public static final int EGG_REG = 10000;

    public static final int PIG_BABY_REG = 10001;
    public static final int PIG_ADULT_REG = 10002;

    public static final int PENGUIN_BABY_REG = 10003;
    public static final int PENGUIN_ADULT_REG = 10004;

    public static final int PANDA_BABY_REG = 10005;
    public static final int PANDA_ADULT_REG = 10006;

    private GoogleApiClient mGoogleApiClient;
    public static int affinityLevel, stepCount, register, affinityPoint;
    public int animationIdle, animationHappy;
    private AnimationDrawable animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();

        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        sprite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClick();
            }
        });
    }

    public static void updateAffinityPoint(){
        affinityPoint = stepCount/FACTOR - affinityLevel;
    }


    private void handleOnClick(){
        stopAnimation();
        startHappyAnimation();
        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        if (affinityPoint > 0){
            ++affinityLevel;
            updateAffinityPoint();
        }

        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        sendData(affinityLevel, stepCount);

        long delay = 5000;

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnimation();
                startIdleAnimation();
            }
        },delay);
    }

    private void sendData(int affinityLevel, int stepCount){
        PutDataMapRequest req = PutDataMapRequest.create("/data");
        req.getDataMap().putInt("affinityLevel", affinityLevel);
        req.getDataMap().putInt("stepCount", stepCount);
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
        setImage(register);
        updateAffinityPoint();
        startIdleAnimation();

        TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
        TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

        levelCount.setText("" + affinityLevel);
        pointCount.setText("" + affinityPoint);

        mGoogleApiClient.connect();

        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView levelCount = (TextView) findViewById(R.id.watch_level_count);
                                TextView pointCount = (TextView) findViewById(R.id.watch_affinity_point);

                                stopAnimation();
                                startIdleAnimation();

                                levelCount.setText("" + affinityLevel);
                                pointCount.setText("" + affinityPoint);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    Log.e("Thread", "NOT RUNNING THREAD");
                }
            }
        };
        t.start();
    }

    private void startIdleAnimation(){
        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        assert sprite != null;
        sprite.setImageResource(animationIdle);

        animation = (AnimationDrawable) sprite.getDrawable();
        animation.start();
    }

    private void startHappyAnimation(){
        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        assert sprite != null;
        sprite.setImageResource(animationHappy);

        animation = (AnimationDrawable) sprite.getDrawable();
        animation.start();
    }

    private void stopAnimation(){
        ImageView sprite = (ImageView) findViewById(R.id.watch_sprite);
        assert sprite != null;

        animation = (AnimationDrawable) sprite.getDrawable();
        animation.stop();
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

    public void setImage(int register){
        switch(register){
            case EGG_REG:
                animationIdle = R.drawable.egg_idle;
                animationHappy = R.drawable.egg_happy;
                break;
            case PIG_BABY_REG:
                animationIdle = R.drawable.pig_baby_idle;
                animationHappy = R.drawable.pig_baby_happy;
                break;
            case PIG_ADULT_REG:
                animationIdle = R.drawable.pig_adult_idle;
                animationHappy = R.drawable.pig_adult_happy;
                break;
            case PENGUIN_BABY_REG:
                animationIdle = R.drawable.penguin_baby_idle;
                animationHappy = R.drawable.penguin_baby_happy;
                break;
            case PENGUIN_ADULT_REG:
                animationIdle = R.drawable.penguin_adult_idle;
                animationHappy = R.drawable.penguin_adult_happy;
                break;
            case PANDA_BABY_REG:
                animationIdle = R.drawable.panda_baby_idle;
                animationHappy = R.drawable.panda_baby_happy;
                break;
            case PANDA_ADULT_REG:
                animationIdle = R.drawable.panda_adult_idle;
                animationHappy = R.drawable.panda_adult_happy;
                break;
        }
    }
}