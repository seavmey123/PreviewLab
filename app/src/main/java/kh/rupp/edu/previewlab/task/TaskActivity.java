package kh.rupp.edu.previewlab.task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import kh.rupp.edu.previewlab.R;
import kh.rupp.edu.previewlab.Task;
import kh.rupp.edu.previewlab.adapter.TaskListAdapter;

public class TaskActivity extends AppCompatActivity implements TaskContract.View {

    private TaskContract.Presenter presenter;
    private TaskListAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        presenter = new TaskPresenter(this, this);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskListAdapter(task -> presenter.markTaskCompleted(task));
        recyclerView.setAdapter(adapter);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0: presenter.loadTodayTasks();    break;
                    case 1: presenter.loadUpcomingTasks(); break;
                    case 2: presenter.loadCompletedTasks(); break;
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        FloatingActionButton fab = findViewById(R.id.fabAddTask);
        fab.setOnClickListener(v ->
                startActivity(new Intent(this, CreateTaskActivity.class))
        );

        presenter.loadTodayTasks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Reload current tab when returning from CreateTaskActivity
        int pos = tabLayout.getSelectedTabPosition();
        if (pos == 0)      presenter.loadTodayTasks();
        else if (pos == 1) presenter.loadUpcomingTasks();
        else               presenter.loadCompletedTasks();
    }

    @Override
    public void showTasks(List<Task> tasks) {
        adapter.setTasks(tasks);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }
}
