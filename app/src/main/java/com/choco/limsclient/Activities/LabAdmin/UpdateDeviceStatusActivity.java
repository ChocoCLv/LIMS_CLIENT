package com.choco.limsclient.Activities.LabAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.choco.limsclient.Activities.QRCode.ScanHelper;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.Config.Global;
import com.choco.limsclient.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UpdateDeviceStatusActivity extends AppCompatActivity {

    ImageView ivDevice;
    TextView tvDeviceInfo;
    EditText edtCurrentLoc;
    EditText edtCurrentStatus;
    EditText edtComments;
    Button btnConfirmUpdate;
    String deviceInfo;
    String deviceId;

    CommThread comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("更新设备信息");
        setContentView(R.layout.activity_labadmin_update_device_info);
        findView();
        comm = CommThread.getInstance();
        comm.setHandler(newHandler());
        setOnClickListener(newOnClickListener());
    }

    private void findView() {
        ivDevice = (ImageView) findViewById(R.id.iv_device);
        tvDeviceInfo = (TextView) findViewById(R.id.tv_deviceInfo);
        edtCurrentLoc = (EditText) findViewById(R.id.edt_currentLoc);
        edtCurrentStatus = (EditText) findViewById(R.id.edt_currentStatus);
        edtComments = (EditText) findViewById(R.id.edt_comments);
        btnConfirmUpdate = (Button) findViewById(R.id.btn_confirmUpdate);
    }

    private View.OnClickListener newOnClickListener() {
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_device:
                        scanDeviceQRCode();
                        break;
                    case R.id.btn_confirmUpdate:
                        updateDeviceStatus();
                        break;
                    default:
                        break;
                }
            }
        };
        return btnOnClickListener;
    }

    private void updateDeviceStatus() {
        String loc = edtCurrentLoc.getText().toString();
        String status = edtCurrentStatus.getText().toString();
        String comments = edtComments.getText().toString();
        if (loc.isEmpty()) {
            Toast.makeText(this, "当前位置不能为空", Toast.LENGTH_SHORT).show();
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "UPDATE_DEVICE_STATUS");
            jo.put("DEVICE_CURRENT_LOCATION", loc);
            jo.put("DEVICE_CURRENT_STATUS", status);
            jo.put("DEVICE_COMMENTS", comments);
            jo.put("DEVICE_ID", deviceId);
            Message msg = new Message();
            msg.what = Global.FROM_LADADMIN_UPDATEDEVICEINFO;
            msg.obj = jo.toString();
            comm.commHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                String[] res = deviceInfo.split("\\n");
                deviceId = res[3].split("：")[1];
            }
        }
    }

    private void scanDeviceQRCode() {
        ScanHelper sh = new ScanHelper();
        sh.scanQRCode(this);
    }

    private void setOnClickListener(View.OnClickListener btnOnClickListener) {
        ivDevice.setOnClickListener(btnOnClickListener);
        btnConfirmUpdate.setOnClickListener(btnOnClickListener);
    }

    public Handler newHandler() {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    JSONObject resp;
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        resp = (JSONObject) jsonParser.nextValue();
                        String result = resp.getString("UPDATE_STATUS");
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(UpdateDeviceStatusActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }
}
