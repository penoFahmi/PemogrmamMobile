package com.peno.mierantauptk.admin.crud;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

import java.io.OutputStream;
import java.util.Calendar;

public class EditMenuActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNamaMenu, etDeskripsi, etHarga, etTanggal;
    private RadioGroup rgCategory;
    private CheckBox cbPromo, cbTersedia;
    private ImageView imgPreview;
    private Button btnSelectImage, btnSimpan;

    private DatabaseHelper databaseHelper;
    private Bitmap selectedImageBitmap = null;
    private int menuId;
    private String currentImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        etNamaMenu = findViewById(R.id.et_nama_menuEdit);
        etDeskripsi = findViewById(R.id.et_deskripsiEdit);
        etHarga = findViewById(R.id.et_hargaEdit);
        etTanggal = findViewById(R.id.et_tanggalEdit);
        rgCategory = findViewById(R.id.rg_categoryEdit);
        cbPromo = findViewById(R.id.cb_promoEdit);
        cbTersedia = findViewById(R.id.cb_tersediaEdit);
        imgPreview = findViewById(R.id.img_previewEdit);
        btnSelectImage = findViewById(R.id.btn_select_imageEdit);
        btnSimpan = findViewById(R.id.btn_update);

        databaseHelper = new DatabaseHelper(this);
        menuId = getIntent().getIntExtra("menu_id", -1);

        if (menuId != -1) {
            loadMenuData(menuId);
        }

        etTanggal.setOnClickListener(v -> showDatePicker());
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSimpan.setOnClickListener(v -> saveChanges());
    }

    private void loadMenuData(int id) {
        Cursor cursor = databaseHelper.getMenuById(id);
        if (cursor != null && cursor.moveToFirst()) {
            int namaMenuIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMA_MENU);
            if (namaMenuIndex != -1) {
                String namaMenu = cursor.getString(namaMenuIndex);
                etNamaMenu.setHint("Nama Menu: " + namaMenu);
            }

            int deskripsiIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESKRIPSI);
            if (deskripsiIndex != -1) {
                String deskripsi = cursor.getString(deskripsiIndex);
                etDeskripsi.setHint("Deskripsi: " + deskripsi);
            }

            int hargaIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HARGA);
            if (hargaIndex != -1) {
                double harga = cursor.getDouble(hargaIndex);
                etHarga.setHint("Harga: Rp " + harga);
            }

            int tanggalIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TANGGAL);
            if (tanggalIndex != -1) {
                String tanggal = cursor.getString(tanggalIndex);
                etTanggal.setHint("Tanggal: " + tanggal);
            }

            int fotoIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FOTO);
            if (fotoIndex != -1) {
                currentImagePath = cursor.getString(fotoIndex);
                if (currentImagePath != null) {
                    Glide.with(this).load(currentImagePath).into(imgPreview);
                }
            }

            cursor.close();
        }

    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPreview.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar baru", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            etTanggal.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void saveChanges() {
        ContentValues values = new ContentValues();
        if (!etNamaMenu.getText().toString().trim().isEmpty()) {
            values.put(DatabaseHelper.COLUMN_NAMA_MENU, etNamaMenu.getText().toString().trim());
        }
        if (!etDeskripsi.getText().toString().trim().isEmpty()) {
            values.put(DatabaseHelper.COLUMN_DESKRIPSI, etDeskripsi.getText().toString().trim());
        }
        if (!etHarga.getText().toString().trim().isEmpty()) {
            values.put(DatabaseHelper.COLUMN_HARGA, Double.parseDouble(etHarga.getText().toString().trim()));
        }
        if (!etTanggal.getText().toString().trim().isEmpty()) {
            values.put(DatabaseHelper.COLUMN_TANGGAL, etTanggal.getText().toString().trim());
        }
        if (cbPromo.isChecked()) {
            values.put(DatabaseHelper.COLUMN_PROMO, 1);
        } else {
            values.put(DatabaseHelper.COLUMN_PROMO, 0);
        }
        if (cbTersedia.isChecked()) {
            values.put(DatabaseHelper.COLUMN_TERSEDIA, 1);
        } else {
            values.put(DatabaseHelper.COLUMN_TERSEDIA, 0);
        }

        // Handle gambar baru
        if (selectedImageBitmap != null) {
            String newImagePath = saveImageToExternalStorage(selectedImageBitmap, "menu_" + System.currentTimeMillis() + ".png");
            if (newImagePath != null) {
                values.put(DatabaseHelper.COLUMN_FOTO, newImagePath);
            }
        }

        int result = databaseHelper.updateMenu(menuId, values);
        if (result > 0) {
            Toast.makeText(this, "Perubahan berhasil disimpan!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan perubahan", Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImageToExternalStorage(Bitmap bitmap, String filename) {
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            return imageUri.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
