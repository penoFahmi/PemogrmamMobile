package com.peno.mierantauptk.admin;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.adapters.CartAdapter;
import com.peno.mierantauptk.adapters.KasirMenuAdapter;
import com.peno.mierantauptk.database.DatabaseHelper;
import com.peno.mierantauptk.models.CartItem;
import com.peno.mierantauptk.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class KasirFragment extends Fragment {

    private RecyclerView rvMenu, rvCart;
    private TextView tvTotal;
    private RadioGroup rgPaymentMethod;
    private DatabaseHelper databaseHelper;
    private KasirMenuAdapter kasirMenuAdapter; // Ganti dari MenuAdapter ke KasirMenuAdapter
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private double total = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kasir, container, false);

        // Inisialisasi
        rvMenu = view.findViewById(R.id.rv_menu);
        rvCart = view.findViewById(R.id.rv_cart);
        tvTotal = view.findViewById(R.id.tv_total);
        rgPaymentMethod = view.findViewById(R.id.rg_payment_method);
        databaseHelper = new DatabaseHelper(getContext());

        // Setup RecyclerView
        rvMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load menu data
        loadMenuData();

        // Setup adapters
        cartAdapter = new CartAdapter(cartItems, this::updateCart);
        rvCart.setAdapter(cartAdapter);


        view.findViewById(R.id.btn_confirm).setOnClickListener(v -> confirmTransaction());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> cancelTransaction());

        return view;
    }

    private void loadMenuData() {
        List<MenuItem> menuItems = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllMenus();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAMA_MENU);
                int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_HARGA);

                int id = idIndex != -1 ? cursor.getInt(idIndex) : -1;
                String name = nameIndex != -1 ? cursor.getString(nameIndex) : "Unknown";
                double price = priceIndex != -1 ? cursor.getDouble(priceIndex) : 0.0;

                menuItems.add(new MenuItem(id, name, price));
            } while (cursor.moveToNext());

            cursor.close();
        }

        // Ganti MenuAdapter dengan KasirMenuAdapter
        kasirMenuAdapter = new KasirMenuAdapter(menuItems, this::showAddToCartDialog);
        rvMenu.setAdapter(kasirMenuAdapter);
    }


    private void addToCart(MenuItem menuItem, int quantity, String note) {
        for (CartItem item : cartItems) {
            if (item.getIdMenu() == menuItem.getId()) {
                item.setQuantity(item.getQuantity() + quantity); // Tambahkan jumlah
                item.setNote(note); // Perbarui catatan
                updateCart();
                return;
            }
        }
        cartItems.add(new CartItem(menuItem.getId(), menuItem.getName(), menuItem.getPrice(), quantity, note));
        updateCart();
    }


    private void updateCart() {
        total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        tvTotal.setText("Total: Rp" + total);
        cartAdapter.notifyDataSetChanged();
    }

    private void confirmTransaction() {
        int selectedMethod = rgPaymentMethod.getCheckedRadioButtonId();
        if (selectedMethod == -1) {
            Toast.makeText(getContext(), "Pilih metode pembayaran!", Toast.LENGTH_SHORT).show();
            return;
        }
        String paymentMethod = selectedMethod == R.id.rb_cash ? "Cash" :
                selectedMethod == R.id.rb_qris ? "QRIS" : "Debit";

        long transactionId = databaseHelper.insertTransaksi(
                String.valueOf(System.currentTimeMillis()), total, paymentMethod, "Admin");

        for (CartItem item : cartItems) {
            databaseHelper.insertDetailTransaksi(
                    (int) transactionId,
                    item.getIdMenu(),
                    item.getQuantity(),
                    item.getSubtotal(),
                    //"" // Tambahkan string kosong untuk catatan jika kosong tapi saya ganti mau mabil catatan
                    item.getNote() // Ambil catatan dari CartItem
            );
        }

        Toast.makeText(getContext(), "Transaksi berhasil disimpan!", Toast.LENGTH_SHORT).show();
        cancelTransaction();
    }

    private void cancelTransaction() {
        cartItems.clear();
        updateCart();
        tvTotal.setText("Total: Rp0");
    }

    private void showAddToCartDialog(MenuItem menuItem) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_to_cart, null);
        builder.setView(dialogView);

        EditText etQuantity = dialogView.findViewById(R.id.et_quantity);
        EditText etNote = dialogView.findViewById(R.id.et_note);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);
        Button btnAdd = dialogView.findViewById(R.id.btn_add);

        AlertDialog dialog = builder.create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnAdd.setOnClickListener(v -> {
            String quantityStr = etQuantity.getText().toString().trim();
            String note = etNote.getText().toString().trim();

            if (quantityStr.isEmpty()) {
                Toast.makeText(requireContext(), "Jumlah harus diisi!", Toast.LENGTH_SHORT).show();
                return;
            }

            int quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(requireContext(), "Jumlah harus lebih dari 0!", Toast.LENGTH_SHORT).show();
                return;
            }

            addToCart(menuItem, quantity, note); // Tambahkan ke keranjang
            dialog.dismiss();
        });

        dialog.show();
    }

}
