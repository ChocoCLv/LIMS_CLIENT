package com.choco.limsclient.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.choco.limsclient.R;
import com.choco.limsclient.CommModule.*;
import com.choco.limsclient.Util.Global;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends Activity {


    EditText edtTxtUsername;
    EditText edtTxtPwd;
    Button btnLogin;
    CommThread comm;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        comm = CommThread.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Global.FROM_COMMTHREAD){

                }
            }
        };

        comm.setHandler(handler);
        new Thread(comm).start();

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
