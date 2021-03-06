package com.choco.limsclient.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.choco.limsclient.Activities.UtilActivities.FaceRecognitionActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.Util.Helper;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import static com.choco.limsclient.Util.Global.userInfo;

public class LoginActivity extends AppCompatActivity {
    private final int FACE_REGISTER = 1;
    EditText edtTxtUsername;
    EditText edtTxtPwd;
    Button btnLogin;
    Handler handler;
    String username;

    String permissions[] = {"android.permission.CAMERA"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LIMS-实验室管理系统");
        findView();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        JSONObject resp = (JSONObject) jsonParser.nextValue();
                        String loginResult = resp.getString("LOGIN_STATUS");
                        if (loginResult.equals("SUCCESS")) {
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT)
                                    .show();
                            username = resp.getString("USER_NAME");
                            String userType = resp.getString("USER_TYPE");

                            userInfo.setUserName(username);
                            userInfo.setUserType(userType);

                            if(isFirstIn()){
                                Intent intent = new Intent(LoginActivity.this, FaceRecognitionActivity.class);
                                intent.putExtra("REQUEST_TYPE",Global.FACE_REGISTER);
                                startActivityForResult(intent,FACE_REGISTER);
                            }else if (checkPermissions()) {
                                startNewActivity(userInfo.getUserType());
                            }

                        } else if (loginResult.equals("FAILED")) {
                            Toast.makeText(LoginActivity.this, "Login failed.Please check your username and password", Toast.LENGTH_LONG)
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.i("login", "illegal json data");
                    }
                }
            }
        };
        CommThread.getInstance().setHandler(handler);
        new Thread(CommThread.getInstance()).start();
    }

    private boolean isFirstIn(){
        SharedPreferences pref = this.getSharedPreferences(getString(R.string.app_name),0);
        boolean isCurrentUserFirstIn = pref.getBoolean(userInfo.getUserId(),true);
        if(isCurrentUserFirstIn) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(userInfo.getUserId(), false);
            editor.commit();
        }
        return isCurrentUserFirstIn;
    }

    private void login() {
        userInfo.setUserId(edtTxtUsername.getText().toString());
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "LOGIN");
            jo.put("USER_ID", edtTxtUsername.getText().toString());
            jo.put("PASSWORD", edtTxtPwd.getText().toString());
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.what = Global.FROM_LOGINACTIVITY;
        msg.obj = jo.toString();
        Log.i("login", jo.toString());
        CommThread.getInstance().commHandler.sendMessage(msg);
        Log.i("login", "send msg to comm thread");
    }

    private void findView() {
        edtTxtUsername = (EditText) findViewById(R.id.edtTxt_userName);
        edtTxtPwd = (EditText) findViewById(R.id.edtText_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);

        edtTxtUsername.setText("2013010918015");
        edtTxtPwd.setText("8682502101");
    }

    public boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permission);
                // 权限是否已经 授权 GRANTED---授权  DENIED---拒绝
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
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(requestCode == FACE_REGISTER&&resultCode==RESULT_OK){
            String result = intent.getStringExtra("REGISTER_RESULT");
            if(result.equals("SUCCESS")){
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();
            }else if(result.equals("FAILED")){
                Toast.makeText(this, "注册失败", Toast.LENGTH_SHORT).show();
            }
            startNewActivity(userInfo.getUserType());
        }
    }

    public void startNewActivity(String userType) {
        HashMap<String, String> userTypeToActivity = new HashMap<>();
        userTypeToActivity.put("STUDENT", "com.choco.limsclient.Activities.Student.MainActivity");
        userTypeToActivity.put("TEACHER", "com.choco.limsclient.Activities.Teacher.MainActivity");
        userTypeToActivity.put("LAB_ADMIN", "com.choco.limsclient.Activities.LabAdmin.MainActivity");
        try {
            Intent intent = new Intent(this,Class.forName(userTypeToActivity.get(userType)));
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 321: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startNewActivity(userInfo.getUserType());

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
