package com.peno.mierantauptk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private EditText etRegisterName, etRegisterEmail, etRegisterPhone, etRegisterPassword, etRegisterConfirmPassword;
    private Button btnRegister;
    private TextView tvBackLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi UI
        etRegisterName = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPhone = findViewById(R.id.etRegisterPhone);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackLogin = findViewById(R.id.tvBackLogin);

        // Inisialisasi DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Tombol Register
        btnRegister.setOnClickListener(view -> {
            String name = etRegisterName.getText().toString();
            String email = etRegisterEmail.getText().toString();
            String phone = etRegisterPhone.getText().toString();
            String password = etRegisterPassword.getText().toString();
            String confirmPassword = etRegisterConfirmPassword.getText().toString();

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Panggil metode registerUser
            long result = dbHelper.registerUser(name, email, phone, password);
            if (result != -1) {
                Toast.makeText(this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class); // Arahkan ke LoginActivity
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Kembali ke Login
        tvBackLogin.setOnClickListener(view -> {
            Intent intent = new Intent(this, LoginActivity.class); // Arahkan ke LoginActivity
            startActivity(intent);
            finish();
        });
    }
}
