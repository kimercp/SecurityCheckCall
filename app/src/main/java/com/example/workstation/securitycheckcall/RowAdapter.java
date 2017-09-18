package com.example.workstation.securitycheckcall;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/* Custom adapter to display rows in listview component */
public class RowAdapter extends ArrayAdapter<WorkPlace> {

    Context context;
    int layoutResourceId;
    WorkPlace data[] = null;

    public RowAdapter(Context context, int layoutResourceId, WorkPlace[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        // passing WorkPlace array from constructor to local array variable
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WorkPlaceHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WorkPlaceHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.txtNumber = (TextView)row.findViewById(R.id.txtNumber);

            row.setTag(holder);
        }
        else
        {
            holder = (WorkPlaceHolder)row.getTag();
        }

        holder.txtTitle.setText(data[position].title);
        holder.txtNumber.setText(Integer.toString(data[position].numberOfList));
        holder.imgIcon.setImageResource(data[position].icon);


        return row;
    }

    static class WorkPlaceHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView txtNumber;
    }
}
