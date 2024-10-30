package com.peno.uts;

public class Product {
    private String name;
    private String description;
    private double price;
    private int imageResourceId;
    private String category;

    public Product(String name, String description, double price, int imageResourceId, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResourceId = imageResourceId;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategory() {
        return category;
    }
}
