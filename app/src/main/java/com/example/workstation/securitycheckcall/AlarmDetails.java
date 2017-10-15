package com.example.workstation.securitycheckcall;

import java.io.Serializable;
import java.util.ArrayList;

/* This class is to hold the name and time of following alarms as string array. */
public class AlarmDetails implements Serializable {
    public String name;
    public int occurrence;
    public int hourOfDay;
    public int minuteOfHour;
    public ArrayList<String> followingAlarms;

    // constructor
    public AlarmDetails(String nameForAlarm, int hour, int minute, int occurrenceNumber) {
        this.name = nameForAlarm;
        this.occurrence = occurrenceNumber;
        // what time is the first alarm, this variable is exactly the same like the first position in list
        this.hourOfDay = hour;
        this.minuteOfHour = minute;
        followingAlarms = new ArrayList<>();

        // add first time
        followingAlarms.add(Integer.toString(hour) + ":" + Integer.toString(minute));
        // loop to calculate and add to arraylist following alarms
        for (int i = 0; i < occurrence-1; i++) {
            // midnight, therefore clock needs to reset to value 0
            if (hour == 23) hour = 0;
            else hour++;
            // add next times
            followingAlarms.add(Integer.toString(hour) + ":" + Integer.toString(minute));
        }
    }

    // second constructor
    public AlarmDetails(){ }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(int occurrence) {
        this.occurrence = occurrence;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getMinuteOfHour() {
        return minuteOfHour;
    }

    public void setMinuteOfHour(int minuteOfHour) {
        this.minuteOfHour = minuteOfHour;
    }

    public ArrayList<String> getFollowingAlarms() {
        return followingAlarms;
    }

    public void setFollowingAlarms(ArrayList<String> followingAlarms) {
        this.followingAlarms = followingAlarms;
    }
}
