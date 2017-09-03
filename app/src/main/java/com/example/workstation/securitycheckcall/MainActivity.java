package com.example.workstation.securitycheckcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    boolean isAllCaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // this method will open new activity, where user may choose time, occurrence, etc.
    public void newAlarm(View view) {
        Intent intent = new Intent(this, NewAlarmActivity.class);
        startActivity(intent);
    }

    // test method to change letters for all capitals
    public void testButton(){
        TextView textView = (TextView)findViewById(R.id.txtCreatedByKimercp);
        if (isAllCaps) {
            textView.setAllCaps(false);
            isAllCaps = false;
        }
        else {
            textView.setAllCaps(true);
            isAllCaps = true;
        }
    }
}
