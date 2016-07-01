package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitSync extends IntentService {
    public static final String TAG = "GoogleFitService";

    private GoogleApiClient mGoogleApiFitnessClient;
    private boolean mTryingToConnect = false;

    public static final boolean TYPE_TRUE = true;

    public static final String TYPE_GET_STEP_DATA = "getStepData";
    public static final String TYPE_REQUEST_CONNECTION = "requestConnection";
    public static final String TYPE_REQUEST_BLUETOOTH = "requestBluetooth";

    public static final String STEP_COUNT = "stepCount";
    public static final String FIT_NOTIFY_INTENT = "fitStatusUpdateIntent";
    public static final String FIT_EXTRA_CONNECTION_MESSAGE = "fitFirstConnection";
    public static final String FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE = "fitExtraFailedStatusCode";
    public static final String FIT_EXTRA_NOTIFY_FAILED_INTENT = "fitExtraFailedIntent";

    @Override
    public void onCreate(){
        super.onCreate();
        buildFitnessClient();
        Log.d(TAG,"GoogleFitSync started");
    }

    @Override
    public void onDestroy(){
        Log.d(TAG,"GoogleFitService destroyed");
        if (mGoogleApiFitnessClient.isConnected()){
            Log.d(TAG,"Disconnecting from Google Fit");
            mGoogleApiFitnessClient.disconnect();
        }
    }

    public void buildFitnessClient(){
        mGoogleApiFitnessClient = new GoogleApiClient.Builder(this)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.BLE_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                Log.i(TAG, "Connected to Google Fit");
                                mTryingToConnect=false;
                                Log.i(TAG, "Broadcasting information");
                                notifyConnected();
                                subscribe();
                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                Log.i(TAG,"Connection Suspended");
                            }
                        }
                )
                .addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                mTryingToConnect = false;
                                notifyFailed(connectionResult);
                            }
                        }
                ).build();

    }

    public GoogleFitSync(){
        super("GoogleFitService");
    }

    protected void subscribe(){
        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        Fitness.RecordingApi.subscribe(mGoogleApiFitnessClient,ESTIMATED_STEP_DELTAS).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    if(status.getStatusCode() == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED){
                        Log.i(TAG, "Subscription already existed");
                    }
                    else{
                        Log.i(TAG, "Subscription successful");
                    }
                }
                else {
                    Log.e(TAG, "Subscription failed");
                }
            }
        });
    }

    @Override
    protected void onHandleIntent(Intent intent){
        boolean connectionStatusRequest = intent.getBooleanExtra(TYPE_REQUEST_CONNECTION, false);
        boolean stepDataRequest = intent.getBooleanExtra(TYPE_GET_STEP_DATA, false);

        long startTime = intent.getLongExtra("spiritStartTime", 1);
        long endTime = intent.getLongExtra("spiritEndTime", 2);

        if (connectionStatusRequest){
            if (!mGoogleApiFitnessClient.isConnected()){
                mTryingToConnect=true;
                Log.d(TAG, "Trying to connect to Google Fit");
                mGoogleApiFitnessClient.connect();
                while (mTryingToConnect){
                    try {
                        Thread.sleep(100,0);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
            else
                Log.d(TAG, "Already Connected");
        }
        if (stepDataRequest){
            if(mGoogleApiFitnessClient.isConnected()){
                Log.d(TAG,"Requesting step count from Google Fit");
                getSteps(startTime,endTime);
            }
            else
                Log.e(TAG, "Fit not connected");
        }
    }

    private void getSteps(long startTime, long endTime){
        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(7, TimeUnit.DAYS)
                .setTimeRange(startTime,endTime,TimeUnit.MILLISECONDS)
                .build();

        /*Log.d(TAG, "START TIME IN LONG: " + startTime);
        Log.d(TAG, "END TIME IN LONG: " + endTime);*/

        DataReadResult dataResult = Fitness
                .HistoryApi.readData(mGoogleApiFitnessClient,readRequest)
                .await(1,TimeUnit.MINUTES);

        if(dataResult.getBuckets().size()>0){
            for (Bucket bucket : dataResult.getBuckets()){
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet: dataSets){
                    computeSteps(dataSet);
                }
            }
        }
        else if (dataResult.getDataSets().size() > 0){
            for (DataSet dataSet: dataResult.getDataSets()){
                computeSteps(dataSet);
            }
        }
    }

    private void computeSteps(DataSet dataSet) {
        Log.e(TAG, "Computing steps");
        int steps = 0;
        for (DataPoint dp : dataSet.getDataPoints()) {
            for (Field field : dp.getDataType().getFields()) {
                steps += dp.getValue(field).asInt();
            }
        }
        //Log.e(TAG, "Value: "+steps);
        sendSteps(steps);
        //Log.d(TAG, "Retrieval Successful, terminating Google Fit");
    }

    private void notifyConnected(){
        Intent intent = new Intent(FIT_NOTIFY_INTENT);
        intent.putExtra(FIT_EXTRA_CONNECTION_MESSAGE, FIT_EXTRA_CONNECTION_MESSAGE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void notifyFailed(ConnectionResult result){
        Intent intent = new Intent(FIT_NOTIFY_INTENT);
        intent.putExtra(FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE,result.getErrorCode());
        intent.putExtra(FIT_EXTRA_NOTIFY_FAILED_INTENT, result.getResolution());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendSteps(int stepCount){
        Intent intent = new Intent(FIT_NOTIFY_INTENT);
        intent.putExtra(STEP_COUNT, stepCount);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
