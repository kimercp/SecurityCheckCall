package com.example.workstation.securitycheckcall;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

/* Main menu when user may start the alarm, add new one or delete. */
public class MainActivity extends AppCompatActivity {

    private ListView lstWorkPlace;
    // this list is going to be saved and read from file
    private List<AlarmDetails> myListOfAlarmDetails = new ArrayList<AlarmDetails>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // read the data from file with alarms details
        myListOfAlarmDetails = null;
        myListOfAlarmDetails = readList();
        // custom adapter to display data in list's row
        RowAdapter adapter = new RowAdapter(this, R.layout.mymodel, myListOfAlarmDetails);
        // set adapter to listview
        lstWorkPlace = (ListView)findViewById(R.id.workPlaceList);
        lstWorkPlace.setAdapter(adapter);
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

    /* This method will open new activity after
     clicking on button "New alarm"
     where user may choose time, occurrence, etc.*/
    public void newAlarm(View view) {
        Intent intent = new Intent(this, SetNewAlarmActivity.class);
        startActivity(intent);
    }

    /* Method to read list of AlarmDetails from file on internal storage */
    private List<AlarmDetails> readList(){
        List<AlarmDetails> tempList = null;
        try {
            FileInputStream fis = openFileInput("file.txt");
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
