package com.choco.limsclient.Activities.Student;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.choco.limsclient.R;
import com.google.zxing.client.android.CaptureActivity;

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
                if(checkPermissions()){
                    borrowDevice();
                }
            }
        });

    }

    private void findView() {
        btnBorrowDevice = (Button) findViewById(R.id.btn_borrowDevice);
    }

    private void borrowDevice() {
       Intent intent = new Intent(com.choco.limsclient.Activities.Student.MainActivity.this,
               CaptureActivity.class);
        startActivity(intent);
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permission);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 如果没有授予该权限，就去提示用户请求  异步方法 调用后即返回
                    ActivityCompat.requestPermissions(this, permissions, 321);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults){
        switch (requestCode) {
            case 321: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    borrowDevice();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
