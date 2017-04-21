package com.choco.limsclient.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;

import com.choco.limsclient.R;

import java.util.Calendar;

public class PickDateActivity extends AppCompatActivity {

    DatePicker datePicker;
    private int year;
    private int month;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_date);


        datePicker = (DatePicker) findViewById(R.id.datePicker);
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        datePicker.init(year, month, day, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                PickDateActivity.this.year = i;
                PickDateActivity.this.month = i1 + 1;
                PickDateActivity.this.day = i2;
            }
        });

        (findViewById(R.id.btn_confirmDate)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                intent.putExtra("year", String.valueOf(year));
                intent.putExtra("month", String.valueOf(month));
                intent.putExtra("day", String.valueOf(day));
                PickDateActivity.this.setResult(1, intent);
                PickDateActivity.this.finish();
            }
        });
    }
}
