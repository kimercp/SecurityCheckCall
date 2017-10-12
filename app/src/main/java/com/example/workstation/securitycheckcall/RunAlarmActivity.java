package com.example.workstation.securitycheckcall;

import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RunAlarmActivity extends AppCompatActivity {

    private Typeface typeface;
    private TextView txtLeftMinute;
    private TextView txtCurrentTime;
    private TextView txtNextAlarm;
    private LinearLayout myHorizontalAlarms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_alarm);

        myHorizontalAlarms = (LinearLayout) findViewById(R.id.listFollowingAlarms);
        // set the font for text views
        txtLeftMinute = (TextView) findViewById(R.id.txtLeftMinute);
        txtCurrentTime = (TextView) findViewById(R.id.txtCurrentTime);
        txtNextAlarm = (TextView) findViewById(R.id.txtNextAlarm);
        typeface = Typeface.createFromAsset(getAssets(),"fonts/digital7.ttf");
        txtLeftMinute.setTypeface(typeface);
        txtCurrentTime.setTypeface(typeface);
        txtNextAlarm.setTypeface(typeface);

        // Using getSerializableExtra(String key) method
        AlarmDetails alarm = (AlarmDetails) getIntent().getSerializableExtra("serializeData");
        displayFollowingAlarms(alarm.getHourOfDay(), alarm.getMinuteOfHour(), alarm.getOccurrence());

        // countdown timer for alarms
        new CountDownTimer(12000,1000){
           @Override
            public void onTick(long millisUntilFinished) {
               String minutesLeft = Long.toString(millisUntilFinished / 1000);
               txtLeftMinute.setText(minutesLeft);
            }

            @Override
            public void onFinish() {
                txtLeftMinute.setText("Done");
            }
        }.start();
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
            textView.setWidth(150);
            textView.setTextSize(28);
            textView.setText(Integer.toString(hour) + ":" + Integer.toString(minute));
            // set the color for textview
            int timeColor = ContextCompat.getColor(this, R.color.colorAccent);
            textView.setTextColor(timeColor);
            // adding new textview components with time of next alarm
            myHorizontalAlarms.addView(textView);

        }
    }
}
