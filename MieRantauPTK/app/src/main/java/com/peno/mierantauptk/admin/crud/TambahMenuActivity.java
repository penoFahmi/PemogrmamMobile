package com.peno.mierantauptk.admin.crud;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahMenuActivity extends AppCompatActivity {

    private EditText etNamaMenu, etDeskripsi, etHarga, etTanggal;
    private CheckBox cbPromo, cbTersedia;
    private RadioGroup rgCategory;
    private LinearLayout layoutCheckboxes;
    private Button btnSelectImage, btnSimpan;
    private ImageView imgPreview;

    private Bitmap selectedImageBitmap = null;
    private String selectedCategory = null;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        // Inisialisasi View
        etNamaMenu = findViewById(R.id.et_nama_menu);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        etTanggal = findViewById(R.id.et_tanggal);
        cbPromo = findViewById(R.id.cb_promo);
        cbTersedia = findViewById(R.id.cb_tersedia);
        rgCategory = findViewById(R.id.rg_category);
        layoutCheckboxes = findViewById(R.id.layout_checkboxes);
        btnSelectImage = findViewById(R.id.btn_select_image);
        imgPreview = findViewById(R.id.img_preview);
        btnSimpan = findViewById(R.id.btn_simpan);

        databaseHelper = new DatabaseHelper(this);

        // Tombol untuk memilih gambar
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // Tombol untuk menyimpan menu
        btnSimpan.setOnClickListener(v -> saveMenu());

        // Tampilkan kategori statis
        loadCategories();

        // Tampilkan DatePicker saat memilih tanggal
        etTanggal.setOnClickListener(v -> showDatePicker());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPreview.setImageBitmap(selectedImageBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadCategories() {
        String[] categories = {"Mie", "Minuman", "Extra", "Dimsum"};
        for (String kategori : categories) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(kategori);
            radioButton.setOnClickListener(v -> {
                selectedCategory = kategori;
                handleCategoryChange(kategori);
            });
            rgCategory.addView(radioButton);
        }
    }

    private void handleCategoryChange(String kategori) {
        layoutCheckboxes.removeAllViews();
        if ("Mie".equalsIgnoreCase(kategori)) {
            for (int i = 1; i <= 7; i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText("Level Pedas " + i);
                layoutCheckboxes.addView(checkBox);
            }
        } else if ("Minuman".equalsIgnoreCase(kategori)) {
            String[] sizes = {"Kecil", "Sedang", "Besar"};
            for (String size : sizes) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(size);
                layoutCheckboxes.addView(checkBox);
            }
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String formattedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
            etTanggal.setText(formattedDate);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private String getSelectedCheckboxValues() {
        StringBuilder selectedValues = new StringBuilder();
        for (int i = 0; i < layoutCheckboxes.getChildCount(); i++) {
            View view = layoutCheckboxes.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    selectedValues.append(checkBox.getText().toString()).append(", ");
                }
            }
        }
        return selectedValues.toString().isEmpty() ? null : selectedValues.substring(0, selectedValues.length() - 2);
    }

    private String saveImageToExternalStorage(Bitmap bitmap, String filename) {
        try {
            String imagesDir = getExternalFilesDir(null) + "/MenuImages";
            java.io.File file = new java.io.File(imagesDir);
            if (!file.exists()) file.mkdirs();

            java.io.File imageFile = new java.io.File(file, filename);
            OutputStream outputStream = new java.io.FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void saveMenu() {
        String namaMenu = etNamaMenu.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String tanggal = etTanggal.getText().toString().trim();
        boolean promo = cbPromo.isChecked();
        boolean tersedia = cbTersedia.isChecked();

        if (namaMenu.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty() || tanggal.isEmpty() || selectedCategory == null) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(tanggal);
        } catch (ParseException e) {
            Toast.makeText(this, "Format tanggal tidak valid!", Toast.LENGTH_SHORT).show();
            return;
        }

        double harga = Double.parseDouble(hargaStr);
        if (selectedImageBitmap == null) {
            selectedImageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.miemangkok);
        }

        String imagePath = saveImageToExternalStorage(selectedImageBitmap, namaMenu + "_" + System.currentTimeMillis() + ".png");
        String additionalInfo = getSelectedCheckboxValues();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAMA_MENU, namaMenu);
        values.put(DatabaseHelper.COLUMN_DESKRIPSI, deskripsi);
        values.put(DatabaseHelper.COLUMN_HARGA, harga);
        values.put(DatabaseHelper.COLUMN_KATEGORI, selectedCategory);
        values.put(DatabaseHelper.COLUMN_PROMO, promo ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_TERSEDIA, tersedia ? 1 : 0);
        values.put(DatabaseHelper.COLUMN_FOTO, imagePath);
        values.put(DatabaseHelper.COLUMN_TANGGAL, tanggal);
        values.put(DatabaseHelper.COLUMN_LEVEL_PEDAS, "Mie".equalsIgnoreCase(selectedCategory) ? additionalInfo : null);
        values.put(DatabaseHelper.COLUMN_UKURAN_MINUMAN, "Minuman".equalsIgnoreCase(selectedCategory) ? additionalInfo : null);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.insert(DatabaseHelper.TABLE_MENU, null, values);

        if (result != -1) {
            Toast.makeText(this, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menyimpan menu", Toast.LENGTH_SHORT).show();
        }
    }
}
