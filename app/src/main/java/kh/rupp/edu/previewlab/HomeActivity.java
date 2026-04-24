package kh.rupp.edu.previewlab;
import kh.rupp.edu.previewlab.task.TaskActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeActivity extends AppCompatActivity {

    private TaskAdapter adapter;
    private List<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // Matches your list design

        // 1. Initialize RecyclerView
        RecyclerView recycler = findViewById(R.id.recyclerTasks);
        list = new ArrayList<>();
        adapter = new TaskAdapter(list);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

//        // 2. Initialize FAB (to go to Add Task screen)
//        FloatingActionButton fab = findViewById(R.id.fabAdd);
//        fab.setOnClickListener(v -> {
//            Intent intent = new Intent(HomeActivity.this, AddTaskActivity.class);
//            startActivity(intent);
//        });

        // 3. Fetch Data
        findViewById(R.id.navTask).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, TaskActivity.class));
        });
    }

    private void setupRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.example.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        apiService.getTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    list.clear();
                    list.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                loadFallbackData();
                Toast.makeText(HomeActivity.this, "Loaded local data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFallbackData() {
        list.add(new Task("Office Project", "23 Tasks", 70));
        list.add(new Task("Personal Project", "30 Tasks", 52));
        list.add(new Task("Daily Study", "30 Tasks", 87));
        adapter.notifyDataSetChanged();
    }
}