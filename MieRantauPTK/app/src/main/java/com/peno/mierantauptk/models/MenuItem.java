package com.peno.mierantauptk.models;


public class MenuItem {
    private int id;         // ID menu (diambil dari database)
    private String name;    // Nama menu
    private double price;   // Harga menu

    public MenuItem(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getter untuk ID menu
    public int getId() {
        return id;
    }

    // Setter untuk ID menu
    public void setId(int id) {
        this.id = id;
    }

    // Getter untuk nama menu
    public String getName() {
        return name;
    }

    // Setter untuk nama menu
    public void setName(String name) {
        this.name = name;
    }

    // Getter untuk harga menu
    public double getPrice() {
        return price;
    }

    // Setter untuk harga menu
    public void setPrice(double price) {
        this.price = price;
    }

}
