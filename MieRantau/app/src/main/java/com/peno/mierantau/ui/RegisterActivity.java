package com.peno.mierantau.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.peno.mierantau.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegEmail, etRegPassword, etRegConfirmPassword;
    private Button btnRegister;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegConfirmPassword = findViewById(R.id.etRegConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        btnRegister.setOnClickListener(view -> {

            String email = etRegEmail.getText().toString();
            String password = etRegPassword.getText().toString();
            String confirmPassword = etRegConfirmPassword.getText().toString();

            if (password.equals(confirmPassword)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("email", email);
                editor.putString("password", password);
                editor.apply();

                Log.d("RegisterActivity", "Data tersimpan: Email = " + email + ", Password = " + password); // Tambahkan log ini


                Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
                finish();  // Kembali ke LoginActivity
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
