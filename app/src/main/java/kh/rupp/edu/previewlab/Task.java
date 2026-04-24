package kh.rupp.edu.previewlab;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    // New Room fields
    public String name;
    public String description;
    public long dueDate;
    public String taskGroup;
    public String priority;
    public boolean isCompleted;

    // Keep existing fields for HomeActivity
    public String title;
    public String subtitle;
    public int progress;

    // Keep your existing constructor (ignored by Room)
    @Ignore
    public Task(String title, String subtitle, int progress) {
        this.title = title;
        this.subtitle = subtitle;
        this.progress = progress;
    }

    // Required empty constructor for Room
    public Task() {}

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public int getProgress() { return progress; }
}