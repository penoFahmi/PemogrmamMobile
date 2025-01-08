package com.peno.mierantauptk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.admin.AdminDashboardActivity;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.users.UserDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etLoginName, etLoginPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi UI
        etLoginName = findViewById(R.id.etLoginName);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Tombol Login
        btnLogin.setOnClickListener(view -> {
            String name = etLoginName.getText().toString();
            String password = etLoginPassword.getText().toString();

            // Validasi input
            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil metode loginUser
            int roleId = dbHelper.loginUser(name, password);

            if (roleId == -1) {
                // Login gagal
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            } else if (roleId == 1) {
                // Arahkan ke AdminDashboard
                Toast.makeText(this, "Login as Admin", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AdminDashboardActivity.class);
                startActivity(intent);
                finish();
            } else if (roleId == 2) {
                // Arahkan ke UserDashboard
                Toast.makeText(this, "Login as User", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, UserDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Unknown role. Contact support.", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Register
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
