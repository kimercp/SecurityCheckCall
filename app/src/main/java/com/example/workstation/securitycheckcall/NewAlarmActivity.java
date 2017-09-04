package com.example.workstation.securitycheckcall;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

//import android.content.Context;

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
//        EditText edtAlarmName = (EditText)findViewById(R.id.edtAlarmName);
//        EditText edtOccurrence = (EditText)findViewById(R.id.edtOccurrence);

        // display the message
        Toast.makeText(NewAlarmActivity.this, R.string.alarmSaved, Toast.LENGTH_LONG).show();
    }


    // set up the time when alarms should start
    public void takeTime(View view) {

        final TextView textTime = (TextView)findViewById(R.id.textView2);
        // Get Current time
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(NewAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    // Called when the user is done setting a new time and the dialog has closed
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String tempString = getText(R.string.alarmStartAt).toString();
                        textTime.setText(tempString + " " +hourOfDay + ":" +minute);
                    }
                }, hour, minute, true).show();
    }
}
