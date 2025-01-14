package com.peno.mierantauptk.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.peno.mierantauptk.R;
import com.peno.mierantauptk.models.Transaction;

import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {

    private final List<Transaction> transactions;
    private final OnTransactionClickListener listener;

    public interface OnTransactionClickListener {
        void onTransactionClick(Transaction transaction);
    }

    public RiwayatAdapter(List<Transaction> transactions, OnTransactionClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.tvDate.setText(transaction.getDate());
        holder.tvTotal.setText("Rp" + transaction.getTotal());
        holder.tvPaymentMethod.setText(transaction.getPaymentMethod());

        holder.itemView.setOnClickListener(v -> listener.onTransactionClick(transaction));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal, tvPaymentMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
            tvPaymentMethod = itemView.findViewById(R.id.tv_payment_method);
        }
    }
}

