package com.zdzirinc323.finalprojectsandbox;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private Context context;
    private List<Task> taskList;

    public TaskAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_layout, null);
        TaskViewHolder holder = new TaskViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.title.setText(task.getTitle());
        holder.description.setText(task.getDescription());
        holder.duedate.setText(task.getDuedateStr());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title, description, duedate;

        public TaskViewHolder(View itemView){
            super(itemView);

            image = itemView.findViewById(R.id.iv_image);
            title = itemView.findViewById(R.id.tv_task_title);
            description = itemView.findViewById(R.id.tv_description);
            duedate = itemView.findViewById(R.id.tv_duedate);
        }
    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        dialog.show(manager,"dialog");
    }
}
