package com.zdzirinc323.finalprojectsandbox;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.zdzirinc323.finalprojectsandbox.ui.home.HomeFragment;
import com.zdzirinc323.finalprojectsandbox.ui.pending.PendingFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

import static android.app.Activity.RESULT_CANCELED;
import static android.content.Context.MODE_PRIVATE;

public class addTaskDialog extends AppCompatDialogFragment {
    ImageView imageView;
    Button cameraBtn;
    Button galleryBtn;
    AlertDialog.Builder builder;
    SharedPreferences.Editor editor;

    EditText setTitle, setDescription, setAddress;
    TextView setTime, setDate;

    DatabaseHelper databaseHelper;
    SharedPreferences sharePref;
    Geocoder geocoder;
    static Calendar calSet;
    static int selectedHourClone, selectedMinuteClone, selectedYearClone, selectedMonthClone, selectedDayClone;

    PendingFragment pendingFragment;

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_task_dialog_layout,null);
        databaseHelper = new DatabaseHelper(this.getContext());
        editor = this.getContext().getSharedPreferences("ids", MODE_PRIVATE).edit();
        sharePref = this.getContext().getSharedPreferences("USER", MODE_PRIVATE);

        pendingFragment = new PendingFragment();

        cameraBtn = (Button) view.findViewById(R.id.btnCamera);
        galleryBtn = (Button) view.findViewById(R.id.btnGallery);
        imageView = (ImageView) getActivity().findViewById(R.id.iv_image);

        setTitle = view.findViewById(R.id.ed_set_title);
        setDescription = view.findViewById(R.id.ed_set_desc);
        setDate = view.findViewById(R.id.tv_set_duedate);
        setTime = view.findViewById(R.id.duetime);
        setAddress = view.findViewById(R.id.ed_set_address);

        setTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                final TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar calNow = Calendar.getInstance();
                        Calendar calSet = (Calendar) calNow.clone();

                        selectedHourClone = selectedHour;
                        selectedMinuteClone = selectedMinute;

                        calSet.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calSet.set(Calendar.MINUTE, selectedMinute);
                        calSet.set(Calendar.SECOND, 0);
                        calSet.set(Calendar.MILLISECOND, 0);

                        if (calSet.compareTo(calNow) <= 0) {
                            // Today Set time passed, count to tomorrow
                            calSet.add(Calendar.DATE, 1);
                        }
                        setTime.setText(""+selectedHour+":"+selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", mTimePicker);
                mTimePicker.show();
            }
        });

        setDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                final int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        setDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                        selectedYearClone = year;
                        selectedDayClone = dayOfMonth;
                        selectedMonthClone = month + 1;

                    }
                }, year, month, day);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", datePickerDialog);
                datePickerDialog.show();
            }
        });

        builder.setView(view)
                .setTitle("Add task")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addTaskToDatabase();
                        pendingFragment.loadTasks(0);
                        pendingFragment.adapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void addTaskToDatabase() {
        String title, description, date, time, address;
        title = setTitle.getText().toString();
        description = setDescription.getText().toString();
        date = setDate.getText().toString();
        time = setTime.getText().toString();
        address = setAddress.getText().toString();
        int user_id = sharePref.getInt("USER_ID", 0);

        databaseHelper.addTask(title, description, date, time, address, user_id);
        int id = databaseHelper.getTaskID(title,description,user_id);

        Calendar calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.DAY_OF_MONTH, selectedDayClone);
        calSet.set(Calendar.MONTH, selectedMonthClone);
        calSet.set(Calendar.YEAR, selectedYearClone);
        calSet.set(Calendar.HOUR_OF_DAY, selectedHourClone);
        calSet.set(Calendar.MINUTE, selectedMinuteClone);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);
        calNow.set(Calendar.SECOND, 0);
        calNow.set(Calendar.MILLISECOND, 0);
        Log.v("alarm", ""+selectedHourClone);
        Log.v("alarm", ""+selectedMinuteClone);

        long timeToSet = calSet.getTimeInMillis() - calNow.getTimeInMillis();
        if (timeToSet >= 60000) {
            setAlarm(timeToSet,id);
        }

        address = setAddress.getText().toString();

    }

    private void setAlarm(long timeToSet, int id) {
        Intent intent = new Intent(getActivity().getBaseContext(), Alarm.class);
        intent.putExtra("task_id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity().getBaseContext(), id, intent, 0);
        //editor.putInt("idname"+id, id);
        //editor.apply();

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeToSet - 60000,pendingIntent);
        System.out.println("Alarm Set!");
    }

}

