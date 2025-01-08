package com.peno.mierantauptk.admin.crud;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.database.DatabaseHelper;

public class TambahMenuActivity extends AppCompatActivity {

    private EditText etNamaMenu, etDeskripsi, etHarga, etStok, etFotoUrl;
    private Button btnSimpan;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_menu);

        etNamaMenu = findViewById(R.id.et_nama_menu);
        etDeskripsi = findViewById(R.id.et_deskripsi);
        etHarga = findViewById(R.id.et_harga);
        etStok = findViewById(R.id.et_stok);
        etFotoUrl = findViewById(R.id.et_foto_url);
        btnSimpan = findViewById(R.id.btn_simpan);

        databaseHelper = new DatabaseHelper(this);

        btnSimpan.setOnClickListener(v -> saveMenu());
    }

    private void saveMenu() {
        String namaMenu = etNamaMenu.getText().toString().trim();
        String deskripsi = etDeskripsi.getText().toString().trim();
        String hargaStr = etHarga.getText().toString().trim();
        String stokStr = etStok.getText().toString().trim();
        String fotoUrl = etFotoUrl.getText().toString().trim();

        if (namaMenu.isEmpty() || deskripsi.isEmpty() || hargaStr.isEmpty() || stokStr.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        double harga = Double.parseDouble(hargaStr);
        int stok = Integer.parseInt(stokStr);

        long result = databaseHelper.addMenu(namaMenu, deskripsi, fotoUrl, harga, stok, 1);
        if (result != -1) {
            Toast.makeText(this, "Menu berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Gagal menambahkan menu.", Toast.LENGTH_SHORT).show();
        }
    }
}
