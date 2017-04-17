package com.choco.limsclient.Activities.QRCode;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

/**
 * Created by choco on 2017/4/14.
 */

public class ScanHelper {
    public void scanQRCode(Activity activity) {
        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setCaptureActivity(ScanActivity.class);
        intentIntegrator.initiateScan();
    }
}
