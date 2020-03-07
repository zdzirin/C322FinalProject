package com.zdzirinc323.finalprojectsandbox.ui.pending;

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

import com.zdzirinc323.finalprojectsandbox.Coordinate;
import com.zdzirinc323.finalprojectsandbox.DatabaseHelper;
import com.zdzirinc323.finalprojectsandbox.DeleteDialog;
import com.zdzirinc323.finalprojectsandbox.R;
import com.zdzirinc323.finalprojectsandbox.RecyclerTouchListener;
import com.zdzirinc323.finalprojectsandbox.Task;
import com.zdzirinc323.finalprojectsandbox.TaskAdapter;
import com.zdzirinc323.finalprojectsandbox.addTaskDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PendingFragment extends Fragment {


    public static final String USER_PREFS = "USER";


    RecyclerView recyclerView;
    public static TaskAdapter adapter;
    Button addTaskBtn;
    public static List<Task> taskList;

    static SharedPreferences sharePref;
    static DatabaseHelper database;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        sharePref = this.getContext().getSharedPreferences(USER_PREFS, MODE_PRIVATE);
        database = new DatabaseHelper(this.getContext());

        addTaskBtn = (Button) root.findViewById(R.id.btn_add_task);
        taskList = new ArrayList<>();
        recyclerView = (RecyclerView) root.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openAddTaskDialog();

            }
        });

        loadTasks(0);

        adapter = new TaskAdapter(this.getContext(),taskList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onLongClick(View view, int position) {

                Task curr = taskList.get(position);
                //For testing
                Coordinate coordinate = database.getTaskLatLng(curr.getId());
                Toast.makeText(getContext(), coordinate.toString(), Toast.LENGTH_SHORT).show();

                openConfirmDialog(position);
            }

            @Override
            public void onRightSwipe(View view, int position) {
                Task curr = taskList.get(position);

                boolean bool = database.markCompleted(curr.getId());
                if (bool) {
                    loadTasks(0);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "Completed Task!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onLeftSwipe(View child, int position) { }

            @Override
            public void onClick(View child, int position) { }
        }));



        return root;
    }

    public void deleteEntry(int position) {
        Task curr = taskList.get(position);
        boolean bool = database.deleteTask(curr.getId());
        if (bool) {
            loadTasks(0);
            adapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Deleted Task!", Toast.LENGTH_SHORT).show();
        } else {
            Log.i("CHECK", "did not bool");
        }
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

    public void openConfirmDialog(int position) {
        DeleteDialog dialog = new DeleteDialog(PendingFragment.this, position);
        dialog.show(getFragmentManager(), "DeleteDialog");
    }



    public void openAddTaskDialog(){
        addTaskDialog dialog = new addTaskDialog();
        dialog.show(getFragmentManager(),"addTaskDialog");
    }

    public static int getCurrentUser() {
        return sharePref.getInt("USER_ID",0);
    }


    public void deleteDatabase() {

        database.clearDatabase();

    }


}