package kh.rupp.edu.previewlab.task;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import kh.rupp.edu.previewlab.Task;
import kh.rupp.edu.previewlab.data.db.AppDatabase;

public class TaskPresenter implements TaskContract.Presenter {

    private final TaskContract.View view;
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public TaskPresenter(TaskContract.View view, Context context) {
        this.view = view;
        this.db = AppDatabase.getInstance(context);
    }

    @Override
    public void loadTodayTasks() {
        executor.execute(() -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            long startOfDay = cal.getTimeInMillis();

            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            long endOfDay = cal.getTimeInMillis();

            List<Task> tasks = db.taskDao().getTodayTasks(startOfDay, endOfDay);
            mainHandler.post(() -> view.showTasks(tasks));
        });
    }

    @Override
    public void loadUpcomingTasks() {
        executor.execute(() -> {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            long endOfDay = cal.getTimeInMillis();

            List<Task> tasks = db.taskDao().getUpcomingTasks(endOfDay);
            mainHandler.post(() -> view.showTasks(tasks));
        });
    }

    @Override
    public void loadCompletedTasks() {
        executor.execute(() -> {
            List<Task> tasks = db.taskDao().getCompletedTasks();
            mainHandler.post(() -> view.showTasks(tasks));
        });
    }

    @Override
    public void markTaskCompleted(Task task) {
        executor.execute(() -> {
            task.isCompleted = true;
            db.taskDao().update(task);
            mainHandler.post(this::loadTodayTasks);
        });
    }

    @Override
    public void destroy() {
        executor.shutdown();
    }
}
