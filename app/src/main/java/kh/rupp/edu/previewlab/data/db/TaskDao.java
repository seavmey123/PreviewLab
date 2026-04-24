package kh.rupp.edu.previewlab.data.db;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import kh.rupp.edu.previewlab.Task;

@Dao
public interface TaskDao {

    @Insert
    void insert(Task task);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT * FROM tasks WHERE dueDate >= :startOfDay AND dueDate <= :endOfDay AND isCompleted = 0 ORDER BY dueDate ASC")
    List<Task> getTodayTasks(long startOfDay, long endOfDay);

    @Query("SELECT * FROM tasks WHERE dueDate > :endOfDay AND isCompleted = 0 ORDER BY dueDate ASC")
    List<Task> getUpcomingTasks(long endOfDay);

    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY dueDate DESC")
    List<Task> getCompletedTasks();
}
