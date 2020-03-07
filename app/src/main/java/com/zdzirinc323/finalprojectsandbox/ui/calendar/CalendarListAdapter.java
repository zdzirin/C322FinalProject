package com.zdzirinc323.finalprojectsandbox.ui.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.Task;

import java.util.ArrayList;
import java.util.Date;

public class CalendarListAdapter extends ArrayAdapter<Task> {

    private Context context;
    int resource;

    public CalendarListAdapter(Context context, int resource, ArrayList<Task> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String title = getItem(position).getTitle();
        String desc = getItem(position).getDescription();
        String duedate = getItem(position).getDuedateStr();
        //int image = getItem(position).getImage();
        //Task task = new Task(1,title,desc,duedate);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource,parent,false);

        TextView tvCal = (TextView) convertView.findViewById(R.id.tv_calendar_list);
        ImageView ivCal = (ImageView) convertView.findViewById(R.id.iv_calendar_list);

        tvCal.setText(title + " on date " + duedate);
        //ivCal.setImageResource();
        return convertView;
    }
}
