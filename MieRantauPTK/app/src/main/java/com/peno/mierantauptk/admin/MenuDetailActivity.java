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
import com.peno.mierantauptk.database.DatabaseHelper;

public class MenuDetailActivity extends AppCompatActivity {

    private ImageView imgMenuDetail;
    private TextView tvMenuName, tvMenuDescription, tvMenuPrice, tvMenuCategory;
    private TextView tvPromo, tvAvailability, tvSpicyLevel, tvDrinkSize;
    private Button btnEditMenu, btnDeleteMenu;
    private TextView tvPromoDate, tvAvailableTime;


    private DatabaseHelper databaseHelper;
    private int menuId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_detail);

        // Inisialisasi view
        imgMenuDetail = findViewById(R.id.img_menu_detail);
        tvMenuName = findViewById(R.id.tv_menu_name);
        tvMenuDescription = findViewById(R.id.tv_menu_description);
        tvMenuPrice = findViewById(R.id.tv_menu_price);
        tvMenuCategory = findViewById(R.id.tv_menu_category);
        tvPromo = findViewById(R.id.tv_promo);
        tvAvailability = findViewById(R.id.tv_availability);
        tvSpicyLevel = findViewById(R.id.tv_spicy_level);
        tvDrinkSize = findViewById(R.id.tv_drink_size);
        tvPromoDate = findViewById(R.id.tv_promo_date);
        tvAvailableTime = findViewById(R.id.tv_available_time);

        btnEditMenu = findViewById(R.id.btn_edit_menu);
        btnDeleteMenu = findViewById(R.id.btn_delete_menu);

        databaseHelper = new DatabaseHelper(this);
        menuId = getIntent().getIntExtra("menu_id", -1);

        menuId = getIntent().getIntExtra("menu_id", -1);
        if (menuId == -1) {
            Toast.makeText(this, "Menu ID tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish(); // Kembali jika menu_id tidak valid
            return;
        }

        if (menuId != -1) {
            loadMenuDetail();
        }


        btnEditMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditMenuActivity.class);
            intent.putExtra("menu_id", menuId);
            startActivity(intent);
        });

        btnDeleteMenu.setOnClickListener(v -> {
            int result = databaseHelper.deleteMenu(menuId);
            if (result > 0) {
                Toast.makeText(this, "Menu berhasil dihapus", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menghapus menu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMenuDetail() {
        Cursor cursor = databaseHelper.getMenuById(menuId);
        if (cursor != null && cursor.moveToFirst()) {
            // Data utama
            tvMenuName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU)));
            tvMenuDescription.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI)));
            tvMenuPrice.setText("Rp " + cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA)));
            tvMenuCategory.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI)));
            tvPromo.setText(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO)) == 1 ? "Promo Aktif" : "Tidak Ada Promo");
            tvAvailability.setText(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TERSEDIA)) == 1 ? "Tersedia" : "Tidak Tersedia");

            // Validasi level pedas
            int spicyLevelIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LEVEL_PEDAS);
            if (!cursor.isNull(spicyLevelIndex)) {
                tvSpicyLevel.setText("Level Pedas: " + cursor.getInt(spicyLevelIndex));
            } else {
                tvSpicyLevel.setText("Level Pedas: Tidak tersedia");
            }

            // Validasi ukuran minuman
            int drinkSizeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_UKURAN_MINUMAN);
            if (!cursor.isNull(drinkSizeIndex)) {
                tvDrinkSize.setText("Ukuran Minuman: " + cursor.getString(drinkSizeIndex));
            } else {
                tvDrinkSize.setText("Ukuran Minuman: Tidak tersedia");
            }

            // Tanggal promo dan jam tersedie
            if (cursor != null && cursor.moveToFirst()) {
                // Data Promo
                int promoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMO);
                if (promoIndex != -1 && cursor.getInt(promoIndex) == 1) {
                    String promoDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL));
                    tvPromoDate.setText("Promo Berakhir pada: " + promoDate);
                } else {
                    tvPromoDate.setText("Promo Tidak Aktif");
                }

                // Waktu Tersedia
                int availableIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TERSEDIA);
                if (availableIndex != -1 && cursor.getInt(availableIndex) == 1) {
                    String availableTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WAKTU));
                    tvAvailableTime.setText("Waktu Tersedia: " + availableTime);
                } else {
                    tvAvailableTime.setText("Tidak Tersedia");
                }
            }

            // Gambar
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
            Glide.with(this)
                    .load(imageUrl != null ? imageUrl : R.drawable.miemangkok)
                    .placeholder(R.drawable.miemangkok)
                    .into(imgMenuDetail);
        }
        cursor.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadMenuDetail();
    }
}
