package com.orbital.thegame.spiffitnesspetsimulator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WearService extends Service {
    public WearService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
