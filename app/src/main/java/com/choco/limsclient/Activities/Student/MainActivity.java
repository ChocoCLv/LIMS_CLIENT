package com.choco.limsclient.Activities.Student;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.choco.limsclient.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends Activity {

    Button btnBorrowDevice;
    String permissions[] = {"android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);

        findView();

        btnBorrowDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
                borrowDevice();
            }
        });

    }

    private void findView() {
        btnBorrowDevice = (Button) findViewById(R.id.btn_borrowDevice);
    }

    private void borrowDevice() {
        IntentIntegrator integrator = new IntentIntegrator(com.choco.limsclient.Activities.Student.MainActivity.this);
        ArrayList<String> formats = new ArrayList<>();
        formats.add("QR_CODE");
        integrator.setPrompt("test");
        integrator.setScanningRectangle(600, 600);
        integrator.initiateScan(formats);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            Log.d("code", result);
            Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permission);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求
                    ActivityCompat.requestPermissions(this, permissions, 321);
                }
            }
        }
    }
}
