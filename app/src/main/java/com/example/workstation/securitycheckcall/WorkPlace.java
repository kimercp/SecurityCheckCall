package com.example.workstation.securitycheckcall;

/* This class is to create an instance of object which will keep data to display in separate row.
 */
public class WorkPlace {

    public int icon;
    public int numberOfList;
    public String title;

    // constructor with ordinal number, title (site) and icon (image to display)
    public WorkPlace(int number, String title, int icon) {

        this.numberOfList = number;
        this.title = title;
        this.icon = icon;
    }
}
