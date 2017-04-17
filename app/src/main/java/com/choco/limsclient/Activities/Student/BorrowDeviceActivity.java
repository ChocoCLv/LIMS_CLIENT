package com.choco.limsclient.Activities.Student;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.choco.limsclient.Activities.LabAdmin.UpdateDeviceStatusActivity;
import com.choco.limsclient.Activities.QRCode.ScanHelper;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.CurrentUserInformation;
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
    String permissions[] = {"android.permission.CAMERA"};
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
        //TODO:向服务器发送借入请求 登记本次借入记录
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "BORROW_DEVICE");
            jo.put("STUDENT_ID",CurrentUserInformation.getInstance().getUserId());
            jo.put("DEVICE_ID",deviceId);
            Message msg = new Message();
            msg.what = Global.FROM_STUDENT_BORROWDEVICE;
            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private Handler newHandler(){
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
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    private View.OnClickListener newOnClickListener(){
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_devicePic:
                        if (checkPermissions()) {
                            scanDeviceQR();
                        }
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

    private void setOnClickListener(View.OnClickListener btnOnClickListener){
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
                tvDeviceInfo.setText(deviceInfo);
                deviceId = StringParseHelper.getDeviceIdFromDeviceInfo(deviceInfo);
            }
        }
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
            int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 321: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    scanDeviceQR();

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
