package com.choco.limsclient.Activities.LabAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;

public class MainActivity extends AppCompatActivity {

    Button btnLendDevice;
    Button btnAddDevice;
    Button btnUpdateDeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labadmin_main);

        findView();
        setOnClickListener(newOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Global.userInfo.getUserName());
    }

    private View.OnClickListener newOnClickListener() {
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_addDevice:
                        intent = new Intent(com.choco.limsclient.Activities.LabAdmin.MainActivity.this,
                                AddDeviceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_retrieveDevice:
                        intent = new Intent(com.choco.limsclient.Activities.LabAdmin.MainActivity.this,
                                LendDeviceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_updateDeviceInfo:
                        intent = new Intent(com.choco.limsclient.Activities.LabAdmin.MainActivity.this,
                                UpdateDeviceStatusActivity.class);
                        startActivity(intent);
                    default:
                        break;
                }

            }
        };
        return btnOnClickListener;
    }

    private void setOnClickListener(View.OnClickListener btnOnClickListener) {
        btnAddDevice.setOnClickListener(btnOnClickListener);
        btnLendDevice.setOnClickListener(btnOnClickListener);
        btnUpdateDeviceInfo.setOnClickListener(btnOnClickListener);
    }

    private void findView() {
        btnLendDevice = (Button) findViewById(R.id.btn_retrieveDevice);
        btnAddDevice = (Button) findViewById(R.id.btn_addDevice);
        btnUpdateDeviceInfo = (Button) findViewById(R.id.btn_updateDeviceInfo);
    }
}
