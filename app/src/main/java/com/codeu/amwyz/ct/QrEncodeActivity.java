package com.codeu.amwyz.ct;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Created by goodautumn on 8/4/2015.
 */
public class QrEncodeActivity extends ActionBarActivity {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int HEIGHT = 400;
    public final static int WIDTH = 400;
    public String qrData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_encode_main);

        QrDoneButtonFragment qrDoneButtonFragment = new QrDoneButtonFragment();
        fragmentTransaction.add(R.id.bottom_qr_encode_fragment_container, qrDoneButtonFragment);
        fragmentTransaction.commit();

        // ImageView to display the QR code in.  This should be defined in
// your Activity's XML layout file

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        ImageView imageView = (ImageView) findViewById(R.id.qr_code);

        if(imageView == null)
            System.out.println("Issue found");
        qrData = prefs.getString("user_id",null);
        if(qrData != null){

            try {
                Bitmap bitmap = this.encodeAsBitmap(qrData);
                imageView.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }
}

}

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        //try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, WIDTH, WIDTH, null);
       // } catch (IllegalArgumentException iae) {
            // Unsupported format
          //  return null;
        //}
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, com.codeu.amwyz.ct.SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        Intent toHomeIntent = new Intent(this, MainActivity.class);
        startActivity(toHomeIntent);
    }
}
