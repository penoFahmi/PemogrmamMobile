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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

import java.io.OutputStream;

public class TambahMenuActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etNamaMenu, etDeskripsi, etHarga, etStok, etNamaGambar;
    private RadioGroup rgCategory;
    private Button btnSelectImage, btnSimpan;
    private ImageView imgPreview;

    private Bitmap selectedImageBitmap = null;
    private DatabaseHelper databaseHelper;
    private int selectedCategoryId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        etNamaMenu = findViewById(R.id.et_nama_menu);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        etStok = findViewById(R.id.et_stok);
        etNamaGambar = findViewById(R.id.et_nama_gambar);
        rgCategory = findViewById(R.id.rg_category);
        btnSelectImage = findViewById(R.id.btn_select_image);
        imgPreview = findViewById(R.id.img_preview);
        btnSimpan = findViewById(R.id.btn_simpan);

        databaseHelper = new DatabaseHelper(this);

        // Muat kategori dari database
        loadCategories();

        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnSimpan.setOnClickListener(v -> saveMenu());
    }

    private void loadCategories() {
        Cursor cursor = databaseHelper.getAllCategories();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String namaCategory = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAMA_CATEGORY));

                RadioButton radioButton = new RadioButton(this);
                radioButton.setId(id);
                radioButton.setText(namaCategory);

                rgCategory.addView(radioButton);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // Set listener untuk RadioGroup
        rgCategory.setOnCheckedChangeListener((group, checkedId) -> selectedCategoryId = checkedId);
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
                Toast.makeText(this, "Gagal memuat gambar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveMenu() {
        String namaMenu = etNamaMenu.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String namaGambar = etNamaGambar.getText().toString().trim();

        if (namaMenu.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty() || namaGambar.isEmpty() || selectedImageBitmap == null || selectedCategoryId == -1) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double harga = Double.parseDouble(hargaStr);
        int stok = Integer.parseInt(stokStr);

        // Gunakan nama gambar dari EditText
        String imageName = namaGambar + "_" + System.currentTimeMillis() + ".png";
        String imagePath = saveImageToExternalStorage(selectedImageBitmap, imageName);

        if (imagePath != null) {
            long result = databaseHelper.addMenu(namaMenu, deskripsi, imagePath, harga, stok, selectedCategoryId);
            if (result != -1) {
                Toast.makeText(this, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan menu", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show();
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
