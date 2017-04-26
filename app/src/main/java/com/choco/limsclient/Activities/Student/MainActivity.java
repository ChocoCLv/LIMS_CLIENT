package com.choco.limsclient.Activities.Student;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;

public class MainActivity extends AppCompatActivity {
    Button btnBorrowDevice;
    Button btnCheckProjectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        findView();

        setOnClickListener(newOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Global.userInfo.getUserName());
    }

    private View.OnClickListener newOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_borrowDevice:
                        intent = new Intent(com.choco.limsclient.Activities.Student.MainActivity.this, BorrowDeviceActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.btn_checkProjectInfo:
                        intent = new Intent(com.choco.limsclient.Activities.Student.MainActivity.this, ProjectsInfoListActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
        return listener;
    }

    private void setOnClickListener(View.OnClickListener listener) {
        btnBorrowDevice.setOnClickListener(listener);
        btnCheckProjectInfo.setOnClickListener(listener);
    }

    private void findView() {
        btnBorrowDevice = (Button) findViewById(R.id.btn_borrowDevice);
        btnCheckProjectInfo = (Button) findViewById(R.id.btn_checkProjectInfo);
    }
}
