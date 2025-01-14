package com.peno.mierantauptk.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.models.MenuItem;

import java.util.List;

public class KasirMenuAdapter extends RecyclerView.Adapter<KasirMenuAdapter.ViewHolder> {

    private final List<MenuItem> menuItems;
    private final MenuClickListener listener;

    public interface MenuClickListener {
        void onMenuClick(MenuItem menuItem); // Callback untuk menangani klik tombol tambah
    }

    public KasirMenuAdapter(List<MenuItem> menuItems, MenuClickListener listener) {
        this.menuItems = menuItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kasir_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.tvName.setText(menuItem.getName());
        holder.tvPrice.setText("Rp" + menuItem.getPrice());
        holder.btnAdd.setOnClickListener(v -> listener.onMenuClick(menuItem)); // Panggil listener
    }
    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_menu_name);
            tvPrice = itemView.findViewById(R.id.tv_menu_price);
            btnAdd = itemView.findViewById(R.id.btn_add_to_cart);
        }
    }
}
