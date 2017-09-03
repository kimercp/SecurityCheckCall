package com.example.workstation.securitycheckcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TimePicker;

/* This activity will serve adding new alarms only
* User may set the name, occurrence and time when
* the alarm supposed to start. */
public class NewAlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
    }

    /* This method will save a new alarm after
      clicking on button "Save New Alarm"      */
    public void saveAlarm_buttonOnClick(){
        EditText edtAlarmName = (EditText)findViewById(R.id.edtAlarmName);
        EditText edtOccurrence = (EditText)findViewById(R.id.edtOccurrence);
        TimePicker tmpAlarmStart = (TimePicker)findViewById(R.id.tmpAlarmStart);

    }
}
