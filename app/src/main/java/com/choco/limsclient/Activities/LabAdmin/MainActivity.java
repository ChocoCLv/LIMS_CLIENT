package com.choco.limsclient.Activities.LabAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.choco.limsclient.R;

public class MainActivity extends AppCompatActivity {

    Button btnLendDevice;
    Button btnAddDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labadmin_main);
        setTitle(getIntent().getStringExtra("USER_NAME"));
        findView();

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
                    case R.id.btn_lendDevice:
                        intent = new Intent(com.choco.limsclient.Activities.LabAdmin.MainActivity.this,
                                LendDeviceActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

            }
        };
        btnAddDevice.setOnClickListener(btnOnClickListener);
        btnLendDevice.setOnClickListener(btnOnClickListener);
    }

    public void findView() {
        btnLendDevice = (Button) findViewById(R.id.btn_lendDevice);
        btnAddDevice = (Button) findViewById(R.id.btn_addDevice);
    }
}