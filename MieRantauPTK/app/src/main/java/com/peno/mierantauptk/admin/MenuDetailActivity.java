package com.peno.mierantauptk.admin;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.admin.crud.EditMenuActivity;
import com.peno.mierantauptk.admin.crud.TambahMenuActivity;
import com.peno.mierantauptk.database.DatabaseHelper;

public class MenuDetailActivity extends AppCompatActivity {

    private ImageView imgMenuDetail;
    private TextView tvMenuName, tvMenuDescription, tvMenuPrice, tvMenuStock;
    private Button btnEditMenu, btnDeleteMenu;

    private DatabaseHelper databaseHelper;
    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        imgMenuDetail = findViewById(R.id.img_menu_detail);
        tvMenuName = findViewById(R.id.tv_menu_name);
        tvMenuDescription = findViewById(R.id.tv_menu_description);
        tvMenuPrice = findViewById(R.id.tv_menu_price);
        tvMenuStock = findViewById(R.id.tv_menu_stock);
        btnEditMenu = findViewById(R.id.btn_edit_menu);
        btnDeleteMenu = findViewById(R.id.btn_delete_menu);

        databaseHelper = new DatabaseHelper(this);

        menuId = getIntent().getIntExtra("menu_id", -1);

        if (menuId != -1) {
            loadMenuDetail();
        }

        btnEditMenu.setOnClickListener(v -> editMenu());
        btnDeleteMenu.setOnClickListener(v -> deleteMenu());
    }

    private void loadMenuDetail() {
        Cursor cursor = databaseHelper.getMenuById(menuId);
        if (cursor != null && cursor.moveToFirst()) {
            String namaMenu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI));
            String foto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
            double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA));
            int stok = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOK));

            tvMenuName.setText(namaMenu);
            tvMenuDescription.setText(deskripsi);
            tvMenuPrice.setText("Rp " + harga);
            tvMenuStock.setText("Stok: " + stok);
            Glide.with(this).load(foto).into(imgMenuDetail);
        }
    }

    private void editMenu() {
        Intent intent = new Intent(this, EditMenuActivity.class);
        intent.putExtra("menu_id", menuId);
        startActivity(intent);
    }

    private void deleteMenu() {
        int result = databaseHelper.deleteMenu(menuId);
        if (result > 0) {
            Toast.makeText(this, "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menghapus menu", Toast.LENGTH_SHORT).show();
        }
    }
}
