package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.AlarmManager;
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

    public static Spirits UserSpirit;

    public static int stepCount;
    private static int affinityLevel = UserSpirit.getAffinityLevel();
    private static final int FACTOR = 1000;

    private JSONSerializer mSerializer = new JSONSerializer("Spirits.json", MainActivity.this.getApplicationContext());

    public final static String TAG = "GoogleFitService";
    private boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 1337;
    private ConnectionResult mFitResultResolution;
    private static final String AUTH_PENDING = "auth_state_pending";

    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (settings.getBoolean("firstLaunch",true)){
            Log.d("Settings", "First Launch Detected");
            UserSpirit = new Egg();

            settings.edit().putBoolean("firstLaunch",false).apply();
        }
        else{
            try{
                UserSpirit = mSerializer.load();
                Log.e("JSON", "Successfully load");
            }catch(Exception e){
                Log.e("ERROR", "ERROR LOADING SPIRITS");
            }
        }

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);

        sprite.setImageResource(UserSpirit.image_idle1);

        sprite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if ((stepCount - affinityLevel*FACTOR) > FACTOR) {
                    affinityLevel++;
                }
                UserSpirit.setAffinityLevel(affinityLevel);
                changeText(levelCount, "" + affinityLevel);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }
        });

        record.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RecordsActivity.class);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter(GoogleFitSync.STEP_COUNT));

        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
    }

    public class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent){
            affinityLevel = UserSpirit.getAffinityLevel();
            requestGoogleFitSync();
            if (UserSpirit.evolveCheck(affinityLevel)!= null){
                UserSpirit = UserSpirit.evolveCheck(affinityLevel);
            }
            saveSpirits();
        }
    }
    public void saveSpirits(){
        try{
            mSerializer.save(UserSpirit);
            Log.e("JSON", "Saved successfully");
        }catch(Exception e){
            Log.e("ERROR", "Error Saving notes");
        }
    }


    public void startAlarm(View view){
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 1000*60;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        Log.d("AlarmService", "Alarm Started");
    }

    private void requestGoogleFitSync(){
        Log.e(TAG,"Executing requestFitConnection...");
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
            if(intent.hasExtra(GoogleFitSync.STEP_COUNT)){
                stepCount = intent.getIntExtra(GoogleFitSync.STEP_COUNT, 0);
                Log.e(TAG, "Broadcasted Value: " + stepCount);
            }
            else if (!intent.hasExtra(GoogleFitSync.STEP_COUNT)){
                Log.e(TAG, "POOP");
            }
        }
    };

    private void fitHandleConnection(){
        Log.e(TAG, "Fit connected");
    }
    private void fitHandleFailedConnection(ConnectionResult result){
        Log.i(TAG, "Google Fit Connection failed. Cause: " + result.toString());
        if(!result.hasResolution()){
        //    GoogleApiAvailability.getErrorDialog(MainActivity.this, 0, result.getErrorCode());
            Log.e(TAG, "Google Fit connection failed not due to hasResultion");
            return;
        }
        if(!authInProgress){
            if(result.getErrorCode()== FitnessStatusCodes.NEEDS_OAUTH_PERMISSIONS){
                try{
                    Log.d(TAG, "Google Fit connection failed with OAuth failure. trying to ask for consent(again)");
                    result.startResolutionForResult(MainActivity.this, REQUEST_OAUTH);
                }catch (IntentSender.SendIntentException e){
                    Log.e(TAG, "Activity Thread Google Fit Exception while starting resolution activity", e);
                }
            }
            else{
                Log.i(TAG, "Activity thread Google Fit Attempting to resolve failed connection");
                mFitResultResolution = result;
            }
        }
    }

    private int updateAffinityPoint(int stepCount, int affinityLevel){
        return (stepCount - FACTOR*affinityLevel)/FACTOR;
    }

    private void changeImage(ImageView view, int drawable){
        view.setImageResource(drawable);
    }

    private void changeText(TextView view, String string){
        view.setText(string);
    }
}
