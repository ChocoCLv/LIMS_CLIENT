package com.choco.limsclient.Activities.LabAdmin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.choco.limsclient.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labadmin_main);
        setTitle("学生姓名学号");
    }
}
