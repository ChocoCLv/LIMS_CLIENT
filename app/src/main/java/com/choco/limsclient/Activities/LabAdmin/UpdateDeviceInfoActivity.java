package com.choco.limsclient.Activities.LabAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.choco.limsclient.Activities.QRCode.ScanHelper;
import com.choco.limsclient.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class UpdateDeviceInfoActivity extends AppCompatActivity {

    ImageView ivDevice;
    TextView tvDeviceInfo;
    EditText edtCurrentLoc;
    EditText edtCurrentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("更新设备信息");
        setContentView(R.layout.activity_update_device_info);
        findView();

        setOnClickListener(newOnClickListener());
    }

    private void findView() {
        ivDevice = (ImageView) findViewById(R.id.iv_device);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_deviceInfo);
        edtCurrentLoc = (EditText) findViewById(R.id.edt_currentLoc);
        edtCurrentStatus = (EditText) findViewById(R.id.edt_currentStatus);
    }

    private View.OnClickListener newOnClickListener() {
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_device:
                        scanDeviceQRCode();
                    case R.id.btn_confirmUpdate:
                        updateDeviceInfo();
                    default:
                        break;
                }
            }
        };
        return btnOnClickListener;
    }

    private void updateDeviceInfo(){
        //TODO:发送更新的信息到服务器 包括设备ID
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                tvDeviceInfo.setText(result.getContents());
            }
        }
    }


    private void scanDeviceQRCode() {
        ScanHelper sh = new ScanHelper();
        sh.scanQRCode(this);
    }

    private void setOnClickListener(View.OnClickListener btnOnClickListener) {
        ivDevice.setOnClickListener(btnOnClickListener);

    }
}
