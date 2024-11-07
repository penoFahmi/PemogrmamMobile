package com.peno.tugaskuiskota;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CheckBox pontianakCB;
    private CheckBox majalengkaCB;
    private CheckBox singkawangCB;
    private CheckBox melawiCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inisialisasi CheckBox
        pontianakCB = findViewById(R.id.pontianakCB);
        majalengkaCB = findViewById(R.id.majalengkaCB);
        singkawangCB = findViewById(R.id.singkawangCB);
        melawiCB = findViewById(R.id.melawiCB);
    }

    public void submit(View view) {
        String jawaban = "Pilihan yang benar di Kalimantan Barat:\n";

        if (pontianakCB.isChecked()) {
            jawaban += "- Pontianak\n";
        }
        if (singkawangCB.isChecked()) {
            jawaban += "- Singkawang\n";
        }
        if (melawiCB.isChecked()) {
            jawaban += "- Melawi\n";
        }

        if (!pontianakCB.isChecked() && !singkawangCB.isChecked() && !melawiCB.isChecked()) {
            jawaban = "Tidak ada jawaban yang benar yang dipilih.";
        }

        Toast.makeText(this, jawaban, Toast.LENGTH_LONG).show();
    }
}
