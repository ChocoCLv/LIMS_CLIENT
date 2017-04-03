package com.choco.limsclient.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.choco.limsclient.Activities.Student.MainActivity;
import com.choco.limsclient.R;
import com.choco.limsclient.CommModule.*;
import com.choco.limsclient.Util.Global;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.HashMap;


public class LoginActivity extends Activity {


    EditText edtTxtUsername;
    EditText edtTxtPwd;
    Button btnLogin;
    CommThread comm;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findView();
        comm = CommThread.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //login();
                startNewActivity("STUDENT");
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Global.FROM_COMMTHREAD){
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        JSONObject resp =  (JSONObject)jsonParser.nextValue();
                        String loginResult = resp.getString("LOGIN_STATUS");
                        if(loginResult.equals("SUCCESS")){
                            Log.i("login","登录成功");
                            String userType = resp.getString("USERTYPE");
                            startNewActivity(userType);
                        }else if(loginResult.equals("FAILED")){
                            Log.i("login","登录失败");
                            Toast.makeText(LoginActivity.this,"Login failed.Please check your username and password",Toast.LENGTH_LONG)
                                    .show();
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        comm.setHandler(handler);
        new Thread(comm).start();
    }

    private void startNewActivity(String userType){
        HashMap<String,String> userTypeToActivity = new HashMap<>();
        userTypeToActivity.put("STUDENT","com.choco.limsclient.Activities.Student.MainActivity");
        userTypeToActivity.put("TEACHER","com.choco.limsclient.Activities.Teacher.MainActivity");
        try{
            Intent intent = new Intent(LoginActivity.this,Class.forName(userTypeToActivity.get(userType)));
            startActivity(intent);
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void login(){

        JSONObject jo =  new JSONObject();
        try {
            jo.put("username", edtTxtUsername.getText().toString());
            jo.put("password", edtTxtPwd.getText().toString());
        }catch (org.json.JSONException e){
            e.printStackTrace();
        }

        Message msg = new Message();
        msg.what = Global.FROM_UITHREAD;
        msg.obj = jo.toString();
        Log.i("login",jo.toString());
        comm.rcvHandler.sendMessage(msg);
        Log.i("login","send msg to comm thread");

    }

    private  void findView(){
        edtTxtUsername = (EditText)findViewById(R.id.edtTxt_userName);
        edtTxtPwd=(EditText)findViewById(R.id.edtText_pwd);
        btnLogin=(Button)findViewById(R.id.btn_login);

        edtTxtUsername.setText("2013010918015");
        edtTxtPwd.setText("8682502101");
    }

}
