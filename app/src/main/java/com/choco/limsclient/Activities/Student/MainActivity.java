package com.choco.limsclient.Activities.Student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.choco.limsclient.R;

import java.util.ArrayList;

public class MainActivity extends Activity {
    Button btnBorrowDevice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        findView();

        btnBorrowDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                borrowDevice();
            }
        });

    }

    private void findView(){
        btnBorrowDevice = (Button)findViewById(R.id.btn_borrowDevice);
    }

    private void borrowDevice(){
        IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
        ArrayList<String> formats = new ArrayList<>();
        formats.add("QR_CODE");
        integrator.setPrompt("test");
        integrator.setScanningRectangle(600,600);
        integrator.initiateScan(formats);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.d("code", result);
            Toast.makeText(this,result, Toast.LENGTH_LONG).show();
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }
    }
}
