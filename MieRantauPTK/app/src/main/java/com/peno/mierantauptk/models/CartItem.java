package com.peno.mierantauptk.models;

public class CartItem {
    private int idMenu;
    private String name;
    private int quantity;
    private double price;
    private String note; // Catatan tambahan

    public CartItem(int idMenu, String name, double price) {
        this.idMenu = idMenu;
        this.name = name;
        this.price = price;
        this.quantity = 1; // Default jumlah
        this.note = ""; // Default tanpa catatan
    }

    public CartItem(int idMenu, String name, double price, int quantity, String note) {
        this.idMenu = idMenu;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.note = note;
    }

    public int getIdMenu() {
        return idMenu;
    }

    public void setIdMenu(int idMenu) {
        this.idMenu = idMenu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getSubtotal() {
        return quantity * price;
    }

    public void incrementQuantity() {
        this.quantity++;
    }

    public void decrementQuantity() {
        if (this.quantity > 1) {
            this.quantity--;
        }
    }
}
