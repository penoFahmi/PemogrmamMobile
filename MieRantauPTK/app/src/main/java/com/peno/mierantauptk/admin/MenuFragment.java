package com.peno.mierantauptk.admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.adapters.MenuAdapter;
import com.peno.mierantauptk.admin.crud.EditMenuActivity;
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
        adapter = new MenuAdapter(getContext(), menuList, menu -> {
            Intent intent = new Intent(getContext(), EditMenuActivity.class);
            intent.putExtra("menuId", menu.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        loadMenuData();

        btnAddMenu.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahMenuActivity.class);
            startActivity(intent);
        });

        //untuk kategori
        view.findViewById(R.id.btn_filter_all).setOnClickListener(v -> loadMenuByCategory("Semua"));
        view.findViewById(R.id.btn_filter_mie).setOnClickListener(v -> loadMenuByCategory("Mie"));
        view.findViewById(R.id.btn_filter_minuman).setOnClickListener(v -> loadMenuByCategory("Minuman"));
        view.findViewById(R.id.btn_filter_dimsum).setOnClickListener(v -> loadMenuByCategory("Dimsum"));
        view.findViewById(R.id.btn_filter_extra).setOnClickListener(v -> loadMenuByCategory("Extra"));
        view.findViewById(R.id.btn_filter_dimsum).setOnClickListener(v -> loadMenuByCategory("Dimsum"));


        EditText searchInput = view.findViewById(R.id.et_search);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMenu(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
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
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI));
                String fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
                int promo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO));
                int tersedia = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TERSEDIA));
                int levelPedas = cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS))
                        ? -1 : cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS));
                String ukuranMinuman = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UKURAN_MINUMAN));
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL));

                menuList.add(new MenuModel(id, namaMenu, deskripsi, fotoUrl, harga, kategori, promo, tersedia, levelPedas, ukuranMinuman, tanggal));
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    //button kategori
    private void loadMenuByCategory(String category) {
        Cursor cursor = databaseHelper.getMenusByCategory(category);
        menuList.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaMenu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI));
                double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI));
                String fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
                int promo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO));
                int tersedia = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TERSEDIA));
                int levelPedas = cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS))
                        ? -1 : cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS));
                String ukuranMinuman = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UKURAN_MINUMAN));
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL));

                menuList.add(new MenuModel(id, namaMenu, deskripsi, fotoUrl, harga, kategori, promo, tersedia, levelPedas, ukuranMinuman, tanggal));
            } while (cursor.moveToNext());
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    //logika tombol search
    private void searchMenu(String query) {
        Cursor cursor = databaseHelper.searchMenus(query);
        menuList.clear();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaMenu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU));
                String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI));
                double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA));
                String kategori = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI));
                String fotoUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
                int promo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO));
                int tersedia = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TERSEDIA));
                int levelPedas = cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS))
                        ? -1 : cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LEVEL_PEDAS));
                String ukuranMinuman = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_UKURAN_MINUMAN));
                String tanggal = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL));

                menuList.add(new MenuModel(id, namaMenu, deskripsi, fotoUrl, harga, kategori, promo, tersedia, levelPedas, ukuranMinuman, tanggal));
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
