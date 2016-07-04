package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.fitness.FitnessStatusCodes;


public class MainActivity extends AppCompatActivity{
    boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 1337;
    public final static String TAG = "GoogleFitService";
    int stepCount, affinityLevel, affinityPoint;

    public final static String PTAG = "SharedPref";
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    private AnimationDrawable animation_idle, animation_happy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity", "onCreate started");
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);

        if (settings.getBoolean("firstLaunch", true)) {
            Log.d("Settings", "First Launch Detected");
            GameService.UserSpirit = new Egg();

            //Fragment open up when first launch
            FragmentManager fm = getFragmentManager();
            NavigationTutorial navigationTutorial = new NavigationTutorial();
            navigationTutorial.setRetainInstance(true);
            navigationTutorial.show(fm, "fragment_name");


            if (GameService.UserSpirit.getRegister() == Spirits.EGG_REG) {
                Log.e("FirstLaunch", "UserSpirit successfully created");
                saveSpirits();
            }

            settings.edit().putBoolean("firstLaunch", false).apply();
        } else {
            loadSpirits();
        }

        stepCount = GameService.UserSpirit.getStepCount();
        affinityLevel = GameService.UserSpirit.getAffinityLevel();
        affinityPoint = GameService.UserSpirit.getAffinityPoint();

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);
        final TextView affinityPointCount = (TextView) findViewById(R.id.experience);

        changeText(levelCount, "" + affinityLevel);
        changeText(affinityPointCount, "" + affinityPoint);

        assert sprite != null;
        sprite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                affinityLevel = GameService.UserSpirit.getAffinityLevel();
                affinityPoint = GameService.UserSpirit.getAffinityPoint();

                if (affinityPoint > 0) {
                    GameService.UserSpirit.setAffinityLevel(++affinityLevel);
                    GameService.UserSpirit.setAffinityPoint(--affinityPoint);

                    /*Log.d(TAG,"START TIME IN LONG: "+ GameService.UserSpirit.getStartTime());
                    Log.d(TAG,"END TIME IN LONG: "+GameService.UserSpirit.getEndTime());*/
                }

                stopAnimation();
                startHappyAnimation();

                changeText(levelCount, "" + GameService.UserSpirit.getAffinityLevel());
                changeText(affinityPointCount, "" + GameService.UserSpirit.getAffinityPoint());

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

        assert record != null;
        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
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

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        requestConnection();

        Intent intent = new Intent(this, AlarmService.class);
        startService(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
        startIdleAnimation();

        // This portion of the code updates the MainActivity every second.
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GameService.updateAffinityPoint();

                                final TextView levelCount = (TextView) findViewById(R.id.level_count);
                                final TextView affinityPointCount = (TextView) findViewById(R.id.experience);

                                int affinityLevel = GameService.UserSpirit.getAffinityLevel();
                                int affinityPoint = GameService.UserSpirit.getAffinityPoint();

                                checkTutorial();

                                startIdleAnimation();

                                changeText(levelCount, ""+ affinityLevel);
                                changeText(affinityPointCount, "" + affinityPoint);
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

    private void checkTutorial(){
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);

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

    private void changeText(TextView view, String string){
        view.setText(string);
    }

    // This portion of the code is for Google Fit Connection
    private void requestConnection(){
        Log.d("MainActivity","STARTED: requestConnection");
        Intent service = new Intent(this, GoogleFitSync.class);
        service.putExtra(GoogleFitSync.TYPE_REQUEST_CONNECTION, GoogleFitSync.TYPE_TRUE);
        service.putExtra(GoogleFitSync.TYPE_GET_STEP_DATA, GoogleFitSync.TYPE_TRUE);
        startService(service);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "Executing Connection onReceive");
            if(intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE)&&
                    intent.hasExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE)){
                int errorCode = intent.getIntExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE, 0);
                PendingIntent pendingIntent = intent.getParcelableExtra(GoogleFitSync.FIT_EXTRA_NOTIFY_FAILED_INTENT);

                ConnectionResult result = new ConnectionResult(errorCode, pendingIntent);
                Log.d(TAG, "Fit connection failed - opening connect screen.");
                fitHandleFailedConnection(result);
            }

            if(intent.hasExtra(GoogleFitSync.FIT_EXTRA_CONNECTION_MESSAGE)){
                Log.d(TAG, "Fit connection successful - closing connect screen if it's open.");
                fitHandleConnection();
            }
        }
    };

    private void fitHandleConnection(){
        Log.e(TAG, "Fit connected");
    }

    private void fitHandleFailedConnection(ConnectionResult result) {
        Log.i(TAG, "Google Fit Connection failed. Cause: " + result.toString());
        if (!result.hasResolution()) {
            Log.e(TAG, "Google Fit connection failed not due to false hasResolution");
            return;
        }
        if (!authInProgress) {
            if (result.getErrorCode() == FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS) {
                try {
                    Log.d(TAG, "Google Fit connection failed with OAuth failure. trying to ask for consent(again)");
                    result.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Activity Thread Google Fit Exception while starting resolution activity", e);
                }
            } else {
                try {
                    Log.i(TAG, "Activity thread Google Fit Attempting to resolve failed connection");
                    result.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Activity Thread Google Fit Exception while starting resolution activity", e);
                }
            }
        }
    }

    // This portion of the code is for saving and loading of game data.
    public void saveSpirits(){
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("spiritName", GameService.UserSpirit.getName());
        editor.putInt("stepCount", GameService.UserSpirit.getStepCount());

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
