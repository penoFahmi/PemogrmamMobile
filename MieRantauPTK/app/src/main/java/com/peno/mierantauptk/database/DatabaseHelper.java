package com.peno.mierantauptk.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MieRantauPontianak.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_ROLE = "Role";
    public static final String TABLE_USER = "User";
    public static final String TABLE_MENU = "Menu";
    public static final String TABLE_TRANSAKSI = "Transaksi";
    public static final String TABLE_DETAIL_TRANSAKSI = "DetailTransaksi";

    // Common Columns
    public static final String COLUMN_ID = "id";

    // Role Table Columns
    public static final String COLUMN_ROLE_NAME = "role_name";
    public static final String COLUMN_DESCRIPTION = "description";

    // User Table Columns
    public static final String COLUMN_NAMA = "nama";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_NO_TELP = "no_telp";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE_ID = "role_id";


    // Tabel: Menu
    public static final String COLUMN_NAMA_MENU = "nama_menu";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_HARGA = "harga";
    public static final String COLUMN_KATEGORI = "kategori_menu";
    public static final String COLUMN_PROMO = "promo";
    public static final String COLUMN_TERSEDIA = "tersedia";
    public static final String COLUMN_LEVEL_PEDAS = "level_pedas";
    public static final String COLUMN_UKURAN_MINUMAN = "ukuran_minuman";
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_WAKTU = "waktu";

    // Tabel: Transaksi
    public static final String COLUMN_TANGGAL_TRANSAKSI = "tanggal_transaksi";
    public static final String COLUMN_TOTAL_HARGA = "total_harga";
    public static final String COLUMN_METODE_PEMBAYARAN = "metode_pembayaran";
    public static final String COLUMN_KASIR = "kasir";

    // DetailTransaksi Table Columns

    // Tabel: Detail Transaksi
    public static final String COLUMN_ID_MENU = "id_menu";
    public static final String COLUMN_JUMLAH = "jumlah";
    public static final String COLUMN_SUB_TOTAL = "sub_total";
    public static final String COLUMN_CATATAN = "catatan";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Role Table
        db.execSQL("CREATE TABLE " + TABLE_ROLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ROLE_NAME + " TEXT UNIQUE, " +
                COLUMN_DESCRIPTION + " TEXT)");

        // Tambahkan data default untuk Role jika belum ada
        if (!isRoleDataExists(db)) {
            db.execSQL("INSERT INTO " + TABLE_ROLE + " (" + COLUMN_ROLE_NAME + ", " + COLUMN_DESCRIPTION + ") VALUES " +
                    "('admin', 'Administrator with full access'), " +
                    "('user', 'Regular user with limited access')");
        }
        // Create User Table
        db.execSQL("CREATE TABLE " + TABLE_USER + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_NO_TELP + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_ROLE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ROLE_ID + ") REFERENCES " + TABLE_ROLE + "(" + COLUMN_ID + "))");

        // Tambahkan data default untuk User (Admin)
        if (!isAdminDataExists(db)) {
            db.execSQL("INSERT INTO " + TABLE_USER + " (" + COLUMN_NAMA + ", " + COLUMN_EMAIL + ", " +
                    COLUMN_NO_TELP + ", " + COLUMN_PASSWORD + ", " + COLUMN_ROLE_ID + ") VALUES " +
                    "('admin', 'admin@mierantau.com', '081234567890', 'admin', 1)");
        }


        // Create Menu Table
        // Membuat tabel Menu
        String createTableMenu = "CREATE TABLE " + TABLE_MENU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_MENU + " TEXT, " +
                COLUMN_DESKRIPSI + " TEXT, " +
                COLUMN_FOTO + " TEXT, " +
                COLUMN_HARGA + " REAL, " +
                COLUMN_KATEGORI + " TEXT, " +
                COLUMN_PROMO + " INTEGER, " +
                COLUMN_TERSEDIA + " INTEGER, " +
                COLUMN_LEVEL_PEDAS + " INTEGER, " +
                COLUMN_UKURAN_MINUMAN + " TEXT, " +
                COLUMN_TANGGAL + " TEXT, " +
                COLUMN_WAKTU + " TEXT) ";
        db.execSQL(createTableMenu);

        // Membuat tabel Transaksi
        String createTableTransaksi = "CREATE TABLE " + TABLE_TRANSAKSI + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TANGGAL_TRANSAKSI + " TEXT, " +
                COLUMN_TOTAL_HARGA + " REAL, " +
                COLUMN_METODE_PEMBAYARAN + " TEXT, " +
                COLUMN_KASIR + " TEXT)";
        db.execSQL(createTableTransaksi);

        // Create DetailTransaksi Table
        // Membuat tabel Detail Transaksi
        String createTableDetailTransaksi = "CREATE TABLE " + TABLE_DETAIL_TRANSAKSI + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ID + "_transaksi INTEGER, " +
                COLUMN_ID_MENU + " INTEGER, " +
                COLUMN_JUMLAH + " INTEGER, " +
                COLUMN_SUB_TOTAL + " REAL, " +
                COLUMN_CATATAN + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_ID + "_transaksi) REFERENCES " + TABLE_TRANSAKSI + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ID_MENU + ") REFERENCES " + TABLE_MENU + "(" + COLUMN_ID + "))";
        db.execSQL(createTableDetailTransaksi);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSAKSI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAIL_TRANSAKSI);
        onCreate(db);
    }

    // mengambil data dari tabel Role biar tidak duplikan data defaultnye
    private boolean isRoleDataExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ROLE, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        cursor.close();
        return false;
    }

    private boolean isMenuDataExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_MENU, null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        cursor.close();
        return false;
    }

    private boolean isAdminDataExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USER + " WHERE " + COLUMN_ROLE_ID + "=1", null);
        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        cursor.close();
        return false;
    }


    // CRUD for User
    public long addUser(String nama, String email, String noTelp, String password, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NO_TELP, noTelp);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE_ID, roleId);
        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER, null);
    }

    public long registerUser(String nama, String email, String noTelp, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NO_TELP, noTelp);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE_ID, 2); // Default Role ID for "user"

        long result = db.insert(TABLE_USER, null, values);
        db.close();
        return result; // Mengembalikan ID dari user yang ditambahkan
    }

    public int loginUser(String nama, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_ROLE_ID +
                        " FROM " + TABLE_USER +
                        " WHERE " + COLUMN_NAMA + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{nama, password});

        int roleId = -1; // Default value if login fails
        if (cursor.moveToFirst()) {
            roleId = cursor.getInt(0); // Get role_id if user exists
        }
        cursor.close();
        return roleId; // Returns -1 if login fails, or role_id if successful
    }



    public int updateUser(int id, String nama, String email, String noTelp, String password, int roleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA, nama);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_NO_TELP, noTelp);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_ROLE_ID, roleId);
        return db.update(TABLE_USER, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_USER, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }




    // Insert Menu
    public long insertMenu(String namaMenu, String deskripsi, String foto, double harga, String kategori,
                           int promo, int tersedia, Integer levelPedas, String ukuranMinuman, String tanggal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_MENU, namaMenu);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_FOTO, foto);
        values.put(COLUMN_HARGA, harga);
        values.put(COLUMN_KATEGORI, kategori);
        values.put(COLUMN_PROMO, promo);
        values.put(COLUMN_TERSEDIA, tersedia);
        values.put(COLUMN_LEVEL_PEDAS, levelPedas);
        values.put(COLUMN_UKURAN_MINUMAN, ukuranMinuman);
        values.put(COLUMN_TANGGAL, tanggal);
        return db.insert(TABLE_MENU, null, values); // Mengembalikan ID baris baru
    }

    // Mengambil semua data Menu
    public Cursor getAllMenus() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU, null); // Mengembalikan Cursor
    }

    public Cursor getMenusByCategory(String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query;
        if (category.equalsIgnoreCase("Semua")) {
            query = "SELECT * FROM " + TABLE_MENU;
        } else {
            query = "SELECT * FROM " + TABLE_MENU + " WHERE " + COLUMN_KATEGORI + " = ?";
        }
        return db.rawQuery(query, category.equalsIgnoreCase("Semua") ? null : new String[]{category});
    }

    public Cursor searchMenus(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String searchQuery = "SELECT * FROM " + TABLE_MENU + " WHERE " + COLUMN_NAMA_MENU + " LIKE ?";
        return db.rawQuery(searchQuery, new String[]{"%" + query + "%"});
    }


    public int updateMenu(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_MENU, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    // Hapus Menu berdasarkan ID
    public int deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getMenuById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    // Insert Transaksi
    public long insertTransaksi(String tanggalTransaksi, double totalHarga, String metodePembayaran, String kasir) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TANGGAL_TRANSAKSI, tanggalTransaksi);
        values.put(COLUMN_TOTAL_HARGA, totalHarga);
        values.put(COLUMN_METODE_PEMBAYARAN, metodePembayaran);
        values.put(COLUMN_KASIR, kasir);
        return db.insert(TABLE_TRANSAKSI, null, values);
    }

    // Insert Detail Transaksi
    public long insertDetailTransaksi(int idTransaksi, int idMenu, int jumlah, double subTotal, String catatan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID + "_transaksi", idTransaksi);
        values.put(COLUMN_ID_MENU, idMenu);
        values.put(COLUMN_JUMLAH, jumlah);
        values.put(COLUMN_SUB_TOTAL, subTotal);
        values.put(COLUMN_CATATAN, catatan);
        return db.insert(TABLE_DETAIL_TRANSAKSI, null, values);
    }

    // Mengambil semua data Transaksi
    public Cursor getAllTransaksi() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TRANSAKSI, null);
    }

    public Cursor getDetailTransaksiById(int idTransaksi) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_DETAIL_TRANSAKSI + " WHERE " + COLUMN_ID + "_transaksi=?";
        return db.rawQuery(query, new String[]{String.valueOf(idTransaksi)});
    }

    public Cursor getAdminData() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_ROLE_ID + " = 1 LIMIT 1", null);
    }

}
