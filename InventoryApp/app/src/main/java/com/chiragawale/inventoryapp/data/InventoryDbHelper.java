package com.chiragawale.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.chiragawale.inventoryapp.data.InventoryContract.InventoryEntry;
public class InventoryDbHelper extends SQLiteOpenHelper {
    /*
    Database name
     */
    private static final  String DATABASE_NAME = "inventory.db";
    /*
    Database version
     */
    private static final int DATABASE_VERSION = 1;


    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //Creates a table if the table is not already created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE "
                +InventoryEntry.TABLE_NAME + "("
                +InventoryEntry._ID + " INTEGER   PRIMARY KEY AUTOINCREMENT, "
                +InventoryEntry.COLUMN_PRODUCT_NAME+ " TEXT NOT NULL, "
                +InventoryEntry.COLUMN_PRICE + " REAL NOT NULL, "
                +InventoryEntry.COLUMN_QUANTITY_AVAILABLE + " INTEGER DEFAULT 0 NOT NULL, "
                +InventoryEntry.COLUMN_SUPPLIER + " TEXT); ";
        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
