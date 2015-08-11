package com.codeu.amwyz.ct;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import eu.livotov.zxscan.ScannerFragment;
import eu.livotov.zxscan.ScannerView;

/**
 * Created by goodautumn on 8/3/2015.
 */
public class QrScanActivity extends ActionBarActivity implements ScannerView.ScannerViewEventListener{
    public final static String RESULT_EXTRA_STR = "data";
    private ScannerFragment scanner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scan_main);

        if (savedInstanceState == null)
        {
            if (scanner == null)
            {
                scanner = new ScannerFragment();
                scanner.setScannerViewEventListener(this);
            }

            getSupportFragmentManager().beginTransaction().add(R.id.qr_scan_container, scanner).commit();
        }
    }


    @Override
    public void onScannerReady() {

    }
    public void onScannerStopped()
    {

    }

    @Override
    public void onScannerFailure(int cameraError)
    {

    }

    public boolean onCodeScanned(final String data)
    {
        Intent res = new Intent();
        res.putExtra(RESULT_EXTRA_STR, data);
        setResult(RESULT_OK, res);
        finish();

        //use data to load contact info, save to local database
        Utility.QRAdd(this,data);
        Log.e("QR ", data);
        Intent toHomeIntent = new Intent(this, MainActivity.class);
        startActivity(toHomeIntent);
        return true;
    }

}
