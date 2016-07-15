package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.FragmentManager;
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
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.fitness.FitnessStatusCodes;

public class connectActivity extends AppCompatActivity {

    boolean authInProgress = false;
    public static final int REQUEST_OAUTH = 1337;
    public final static String TAG = "GoogleFitService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter((GoogleFitSync.FIT_NOTIFY_INTENT)));

        Button button = (Button) findViewById(R.id.connect_button);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestConnection();
            }
        });
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
        SharedPreferences settings = getSharedPreferences("GameSettings", 0);
        if (settings.getBoolean("firstLaunch", true)) {
            Toast.makeText(this, "Google Fit Connected", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(connectActivity.this, welcomeActivity.class);
            startActivity(intent);
            finish();
        }
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
                    result.startResolutionForResult(connectActivity.this, REQUEST_OAUTH);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Activity Thread Google Fit Exception while starting resolution activity", e);
                    startFragment();
                }
            } else {
                try {
                    Log.i(TAG, "Activity thread Google Fit Attempting to resolve failed connection");
                    result.startResolutionForResult(connectActivity.this, REQUEST_OAUTH);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "Activity Thread Google Fit Exception while starting resolution activity", e);
                    startFragment();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            requestConnection();
        }
        else if (resultCode == RESULT_CANCELED){
            startFragment();
        }
    }

    public void startFragment(){
        FragmentManager fm = getFragmentManager();
        OnConnectedFailPrompt onConnectedFailPrompt= new OnConnectedFailPrompt();
        onConnectedFailPrompt.setRetainInstance(true);
        onConnectedFailPrompt.show(fm, "fragment_name");
    }
}
