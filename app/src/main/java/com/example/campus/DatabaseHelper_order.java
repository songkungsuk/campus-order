package com.example.campus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper_order extends SQLiteOpenHelper {
    public static String NAME = "order.db";
    public static int VERSION = 1;
    public static String tableName = "index_product";
    public DatabaseHelper_order(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists index_product("
                + " _number integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " brand text, "
                + " price integer, "
                + " img integer)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS index_product");
        }
        db.execSQL("DROP TABLE IF EXISTS index_product");
        onCreate(db);
    }

}
