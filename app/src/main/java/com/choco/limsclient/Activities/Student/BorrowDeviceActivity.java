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

import com.choco.limsclient.Activities.UtilActivities.QRCode.ScanHelper;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.CurrentUserInfo;
import com.choco.limsclient.Util.Global;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class BorrowDeviceActivity extends AppCompatActivity {

    ImageView ivDevicePic;
    Button btnConfirmBorrow;
    TextView tvDeviceInfo;
    JSONObject deviceInfo;
    String deviceId;
    String deviceType;
    String deviceIp;
    final String computer = "电脑";

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
        ScanHelper sh = new ScanHelper();
        sh.scanQRCode(this);
    }

    private void borrowDevice() {
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "BORROW_DEVICE");
            jo.put("STUDENT_ID", CurrentUserInfo.getInstance().getUserId());
            jo.put("DEVICE_ID", deviceId);
            jo.put("DEVICE_TYPE",deviceType);
            if(computer.equals(deviceType)){
                deviceIp = deviceInfo.getString("DEVICE_IP");
                jo.put("DEVICE_IP",deviceIp);
            }
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
                try {
                    deviceInfo = new JSONObject(result.getContents());
                    updateDeviceInfoTextView();
                    deviceId = deviceInfo.getString("DEVICE_ID");
                    deviceType = deviceInfo.getString("DEVICE_TYPE");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                if(deviceId==null){
                    Toast.makeText(this, deviceInfo.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void updateDeviceInfoTextView(){
        tvDeviceInfo.setText(deviceInfo.toString());
    }
}
