package com.peno.mierantau;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);

        EditText etNewUsername = findViewById(R.id.etNewUsername);
        EditText etNewPassword = findViewById(R.id.etNewPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(view -> {
            String username = etNewUsername.getText().toString();
            String password = etNewPassword.getText().toString();

            dbHelper.registerUser(username, password);
            Toast.makeText(RegisterActivity.this, "User registered", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
