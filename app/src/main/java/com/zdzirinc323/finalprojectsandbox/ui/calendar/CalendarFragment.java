package com.zdzirinc323.finalprojectsandbox.ui.calendar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.zdzirinc323.finalprojectsandbox.DatabaseHelper;
import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.Task;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class CalendarFragment extends Fragment {
    private static SharedPreferences sharePref;
    public static final String USER_PREFS = "USER";
    ArrayList<Task> taskList = new ArrayList<>();
    CalendarView calendarView;
    CalendarListAdapter adapter;
    ListView lv;
    DatabaseHelper database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)          {
        View v = inflater.inflate(R.layout.fragmeng_calendar, container, false);

        sharePref = this.getContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        database = new DatabaseHelper(this.getContext());
        calendarView = (CalendarView) v.findViewById(R.id.cal_view);
        lv = (ListView) v.findViewById(R.id.lv_calendar_task);
        final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String selectedDate = sdf.format(new Date(calendarView.getDate()));
        getTasksOnDay(selectedDate);
        adapter = new CalendarListAdapter(calendarView.getContext(), R.layout.calendar_list_layout, taskList);
        lv.setAdapter(adapter);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                String date = "" + (month+1) +"/" + day +"/" + year;
                //For testing
                //Toast.makeText(getContext(),date,Toast.LENGTH_SHORT).show();
                getTasksOnDay(date);
                adapter.notifyDataSetChanged();
            }
        });
        return v;
    }

    private void getTasksOnDay(String selectedDate) {
        taskList.clear();

        Cursor c = database.getTasksOnDay(selectedDate, getCurrentUser());

        while (c.moveToNext()) {
            String title, description, duedate, duetime;

            int id = c.getInt(0);
            title = c.getString(1);
            description = c.getString(2);
            duedate = c.getString(3);
            duetime = c.getString(4);

            taskList.add(new Task(id, title, description, duedate, duetime));
        }



    }

    public static int getCurrentUser() {
        return sharePref.getInt("USER_ID",0);
    }

}

