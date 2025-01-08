package com.peno.mierantauptk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.admin.AdminDashboardActivity;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.users.UserDashboardActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ROLE_ID = "roleId";
    private static final String KEY_USER_ID = "userId";


    private DatabaseHelper dbHelper;
    private EditText etLoginName, etLoginPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Checking login status...");
        progressDialog.setCancelable(false);

        // Cek status login
        showLoadingDialog();
        new Handler().postDelayed(() -> {
            checkLoginStatus();
            hideLoadingDialog();
        }, 1500);

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

            // Tampilkan animasi loading
            showLoadingDialog();

            // Panggil metode loginUser
            new Handler().postDelayed(() -> {
                int roleId = dbHelper.loginUser(name, password);
                hideLoadingDialog();

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
            }, 1500);
        });

        // Tombol Register
        tvRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }
    private void checkLoginStatus() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        int roleId = sharedPreferences.getInt(KEY_ROLE_ID, -1);

        if (isLoggedIn) {
            if (roleId == 1) {
                navigateToDashboard(AdminDashboardActivity.class);
            } else if (roleId == 2) {
                navigateToDashboard(UserDashboardActivity.class);
            }
        }
    }

    private void saveLoginStatus(int roleId, int userId) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putInt(KEY_ROLE_ID, roleId);
        editor.putInt(KEY_USER_ID, userId);
        editor.apply();
    }

    private void navigateToDashboard(Class<?> targetActivity) {
        Intent intent = new Intent(this, targetActivity);
        startActivity(intent);
        finish();
    }

    private void showLoadingDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
