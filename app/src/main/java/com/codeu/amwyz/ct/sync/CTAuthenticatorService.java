package com.codeu.amwyz.ct.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * direct copy from developer guide
 */
public class CTAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private CTAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new CTAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
