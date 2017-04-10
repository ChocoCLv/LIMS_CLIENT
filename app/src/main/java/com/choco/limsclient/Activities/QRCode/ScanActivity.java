package com.choco.limsclient.Activities.QRCode;

import android.os.Bundle;
import android.view.KeyEvent;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.choco.limsclient.R;

public class ScanActivity extends CaptureActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ScanActivity.this.finish();
        }else{
            return super.onKeyDown(keyCode,event);
        }
        return true;
    }*/
}

