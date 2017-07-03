package com.chiragawale.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.chiragawale.inventoryapp.MainActivity;
import com.chiragawale.inventoryapp.data.InventoryContract.InventoryEntry;


/**
 * Created by chira on 7/2/2017.
 */

public class InventoryProvider extends ContentProvider {
    public static final String LOG_TAG = InventoryEntry.class.getSimpleName();
    InventoryDbHelper mInventoryDbHelper;

    public final static int INVENTORIES = 100;
    public final static int INVENTORY_ID = 101;

    /**
     * Setting up the Uri Matcher to configure behavior according to request (Single inventory or whole list)
     */
    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        mUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORIES, INVENTORIES);
        mUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORIES + "/#", INVENTORY_ID);
    }


    @Override
    public boolean onCreate() {
        mInventoryDbHelper = new InventoryDbHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       //Database for reading data from
        SQLiteDatabase db = mInventoryDbHelper.getReadableDatabase();
        //Cursor for storing result set from database
        Cursor cursor = null;
        switch (mUriMatcher.match(uri)) {
            case INVENTORIES:
                //Executes a select (Projection) from TABLE; command
                cursor = db.query(InventoryEntry.TABLE_NAME, null, selection, selectionArgs, null, null, sortOrder);
                Log.e("Cursor query method","cursor returned");
                break;
            case INVENTORY_ID:
                break;

            default:
                throw new IllegalArgumentException("Invalid query uri");
        }
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        return null;
    }

    //Returns error if invalid uri is sent when trying to insert
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (mUriMatcher.match(uri)) {
            case INVENTORIES:
                return insertProduct(uri, values);

            default:
                throw new IllegalArgumentException("Inserstion not supported for " + uri);
        }
    }

    //The method that actually carries out the insert method
    private Uri insertProduct(Uri uri, ContentValues values) {
        SQLiteDatabase db = mInventoryDbHelper.getWritableDatabase();
        long insID = db.insert(InventoryEntry.TABLE_NAME, null, values);
        if (insID == -1) {
            Log.e(LOG_TAG, "Insert failed");
            return null;
        }
        return ContentUris.withAppendedId(uri, insID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
