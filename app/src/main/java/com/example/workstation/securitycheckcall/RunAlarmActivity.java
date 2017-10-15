package com.example.workstation.securitycheckcall;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class RunAlarmActivity extends AppCompatActivity {

    private Typeface typeface;
    private TextView txtLeftMinute;
    private TextView txtCurrentTime;
    private TextView txtNextAlarm;
    private LinearLayout myHorizontalAlarms;
    // Handler for timer
    private Handler customHandler = new Handler();
    private ArrayList<String> listAllAlarms;
    // variables to check the current time and time of alarms from the list
    private int currentHour;
    private int currentMinute;
    private int alarmMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_alarm);

        myHorizontalAlarms = (LinearLayout) findViewById(R.id.listFollowingAlarms);
        // set the font for text views
        txtLeftMinute = (TextView) findViewById(R.id.txtLeftMinute);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtNextAlarm = (TextView) findViewById(R.id.txtNextAlarm);
        typeface = Typeface.createFromAsset(getAssets(), "fonts/digital7.ttf");
        txtLeftMinute.setTypeface(typeface);
        txtCurrentTime.setTypeface(typeface);
        txtNextAlarm.setTypeface(typeface);

        // Using getSerializableExtra(String key) method
        AlarmDetails alarm = (AlarmDetails) getIntent().getSerializableExtra("serializeData");
        displayFollowingAlarms(alarm.getHourOfDay(), alarm.getMinuteOfHour(), alarm.getOccurrence());

        // Timer for change the current time on text view every currentMinute
        customHandler.removeCallbacks(mUpdateTimeTask);
        customHandler.postDelayed(mUpdateTimeTask, 100);

        /* Check current time and alarms times
        If the current time is after first alarm ask an user
         if he wants to start from following alarm or next day.
         Calculate how many minutes left to next alarm and alarm after the next one.
         */

        // current time
        String timeNow = getCurrentTime();
        // cut the string to get separate number to compare hours and minutes
        String[] nowArray = timeNow.split(":");
        currentHour = Integer.parseInt(nowArray[0]);
        currentMinute = Integer.parseInt(nowArray[1]);

        // list of all alarms
        listAllAlarms = alarm.getFollowingAlarms();

        // first alarm from the list
        String firstAlarm = listAllAlarms.get(0);
        String[] firstParts = firstAlarm.split(":");
        int firstHour = Integer.parseInt(firstParts[0]);
        alarmMinute = Integer.parseInt(firstParts[1]);

        // last alarm from the list
        String lastAlarm = listAllAlarms.get(listAllAlarms.size() - 1);
        String[] secondParts = lastAlarm.split(":");
        int lastHour = Integer.parseInt(secondParts[0]);

        /* Algorithm to check if current time is between the first and last alarm
        1. take current time
        2. check if current hour is in the list of alarms saved by user if NO go to 8
        3. is current hour first from the list? YES got to 4, NO go to 5
        4. is current minute less than minute from alarm? YES go to 8, NO go to 7
        5. is current hour last from the list? YES go to 6, NO go to 7
        6. is current minute more than minute from alarm? YES go to 8, NO go to 7
        7. ask user if he wants to start nearest alarm? YES find nearest alarm, NO go to 8
        8. start first alarm from the list
         */

        if (isHourInList(currentHour) && (listAllAlarms.size() != 1)) {
            if (currentHour == firstHour) {
                if (currentMinute < alarmMinute) {
                    beginning();
                } else {
                    askUser();
                }
            } else {
                if (currentHour == lastHour) {
                    if (currentMinute > alarmMinute) {
                        beginning();
                    } else {
                        askUser();
                    }
                } else {
                    askUser();
                }
            }
        } else {
            beginning();
        }
    }

    /* This method will add set of times of alarms to list, begin from following time and depends from occurrence */
    private void displayFollowingAlarms(int hourOfDay, int minute, int occurrenceNumber) {

        int hour = hourOfDay;
        String hourDisplayFormat = "";
        String minuteDisplayFormat = "";

        // populate horizontal scroll view
        myHorizontalAlarms.removeAllViews();
        for (int i = 0; i < occurrenceNumber-1; i++) {
            // midnight, therefore clock needs to reset to value 0
            if (hour == 23) hour = 0;
            else hour++;
            TextView textView = new TextView(this);
            textView.setTypeface(typeface);
            textView.setWidth(150);
            textView.setTextSize(28);

            // format time output
            if (hour < 10) hourDisplayFormat = "0" + hour;
            else hourDisplayFormat = "" + hour;
            if (minute < 10) minuteDisplayFormat = "0" + minute;
            else minuteDisplayFormat = "" + minute;
            textView.setText(hourDisplayFormat + ":" + minuteDisplayFormat);

            // set the color for textview
            int timeColor = ContextCompat.getColor(this, R.color.colorAccent);
            textView.setTextColor(timeColor);
            // adding new textview components with time of next alarm
            myHorizontalAlarms.addView(textView);
        }
    }

    // Timer to check the time every seconds
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            // Update the time in textview
            txtCurrentTime.setText(getCurrentTime());
            customHandler.postDelayed(this, 1000);
        }
    };

    /* Return the current time */
    private String getCurrentTime() {
        // Take the current time and display this in text view in format 22:45
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        // Create a calendar object
        Calendar c = Calendar.getInstance();
        return formatter.format(c.getTime());
    }

    /* Check if a given integer as current hour is on the list, returns true if found */
    private boolean isHourInList(int passedCurrentHour) {
        for (int i = 0; i < listAllAlarms.size(); i++) {
            String alarmFormat = listAllAlarms.get(i);
            String[] parts = alarmFormat.split(":");
            int hour = Integer.parseInt(parts[0]);
            if (passedCurrentHour == hour) return true;
        }
        return false;
    }

    private void beginning() {
        Toast.makeText(this, "Alarm will start from beginning", Toast.LENGTH_SHORT).show();
        // run firstalarm from the list

        // countdown timer for alarms this is only for test (pozniej to wykasuj jak zrobisz do godzin
//        new CountDownTimer(60000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                int minutesLeft = (int) millisUntilFinished / 1000;
//                txtLeftMinute.setText(Integer.toString(countdownMinutes)+":"+seconds);
//                if (minutesLeft % 5 == 0)
//                    Toast.makeText(RunAlarmActivity.this, Integer.toString(minutesLeft), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFinish() {
//                txtLeftMinute.setText("Done");
//            }
//        }.start();
    }

    private void askUser() {
        Toast.makeText(this, "Ask user", Toast.LENGTH_SHORT).show();

        // calculate how many minutes left to next alarm
        int countdownMinutes;
        if (currentMinute < alarmMinute)
            countdownMinutes = alarmMinute - currentMinute;
        else countdownMinutes = 60 - currentMinute + alarmMinute;

        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("ss");
        // Create a calendar object
        Calendar c = Calendar.getInstance();
        int seconds = Integer.parseInt(formatter.format(c.getTime()));

        // pass minutes and seconds to countdown
        countdown(((countdownMinutes - 1) * 60000) + ((60 - seconds) * 1000));

        // calculate next alarm
 tu musisz policzyc nastepny alarm Lub to usunac zeby zakonczyc te aplikacje

    }

    private void countdown(int miliSeconds) {
        // countdown timer for alarms this is only for test (pozniej to wykasuj jak zrobisz do godzin
        new CountDownTimer(miliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int) millisUntilFinished / 60000;
                int secondsLeft = (int) (millisUntilFinished % 60000) / 1000;
                if (secondsLeft < 10)
                    txtLeftMinute.setText(Integer.toString(minutesLeft) + ":0" + secondsLeft);
                else
                    txtLeftMinute.setText(Integer.toString(minutesLeft) + ":" + secondsLeft);
            }

            @Override
            public void onFinish() {
                // call alarm on mobile device
                txtLeftMinute.setText("Alarm");
            }
        }.start();

        zrobiony countdown do ask user (zrob pytanie usera czy chce kontynowac od nastepnego czy od poczatku)

        zrob countdown dla begginning

                alram, widgeg etc
    }
}
