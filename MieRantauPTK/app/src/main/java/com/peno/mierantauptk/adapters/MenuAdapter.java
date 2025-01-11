package com.peno.mierantauptk.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.peno.mierantauptk.R;
import com.peno.mierantauptk.admin.MenuDetailActivity;
import com.peno.mierantauptk.models.MenuModel;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private Context context;
    private List<MenuModel> menuList;
    private OnMenuClickListener onMenuClickListener;

    public interface OnMenuClickListener {
        void onMenuClick(MenuModel menu);
    }

    public MenuAdapter(Context context, List<MenuModel> menuList, OnMenuClickListener onMenuClickListener) {
        this.context = context;
        this.menuList = menuList;
        this.onMenuClickListener = onMenuClickListener;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuModel menu = menuList.get(position);

        // Validasi data sebelum di-set
        holder.tvNamaMenu.setText(menu.getNamaMenu() != null ? menu.getNamaMenu() : "Tidak ada nama");
        holder.tvHarga.setText(menu.getHarga() > 0 ? "Rp " + menu.getHarga() : "Harga tidak tersedia");
        holder.tvKategori.setText(menu.getKategori() != null ? menu.getKategori() : "Kategori tidak tersedia");
        holder.tvTanggal.setText(menu.getTanggal() != null ? "Tanggal: " + menu.getTanggal() : "Tanggal tidak tersedia");

        // Load gambar dengan Glide dan placeholder
        Glide.with(context)
                .load(menu.getFotoUrl() != null ? menu.getFotoUrl() : R.drawable.miemangkok)
                .placeholder(R.drawable.miemangkok)
                .error(R.drawable.miemangkok) // Default jika URL salah
                .into(holder.imgMenu);

        // Set klik item
//        holder.itemView.setOnClickListener(v -> {
//            if (onMenuClickListener != null) {
//                onMenuClickListener.onMenuClick(menu);
//            }
//        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MenuDetailActivity.class);
            intent.putExtra("menu_id", menu.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaMenu, tvHarga, tvKategori, tvTanggal;
        ImageView imgMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            tvKategori = itemView.findViewById(R.id.tv_kategori);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            imgMenu = itemView.findViewById(R.id.img_menu);
        }
    }
}
