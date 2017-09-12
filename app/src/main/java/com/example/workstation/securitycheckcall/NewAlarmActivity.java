package com.example.workstation.securitycheckcall;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/* This activity will serve adding new alarms only
* User may set the name, occurrence and time when
* the alarm supposed to start. */
public class NewAlarmActivity extends AppCompatActivity {

    // defining a string adapter which will handle the data of the list view
    private ArrayAdapter<String> arrayAdapter;
    // list of strings which will serve as list items
    private List<String> arrayList = new ArrayList<String>();
    // edit text field with name of the alarm
    private EditText alarmName;
    private EditText occurrence;
    private TextView textAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

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

        ListView list = (ListView) findViewById(R.id.lstFollowingAlarms);
        // Array adapter takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        arrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, arrayList);
        list.setAdapter(arrayAdapter);
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
        if (TextUtils.isEmpty(occurrence.getText().toString())) {
            Toast.makeText(NewAlarmActivity.this, R.string.occurrenceFirst,
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // display date time picker for user to choose time
        new TimePickerDialog(NewAlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        // arrayList needs to be empty before adding items
        arrayList.clear();
        // loop starts from 1 and adds value i to hourOfDay
        for (int i = 1; i < occurrenceNumber; i++) {
            // reset hours if midnight
            if (hourOfDay == 23) hourOfDay = 0;
            else hourOfDay++;
            // add new item
            arrayList.add(Integer.toString(hourOfDay) + ":" + Integer.toString(minute));
        }
        // notify adapter about change
        arrayAdapter.notifyDataSetChanged();
    }

    /* This method will save a new alarm after clicking on button "Save New Alarm" */
    public void saveAlarm_buttonOnClick(View view) {

        // check if alarm name is empty
        if (TextUtils.isEmpty(alarmName.getText().toString())) {
            Toast.makeText(NewAlarmActivity.this, R.string.typeTheAlarmName,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // add validate of occurence following alrms etc.
        // maybe when user clicks on time , activate save alarm button
        // or check all fields

        // display the message
        Toast.makeText(NewAlarmActivity.this, R.string.alarmSaved, Toast.LENGTH_LONG).show();
    }
}
