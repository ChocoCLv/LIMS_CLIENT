package com.choco.limsclient.Activities.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.choco.limsclient.Activities.PickDateActivity;
import com.choco.limsclient.Activities.PickTimeActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.Util.StringParseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PublishProjectActivity extends AppCompatActivity {
    private static final int PICK_DATE = 1;
    private static final int PICK_START_TIME = 2;
    private static final int PICK_END_TIME = 3;
    Spinner spinnerCourseName;
    EditText edtProjectLoc;
    EditText edtProjectDate;
    EditText edtProjectName;
    EditText edtProjectStartTime;
    EditText edtProjectEndTime;
    String date;
    String startTime;
    String endTime;
    List<String> courseList;
    String courseName;
    Button btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_publish_project);

        findView();

        setOnClickListener(newOnClickListener());

        CommThread.getInstance().setHandler(newHandler());

        courseList = new ArrayList<>();

        spinnerCourseName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                courseName = spinnerCourseName.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getCourseListFromServer();
    }

    private void getCourseListFromServer() {
        Message msg = new Message();
        msg.what = Global.FROM_TEACHER_PUBLISHEEXPERIMENT;

        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "GET_COURSE_LIST");
            jo.put("TEACHER_ID", Global.userInfo.getUserId());

            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                    case R.id.edt_projectDate:
                        intent = new Intent(PublishProjectActivity.this, PickDateActivity.class);
                        startActivityForResult(intent, PICK_DATE);
                        break;
                    case R.id.edt_projectStartTime:
                        intent = new Intent(PublishProjectActivity.this, PickTimeActivity.class);
                        startActivityForResult(intent, PICK_START_TIME);
                        break;
                    case R.id.edt_projectEndTime:
                        intent = new Intent(PublishProjectActivity.this, PickTimeActivity.class);
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
        edtProjectStartTime.setOnClickListener(listener);
        edtProjectEndTime.setOnClickListener(listener);
        edtProjectDate.setOnClickListener(listener);
    }

    private void publishExperiment() {
        String projectName = edtProjectName.getText().toString();
        String expLoc = edtProjectLoc.getText().toString();
        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "PUBLISH_PROJECT");
            jo.put("COURSE_NAME", courseName);
            jo.put("PROJECT_NAME", projectName);
            jo.put("PROJECT_LOC", expLoc);
            jo.put("PROJECT_DATE", date);
            jo.put("PROJECT_START_TIME", startTime);
            jo.put("PROJECT_END_TIME", endTime);
            jo.put("TEACHER_ID", Global.userInfo.getUserId());
            Message msg = new Message();
            msg.what = Global.FROM_TEACHER_PUBLISHEEXPERIMENT;
            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void findView() {
        edtProjectDate = (EditText) findViewById(R.id.edt_projectDate);
        edtProjectEndTime = (EditText) findViewById(R.id.edt_projectEndTime);
        edtProjectLoc = (EditText) findViewById(R.id.edt_projectLoc);
        edtProjectName = (EditText) findViewById(R.id.edt_projectName);
        spinnerCourseName = (Spinner) findViewById(R.id.spinner_courseName);
        edtProjectStartTime = (EditText) findViewById(R.id.edt_projectStartTime);
        btnPublish = (Button) findViewById(R.id.btn_publish);
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
                        String result;
                        if (resp.has("PUBLISH_RESULT")) {
                            result = resp.getString("PUBLISH_RESULT");
                            if (result.equals("SUCCESS")) {
                                Toast.makeText(PublishProjectActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(PublishProjectActivity.this, resp.getString("PUBLISH_DESCRIPTION"), Toast.LENGTH_SHORT).show();
                            }
                        } else if (resp.has("GET_RESULT")) {
                            result = resp.getString("GET_RESULT");
                            if (result.equals("SUCCESS")) {
                                JSONArray cl = resp.getJSONArray("COURSE_ARRAY");
                                updateCourseSpinner(cl);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        return handler;
    }

    private void updateCourseSpinner(JSONArray ca) {
        courseList = StringParseHelper.convertJSONArrayToList(ca);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1,courseList);
        spinnerCourseName.setAdapter(spinnerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case PICK_DATE:
                String year = intent.getStringExtra("year");
                String month = intent.getStringExtra("month");
                String day = intent.getStringExtra("day");
                date = year + "-" + month + "-" + day;
                edtProjectDate.setText(date);
                break;
            case PICK_START_TIME:
                String shour = intent.getStringExtra("hour");
                String smin = intent.getStringExtra("minute");
                startTime = shour + ":" + smin;
                edtProjectStartTime.setText(startTime);
                break;
            case PICK_END_TIME:
                String ehour = intent.getStringExtra("hour");
                String emin = intent.getStringExtra("minute");
                endTime = ehour + ":" + emin;
                edtProjectEndTime.setText(endTime);
                break;
        }
    }
}
