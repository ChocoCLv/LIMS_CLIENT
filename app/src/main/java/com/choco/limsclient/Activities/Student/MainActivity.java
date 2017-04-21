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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        findView();

        btnBorrowDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.choco.limsclient.Activities.Student.MainActivity.this, BorrowDeviceActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setTitle(Global.userInfo.getUserName());
    }

    private void findView() {
        btnBorrowDevice = (Button) findViewById(R.id.btn_borrowDevice);
    }
}
