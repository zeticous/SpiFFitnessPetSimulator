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
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitSync extends IntentService {
    public static final String TAG = "GoogleFitService";
    private GoogleApiClient mGoogleApiFitnessClient;
    private boolean mTryingToConnect = false;

    public static final String SERVICE_REQUEST_TYPE = "requestType";
    public static final int TYPE_GET_STEP_DATA = 1;
    public static final int TYPE_REQUEST_CONNECTION = 2;

    public static final String GET_STEP_DATA = "getStepData";
    public static final String REQUEST_CONNECTION = "requestConnection";

    public static final String STEP_COUNT = "stepCount";
    public static final String FIT_NOTIFY_INTENT = "fitStatusUpdateIntent";
    public static final String FIT_EXTRA_CONNECTION_MESSAGE = "fitFirstConnection";
    public static final String FIT_EXTRA_NOTIFY_FAILED_STATUS_CODE = "fitExtraFailedStatusCode";
    public static final String FIT_EXTRA_NOTIFY_FAILED_INTENT = "fitExtraFailedIntent";

    public static int numOfSteps;


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
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnected(@Nullable Bundle bundle) {
                                Log.i(TAG, "Connected to Google Fit");
                                mTryingToConnect=false;
                                Log.i(TAG, "Broadcasting information");
                                notifyConnected();
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

    @Override
    protected void onHandleIntent(Intent intent){
        int type = intent.getIntExtra(SERVICE_REQUEST_TYPE, -1);

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

        if (mGoogleApiFitnessClient.isConnected()){
            if(type == TYPE_GET_STEP_DATA){
                Log.d(TAG,"Requesting step count from Google Fit");
                getSteps();
            }
            else if(type == TYPE_REQUEST_CONNECTION){

            }
        }else {
            Log.d(TAG, "Fit wasn't able to connect, so the request failed");
        }
    }

    private void getSteps(){
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.DAY_OF_YEAR, -3);
        long startTime = cal.getTimeInMillis();

        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms")
                .build();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(ESTIMATED_STEP_DELTAS, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(3, TimeUnit.DAYS)
                .setTimeRange(startTime,endTime,TimeUnit.MILLISECONDS)
                .build();

        DataReadResult dataResult = Fitness
                .HistoryApi.readData(mGoogleApiFitnessClient,readRequest)
                .await(30,TimeUnit.SECONDS);

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
        numOfSteps += steps;
        Log.e(TAG, "Value: " + numOfSteps);
        sendSteps(numOfSteps);
        Log.d(TAG, "Retrieval Successful, terminating Google Fit");
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
        Intent intent = new Intent(STEP_COUNT);
        intent.putExtra(STEP_COUNT, stepCount);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
