package com.peno.learnrecyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private String[] contacts = {"John", "Jane", "Jack", "Andy", "Bob", "Donny"};

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.getNameTextView().setText(contacts[position]);
    }

    @Override
    public int getItemCount() {
        return contacts.length;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.contactNameTextView);
        }
        public TextView getNameTextView() {
            return nameTextView;
        }
    }
}
