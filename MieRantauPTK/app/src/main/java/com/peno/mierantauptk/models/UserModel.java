package com.peno.mierantauptk.models;

public class UserModel {
    private int id;
    private String nama;
    private String email;
    private String noTelp;

    public UserModel(int id, String nama, String email, String noTelp) {
        this.id = id;
        this.nama = nama;
        this.email = email;
        this.noTelp = noTelp;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getNoTelp() {
        return noTelp;
    }
}
