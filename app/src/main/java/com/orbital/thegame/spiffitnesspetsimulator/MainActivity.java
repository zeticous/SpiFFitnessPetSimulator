package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
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

    public static int stepCount = 0;
    public static int affinityPoint = 5;
    public static int affinityLevel = 0;

    public final static String TAG = "GoogleFitService";
    private boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 1337;
    private ConnectionResult mFitResultResolution;
    private static final String AUTH_PENDING = "auth_state_pending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateAffinityImage(affinityPoint);

        ImageButton sprite = (ImageButton) findViewById(R.id.sprite);
        ImageButton menu = (ImageButton) findViewById(R.id.menu);
        ImageButton help = (ImageButton) findViewById(R.id.help);
        ImageButton record = (ImageButton) findViewById(R.id.record);

        final TextView levelCount = (TextView) findViewById(R.id.level_count);

        sprite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (affinityPoint > 0) {
                    affinityLevel++;
                    affinityPoint--;
                }
                updateAffinityImage(affinityPoint);
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

        requestGoogleFitSync();
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

    private void updateAffinityImage(int affinityPoint){
        ImageView heart01 = (ImageView) findViewById(R.id.heart01);
        ImageView heart02 = (ImageView) findViewById(R.id.heart02);
        ImageView heart03 = (ImageView) findViewById(R.id.heart03);
        ImageView heart04 = (ImageView) findViewById(R.id.heart04);
        ImageView heart05 = (ImageView) findViewById(R.id.heart05);

        switch(affinityPoint){
            case 5:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_fullheart);
                changeImage(heart05,R.drawable.view_fullheart);
                break;
            case 4:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_fullheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 3:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_fullheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 2:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_fullheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 1:
                changeImage(heart01,R.drawable.view_fullheart);
                changeImage(heart02,R.drawable.view_emptyheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
            case 0:
                changeImage(heart01,R.drawable.view_emptyheart);
                changeImage(heart02,R.drawable.view_emptyheart);
                changeImage(heart03,R.drawable.view_emptyheart);
                changeImage(heart04,R.drawable.view_emptyheart);
                changeImage(heart05,R.drawable.view_emptyheart);
                break;
        }
    }

    private void changeImage(ImageView view, int drawable){
        view.setImageResource(drawable);
    }

    private void changeText(TextView view, String string){
        view.setText(string);
    }


}
