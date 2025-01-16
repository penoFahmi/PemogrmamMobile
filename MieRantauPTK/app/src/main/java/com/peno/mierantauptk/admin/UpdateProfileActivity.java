package com.peno.mierantauptk.admin;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

public class UpdateProfileActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPhone, edtPassword;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int adminId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhone = findViewById(R.id.edt_phone);
        edtPassword = findViewById(R.id.edt_password);
        btnSave = findViewById(R.id.btn_save);

        dbHelper = new DatabaseHelper(this);

        loadAdminData();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void loadAdminData() {
        Cursor cursor = dbHelper.getAdminData();
        if (cursor != null && cursor.moveToFirst()) {
            adminId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            edtName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMA)));
            edtEmail.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
            edtPhone.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NO_TELP)));
            edtPassword.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PASSWORD)));
            cursor.close();
        }
    }

    private void saveProfile() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAMA, name);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_NO_TELP, phone);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        int result = dbHelper.updateUser(adminId, name, email, phone, password, 1);
        if (result > 0) {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
        }
    }
}
