package com.peno.mierantauptk.models;

public class MenuModel {
    private int id;
    private String namaMenu;
    private String deskripsi;
    private String fotoUrl;
    private double harga;
    private String kategori;
    private int promo;
    private int tersedia;
    private int levelPedas;
    private String ukuranMinuman;
    private String tanggal;

    public MenuModel(int id, String namaMenu, String deskripsi, String fotoUrl, double harga, String kategori,
                     int promo, int tersedia, int levelPedas, String ukuranMinuman, String tanggal) {
        this.id = id;
        this.namaMenu = namaMenu;
        this.deskripsi = deskripsi;
        this.fotoUrl = fotoUrl;
        this.harga = harga;
        this.kategori = kategori;
        this.promo = promo;
        this.tersedia = tersedia;
        this.levelPedas = levelPedas;
        this.ukuranMinuman = ukuranMinuman;
        this.tanggal = tanggal;
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

    public String getKategori() {
        return kategori;
    }

    public int getPromo() {
        return promo;
    }

    public int getTersedia() {
        return tersedia;
    }

    public int getLevelPedas() {
        return levelPedas;
    }

    public String getUkuranMinuman() {
        return ukuranMinuman;
    }

    public String getTanggal() {
        return tanggal;
    }
}
