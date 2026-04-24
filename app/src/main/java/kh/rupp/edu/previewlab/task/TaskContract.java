package kh.rupp.edu.previewlab.task;

import java.util.List;

import kh.rupp.edu.previewlab.Task;

public interface TaskContract {

    interface View {
        void showTasks(List<Task> tasks);
        void showError(String message);
    }

    interface Presenter {
        void loadTodayTasks();
        void loadUpcomingTasks();
        void loadCompletedTasks();
        void markTaskCompleted(Task task);
        void destroy();
    }
}
