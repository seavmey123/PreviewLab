package kh.rupp.edu.previewlab.task;

public interface CreateTaskContract {

    interface View {
        void onTaskSaved();
        void showError(String message);
    }

    interface Presenter {
        void saveTask(String name, String description, long dueDate,
                      String taskGroup, String priority);
        void destroy();
    }
}
