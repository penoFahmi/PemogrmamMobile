package com.peno.mierantauptk.admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.adapters.MenuAdapter;
import com.peno.mierantauptk.admin.crud.TambahMenuActivity;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.models.MenuModel;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private Button btnAddMenu;
    private MenuAdapter adapter;
    private List<MenuModel> menuList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = view.findViewById(R.id.rv_menu_list);
        btnAddMenu = view.findViewById(R.id.btn_add_menu);
        databaseHelper = new DatabaseHelper(getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MenuAdapter(getContext(), menuList);
        recyclerView.setAdapter(adapter);

        loadMenuData();

        btnAddMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahMenuActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadMenuData() {
        Cursor cursor = databaseHelper.getAllMenus();
        menuList.clear();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaMenu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI));
                double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA));
                int stok = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOK));
                String fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));

                menuList.add(new MenuModel(id, namaMenu, deskripsi, fotoUrl, harga, stok));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenuData();
    }
}
