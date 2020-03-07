package com.zdzirinc323.finalprojectsandbox;

import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AlarmPage extends AppCompatActivity {


    Button stopButton, completeButton;
    TextView title, description;

    private String task_title, task_description;
    private int task_id;
    DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);

        databaseHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        task_id = intent.getIntExtra("task_id", 0);

        if (task_id != 0) {

            Cursor c = databaseHelper.getTaskInfo(task_id);
            c.moveToFirst();
            task_title = c.getString(0);
            task_description = c.getString((1));
            title.setText(task_title);
            description.setText(task_description);

        } else Log.i("CHECK", "Zero task id");

        stopButton = (Button) findViewById(R.id.bt_stop);
        completeButton = (Button) findViewById(R.id.completedButton);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task_id != 0) databaseHelper.markCompleted(task_id);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
