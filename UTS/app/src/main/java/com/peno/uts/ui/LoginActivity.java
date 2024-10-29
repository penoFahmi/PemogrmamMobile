package com.peno.uts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.uts.MainActivity;
import com.peno.uts.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Pastikan layout file ini sesuai

        // Inisialisasi komponen UI dan pastikan ID sesuai dengan file XML
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.cirLoginButton);
        tvRegister = findViewById(R.id.btnRegister);

        // Cek apakah semua komponen berhasil diinisialisasi
        if (etEmail == null || etPassword == null || btnLogin == null || tvRegister == null) {
            Toast.makeText(this, "Gagal memuat komponen UI.", Toast.LENGTH_SHORT).show();
            return;
        }

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        // Klik daftar untuk membuka aktivitas Register
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Klik login untuk validasi login
        btnLogin.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Validasi input tidak kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email atau password tidak boleh kosong.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil data email dan password dari SharedPreferences
            String registeredEmail = sharedPreferences.getString("email", "");
            String registeredPassword = sharedPreferences.getString("password", "");

            // Cek apakah data login sesuai dengan yang terdaftar
            if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Login gagal. Email atau password salah.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
