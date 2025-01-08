package com.peno.mierantauptk.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peno.mierantauptk.R;

public class HomeFragment extends Fragment {

    CardView KelolaMenuCard, penjualanCard, userCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the CardView
        KelolaMenuCard = view.findViewById(R.id.KelolaMenuCard);
        penjualanCard = view.findViewById(R.id.penjualanCard);
        userCard = view.findViewById(R.id.userCard);

        // Set onClickListener
        KelolaMenuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MenuFragment
                Intent intent = new Intent(getActivity(), MenuFragment.class);
                startActivity(intent);
            }
        });
        penjualanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open PenjualanFragment
                Intent intent = new Intent(getActivity(), OrdersFragment.class);
                startActivity(intent);
            }
        });
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open UserFragment
                Intent intent = new Intent(getActivity(), UsersFragment.class);
            }
        });

        return view;
    }
}
