package com.peno.uas.databinding;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ActivityMainBinding {
    public RecyclerView rvCategories;
    public AlertDialog.Builder rvPopular;
    public View btnCart;
    public View bottomNavView;

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater) {
        return null;
    }

    public int getRoot() {
        return 0;
    }
}
