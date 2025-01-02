package com.peno.learnasynctask;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView statusTextView;
    private ProgressBar statusProgessBar;
    private Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        statusTextView = findViewById(R.id.statusTV);
        statusProgessBar = findViewById(R.id.statusPB);
        startButton = findViewById(R.id.startB);

        startButton.setOnClickListener(v -> {
            statusTextView.setText("Waiting...");
            new SimpleAsycnTask(statusTextView, statusProgessBar).execute();
        });

    }
}