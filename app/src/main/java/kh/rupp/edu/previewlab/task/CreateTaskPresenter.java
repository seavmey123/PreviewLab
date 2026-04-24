package kh.rupp.edu.previewlab.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kh.rupp.edu.previewlab.Task;
import kh.rupp.edu.previewlab.data.db.AppDatabase;

public class CreateTaskPresenter implements CreateTaskContract.Presenter {

    private final CreateTaskContract.View view;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public CreateTaskPresenter(CreateTaskContract.View view, Context context) {
        this.view = view;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    public void saveTask(String name, String description, long dueDate,
                         String taskGroup, String priority) {
        if (name == null || name.trim().isEmpty()) {
            view.showError("Task name is required");
            return;
        }

        Task task = new Task();
        task.name        = name.trim();
        task.description = description != null ? description.trim() : "";
        task.dueDate     = dueDate;
        task.taskGroup   = taskGroup;
        task.priority    = priority;
        task.isCompleted = false;

        executor.execute(() -> {
            db.taskDao().insert(task);
            mainHandler.post(() -> view.onTaskSaved());
        });
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }
}
