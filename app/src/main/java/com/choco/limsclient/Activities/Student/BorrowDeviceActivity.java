package com.choco.limsclient.Activities.Student;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.choco.limsclient.Activities.QRCode.ScanActivity;
import com.choco.limsclient.Activities.QRCode.ScanHelper;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.CurrentUserInfo;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.Util.StringParseHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BorrowDeviceActivity extends AppCompatActivity {

    ImageView ivDevicePic;
    Button btnConfirmBorrow;
    TextView tvDeviceInfo;
    String deviceInfo;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_borrow_device);

        findView();

        CommThread.getInstance().setHandler(newHandler());

        setOnClickListener(newOnClickListener());
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle("设备借入");
    }

    private void findView() {
        ivDevicePic = (ImageView) findViewById(R.id.iv_devicePic);
        btnConfirmBorrow = (Button) findViewById(R.id.btn_confirmBorrow);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_deviceInfo);
    }

    private void scanDeviceQR() {
        /*ScanHelper sh = new ScanHelper();
        sh.scanQRCode(this);*/

        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(ScanActivity.class);
        intentIntegrator.initiateScan();
    }

    private void borrowDevice() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "BORROW_DEVICE");
            jo.put("STUDENT_ID", CurrentUserInfo.getInstance().getUserId());
            jo.put("DEVICE_ID", deviceId);
            Message msg = new Message();
            msg.what = Global.FROM_STUDENT_BORROWDEVICE;
            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Handler newHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    JSONObject resp;
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        resp = (JSONObject) jsonParser.nextValue();
                        String result = resp.getString("BORROW_STATUS");
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(BorrowDeviceActivity.this, "借入登记成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(BorrowDeviceActivity.this, resp.getString("DESCRIPTION"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    private View.OnClickListener newOnClickListener() {
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_devicePic:
                        scanDeviceQR();
                        break;
                    case R.id.btn_confirmBorrow:
                        borrowDevice();
                        break;
                    default:
                        break;
                }
            }
        };
        return btnOnClickListener;
    }

    private void setOnClickListener(View.OnClickListener btnOnClickListener) {
        ivDevicePic.setOnClickListener(btnOnClickListener);
        btnConfirmBorrow.setOnClickListener(btnOnClickListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                deviceInfo = result.getContents();
                updateDeviceInfoTextView();
                try {
                    deviceId = new JSONObject(deviceInfo).getString("设备ID");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(deviceId==null){
                    Toast.makeText(this, deviceInfo, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void updateDeviceInfoTextView(){
        tvDeviceInfo.setText(deviceInfo);
    }
}
