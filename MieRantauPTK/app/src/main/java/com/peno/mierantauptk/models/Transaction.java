package com.peno.mierantauptk.models;
public class Transaction {
    private int id;
    private String date;
    private double total;
    private String paymentMethod;

    public Transaction(int id, String date, double total, String paymentMethod) {
        this.id = id;
        this.date = date;
        this.total = total;
        this.paymentMethod = paymentMethod;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getTotal() {
        return total;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
