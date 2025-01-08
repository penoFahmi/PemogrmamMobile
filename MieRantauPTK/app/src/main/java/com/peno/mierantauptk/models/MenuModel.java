package com.peno.mierantauptk.models;

public class MenuModel {
    private int id;
    private String namaMenu;
    private String deskripsi;
    private String fotoUrl;
    private double harga;
    private int stok;

    public MenuModel(int id, String namaMenu, String deskripsi, String fotoUrl, double harga, int stok) {
        this.id = id;
        this.namaMenu = namaMenu;
        this.deskripsi = deskripsi;
        this.fotoUrl = fotoUrl;
        this.harga = harga;
        this.stok = stok;
    }

    public int getId() {
        return id;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }
}
