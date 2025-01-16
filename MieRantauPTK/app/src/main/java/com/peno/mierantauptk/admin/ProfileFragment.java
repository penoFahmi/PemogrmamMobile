package com.peno.mierantauptk.admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.LoginActivity;

public class ProfileFragment extends Fragment {

    private TextView profileName, profileEmail;
    private Button btnUpdateProfile, btnLogout;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        btnUpdateProfile = view.findViewById(R.id.btn_update_profile);
        btnLogout = view.findViewById(R.id.btn_logout);
        dbHelper = new DatabaseHelper(getContext());

        // Load admin profile data
        loadAdminProfile();

        // Handle update profile button click
        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        // Handle logout button click
        btnLogout.setOnClickListener(v -> logout());

        btnUpdateProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), UpdateProfileActivity.class);
            startActivity(intent);
        });


        return view;
    }

    private void loadAdminProfile() {
        Cursor cursor = dbHelper.getAllUsers(); // Adjust query as needed to get admin data only
        if (cursor != null && cursor.moveToFirst()) {
            profileName.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMA)));
            profileEmail.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
            cursor.close();
        } else {
            Toast.makeText(getContext(), "Failed to load profile data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        // Implement logic to navigate to update profile screen or show a dialog
        Toast.makeText(getContext(), "Update Profile clicked", Toast.LENGTH_SHORT).show();
    }

    private void logout() {
        // Implement logout logic, e.g., clearing shared preferences or navigating to login screen
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onResume() {
        super.onResume();
        loadAdminProfile();
    }
}
