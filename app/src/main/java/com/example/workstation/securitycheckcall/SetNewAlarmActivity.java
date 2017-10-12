package com.example.workstation.securitycheckcall;

import android.app.TimePickerDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/* This activity will serve adding new alarms only
* User may set the name, occurrence and time when
* the alarm supposed to start. */
public class SetNewAlarmActivity extends AppCompatActivity {

    // true when user set the alarm using time picker
    private boolean isAlarmTimeSet;
    private EditText alarmName;
    private EditText occurrence;
    private TextView textAlarm;
    private Typeface typeface;
    private LinearLayout myHorizontalAlarms;
    private ImageView setTimeButton;
    private ImageButton decrement;
    private ImageButton increment;
    private AlarmDetails myAlarmDetails;
    // this list is going to be saved and read from file
    private List<AlarmDetails> myListOfAlarmDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_alarm);

        setTimeButton = (ImageView) findViewById(R.id.imgviewSetTime);
        decrement = (ImageButton) findViewById(R.id.ibtnDecrementOccurrence);
        increment = (ImageButton) findViewById(R.id.ibtnIncrementOccurrence);
        alarmName = (EditText) findViewById(R.id.edtAlarmName);
        occurrence = (EditText) findViewById(R.id.edtOccurrence);
        myHorizontalAlarms = (LinearLayout) findViewById(R.id.listFollowingAlarms);
        textAlarm = (TextView) findViewById(R.id.txtAlarmTime);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/digital7.ttf");
        textAlarm.setTypeface(typeface);
    }

    /* Get the time when alarms should start from user and add display in app as text */
    public void takeTime(View view) {

        // Get Current time
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // check if occurrence is not empty otherwise inform the user
        // isEmpty returns true if length is 0 otherwise false
        if (occurrence.getText().toString().isEmpty() || alarmName.getText().toString().isEmpty()) {
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

        int hour = hourOfDay;

        // populate horizontal scroll view
        myHorizontalAlarms.removeAllViews();
        for (int i = 0; i < occurrenceNumber; i++) {
            // midnight, therefore clock needs to reset to value 0
            if (hour == 23) hour = 0;
            else hour++;
            TextView textView = new TextView(this);
            textView.setTypeface(typeface);
            textView.setWidth(100);
            textView.setTextSize(28);
            textView.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
            // set the color for textview
            int timeColor = ContextCompat.getColor(this, R.color.colorAccent);
            textView.setTextColor(timeColor);
            // adding new textview components with time of next alarm
            myHorizontalAlarms.addView(textView);
            // add next times
        }
        // set the value as true when user set the alarm time
        isAlarmTimeSet = true;
        myAlarmDetails = new AlarmDetails(alarmName.getText().toString(), hourOfDay, minute, occurrenceNumber);
        // set the components disabled
        alarmName.setEnabled(false);
        occurrence.setEnabled(false);
        setTimeButton.setEnabled(false);
        decrement.setEnabled(false);
        increment.setEnabled(false);
    }

    /* This method will save a new alarm after clicking on button "Save New Alarm" */
    public void saveAlarm_buttonOnClick(View view) {

        // check if all data input has been done by user
        if (!isAlarmTimeSet || alarmName.getText().toString().isEmpty()
                || occurrence.getText().toString().trim().length() == 0) {
            Toast.makeText(SetNewAlarmActivity.this, R.string.fillUp,
                    Toast.LENGTH_LONG).show();
            return;
        }

        myListOfAlarmDetails = new ArrayList<AlarmDetails>();
        // check if file exist in internal storage
        File file = new File(this.getFilesDir(),getResources().getString(R.string.fileWithListofAlarms));
        // read the data from file with alarms details
        if (file.exists()) myListOfAlarmDetails = readList();

        // add an object AlarmDetails to list which will be saved in file
        myListOfAlarmDetails.add(myAlarmDetails);

        // here the method to start saving the alarm
        writeList(myListOfAlarmDetails);
        Toast.makeText(SetNewAlarmActivity.this, R.string.alarmSaved, Toast.LENGTH_LONG).show();
        finish();
    }

    /* This method increment the number of occurrence
    * The only acceptable value is be between 1 and 36.
     *  If edit text is empty, onclick event set the value 11.
     *  If the value is higher than 36, onclick event set the value 36. */
    public void addOccurrence(View view) {

        if (occurrence.getText().toString().isEmpty()) occurrence.setText("12");
        else {
            int temp = Integer.parseInt(occurrence.getText().toString());
            if (temp < 36) {
                temp++;
                occurrence.setText(Integer.toString(temp));
            } else occurrence.setText("36");
        }
    }

    /* This method decrement the number of occurrence in edit text field
    *  The only acceptable value is be between 1 and 36 other wise the value of 1 will set.
     *  If edit text is empty, onclick event set the value 11. */
    public void substractOccurrence(View view) {

        if (occurrence.getText().toString().isEmpty()) occurrence.setText("12");
        else {
            int temp = Integer.parseInt(occurrence.getText().toString());
            if (temp > 1 && temp < 37) {
                temp--;
                occurrence.setText(Integer.toString(temp));
            } else occurrence.setText("1");
        }
    }

    /* This method reset all data input fields like editview.
    * It also remove all textview from list listFollowingAlarms and
     * make the components enable again. */
    public void resetAll(View view) {

        isAlarmTimeSet = false;
        alarmName.setText("");
        occurrence.setText("");
        textAlarm.setText("00:00");
        myHorizontalAlarms.removeAllViews();
        alarmName.setEnabled(true);
        occurrence.setEnabled(true);
        setTimeButton.setEnabled(true);
        decrement.setEnabled(true);
        increment.setEnabled(true);
    }

    /* Method to save list of AlarmDetails into file on internal storage */
    private void writeList(List<AlarmDetails> myListOfAlarmDetails) {
        try {
            FileOutputStream fos = openFileOutput(getResources().getString(R.string.fileWithListofAlarms), MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(myListOfAlarmDetails);
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Method to read list of AlarmDetails from file on internal storage */
    private List<AlarmDetails> readList(){
        List<AlarmDetails> tempList = null;
        try {
            FileInputStream fis = openFileInput(getResources().getString(R.string.fileWithListofAlarms));
            ObjectInputStream is = new ObjectInputStream(fis);
            tempList = (List<AlarmDetails>) is.readObject();
            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tempList;
    }
}
