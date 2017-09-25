package com.example.workstation.securitycheckcall;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/* This activity will serve adding new alarms only
* User may set the name, occurrence and time when
* the alarm supposed to start. */
public class SetNewAlarmActivity extends AppCompatActivity {


    // edit text field with name of the alarm
    private EditText alarmName;
    private EditText occurrence;
    private TextView textAlarm;
    // true when user set the alarm using time picker
    boolean isAlarmTimeSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_alarm);
        alarmName = (EditText) findViewById(R.id.edtAlarmName);
        // default text in edit text field service (put default text if is empty)
        alarmName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    alarmName.setText("");
                } else {
                    if (alarmName.getText().toString().isEmpty())
                        alarmName.setText(R.string.typeTheAlarmName);
                }
            }
        });
    }

    /* Get the time when alarms should start from user and add display in app as text */
    public void takeTime(View view) {

        textAlarm = (TextView) findViewById(R.id.txtAlarmTime);
        occurrence = (EditText) findViewById(R.id.edtOccurrence);
        // Get Current time
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // check if occurrence is not empty otherwise inform the user
        if (occurrence.getText().toString().isEmpty()) {
            Toast.makeText(SetNewAlarmActivity.this, R.string.occurrenceFirst,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // display date time picker for user to choose time
        new TimePickerDialog(SetNewAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            // Called when the user is done setting a new time and the dialog has closed
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // display the text as time in text view
                textAlarm.setText(hourOfDay + ":" + minute);
                // add following alarm's times into the list
                displayFollowingAlarms(hourOfDay, minute, Integer.parseInt(occurrence.getText().toString()));
            }
        }, hour, minute, true).show();
    }

    /* This method will add set of times of alarms to list, begin from following time and depends from occurrence */
    private void displayFollowingAlarms(int hourOfDay, int minute, int occurrenceNumber) {

        // populate horizontal scroll view
        LinearLayout myHorizontalAlarms = (LinearLayout)findViewById(R.id.listFollowingAlarms);
        for (int i=1; i <occurrenceNumber; i++){
            if (hourOfDay == 23) hourOfDay = 0;
            else hourOfDay++;
            TextView textView = new TextView(this);
            textView.setText(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
            textView.setWidth(100);
            textView.setTextSize(20);
            myHorizontalAlarms.addView(textView);
        }

        // set the value as true when user set the alarm time
        isAlarmTimeSet = true;
    }

    /* This method will save a new alarm after clicking on button "Save New Alarm" */
    public void saveAlarm_buttonOnClick(View view) {

        occurrence = (EditText) findViewById(R.id.edtOccurrence);

        if (!isAlarmTimeSet || alarmName.getText().toString().isEmpty()
                || occurrence.getText().toString().trim().length()==0) {
            Toast.makeText(SetNewAlarmActivity.this, R.string.fillUp,
                    Toast.LENGTH_LONG).show();
            return;
        }
        // display the message
        Toast.makeText(SetNewAlarmActivity.this, R.string.alarmSaved, Toast.LENGTH_LONG).show();
    }
}
