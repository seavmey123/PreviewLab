package kh.rupp.edu.previewlab.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import kh.rupp.edu.previewlab.R;
import kh.rupp.edu.previewlab.Task;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    public interface OnTaskChecked {
        void onCheck(Task task);
    }

    private List<Task> tasks = new ArrayList<>();
    private final OnTaskChecked listener;

    public TaskListAdapter(OnTaskChecked listener) {
        this.listener = listener;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks != null ? tasks : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);

        holder.tvName.setText(task.name);
        holder.tvGroup.setText(task.taskGroup);

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        holder.tvTime.setText(sdf.format(new Date(task.dueDate)));

        holder.checkBox.setChecked(task.isCompleted);
        holder.checkBox.setOnClickListener(v -> {
            if (!task.isCompleted) {
                listener.onCheck(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvName, tvTime, tvGroup;

        ViewHolder(View v) {
            super(v);
            checkBox = v.findViewById(R.id.checkBox);
            tvName   = v.findViewById(R.id.tvTaskName);
            tvTime   = v.findViewById(R.id.tvTaskTime);
            tvGroup  = v.findViewById(R.id.tvTaskGroup);
        }
    }
}
