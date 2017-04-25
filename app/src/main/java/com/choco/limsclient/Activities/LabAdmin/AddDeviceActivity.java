package com.choco.limsclient.Activities.LabAdmin;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.choco.limsclient.Activities.QRCode.GenQRCodeActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.CurrentUserInfo;
import com.choco.limsclient.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class AddDeviceActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Button btnAddDevice;
    Button btnGenQRCode;
    ImageButton ibTakeDevicePhotos;
    EditText edtDeviceName;
    EditText edtDeviceType;
    EditText edtDevicePrincipal;
    EditText edtDeviceId;
    EditText edtDeviceLocDefault;
    CurrentUserInfo userInfo;
    CommThread comm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labadmin_add_device);
        setTitle("添加新设备");
        findView();
        comm = CommThread.getInstance();
        userInfo = CurrentUserInfo.getInstance();

        setOnClickListener(newOnClickListener());

        edtDevicePrincipal.setHint(userInfo.getUserId());

        comm.setHandler(newHandler());
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

    private void setOnClickListener(View.OnClickListener listener) {
        btnAddDevice.setOnClickListener(listener);
        btnGenQRCode.setOnClickListener(listener);
        ibTakeDevicePhotos.setOnClickListener(listener);
    }

    private View.OnClickListener newOnClickListener() {
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
                    case R.id.ib_takeDevicePhoto:
                        takeDevicePhotos();
                        break;
                    default:
                        break;
                }
            }
        };
        return btnOnClickListener;
    }

    private void takeDevicePhotos() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues();
        Uri mPhotoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mPhotoUri);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                // 通过照相获取图片
                if (resultCode == Activity.RESULT_OK) {
                    //TODO:保存图片到指定路径
                }
                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }


    private void findView() {
        btnAddDevice = (Button) findViewById(R.id.btn_add);
        btnGenQRCode = (Button) findViewById(R.id.btn_genQR);
        ibTakeDevicePhotos = (ImageButton) findViewById(R.id.ib_takeDevicePhoto);
        edtDeviceName = (EditText) findViewById(R.id.edt_deviceName);
        edtDeviceType = (EditText) findViewById(R.id.edt_deviceType);
        edtDevicePrincipal = (EditText) findViewById(R.id.edt_devicePrincipalId);
        edtDeviceId = (EditText) findViewById(R.id.edt_deviceId);
        edtDeviceLocDefault = (EditText) findViewById(R.id.edt_deviceLocDefault);
    }

    private void addDevice() {
        String name = edtDeviceName.getText().toString();
        String type = edtDeviceType.getText().toString();
        String principalId = edtDevicePrincipal.getText().toString();
        String deviceId = edtDeviceId.getText().toString();
        String deviceLocDefault = edtDeviceLocDefault.getText().toString();
        if (name.isEmpty() || type.isEmpty() || deviceId.isEmpty()) {
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
            jo.put("DEVICE_LOC_DEFAULT", deviceLocDefault);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = Global.FROM_LABADMIN_LENDDEVICE;
        msg.obj = jo.toString();
        comm.commHandler.sendMessage(msg);
    }

    private void genQRCode() {
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
        JSONObject jo = new JSONObject();
        try {
            jo.put("设备名称", name);
            jo.put("设备类型", type);
            jo.put("设备负责人ID", principalId);
            jo.put("设备ID", id);
            intent.putExtra("DEVICE_INFO",jo.toString());
            startActivity(intent);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}
