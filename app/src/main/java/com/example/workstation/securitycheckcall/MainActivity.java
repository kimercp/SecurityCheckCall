package com.example.workstation.securitycheckcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

/* Main menu when user may start the alarm, add new one or delete. */
public class MainActivity extends AppCompatActivity {

    private ListView lstWorkPlace;
    // this list is going to be saved and read from file
    private List<AlarmDetails> myListOfAlarmDetails = new ArrayList<AlarmDetails>();
    private RowAdapter adapter;
    static int positionToRemoveFromList;
    private Button btnDelete;
    private Button btnStart;

    @Override
    protected void onResume() {
        super.onResume();

        // load data from file to listview component
        loadData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnStart = (Button)findViewById(R.id.btnStart);
        lstWorkPlace = (ListView)findViewById(R.id.workPlaceList);

        myListOfAlarmDetails = null;
        // load data from file to listview component
        loadData();

        // create on item click listener for list with alarms
        lstWorkPlace.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionToRemoveFromList = position;
                setListViewBackground(ContextCompat.getColor(MainActivity.this, R.color.backroundActivities));
                // enable start and delete button
                btnDelete.setEnabled(true);
                btnStart.setEnabled(true);
                // change the color of actually selected item
                view.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.selectedItemColor));
            }
        });
    }

//    // activate the menu on this activity
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        // close the app option "exit" from menu
//        if (id == R.id.action_exit) {
//            // create a dialog (question to user to confirm exit)
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            // Setting Dialog Message
//            builder.setMessage(R.string.dialog_confirmExit)
//                    // Setting Dialog Title
//                    //.setTitle(R.string.action_exit)
//                    // Setting Icon to Dialog
//                    .setIcon(null) // or .setIcon(R.drawable.icon_name) to display the icon
//                    // Setting Positive "Yes" Btn
//                    .setPositiveButton(R.string.dialog_Yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Write your code here to execute after yes
//                            System.exit(0);
//                        }
//                    })
//                    // Setting Negative "Cancel" Btn
//                    .setNegativeButton(R.string.dialog_Cancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // User cancelled the dialog
//                            Toast.makeText(getApplicationContext(), "Ok, enjoy the app", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    /*.setNeutralButton(R.string.remindMeLater, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })*/
//                    ;
//                    // Showing Alert Dialog
//                    AlertDialog dialog = builder.create();
//                    dialog.show();
//
//            /* // another way for alertdialog message
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.action_exit)
//                    .setMessage(R.string.dialog_conifrmExit)
//                    .setIcon(null)
//                    .setPositiveButton(R.string.dialog_Yes, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            System.exit(0);
//                        }
//                    })
//                    .setNegativeButton(R.string.dialog_Cancel, null)
//                    .setNeutralButton(R.string.remindMeLater, null).show();
//                    */
//        }
//        return super.onOptionsItemSelected(item);
//    }

    /* This method check if file is already exist, load the data from file to display in listview component
       Check for file if exist is not necessary because if the file is not exist the function readList return null anyway.
     */
    public void loadData(){
        // check if file exist in internal storage
        File file=new File(this.getFilesDir(),getResources().getString(R.string.fileWithListofAlarms));
        // read the data from file with alarms details
        if(file.exists()) myListOfAlarmDetails = readList();
        if (myListOfAlarmDetails!=null) {
            // custom adapter to display data in list's row
            adapter = new RowAdapter(this, R.layout.mymodel, myListOfAlarmDetails);
            // set adapter to listview
            lstWorkPlace.setAdapter(adapter);
        }
    }

    /* This method will open new activity after
     clicking on button "New alarm"
     where user may choose time, occurrence, etc.*/
    public void newAlarm(View view) {
        Intent intent = new Intent(this, SetNewAlarmActivity.class);
        startActivity(intent);
    }

    public void deleteAlarm(View view){

        // remove an item from list only if is not empty
        if (!myListOfAlarmDetails.isEmpty() && positionToRemoveFromList != -1) {
            myListOfAlarmDetails.remove(positionToRemoveFromList);
            // reset the selected position on list
            positionToRemoveFromList = -1;
            // set false because item has been removed
            btnDelete.setEnabled(false);
            btnStart.setEnabled(false);

            setListViewBackground(ContextCompat.getColor(MainActivity.this, R.color.backroundActivities));
        }
        else {
            // set false because the list is empty
            btnDelete.setEnabled(false);
            btnStart.setEnabled(false);
        }

        // save the new list
        writeList(myListOfAlarmDetails);
        adapter.notifyDataSetChanged();
    }

    // this method set the color on listview background
    private void setListViewBackground(int backgroundColor) {
        // number of all items in listview
        int lenghtOfList = lstWorkPlace.getAdapter().getCount();
        // change the background color of all items in a list
        for (int i=0; i<lenghtOfList; i++){
            View child = lstWorkPlace.getChildAt(i);
            child.setBackgroundColor(backgroundColor);
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
}
