package com.choco.limsclient.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TimePicker;

import com.choco.limsclient.R;

import java.util.Calendar;

public class PickTimeActivity extends AppCompatActivity {

    TimePicker timePicker;
    private int hour;
    private int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_time);

        timePicker = (TimePicker) findViewById(R.id.timePicker);
        Calendar c = Calendar.getInstance();
        hour = c.get(Calendar.HOUR);
        minute = c.get(Calendar.MINUTE);

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                PickTimeActivity.this.hour = i;
                PickTimeActivity.this.minute = i1;
            }
        });

        (findViewById(R.id.btn_confirmTime)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("hour", String.valueOf(hour));
                intent.putExtra("minute", String.valueOf(minute));
                PickTimeActivity.this.setResult(0, intent);
                PickTimeActivity.this.finish();
            }
        });
    }
}
