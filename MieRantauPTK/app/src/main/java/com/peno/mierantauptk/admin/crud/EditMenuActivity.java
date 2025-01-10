package com.peno.mierantauptk.admin.crud;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

import java.io.OutputStream;

public class EditMenuActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNamaMenu, etDeskripsi, etHarga, etStok;
    private RadioGroup rgCategory;
    private TextView tvNamaGambar;
    private ImageView imgPreview;
    private Button btnSelectImage, btnSimpan;

    private DatabaseHelper databaseHelper;
    private int menuId;
    private int selectedCategoryId = -1;
    private String currentImageName;
    private Bitmap newImageBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);

        etNamaMenu = findViewById(R.id.et_nama_menu);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        etStok = findViewById(R.id.et_stok);
        rgCategory = findViewById(R.id.rg_category);
        tvNamaGambar = findViewById(R.id.tv_nama_gambar);
        imgPreview = findViewById(R.id.img_preview);
        btnSelectImage = findViewById(R.id.btn_select_image);
        btnSimpan = findViewById(R.id.btn_simpan);

        databaseHelper = new DatabaseHelper(this);

        menuId = getIntent().getIntExtra("menu_id", -1);

        if (menuId != -1) {
            loadMenuData();
        }

        btnSelectImage.setOnClickListener(v -> selectImage());
        btnSimpan.setOnClickListener(v -> saveChanges());
    }

    private void loadMenuData() {
        Cursor cursor = databaseHelper.getMenuById(menuId);
        if (cursor != null && cursor.moveToFirst()) {
            String namaMenu = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_MENU));
            String deskripsi = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESKRIPSI));
            String foto = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_FOTO));
            double harga = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_HARGA));
            int stok = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_STOK));
            int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CATEGORY_ID));

            etNamaMenu.setText(namaMenu);
            etDeskripsi.setText(deskripsi);
            etHarga.setText(String.valueOf(harga));
            etStok.setText(String.valueOf(stok));
            currentImageName = foto.substring(foto.lastIndexOf("/") + 1);
            tvNamaGambar.setText("Gambar saat ini: " + currentImageName);

            Glide.with(this).load(foto).into(imgPreview);

            loadCategories(categoryId);
        }
    }

    private void loadCategories(int selectedCategory) {
        Cursor cursor = databaseHelper.getAllCategories();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaCategory = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_CATEGORY));

                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(id);
                radioButton.setText(namaCategory);
                rgCategory.addView(radioButton);

                if (id == selectedCategory) {
                    radioButton.setChecked(true);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        rgCategory.setOnCheckedChangeListener((group, checkedId) -> selectedCategoryId = checkedId);
    }

    private void selectImage() {
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
                newImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imgPreview.setImageBitmap(newImageBitmap);
                tvNamaGambar.setText("Gambar baru dipilih");
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveChanges() {
        String namaMenu = etNamaMenu.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();

        ContentValues values = new ContentValues();
        if (!namaMenu.isEmpty()) values.put(DatabaseHelper.COLUMN_NAMA_MENU, namaMenu);
        if (!deskripsi.isEmpty()) values.put(DatabaseHelper.COLUMN_DESKRIPSI, deskripsi);
        if (!hargaStr.isEmpty()) values.put(DatabaseHelper.COLUMN_HARGA, Double.parseDouble(hargaStr));
        if (!stokStr.isEmpty()) values.put(DatabaseHelper.COLUMN_STOK, Integer.parseInt(stokStr));
        if (selectedCategoryId != -1) values.put(DatabaseHelper.COLUMN_CATEGORY_ID, selectedCategoryId);

        if (newImageBitmap != null) {
            String newImageName = "menu_" + System.currentTimeMillis() + ".png";
            String newImagePath = saveImageToExternalStorage(newImageBitmap, newImageName);
            values.put(DatabaseHelper.COLUMN_FOTO, newImagePath);
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
            Uri imageUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
                values.put(MediaStore.Images.Media.IS_PENDING, true);
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

                imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                if (imageUri != null) {
                    OutputStream outputStream = getContentResolver().openOutputStream(imageUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();

                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    getContentResolver().update(imageUri, values, null, null);

                    return imageUri.toString();
                }
            } else {
                String imagesDir = getExternalFilesDir(null) + "/MyApp";
                java.io.File file = new java.io.File(imagesDir);
                if (!file.exists()) file.mkdirs();

                java.io.File imageFile = new java.io.File(file, filename);
                OutputStream outputStream = new java.io.FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                return imageFile.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
