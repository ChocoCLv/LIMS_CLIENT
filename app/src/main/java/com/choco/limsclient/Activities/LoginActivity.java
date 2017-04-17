package com.choco.limsclient.Activities;

import android.content.Intent;
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
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;

import static com.choco.limsclient.Util.Global.userInfo;

public class LoginActivity extends AppCompatActivity {

    EditText edtTxtUsername;
    EditText edtTxtPwd;
    Button btnLogin;
    CommThread comm;
    Handler handler;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("LIMS-实验室管理系统");
        findView();
        comm = CommThread.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
                //startNewActivity("STUDENT");
                //startNewActivity("LAB_ADMIN");
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
                            Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_LONG)
                                    .show();
                            username = resp.getString("USER_NAME");
                            String userType = resp.getString("USERTYPE");

                            userInfo.setUserName(username);
                            userInfo.setUserType(userType);

                            startNewActivity(userType);
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
        comm.setHandler(handler);
        new Thread(comm).start();
    }

    private void startNewActivity(String userType) {
        HashMap<String, String> userTypeToActivity = new HashMap<>();
        userTypeToActivity.put("STUDENT", "com.choco.limsclient.Activities.Student.MainActivity");
        userTypeToActivity.put("TEACHER", "com.choco.limsclient.Activities.Teacher.MainActivity");
        userTypeToActivity.put("LAB_ADMIN", "com.choco.limsclient.Activities.LabAdmin.MainActivity");
        try {
            Intent intent = new Intent(LoginActivity.this, Class.forName(userTypeToActivity.get(userType)));
            startActivity(intent);
            this.finish();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
        comm.commHandler.sendMessage(msg);
        Log.i("login", "send msg to comm thread");
    }

    private void findView() {
        edtTxtUsername = (EditText) findViewById(R.id.edtTxt_userName);
        edtTxtPwd = (EditText) findViewById(R.id.edtText_pwd);
        btnLogin = (Button) findViewById(R.id.btn_login);

        edtTxtUsername.setText("123");
        edtTxtPwd.setText("admin");
    }
}
