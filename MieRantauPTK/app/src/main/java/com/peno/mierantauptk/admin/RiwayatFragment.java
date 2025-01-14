package com.peno.mierantauptk.admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.adapters.RiwayatAdapter;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class RiwayatFragment extends Fragment {

    private RecyclerView rvTransactions;
    private DatabaseHelper databaseHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_riwayat, container, false);

        rvTransactions = view.findViewById(R.id.rv_transactions);
        rvTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        databaseHelper = new DatabaseHelper(getContext());

        loadTransactionData();

        return view;
    }

    private void loadTransactionData() {
        List<Transaction> transactions = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllTransaksi();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
                String date = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TANGGAL_TRANSAKSI));
                double total = cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_HARGA));
                String paymentMethod = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_METODE_PEMBAYARAN));
                transactions.add(new Transaction(id, date, total, paymentMethod));
            } while (cursor.moveToNext());
        }
        cursor.close();

        RiwayatAdapter adapter = new RiwayatAdapter(transactions, this::showTransactionDetails);
        rvTransactions.setAdapter(adapter);
    }

    private void showTransactionDetails(Transaction transaction) {
        // Tampilkan detail transaksi menggunakan dialog atau pindah ke Activity/Fragment lain
    }
}
