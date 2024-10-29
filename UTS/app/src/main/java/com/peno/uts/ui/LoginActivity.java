package com.peno.uts.ui;

import android.content.Context;
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

    private EditText editTextEmail, editTextPassword;
    private Button btnLogin;
    private TextView btnRegister; // TextView for registering
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.cirLoginButton);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            finish();
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        String registeredEmail = sharedPreferences.getString("Email", "");
        String registeredPassword = sharedPreferences.getString("Password", "");

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email atau password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.equals(registeredEmail) && password.equals(registeredPassword)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("IsLoggedIn", true);
            editor.apply();

            Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Email atau password salah!", Toast.LENGTH_SHORT).show();
        }
    }
}
