package com.example.workstation.securitycheckcall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/* Custom adapter to display rows in listview component */
public class RowAdapter extends ArrayAdapter<AlarmDetails> {

    Context context;
    int layoutResourceId;
    List<AlarmDetails> data = null;

    public RowAdapter(Context context, int layoutResourceId, List<AlarmDetails> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        // passing AlarmDetails array from constructor to local array variable
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        AlarmDetailsHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new AlarmDetailsHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtNumber = (TextView)row.findViewById(R.id.txtNumber);
            holder.txtAlarmTime = (TextView)row.findViewById(R.id.txtAlarmTime);

            row.setTag(holder);
        }
        else
        {
            holder = (AlarmDetailsHolder)row.getTag();
        }

        String tempTextAlarmTime = this.getContext().getResources().getString(R.string.alarmSetFor);

        String hourDisplayFormat = "";
        String minuteDisplayFormat = "";
        int hour = data.get(position).getHourOfDay();
        int minute = data.get(position).getMinuteOfHour();

        // format time output
        if (hour < 10) hourDisplayFormat = "0" + hour;
        else hourDisplayFormat = "" + hour;
        if (minute < 10) minuteDisplayFormat = "0" + minute;
        else minuteDisplayFormat = "" + minute;

        holder.txtAlarmTime.setText(tempTextAlarmTime +""+hourDisplayFormat+":"+minuteDisplayFormat);
        holder.txtTitle.setText(data.get(position).getName());
        holder.txtNumber.setText(Integer.toString(position+1));

        return row;
    }

    static class AlarmDetailsHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtNumber;
        TextView txtAlarmTime;
    }
}
