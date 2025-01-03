package com.peno.mierantau;

public class Produk {
    private int id;
    private String nama;
    private int harga;
    private int gambar;

    public Produk(int id, String nama, int harga, int gambar) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.gambar = gambar;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public int getHarga() {
        return harga;
    }

    public int getGambar() {
        return gambar;
    }
}
