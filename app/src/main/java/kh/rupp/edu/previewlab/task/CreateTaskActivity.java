package kh.rupp.edu.previewlab.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kh.rupp.edu.previewlab.R;

public class CreateTaskActivity extends AppCompatActivity implements CreateTaskContract.View {

    private CreateTaskContract.Presenter presenter;
    private EditText etTaskName, etDescription;
    private TextView tvDueDate;

    private long selectedDueDate = System.currentTimeMillis();
    private String selectedGroup    = "Office Project";
    private String selectedPriority = "Medium";

    private Button btnOffice, btnPersonal, btnDailyStudy, btnHealth;
    private Button btnLow, btnMedium, btnHigh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        presenter = new CreateTaskPresenter(this, this);

        etTaskName    = findViewById(R.id.etTaskName);
        etDescription = findViewById(R.id.etDescription);
        tvDueDate     = findViewById(R.id.tvDueDate);

        btnOffice     = findViewById(R.id.btnOfficeProject);
        btnPersonal   = findViewById(R.id.btnPersonalProject);
        btnDailyStudy = findViewById(R.id.btnDailyStudy);
        btnHealth     = findViewById(R.id.btnHealth);

        btnLow    = findViewById(R.id.btnLow);
        btnMedium = findViewById(R.id.btnMedium);
        btnHigh   = findViewById(R.id.btnHigh);

        // Set defaults
        updateDueDateDisplay();
        setGroupSelected(btnOffice, "Office Project");
        setPrioritySelected(btnMedium, "Medium");

        // Due date picker
        tvDueDate.setOnClickListener(v -> showDateTimePicker());
        findViewById(R.id.ivCalendar).setOnClickListener(v -> showDateTimePicker());

        // Group buttons
        btnOffice.setOnClickListener(v    -> setGroupSelected(btnOffice,     "Office Project"));
        btnPersonal.setOnClickListener(v  -> setGroupSelected(btnPersonal,   "Personal Project"));
        btnDailyStudy.setOnClickListener(v-> setGroupSelected(btnDailyStudy, "Daily Study"));
        btnHealth.setOnClickListener(v    -> setGroupSelected(btnHealth,     "Health"));

        // Priority buttons
        btnLow.setOnClickListener(v   -> setPrioritySelected(btnLow,    "Low"));
        btnMedium.setOnClickListener(v-> setPrioritySelected(btnMedium, "Medium"));
        btnHigh.setOnClickListener(v  -> setPrioritySelected(btnHigh,   "High"));

        // Save
        findViewById(R.id.btnSaveTask).setOnClickListener(v ->
                presenter.saveTask(
                        etTaskName.getText().toString(),
                        etDescription.getText().toString(),
                        selectedDueDate,
                        selectedGroup,
                        selectedPriority
                )
        );

        // Back
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private void showDateTimePicker() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(selectedDueDate);

        new DatePickerDialog(this, (view, year, month, day) -> {
            cal.set(year, month, day);
            new TimePickerDialog(this, (timeView, hour, minute) -> {
                cal.set(Calendar.HOUR_OF_DAY, hour);
                cal.set(Calendar.MINUTE, minute);
                selectedDueDate = cal.getTimeInMillis();
                updateDueDateDisplay();
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDueDateDisplay() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        tvDueDate.setText(sdf.format(new Date(selectedDueDate)));
    }

    private void setGroupSelected(Button selected, String group) {
        selectedGroup = group;
        int purple = ContextCompat.getColor(this, R.color.purple_500);
        int white  = ContextCompat.getColor(this, android.R.color.white);
        int gray   = ContextCompat.getColor(this, R.color.text_secondary);

        for (Button btn : new Button[]{btnOffice, btnPersonal, btnDailyStudy, btnHealth}) {
            boolean active = btn == selected;
            btn.setBackgroundTintList(ColorStateList.valueOf(active ? purple : white));
            btn.setTextColor(active ? white : gray);
        }
    }

    private void setPrioritySelected(Button selected, String priority) {
        selectedPriority = priority;
        int orange = ContextCompat.getColor(this, R.color.priority_orange);
        int white  = ContextCompat.getColor(this, android.R.color.white);
        int gray   = ContextCompat.getColor(this, R.color.text_secondary);

        for (Button btn : new Button[]{btnLow, btnMedium, btnHigh}) {
            boolean active = btn == selected;
            btn.setBackgroundTintList(ColorStateList.valueOf(active ? orange : white));
            btn.setTextColor(active ? white : gray);
        }
    }

    @Override
    public void onTaskSaved() {
        Toast.makeText(this, "Task saved!", Toast.LENGTH_SHORT).show();
        finish();
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
