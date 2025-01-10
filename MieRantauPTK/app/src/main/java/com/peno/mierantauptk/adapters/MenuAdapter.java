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

    public MenuAdapter(Context context, List<MenuModel> menuList) {
        this.context = context;
        this.menuList = menuList;
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

        holder.tvNamaMenu.setText(menu.getNamaMenu());
        holder.tvHarga.setText("Rp " + menu.getHarga());

        Glide.with(context).load(menu.getFoto()).into(holder.imgMenu);

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
        TextView tvNamaMenu, tvHarga;
        ImageView imgMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaMenu = itemView.findViewById(R.id.tv_nama_menu);
            tvHarga = itemView.findViewById(R.id.tv_harga);
            imgMenu = itemView.findViewById(R.id.img_menu);
        }
    }
}
