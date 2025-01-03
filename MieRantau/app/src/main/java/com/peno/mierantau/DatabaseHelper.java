package com.peno.mierantau;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MieRantau.db";
    private static final int DATABASE_VERSION = 1;

    // Table: Users
    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    // Table: Produk
    private static final String TABLE_PRODUK = "produk";
    private static final String COLUMN_PRODUK_ID = "id";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_HARGA = "harga";
    private static final String COLUMN_GAMBAR = "gambar";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users Table
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USERS_TABLE);

        // Create Produk Table
        String CREATE_PRODUCTS_TABLE = "CREATE TABLE " + TABLE_PRODUK + "("
                + COLUMN_PRODUK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAMA + " TEXT, "
                + COLUMN_HARGA + " INTEGER, "
                + COLUMN_GAMBAR + " INTEGER)";
        db.execSQL(CREATE_PRODUCTS_TABLE);

        // Insert Default Master User
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, "admin");
        values.put(COLUMN_PASSWORD, "admin123");
        db.insert(TABLE_USERS, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUK);
        onCreate(db);
    }

    // User Authentication
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, new String[]{COLUMN_USER_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password}, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public void registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
    }

    // Produk Operations
    public void addProduk(String nama, int harga, int gambar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_HARGA, harga);
        values.put(COLUMN_GAMBAR, gambar);
        db.insert(TABLE_PRODUK, null, values);
    }

    public List<Produk> getAllProduk() {
        List<Produk> produkList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUK, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Produk produk = new Produk(
                        cursor.getInt(cursor.getColumnIndex(COLUMN_PRODUK_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAMA)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_HARGA)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_GAMBAR))
                );
                produkList.add(produk);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return produkList;
    }

    public void updateProduk(int id, String nama, int harga, int gambar) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_HARGA, harga);
        values.put(COLUMN_GAMBAR, gambar);
        db.update(TABLE_PRODUK, values, COLUMN_PRODUK_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteProduk(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUK, COLUMN_PRODUK_ID + "=?", new String[]{String.valueOf(id)});
    }
}
