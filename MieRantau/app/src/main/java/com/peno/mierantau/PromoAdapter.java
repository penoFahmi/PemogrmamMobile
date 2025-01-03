package com.peno.mierantau;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PromoAdapter extends RecyclerView.Adapter<PromoAdapter.PromoViewHolder> {

    private List<Integer> promoImages;

    public PromoAdapter(List<Integer> promoImages) {
        this.promoImages = promoImages;
    }

    @NonNull
    @Override
    public PromoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo, parent, false);
        return new PromoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromoViewHolder holder, int position) {
        holder.imageView.setImageResource(promoImages.get(position));
    }

    @Override
    public int getItemCount() {
        return promoImages.size();
    }

    static class PromoViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        PromoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewPromo);
        }
    }
}
