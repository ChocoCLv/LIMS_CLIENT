package com.choco.limsclient.Activities.LabAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.choco.limsclient.Activities.QRCode.GenQRCodeActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.Util.CurrentUserInformation;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AddDeviceActivity extends AppCompatActivity {

    Button btnAddDevice;
    Button btnGenQRCode;
    EditText edtDeviceName;
    EditText edtDeviceType;
    EditText edtDevicePrincipal;
    EditText edtDeviceId;
    EditText edtDeviceBelongTo;
    CurrentUserInformation userInfo;
    CommThread comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labadmin_add_device);
        setTitle("添加新设备");
        findView();
        comm = CommThread.getInstance();
        userInfo = CurrentUserInformation.getInstance();

        setOnClickListener(newOnClickListener());

        edtDevicePrincipal.setHint(userInfo.getUserId());

        comm.setHandler(newHandler());
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
                        String result = resp.getString("ADD_STATUS");
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(AddDeviceActivity.this, "添加成功！", Toast.LENGTH_LONG).show();
                            btnGenQRCode.setEnabled(true);
                        } else {
                            //TODO:返回更多添加失败的原因
                            Toast.makeText(AddDeviceActivity.this, "添加失败！", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    try {
                        String device_id = resp.getString("DEVICE_ID");
                        edtDeviceId.setText(device_id);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    public void setOnClickListener(View.OnClickListener listener) {
        btnAddDevice.setOnClickListener(listener);
        btnGenQRCode.setOnClickListener(listener);
    }

    public View.OnClickListener newOnClickListener() {
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_add:
                        addDevice();
                        break;
                    case R.id.btn_genQR:
                        genQRCode();
                        break;
                    default:
                        break;
                }
            }
        };
        return btnOnClickListener;
    }

    public void findView() {
        btnAddDevice = (Button) findViewById(R.id.btn_add);
        btnGenQRCode = (Button) findViewById(R.id.btn_genQR);
        edtDeviceName = (EditText) findViewById(R.id.edt_deviceName);
        edtDeviceType = (EditText) findViewById(R.id.edt_deviceType);
        edtDevicePrincipal = (EditText) findViewById(R.id.edt_devicePrincipalId);
        edtDeviceId = (EditText) findViewById(R.id.edt_deviceId);
        edtDeviceBelongTo = (EditText) findViewById(R.id.edt_deviceBelongTo);
    }

    public void addDevice() {
        String name = edtDeviceName.getText().toString();
        String type = edtDeviceType.getText().toString();
        String principalId = edtDevicePrincipal.getText().toString();
        String deviceId = edtDeviceId.getText().toString();
        String deviceBelongTo = edtDeviceBelongTo.getText().toString();
        if (name.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (principalId.isEmpty()) {
            principalId = edtDevicePrincipal.getHint().toString();
        }
        JSONObject jo = new JSONObject();
        try {
            if (!deviceId.isEmpty()) {
                jo.put("DEVICE_ID", deviceId);
            }
            jo.put("REQUEST_TYPE", "ADD_DEVICE");
            jo.put("DEVICE_NAME", name);
            jo.put("DEVICE_TYPE", type);
            jo.put("DEVICE_PRINCIPAL_ID", principalId);
            jo.put("DEVICE_BELONG_TO", deviceBelongTo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = Global.FROM_LABADMIN_LENDDEVICE;
        msg.obj = jo.toString();
        comm.commHandler.sendMessage(msg);
    }

    public void genQRCode() {
        String name = edtDeviceName.getText().toString();
        String type = edtDeviceType.getText().toString();
        String principalId = edtDevicePrincipal.getText().toString();
        String id = edtDeviceId.getText().toString();
        if (name.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (principalId.isEmpty()) {
            principalId = edtDevicePrincipal.getHint().toString();
        }
        Intent intent = new Intent(AddDeviceActivity.this, GenQRCodeActivity.class);
        intent.putExtra("DEVICE_NAME", name);
        intent.putExtra("DEVICE_TYPE", type);
        intent.putExtra("DEVICE_PRINCIPAL_ID", principalId);
        intent.putExtra("DEVICE_ID", id);
        startActivity(intent);
    }
}
