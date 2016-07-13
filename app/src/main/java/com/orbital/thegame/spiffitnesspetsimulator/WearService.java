package com.orbital.thegame.spiffitnesspetsimulator;

import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

public class WearService extends WearableListenerService {
    private final String TAG = "WearService";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "WearService started");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        super.onDataChanged(dataEvents);
        if (!mGoogleApiClient.isConnected() || !mGoogleApiClient.isConnecting()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            if (!connectionResult.isSuccess()) {
                Log.e(TAG, "DataLayerListenerService failed to connect to GoogleApiClient, "
                        + "error code: " + connectionResult.getErrorCode());
                return;
            }
        }

        Toast.makeText(this, "WearService started", Toast.LENGTH_SHORT).show();
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/data") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    setUpData(dataMap);
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.e("AndroidWear", "DataEvent DELETED");
            }
        }
    }

    private void setUpData(DataMap dataMap){
        int affinityLevel = dataMap.getInt("affinityLevel");
        int affinityPoint = dataMap.getInt("affinityPoint");

        GameService.UserSpirit.setAffinityLevel(affinityLevel);
        GameService.UserSpirit.setAffinityPoint(affinityPoint);
    }
}
