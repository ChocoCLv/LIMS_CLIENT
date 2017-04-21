package com.choco.limsclient.Activities.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.choco.limsclient.Activities.PickDateActivity;
import com.choco.limsclient.Activities.PickTimeActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class PublishExperimentActivity extends AppCompatActivity {
    private static final int PICK_DATE = 1;
    private static final int PICK_START_TIME = 2;
    private static final int PICK_END_TIME = 3;
    EditText edtExperimentName;
    EditText edtExperimentLoc;
    EditText edtExperimentDate;
    EditText edtExperimentStartTime;
    EditText edtExperimentEndTime;
    String date;
    String startTime;
    String endTime;
    Button btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_publish_experiment);

        findView();

        setOnClickListener(newOnClickListener());

        CommThread.getInstance().setHandler(newHandler());
    }

    private View.OnClickListener newOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_publish:
                        publishExperiment();
                        break;
                    case R.id.edt_experimentDate:
                        intent = new Intent(PublishExperimentActivity.this, PickDateActivity.class);
                        startActivityForResult(intent, PICK_DATE);
                        break;
                    case R.id.edt_experimentStartTime:
                        intent = new Intent(PublishExperimentActivity.this, PickTimeActivity.class);
                        startActivityForResult(intent, PICK_START_TIME);
                        break;
                    case R.id.edt_experimentEndTime:
                        intent = new Intent(PublishExperimentActivity.this, PickTimeActivity.class);
                        startActivityForResult(intent, PICK_END_TIME);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void setOnClickListener(View.OnClickListener listener) {
        btnPublish.setOnClickListener(listener);
        edtExperimentStartTime.setOnClickListener(listener);
        edtExperimentEndTime.setOnClickListener(listener);
        edtExperimentDate.setOnClickListener(listener);
    }

    private void publishExperiment() {
        String expName = edtExperimentName.getText().toString();
        String expLoc = edtExperimentLoc.getText().toString();
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE","PUBLISH_EXPERIMENT");
            jo.put("EXPERIMENT_NAME", expName);
            jo.put("EXPERIMENT_LOC",expLoc);
            jo.put("EXPERIMENT_DATE",date);
            jo.put("EXPERIMENT_START_TIME",startTime);
            jo.put("EXPERIMENT_END_TIME",endTime);
            jo.put("TEACHER_ID",Global.userInfo.getUserId());
            Message msg = new Message();
            msg.what = Global.FROM_TEACHER_PUBLISHEEXPERIMENT;
            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void findView() {
        edtExperimentDate = (EditText) findViewById(R.id.edt_experimentDate);
        edtExperimentEndTime = (EditText) findViewById(R.id.edt_experimentEndTime);
        edtExperimentLoc = (EditText) findViewById(R.id.edt_experimentLoc);
        edtExperimentName = (EditText) findViewById(R.id.edt_experimentName);
        edtExperimentStartTime = (EditText) findViewById(R.id.edt_experimentStartTime);
        btnPublish = (Button) findViewById(R.id.btn_publish);
    }

    private Handler newHandler(){
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    JSONObject resp;
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        resp = (JSONObject) jsonParser.nextValue();
                        String result = resp.getString("PUBLISH_RESULT");
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(PublishExperimentActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PublishExperimentActivity.this, resp.getString("PUBLISH_DESCRIPTION"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICK_DATE:
                String year = intent.getStringExtra("year");
                String month = intent.getStringExtra("month");
                String day = intent.getStringExtra("day");
                date = year + "-" + month + "-" + day;
                edtExperimentDate.setText(date);
                break;
            case PICK_START_TIME:
                String shour = intent.getStringExtra("hour");
                String smin = intent.getStringExtra("minute");
                startTime = shour + ":" + smin;
                edtExperimentStartTime.setText(startTime);
                break;
            case PICK_END_TIME:
                String ehour = intent.getStringExtra("hour");
                String emin = intent.getStringExtra("minute");
                endTime = ehour + ":" + emin;
                edtExperimentEndTime.setText(endTime);
                break;
        }
    }
}
