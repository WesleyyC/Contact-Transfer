package com.codeu.amwyz.ct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * direct copy from developer guide
 */
public class CTSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static CTSyncAdapter sCTSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("CTSyncService", "onCreate - CTSyncService");
        synchronized (sSyncAdapterLock) {
            if (sCTSyncAdapter == null) {
                sCTSyncAdapter = new CTSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sCTSyncAdapter.getSyncAdapterBinder();
    }
}