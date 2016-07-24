package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Date;


public class MainActivity extends AppCompatActivity{
    int stepCount, affinityLevel, affinityPoint;

    public final static String PTAG = "SharedPref";
    public static final String MY_PREFS_NAME = "GameSaveFile";

    private AnimationDrawable animation_idle, animation_happy;
    private boolean happyAnimationRunning = false;

    GoogleApiClient mGoogleWearClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestConnection();

        try {
            stepCount = GameService.UserSpirit.getStepCount();
            affinityLevel = GameService.UserSpirit.getAffinityLevel();
            affinityPoint = GameService.UserSpirit.getAffinityPoint();
        } catch (NullPointerException e) {
            loadSpirits();
        }
        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);

        Typeface font = Typeface.createFromAsset(getAssets(), "DKBlueSheep.ttf");

        final TextView levelCount = (TextView) findViewById(R.id.level_count);
        final TextView affinityPointCount = (TextView) findViewById(R.id.experience);

        assert levelCount != null;
        assert affinityPointCount != null;

        levelCount.setTypeface(font);
        affinityPointCount.setTypeface(font);

        setText();

        assert sprite != null;
        sprite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                affinityLevel = GameService.UserSpirit.getAffinityLevel();
                affinityPoint = GameService.UserSpirit.getAffinityPoint();

                if (affinityPoint > 0) {
                    GameService.UserSpirit.setAffinityLevel(++affinityLevel);
                    GameService.UserSpirit.setAffinityPoint(--affinityPoint);
                    saveSpirits();
                    requestWearConnection();

                    /*Log.d(TAG,"START TIME IN LONG: "+ GameService.UserSpirit.getStartTime());
                    Log.d(TAG,"END TIME IN LONG: "+GameService.UserSpirit.getEndTime());*/
                }

                if (!happyAnimationRunning) {
                    startHappyAnimation();

                    long delay = 5000;
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            happyAnimationRunning = false;
                            stopAnimation();
                            startIdleAnimation();
                        }
                    },delay);
                }

                setText();

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
        });

        assert menu != null;
        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        assert help != null;
        help.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        /*LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        requestConnection();*/

        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    private void requestConnection(){
        Log.d("MainActivity","STARTED: requestConnection");
        Intent service = new Intent(this, GoogleFitSync.class);
        service.putExtra(GoogleFitSync.TYPE_REQUEST_CONNECTION, GoogleFitSync.TYPE_TRUE);
        service.putExtra(GoogleFitSync.TYPE_GET_STEP_DATA, GoogleFitSync.TYPE_TRUE);
        startService(service);
    }

    @Override
    public void onResume(){
        super.onResume();

        GameService.updateAffinityPoint();
        startIdleAnimation();
        checkTutorial();

        // This portion of the code updates the MainActivity every second.
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final TextView levelCount = (TextView) findViewById(R.id.level_count);
                                final TextView affinityPointCount = (TextView) findViewById(R.id.experience);

                                int affinityLevel = GameService.UserSpirit.getAffinityLevel();
                                int affinityPoint = GameService.UserSpirit.getAffinityPoint();

                                try{
                                    checkTutorial();
                                } catch(IllegalStateException e){
                                    Log.e("Tutorial","IllegalStateException due to no running activity");
                                }

                                if (GameService.UserSpirit.isJustEvolved()){
                                    stopAnimation();
                                    startIdleAnimation();
                                    GameService.UserSpirit.setJustEvolved(false);
                                }

                                assert levelCount != null;
                                assert affinityPointCount != null;

                                setText();
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

    @Override
    public void onPause(){
        super.onPause();
        saveSpirits();
    }

    private void checkTutorial(){
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);

        if(!settings.getBoolean("tutorial1",false)){
            Log.d("Settings", "Start Tutorial 1");
            FragmentManager fm = getFragmentManager();
            NavigationTutorial navigationTutorial = new NavigationTutorial();
            navigationTutorial.setRetainInstance(true);
            navigationTutorial.show(fm, "fragment_name");
            settings.edit().putBoolean("tutorial1", true).apply();
        }

        if (settings.getBoolean("firstBaby", false) && !settings.getBoolean("tutorial2", false)){
            // START TUTORIAL 2
            Log.d("Settings", "First Baby Detected");
            FragmentManager fm = getFragmentManager();
            BabyStageTutorial babyStageTutorial = new BabyStageTutorial();
            babyStageTutorial.setRetainInstance(true);
            babyStageTutorial.show(fm, "fragment_name");
            settings.edit().putBoolean("tutorial2", true).apply();
        }

        if (settings.getBoolean("firstAdult", false) && !settings.getBoolean("tutorial3", false)){
            // START TUTORIAL 3
            Log.d("Settings", "First Adult Detected");
            FragmentManager fm = getFragmentManager();
            ReleaseTutorial releaseTutorial = new ReleaseTutorial();
            releaseTutorial.setRetainInstance(true);
            releaseTutorial.show(fm, "fragment_name");
            settings.edit().putBoolean("tutorial3", true).apply();
        }
    }

    private void startIdleAnimation(){
        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        assert sprite != null;
        sprite.setImageResource(GameService.UserSpirit.getAnimation_idle());

        animation_idle = (AnimationDrawable) sprite.getDrawable();
        animation_idle.start();
    }

    private void startHappyAnimation(){
        happyAnimationRunning = true;
        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        assert sprite != null;
        sprite.setImageResource(GameService.UserSpirit.getAnimation_happy());

        animation_happy = (AnimationDrawable) sprite.getDrawable();
        animation_happy.start();
    }

    private void stopAnimation(){
        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        assert sprite != null;

        animation_idle = (AnimationDrawable) sprite.getDrawable();
        animation_idle.stop();
    }

    private void setText(){
        affinityLevel = GameService.UserSpirit.getAffinityLevel();
        affinityPoint = GameService.UserSpirit.getAffinityPoint();

        if(affinityPoint < 0) {
            loadSpirits();
            setText();
        }

        final TextView levelCount = (TextView) findViewById(R.id.level_count);
        final TextView affinityPointCount = (TextView) findViewById(R.id.experience);

        assert levelCount != null;
        assert affinityPointCount != null;

        levelCount.setText(""+ affinityLevel);
        affinityPointCount.setText(""+affinityPoint);
    }

    // This portion of the code is for saving and loading of game data.
    public void saveSpirits(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("stepCount", GameService.UserSpirit.getStepCount());
        editor.putInt("register", GameService.UserSpirit.getRegister());
        editor.putInt("affinityLevel", GameService.UserSpirit.getAffinityLevel());
        editor.putInt("affinityPoint", GameService.UserSpirit.getAffinityPoint());

        editor.putLong("startTime", GameService.UserSpirit.getStartTime());
        editor.putLong("endTime", GameService.UserSpirit.getEndTime());

        editor.apply();
        Log.d(PTAG, "Shared Preference saved");
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
                        Log.d(WEAR, "onConnectionFailed: " + connectionResult);
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
        int restoredAffinityPoint = prefs.getInt("affinityPoint", -99);

        PutDataMapRequest req = PutDataMapRequest.create("/phoneData");
        req.getDataMap().putInt("register", restoredRegister);
        req.getDataMap().putInt("affinityLevel", restoredAffinityLevel);
        req.getDataMap().putInt("affinityPoint", restoredAffinityPoint);
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

    // This portion of the code is for double tap back to exit.
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            this.finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
