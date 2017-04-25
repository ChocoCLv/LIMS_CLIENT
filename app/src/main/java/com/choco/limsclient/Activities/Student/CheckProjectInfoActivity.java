package com.choco.limsclient.Activities.Student;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.choco.limsclient.CommModule.CommThread;
import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;
import com.choco.limsclient.Util.ProjectInfo;
import com.choco.limsclient.Adapter.StudentProjectInfoListAdapter;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

public class CheckProjectInfoActivity extends AppCompatActivity {

    ListView lvProjectInfo;
    ArrayList<ProjectInfo> listProjectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_check_project_info);

        listProjectInfo = new ArrayList<>();
        lvProjectInfo = (ListView) findViewById(R.id.lv_projectInfo);
        CommThread.getInstance().setHandler(newHandler());
        getCoursesInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle("实验项目列表");
    }

    private void getCoursesInfo() {

        JSONObject jo = new JSONObject();
        try {
            jo.put("REQUEST_TYPE", "GET_PROJECT_INFO");
            jo.put("STUDENT_ID", Global.userInfo.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message msg = new Message();
        msg.what = Global.FROM_STUDENT_CHECK_PROJECT;
        msg.obj = jo.toString();
        CommThread.getInstance().commHandler.sendMessage(msg);
    }

    private Handler newHandler() {
        return new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Global.FROM_COMMTHREAD) {
                    JSONObject resp;
                    try {
                        JSONTokener jsonParser = new JSONTokener(msg.obj.toString());
                        resp = (JSONObject) jsonParser.nextValue();
                        String result = resp.getString("GET_RESULT");
                        if (result.equals("SUCCESS")) {
                            String infoList = resp.getString("PROJECTS_INFO");
                            parseProjectInfoList(infoList);
                            updateListView();
                        } else {
                            Toast.makeText(CheckProjectInfoActivity.this, "获取失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void parseProjectInfoList(String pil) {
        String[] infoList = pil.split(";");
        for (String info : infoList) {
            String[] infoContents = info.split(",");
            ProjectInfo pi = new ProjectInfo(infoContents[0], infoContents[1], infoContents[2],
                    infoContents[3], infoContents[4], infoContents[5], infoContents[6]);
            listProjectInfo.add(pi);
        }
    }

    private void updateListView() {
        String[] tags = {"courseName", "projectName", "projectDateTime", "projectLoc"};
        int itemIDs[] = {R.id.tv_courseName, R.id.tv_projectName, R.id.tv_projectDateTime, R.id.tv_projectLoc};
        StudentProjectInfoListAdapter adapter = new StudentProjectInfoListAdapter(
                this, listProjectInfo, tags, R.layout.item_student_experiment_info, itemIDs);
        lvProjectInfo.setAdapter(adapter);
    }
}
