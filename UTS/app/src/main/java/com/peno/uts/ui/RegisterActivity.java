package com.peno.uts.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.uts.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegEmail, etRegPassword, etRegConfirmPassword, etRegName, etRegMobileNumber;
    private Button btnRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        etRegName = findViewById(R.id.etRegName);
        etRegMobileNumber = findViewById(R.id.etRegMobileNumber);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        btnRegister.setOnClickListener(view -> {
            String email = etRegEmail.getText().toString();
            String password = etRegPassword.getText().toString();
            String confirmPassword = etRegConfirmPassword.getText().toString();
            String name = etRegName.getText().toString();
            String mobileNumber = etRegMobileNumber.getText().toString();

            if (password.equals(confirmPassword)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.putString("name", name);
                editor.putString("mobileNumber", mobileNumber);
                editor.apply();

                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();

                // Berpindah ke LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
