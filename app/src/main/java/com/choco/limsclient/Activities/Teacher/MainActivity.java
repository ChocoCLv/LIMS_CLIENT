package com.choco.limsclient.Activities.Teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.choco.limsclient.R;
import com.choco.limsclient.Util.Global;

public class MainActivity extends AppCompatActivity {

    Button btnPublishExperiment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);
        findView();
        setOnClickListener(newOnClickListener());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Global.userInfo.getUserName());
    }

    private void findView() {
        btnPublishExperiment = (Button) findViewById(R.id.btn_publishExperiment);
    }

    private View.OnClickListener newOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                switch (view.getId()) {
                    case R.id.btn_publishExperiment:
                        intent = new Intent(com.choco.limsclient.Activities.Teacher.MainActivity.this, PublishProjectActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };
    }

    private void setOnClickListener(View.OnClickListener listener) {
        btnPublishExperiment.setOnClickListener(listener);
    }
}
