package com.choco.limsclient.Activities.LabAdmin;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.Config.Global;
import com.choco.limsclient.R;

import org.json.JSONException;
import org.json.JSONObject;

public class AddDeviceActivity extends AppCompatActivity {

    Button btnAddDevice;
    EditText edtDeviceName;
    EditText edtDeviceType;
    Handler handler;
    CommThread comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        setTitle("添加新设备");
        findView();
        comm = CommThread.getInstance();
        View.OnClickListener btnOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_add:
                        addDevice();
                        break;
                    default:
                        break;
                }

            }
        };
        btnAddDevice.setOnClickListener(btnOnClickListener);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    Toast.makeText(AddDeviceActivity.this, "添加成功！", Toast.LENGTH_LONG).show();
                }
            }
        };
        comm.setHandler(handler);
    }

    public void findView() {
        btnAddDevice = (Button) findViewById(R.id.btn_add);
        edtDeviceName = (EditText) findViewById(R.id.edt_deviceName);
        edtDeviceType = (EditText) findViewById(R.id.edt_deviceType);
    }

    public void addDevice() {
        String name = edtDeviceName.getText().toString();
        String type = edtDeviceType.getText().toString();
        if (name.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "信息不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUESTTYPE", "ADDDEVICE");
            jo.put("DEVICENAME", name);
            jo.put("DEVICETYPE", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = Global.FROM_LABADMIN_LENDDEVICE;
        msg.obj = jo.toString();
        comm.commHandler.sendMessage(msg);
    }
}
