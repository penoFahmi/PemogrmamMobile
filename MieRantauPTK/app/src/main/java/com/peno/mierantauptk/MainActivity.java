package com.peno.mierantauptk;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    EditText etName, etEmail, etPhone, etPassword, etRoleId;
    Button btnAddUser, btnViewUsers;
    TextView tvOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Initialize UI Components
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etRoleId = findViewById(R.id.etRoleId);
        btnAddUser = findViewById(R.id.btnAddUser);
        btnViewUsers = findViewById(R.id.btnViewUsers);
        tvOutput = findViewById(R.id.tvOutput);

        // Add User Button
        btnAddUser.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String phone = etPhone.getText().toString();
            String password = etPassword.getText().toString();
            int roleId;

            try {
                roleId = Integer.parseInt(etRoleId.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid Role ID", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert user into database
            long result = dbHelper.addUser(name, email, phone, password, roleId);
            if (result != -1) {
                Toast.makeText(this, "User Added Successfully!", Toast.LENGTH_SHORT).show();
                clearInputFields();
            } else {
                Toast.makeText(this, "Failed to Add User", Toast.LENGTH_SHORT).show();
            }
        });

        // View Users Button
        btnViewUsers.setOnClickListener(view -> {
            Cursor cursor = dbHelper.getAllUsers();
            if (cursor.getCount() == 0) {
                tvOutput.setText("No Users Found.");
                return;
            }

            StringBuilder output = new StringBuilder();
            while (cursor.moveToNext()) {
                output.append("ID: ").append(cursor.getInt(0)).append("\n")
                        .append("Name: ").append(cursor.getString(1)).append("\n")
                        .append("Email: ").append(cursor.getString(2)).append("\n")
                        .append("Phone: ").append(cursor.getString(3)).append("\n")
                        .append("Role ID: ").append(cursor.getInt(5)).append("\n\n");
            }

            tvOutput.setText(output.toString());
        });
    }

    private void clearInputFields() {
        etName.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etPassword.setText("");
        etRoleId.setText("");
    }
}
