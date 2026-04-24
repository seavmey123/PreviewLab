package kh.rupp.edu.previewlab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This matches the screenshot with the illustration
        setContentView(R.layout.activity_main);

        Button btnStart = findViewById(R.id.btnStart); // Ensure this ID is in activity_main.xml

        btnStart.setOnClickListener(v -> {
            // Move from Welcome to Home
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });
    }
}