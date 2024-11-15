package com.peno.menudate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            Toast.makeText(this, "Anda ingin membeli", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.favorites) {
            Toast.makeText(this, "Anda ingin menandai barang favorit", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (item.getItemId() == R.id.setting) {
            Toast.makeText(this, "Anda ingin melihat pengaturan", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showDialog(View view) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Warning");
        alertBuilder.setMessage("Aplikasi sedang diperbaharui, apakah anda ingin mengupdate?");
        alertBuilder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "OK telah di-tap!", Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Cancel telah di-tap!", Toast.LENGTH_SHORT).show();
            }
        });
        alertBuilder.show();
    }

    public void showDatePicker (View view) {
        DialogFragment dialogFragment = new DatePickerFragment();
        dialogFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void processDatePickerResult(int year, int month, int day) {
        String stringYear = Integer.toString(year);
        String stringMonth = Integer.toString(month + 1);
        String stringDay = Integer.toString(day);
        String dateMessage = ("Today is :" + stringYear + "-" + stringMonth + "-" + stringDay);
        Toast.makeText(getApplicationContext(), dateMessage, Toast.LENGTH_SHORT).show();
    }
}
