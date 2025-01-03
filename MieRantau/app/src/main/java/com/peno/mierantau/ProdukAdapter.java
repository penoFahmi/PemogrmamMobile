package com.peno.mierantau;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ProdukViewHolder> {

    private List<Produk> produkList;

    public ProdukAdapter(List<Produk> produkList) {
        this.produkList = produkList;
    }

    @NonNull
    @Override
    public ProdukViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_produk, parent, false);
        return new ProdukViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProdukViewHolder holder, int position) {
        Produk produk = produkList.get(position);
        holder.namaProduk.setText(produk.getNama());
        holder.hargaProduk.setText("Rp " + produk.getHarga());
        holder.imageView.setImageResource(produk.getGambar());
    }

    @Override
    public int getItemCount() {
        return produkList.size();
    }

    static class ProdukViewHolder extends RecyclerView.ViewHolder {
        TextView namaProduk, hargaProduk;
        ImageView imageView;

        ProdukViewHolder(@NonNull View itemView) {
            super(itemView);
            namaProduk = itemView.findViewById(R.id.namaProduk);
            hargaProduk = itemView.findViewById(R.id.hargaProduk);
            imageView = itemView.findViewById(R.id.imageViewProduk);
        }
    }
}
