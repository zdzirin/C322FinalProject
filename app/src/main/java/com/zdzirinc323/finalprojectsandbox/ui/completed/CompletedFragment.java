package com.zdzirinc323.finalprojectsandbox.ui.completed;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zdzirinc323.finalprojectsandbox.DatabaseHelper;
import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.Task;
import com.zdzirinc323.finalprojectsandbox.TaskAdapter;
import com.zdzirinc323.finalprojectsandbox.addTaskDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CompletedFragment extends Fragment {


    public static final String USER_PREFS = "USER";


    RecyclerView recyclerView;
    public static TaskAdapter adapter;
    public static List<Task> taskList;

    static SharedPreferences sharePref;
    static DatabaseHelper database;

    FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_completed, container, false);
        sharePref = this.getContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        database = new DatabaseHelper(this.getContext());

        taskList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        loadTasks(1);

        adapter = new TaskAdapter(this.getContext(),taskList);
        recyclerView.setAdapter(adapter);

        return root;
    }


    public static void loadTasks(int i) {

        taskList.clear();

        Cursor c = (i == 0) ? database.getIncompleteUserTasks(getCurrentUser()) :
                              database.getCompleteUserTasks(getCurrentUser());
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadTasks(1);
            adapter.notifyDataSetChanged();
        }
    }
}