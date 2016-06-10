package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GameEngineService extends Service {

    @Override
    public void onCreate(){
        super.onCreate();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
