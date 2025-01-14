package com.peno.mierantauptk.admin;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.peno.mierantauptk.R;

public class HomeFragment extends Fragment {

    CardView KelolaMenuCard, penjualanCard, riwayatCard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the CardView
        KelolaMenuCard = view.findViewById(R.id.KelolaMenuCard);
        penjualanCard = view.findViewById(R.id.penjualanCard);
        riwayatCard = view.findViewById(R.id.riwayatCard);

        // Set onClickListener
//        KelolaMenuCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open MenuFragment
//                Intent intent = new Intent(getActivity(), MenuFragment.class);
//                startActivity(intent);
//            }
//        });
//        penjualanCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open PenjualanFragment
//                Intent intent = new Intent(getActivity(), KasirFragment.class);
//                startActivity(intent);
//            }
//        });
//        userCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Open UserFragment
//                Intent intent = new Intent(getActivity(), UsersFragment.class);
//            }
//        });

        KelolaMenuCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with MenuFragment
                replaceFragment(new MenuFragment());
            }
        });

        penjualanCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with KasirFragment
                replaceFragment(new KasirFragment());
            }
        });

        riwayatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with UsersFragment
                replaceFragment(new RiwayatFragment());
            }
        });
        return view;
    }

    private void replaceFragment(Fragment fragment) {
        try {
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container_fragment, fragment);
            transaction.commit();
            Log.d("HomeFragment", "Fragment replaced successfully");
        } catch (Exception e) {
            Log.e("HomeFragment", "Error during fragment transaction: " + e.getMessage());
        }
    }
}
