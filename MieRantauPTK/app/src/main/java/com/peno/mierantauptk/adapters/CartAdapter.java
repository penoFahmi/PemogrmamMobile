package com.peno.mierantauptk.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.models.CartItem;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final List<CartItem> cartItems;
    private final CartUpdateListener listener;

    public interface CartUpdateListener {
        void onCartUpdated();
    }

    public CartAdapter(List<CartItem> cartItems, CartUpdateListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.tvName.setText(item.getName());
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));
        holder.tvPrice.setText("Rp" + item.getSubtotal());
        holder.tvNote.setText("Catatan: " + (item.getNote().isEmpty() ? "Tidak ada" : item.getNote())); // Tampilkan catatan


        // Increment quantity
        holder.btnAdd.setOnClickListener(v -> {
            item.incrementQuantity();
            notifyItemChanged(position);
            listener.onCartUpdated();
        });

        // Decrement quantity
        holder.btnRemove.setOnClickListener(v -> {
            item.decrementQuantity();
            if (item.getQuantity() == 0) {
                cartItems.remove(position);
                notifyItemRemoved(position);
            } else {
                notifyItemChanged(position);
            }
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQuantity, tvPrice, tvNote;
        Button btnAdd, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_cart_name);
            tvQuantity = itemView.findViewById(R.id.tv_cart_quantity);
            tvPrice = itemView.findViewById(R.id.tv_cart_price);
            tvNote = itemView.findViewById(R.id.tv_cart_note);
            btnAdd = itemView.findViewById(R.id.btn_cart_add);
            btnRemove = itemView.findViewById(R.id.btn_cart_remove);
        }
    }
}

