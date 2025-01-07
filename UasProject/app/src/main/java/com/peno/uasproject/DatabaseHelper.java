package com.peno.uasproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MieRantau.db";
    public static final int DATABASE_VERSION = 1;

    // Table for users
    public static final String TABLE_USER = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Table for categories
    public static final String TABLE_CATEGORY = "categories";
    public static final String COLUMN_CATEGORY_ID = "id";
    public static final String COLUMN_CATEGORY_NAME = "category_name";

    // Table for menu
    public static final String TABLE_MENU = "menu";
    public static final String COLUMN_MENU_ID = "id";
    public static final String COLUMN_MENU_NAME = "menu_name";
    public static final String COLUMN_MENU_PRICE = "menu_price";
    public static final String COLUMN_MENU_DESCRIPTION = "menu_description";
    public static final String COLUMN_MENU_LEVEL_SIZE = "menu_level_size";
    public static final String COLUMN_MENU_PHOTO = "menu_photo";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(CREATE_USER_TABLE);

        // Create categories table
        String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT UNIQUE)";
        db.execSQL(CREATE_CATEGORY_TABLE);

        // Create menu table
        String CREATE_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + " (" +
                COLUMN_MENU_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MENU_NAME + " TEXT, " +
                COLUMN_MENU_PRICE + " REAL, " +
                COLUMN_MENU_DESCRIPTION + " TEXT, " +
                COLUMN_MENU_LEVEL_SIZE + " TEXT, " +
                COLUMN_MENU_PHOTO + " TEXT, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_CATEGORY_ID + "))";
        db.execSQL(CREATE_MENU_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        onCreate(db);
    }

    // Register a new user
    public boolean registerUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        long result = db.insert(TABLE_USER, null, values);
        db.close();

        return result != -1; // Return true if insertion is successful
    }

    // Check if user exists (login validation)
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER +
                " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
}
