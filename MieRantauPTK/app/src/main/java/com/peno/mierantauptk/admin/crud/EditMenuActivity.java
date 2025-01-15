package com.peno.mierantauptk.admin.crud;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.admin.MenuDetailActivity;
import com.peno.mierantauptk.database.DatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class EditMenuActivity extends AppCompatActivity {

    private EditText etNamaMenu, etDeskripsi, etHarga;
    private CheckBox cbPromo, cbTersedia;
    private RadioGroup rgCategory;
    private LinearLayout layoutCheckboxes, layoutPromoDate, layoutAvailableTime;
    private Button btnSelectImage, btnSave, btnSelectPromoDate, btnBatalSimpan;
    private ImageView imgPreview;
    private TimePicker timePickerStart, timePickerEnd;

    private DatabaseHelper databaseHelper;
    private String selectedCategory = null;
    private Bitmap selectedImageBitmap = null;
    private int menuId;
    private String currentCategory, promoDate, availableStartTime, availableEndTime, currentImagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        // Inisialisasi View
        etNamaMenu = findViewById(R.id.et_nama_menuEdit);
        etDeskripsi = findViewById(R.id.et_deskripsiEdit);
        etHarga = findViewById(R.id.et_hargaEdit);
        cbPromo = findViewById(R.id.cb_promoEdit);
        cbTersedia = findViewById(R.id.cb_tersediaEdit);
        rgCategory = findViewById(R.id.rg_categoryEdit);
        layoutCheckboxes = findViewById(R.id.layout_checkboxes_edit);
        layoutPromoDate = findViewById(R.id.layout_promo_dateEdit);
        layoutAvailableTime = findViewById(R.id.layout_available_timeEdit);
        btnSelectPromoDate = findViewById(R.id.btn_select_promo_dateEdit);
        timePickerStart = findViewById(R.id.time_picker_startEdit);
        timePickerEnd = findViewById(R.id.time_picker_endEdit);
        btnSelectImage = findViewById(R.id.btn_select_imageEdit);
        btnSave = findViewById(R.id.btn_update);
        btnBatalSimpan = findViewById(R.id.btn_batal_simpan);
        imgPreview = findViewById(R.id.img_previewEdit);

        databaseHelper = new DatabaseHelper(this);
        menuId = getIntent().getIntExtra("menu_id", -1);

        if (menuId != -1) {
            loadMenuData(menuId);
        }

        cbPromo.setOnCheckedChangeListener((buttonView, isChecked) -> layoutPromoDate.setVisibility(isChecked ? View.VISIBLE : View.GONE));
        cbTersedia.setOnCheckedChangeListener((buttonView, isChecked) -> layoutAvailableTime.setVisibility(isChecked ? View.VISIBLE : View.GONE));

        loadCategories(currentCategory);
        btnSelectPromoDate.setOnClickListener(v -> showDatePicker());
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSave.setOnClickListener(v -> saveChanges());

        timePickerStart.setOnTimeChangedListener((view, hourOfDay, minute) -> availableStartTime = String.format("%02d:%02d", hourOfDay, minute));
        timePickerEnd.setOnTimeChangedListener((view, hourOfDay, minute) -> availableEndTime = String.format("%02d:%02d", hourOfDay, minute));

        btnBatalSimpan.setOnClickListener(v -> {
            Intent intent = new Intent(EditMenuActivity.this, MenuDetailActivity.class);
            intent.putExtra("menu_id", menuId);
            startActivity(intent);
        });
    }

    private void loadMenuData(int id) {
        Cursor cursor = databaseHelper.getMenuById(id);
        if (cursor != null && cursor.moveToFirst()) {
            etNamaMenu.setHint("Nama Menu: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU)));
            etDeskripsi.setHint("Deskripsi: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI)));
            etHarga.setHint("Harga: Rp " + cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA)));

            currentCategory = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_KATEGORI));
            promoDate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TANGGAL));
            availableStartTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_WAKTU));
            currentImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));

            cbPromo.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PROMO)) == 1);
            cbTersedia.setChecked(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TERSEDIA)) == 1);

//            Glide.with(this).load(currentImagePath).into(imgPreview);
//            loadCategories(currentCategory);
//            cursor.close();

            if (cursor != null && cursor.moveToFirst()) {
                currentImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
                Glide.with(this)
                        .load(currentImagePath != null ? currentImagePath : R.drawable.miemangkok)
                        .into(imgPreview);
                cursor.close();
            }

        }
    }

    private void loadCategories(String selectedCategory) {
        String[] categories = {"Mie", "Minuman", "Extra", "Dimsum"};
        for (String category : categories) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(category);
            if (category.equalsIgnoreCase(selectedCategory)) {
                radioButton.setChecked(true);
            }
            radioButton.setOnClickListener(v -> handleCategoryChange(category));
            rgCategory.addView(radioButton);
        }
    }

    private void handleCategoryChange(String category) {
        layoutCheckboxes.removeAllViews();
        if ("Mie".equalsIgnoreCase(category)) {
            for (int i = 1; i <= 7; i++) {
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText("Level Pedas " + i);
                layoutCheckboxes.addView(checkBox);
            }
        } else if ("Minuman".equalsIgnoreCase(category)) {
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
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> promoDate = year + "-" + (month + 1) + "-" + dayOfMonth, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
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
                Log.d("EditMenuActivity", "Gambar baru berhasil dipilih.");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
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

        if (rgCategory.getCheckedRadioButtonId() != -1) {
            RadioButton selectedRadioButton = findViewById(rgCategory.getCheckedRadioButtonId());
            values.put(DatabaseHelper.COLUMN_KATEGORI, selectedRadioButton.getText().toString());
        }
        if (cbPromo.isChecked()) {
            values.put(DatabaseHelper.COLUMN_PROMO, 1);
            values.put(DatabaseHelper.COLUMN_TANGGAL, promoDate);
        } else {
            values.put(DatabaseHelper.COLUMN_PROMO, 0);
        }

        if (cbTersedia.isChecked()) {
            values.put(DatabaseHelper.COLUMN_TERSEDIA, 1);
            values.put(DatabaseHelper.COLUMN_WAKTU, availableStartTime + " - " + availableEndTime);
        } else {
            values.put(DatabaseHelper.COLUMN_TERSEDIA, 0);
        }

        if (selectedImageBitmap != null) {
            // Simpan gambar baru
            String newImagePath = saveImageToExternalStorage(selectedImageBitmap, "menu_" + System.currentTimeMillis() + ".png");
            if (newImagePath != null) {
                values.put(DatabaseHelper.COLUMN_FOTO, newImagePath);

                // Hapus gambar lama jika ada
                if (currentImagePath != null) {
                    deleteOldImage(currentImagePath);
                }
            }
        } else if (currentImagePath != null) {
            // Jika tidak ada gambar baru, gunakan gambar lama
            values.put(DatabaseHelper.COLUMN_FOTO, currentImagePath);
        } else {
            // Jika tidak ada gambar baru atau gambar lama, gunakan gambar default
            Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.miemangkok);
            String defaultImagePath = saveImageToExternalStorage(defaultBitmap, "default_menu_" + System.currentTimeMillis() + ".png");
            values.put(DatabaseHelper.COLUMN_FOTO, defaultImagePath);
        }

        Log.d("EditMenuActivity", "Path gambar yang akan disimpan: " + values.getAsString(DatabaseHelper.COLUMN_FOTO));

        Cursor cursor = databaseHelper.getMenuById(menuId);
        if (cursor != null && cursor.moveToFirst()) {
            String savedImagePath = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
            Log.d("EditMenuActivity", "Path gambar di database setelah update: " + savedImagePath);
            cursor.close();
        }


        values.put(DatabaseHelper.COLUMN_LEVEL_PEDAS, getSelectedCheckboxValues());
        values.put(DatabaseHelper.COLUMN_UKURAN_MINUMAN, getSelectedCheckboxValues());

        databaseHelper.updateMenu(menuId, values);
        Toast.makeText(this, "Perubahan berhasil disimpan!", Toast.LENGTH_SHORT).show();
        finish();
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
        // Menghapus koma terakhir jika ada
        return selectedValues.toString().isEmpty() ? null : selectedValues.substring(0, selectedValues.length() - 2);
    }

    private String saveImageToExternalStorage(Bitmap bitmap, String filename) {
        try {
            String imagesDir = getExternalFilesDir(null) + "/MenuImages";
            File file = new File(imagesDir);
            if (!file.exists() && !file.mkdirs()) {
                Log.e("EditMenuActivity", "Gagal membuat direktori untuk menyimpan gambar.");
                return null;
            }

            File imageFile = new File(file, filename);
            OutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            Log.d("EditMenuActivity", "Gambar berhasil disimpan di: " + imageFile.getAbsolutePath());
            return imageFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void deleteOldImage(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
