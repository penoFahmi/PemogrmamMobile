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
    public static final String TABLE_CATEGORY = "Category";
    public static final String TABLE_MENU = "Menu";
    public static final String TABLE_CART = "Cart";
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

    // Category Table Columns
    public static final String COLUMN_NAMA_CATEGORY = "nama_category";

    // Menu Table Columns
    public static final String COLUMN_NAMA_MENU = "nama_menu";
    public static final String COLUMN_DESKRIPSI = "deskripsi";
    public static final String COLUMN_FOTO = "foto";
    public static final String COLUMN_HARGA = "harga";
    public static final String COLUMN_STOK = "stok";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    // Cart Table Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_MENU_ID = "menu_id";
    public static final String COLUMN_JUMLAH = "jumlah";
    public static final String COLUMN_SUBTOTAL = "subtotal";

    // Transaksi Table Columns
    public static final String COLUMN_TANGGAL = "tanggal";
    public static final String COLUMN_TOTAL_HARGA = "total_harga";
    public static final String COLUMN_STATUS = "status";

    // DetailTransaksi Table Columns
    public static final String COLUMN_TRANSAKSI_ID = "transaksi_id";

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

        // Create Category Table
        db.execSQL("CREATE TABLE " + TABLE_CATEGORY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_CATEGORY + " TEXT)");


        // Tambahkan data default untuk Category
        if (!isCategoryDataExists(db)) {
            db.execSQL("INSERT INTO " + TABLE_CATEGORY + " (" + COLUMN_NAMA_CATEGORY + ") VALUES " +
                    "('Mie'), " +
                    "('Minuman'), " +
                    "('Extra'), " +
                    "('Dimsum')");
        }

        // Create Menu Table
        db.execSQL("CREATE TABLE " + TABLE_MENU + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA_MENU + " TEXT, " +
                COLUMN_DESKRIPSI + " TEXT, " +
                COLUMN_FOTO + " TEXT, " +
                COLUMN_HARGA + " REAL, " +
                COLUMN_STOK + " INTEGER, " +
                COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORY + "(" + COLUMN_ID + "))");

        // Tambahkan data default untuk Menu (Contoh Random)
        if (!isMenuDataExists(db)) {
            db.execSQL("INSERT INTO " + TABLE_MENU + " (" + COLUMN_NAMA_MENU + ", " + COLUMN_DESKRIPSI + ", " +
                    COLUMN_FOTO + ", " + COLUMN_HARGA + ", " + COLUMN_STOK + ", " + COLUMN_CATEGORY_ID + ") VALUES " +
                    "('Mie Ayam', 'Mie ayam dengan kuah lezat', 'mie_ayam.jpg', 15000, 50, 1), " +
                    "('Es Teh Manis', 'Minuman segar es teh manis', 'es_teh_manis.jpg', 5000, 100, 2), " +
                    "('Dimsum Ayam', 'Dimsum ayam dengan saus', 'dimsum_ayam.jpg', 20000, 30, 4), " +
                    "('Telur Rebus', 'Telur rebus tambahan', 'telur_rebus.jpg', 3000, 20, 3)");

        }

        // Create Cart Table
        db.execSQL("CREATE TABLE " + TABLE_CART + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_MENU_ID + " INTEGER, " +
                COLUMN_JUMLAH + " INTEGER, " +
                COLUMN_SUBTOTAL + " REAL, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + COLUMN_ID + "))");

        // Create Transaksi Table
        db.execSQL("CREATE TABLE " + TABLE_TRANSAKSI + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TANGGAL + " TEXT, " +
                COLUMN_TOTAL_HARGA + " REAL, " +
                COLUMN_STATUS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USER + "(" + COLUMN_ID + "))");

        // Create DetailTransaksi Table
        db.execSQL("CREATE TABLE " + TABLE_DETAIL_TRANSAKSI + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRANSAKSI_ID + " INTEGER, " +
                COLUMN_MENU_ID + " INTEGER, " +
                COLUMN_JUMLAH + " INTEGER, " +
                COLUMN_SUBTOTAL + " REAL, " +
                "FOREIGN KEY(" + COLUMN_TRANSAKSI_ID + ") REFERENCES " + TABLE_TRANSAKSI + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_MENU_ID + ") REFERENCES " + TABLE_MENU + "(" + COLUMN_ID + "))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
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
    private boolean isCategoryDataExists(SQLiteDatabase db) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_CATEGORY, null);
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



//    // Add Role
//    public long addRole(String roleName, String description) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_ROLE_NAME, roleName);
//        values.put(COLUMN_DESCRIPTION, description);
//
//        long result = db.insert(TABLE_ROLE, null, values);
//        db.close();
//        return result;
//    }
//
//    // Get All Roles
//    public Cursor getAllRoles() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        return db.rawQuery("SELECT * FROM " + TABLE_ROLE, null);
//    }

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

    // CRUD for Menu
    public long addMenu(String namaMenu, String deskripsi, String foto, double harga, int stok, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_MENU, namaMenu);
        values.put(COLUMN_DESKRIPSI, deskripsi);
        values.put(COLUMN_FOTO, foto);
        values.put(COLUMN_HARGA, harga);
        values.put(COLUMN_STOK, stok);
        values.put(COLUMN_CATEGORY_ID, categoryId);
        long result = db.insert(TABLE_MENU, null, values);
        db.close();
        return result;
    }

    public Cursor getAllMenus() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU, null);
    }

//    public int updateMenu(int id, String namaMenu, String deskripsi, String foto, double harga, int stok, int categoryId) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_NAMA_MENU, namaMenu);
//        values.put(COLUMN_DESKRIPSI, deskripsi);
//        values.put(COLUMN_FOTO, foto);
//        values.put(COLUMN_HARGA, harga);
//        values.put(COLUMN_STOK, stok);
//        values.put(COLUMN_CATEGORY_ID, categoryId);
//        return db.update(TABLE_MENU, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
//    }

    public int updateMenu(int id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(TABLE_MENU, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteMenu(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_MENU, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllCategories() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CATEGORY, null);
    }

    public Cursor getMenuById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_MENU + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }


    // CRUD for Cart
    public long addToCart(int userId, int menuId, int jumlah, double subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_MENU_ID, menuId);
        values.put(COLUMN_JUMLAH, jumlah);
        values.put(COLUMN_SUBTOTAL, subtotal);
        long result = db.insert(TABLE_CART, null, values);
        db.close();
        return result;
    }

    public Cursor getCartItems(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }

    public int updateCartItem(int id, int jumlah, double subtotal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_JUMLAH, jumlah);
        values.put(COLUMN_SUBTOTAL, subtotal);
        return db.update(TABLE_CART, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CART, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }

    public int clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CART, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
    }
}
