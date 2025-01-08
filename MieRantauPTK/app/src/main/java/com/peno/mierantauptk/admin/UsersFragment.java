package com.peno.mierantauptk.admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.adapters.UserAdapter;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<UserModel> userList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);

        recyclerView = view.findViewById(R.id.rv_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        adapter = new UserAdapter(getContext(), userList);
        recyclerView.setAdapter(adapter);

        loadUserData();

        return view;
    }

    private void loadUserData() {
        Cursor cursor = databaseHelper.getAllUsers();
        userList.clear();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String nama = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_EMAIL));
                String noTelp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NO_TELP));
                int roleId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROLE_ID));

                if (roleId == 2) { // Hanya pengguna dengan role_id 2
                    userList.add(new UserModel(id, nama, email, noTelp));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }
}
