package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.fitness.FitnessStatusCodes;


public class MainActivity extends AppCompatActivity{

    private JSONSerializer mSerializer;
    private boolean authInProgress = false;
    public static final int FACTOR = 1000;
    public static final int REQUEST_OAUTH = 1337;
    public final static String TAG = "GoogleFitService";
    int stepCount = 0;
    int affinityLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSerializer = new JSONSerializer("Spirits.json", MainActivity.this.getApplicationContext());

        Log.d("MainActivity", "onCreate started");
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (settings.getBoolean("firstLaunch", true)) {
            Log.d("Settings", "First Launch Detected");
            GameService.UserSpirit = new Egg();

            if (GameService.UserSpirit != null) {
                Log.e("FirstLaunch", "UserSpirit successfully created");
                saveSpirits();
            }

            settings.edit().putBoolean("firstLaunch", false).apply();
        } else {
            loadSpirits();
        }

        Spirits SpiritInstance = GameService.UserSpirit;
        stepCount = SpiritInstance.getStepCount();
        affinityLevel = SpiritInstance.getAffinityLevel();

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);

        updateAffinityPoint(stepCount, affinityLevel);

        changeImage(sprite, SpiritInstance.image_idle1);

        sprite.setOnClickListener(new View.OnClickListener() {
            int stepCount = GameService.UserSpirit.getStepCount();
            int affinityLevel = GameService.UserSpirit.getAffinityLevel();

            public void onClick(View v) {
                if ((stepCount - affinityLevel * FACTOR) > FACTOR) {
                    affinityLevel++;
                }
                GameService.UserSpirit.setAffinityLevel(affinityLevel);
                changeText(levelCount, "" + affinityLevel);

                updateAffinityPoint(stepCount, affinityLevel);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        requestConnection();
    }

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


    private void updateAffinityPoint(int stepCount, int affinityLevel){
        int affinityPoint = (stepCount - FACTOR*affinityLevel)/FACTOR;
        TextView txt = (TextView) findViewById(R.id.experience);

        changeText(txt, ""+affinityPoint);
    }

    private void changeImage(ImageView view, int drawable){
        view.setImageResource(drawable);
    }

    private void changeText(TextView view, String string){
        view.setText(string);
    }

    public void saveSpirits(){
        try{
            mSerializer.save(GameService.UserSpirit);
            Log.e("JSON", "Saved successfully");
        }catch(Exception e){
            Log.e("JSON", "Error Saving notes");
        }
    }

    public void loadSpirits(){
        mSerializer = new JSONSerializer("Spirits.json", MainActivity.this.getApplicationContext());
        try{
            GameService.UserSpirit = mSerializer.load();
            if (GameService.UserSpirit != null){
                Log.e("JSON", "Successfully load");
            }
        }catch(Exception e) {
            Log.e("JSON", "ERROR LOADING SPIRITS");
        }
    }
}
