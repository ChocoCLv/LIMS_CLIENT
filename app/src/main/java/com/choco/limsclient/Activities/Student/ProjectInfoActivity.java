package com.choco.limsclient.Activities.Student;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.choco.limsclient.Activities.UtilActivities.FaceRecognitionActivity;
import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.Util.Helper;
import com.choco.limsclient.Util.ProjectInfo;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import static com.choco.limsclient.Util.Global.FROM_STUDENT_PROJECT_INFO;
import static com.choco.limsclient.Util.Global.userInfo;

public class ProjectInfoActivity extends AppCompatActivity implements View.OnClickListener{
    private ProjectInfo info;
    private final int FACE_VERIFY = 0;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_info);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        info = new ProjectInfo(getIntent().getStringExtra("PROJECT_INFO"));
        ((TextView)findViewById(R.id.tv_projectName)).setText(info.getProjectName());
        ((TextView)findViewById(R.id.tv_teacherName)).setText(info.getTeacherId());
        ((TextView)findViewById(R.id.tv_projectDate)).setText(info.getProjectDate());
        ((TextView)findViewById(R.id.tv_projectTime)).setText(info.getProjectStartTime()+"-"+info.getProjectEndTime());
        ((TextView)findViewById(R.id.tv_projectLoc)).setText(info.getProjectLoc());

        findViewById(R.id.btn_signIn).setOnClickListener(this);
        findViewById(R.id.btn_changeProjectTime).setOnClickListener(this);

        SpeechUtility.createUtility(this,"appid="+getString(R.string.xunfei_appid));

        CommThread.getInstance().setHandler(newHandler());
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle(info.getCourseName());
    }

    public void onClick(View view){
        switch(view.getId()){
            case R.id.btn_signIn:
                Intent intent = new Intent(this, FaceRecognitionActivity.class);
                intent.putExtra("REQUEST_TYPE",Global.FACE_VERIFY);
                //TODO:用startActivityForResult得到签到结果
                startActivityForResult(intent,FACE_VERIFY);
                break;
            case R.id.btn_changeProjectTime:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent intent){
        if(requestCode == FACE_VERIFY&&resultCode==RESULT_OK){
            String result = intent.getStringExtra("VERIFY_RESULT");
            if(result.equals("SUCCESS")){
                //showTip("验证通过");
                signIn();
            }else if(result.equals("FAILED")){
                showTip("验证失败");
            }
        }
    }

    public void signIn(){
        JSONObject jo = new JSONObject();
        try{
            jo.put("REQUEST_TYPE","SIGN_IN");
            jo.put("STUDENT_ID",userInfo.getUserId());
            jo.put("TEACHER_ID",info.getTeacherId());
            jo.put("COURSE_NAME",info.getCourseName());
            jo.put("PROJECT_NAME",info.getProjectName());
            Message msg = new Message();
            msg.what = FROM_STUDENT_PROJECT_INFO;
            msg.obj = jo.toString();
            CommThread.getInstance().commHandler.sendMessage(msg);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private Handler newHandler(){
        return new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == Global.FROM_COMMTHREAD){
                    JSONObject resp;
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        resp = (JSONObject) jsonParser.nextValue();
                        String result = resp.getString("SIGNIN_RESULT");
                        if (result.equals("SUCCESS")) {
                            Toast.makeText(ProjectInfoActivity.this, "签到成功", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(ProjectInfoActivity.this, resp.getString("SIGNIN_DESCRIPTION"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
