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
                selection = InventoryEntry._ID + "=?";
                selectionArgs =  new String [] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(InventoryEntry.TABLE_NAME,null,selection,selectionArgs,null,null,sortOrder);
                break;

            default:
                throw new IllegalArgumentException("Invalid query uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
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
        //Gets the database in a writable format
        SQLiteDatabase db = mInventoryDbHelper.getWritableDatabase();
        //Executeing the insert command
        long insID = db.insert(InventoryEntry.TABLE_NAME, null, values);
        if (insID == -1) {
            Log.e(LOG_TAG, "Insert failed");
            return null;
        }
        //Setting up change notification configuration to notify of changes
        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri, insID);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //Getting the database in editable format
        SQLiteDatabase db = mInventoryDbHelper.getWritableDatabase();
        //Executes the delete statement and returns the delete id
        int delId = db.delete(InventoryEntry.TABLE_NAME,selection,selectionArgs);
        //Setting up change notification configuration to notify of changes
        getContext().getContentResolver().notifyChange(uri,null);
        return delId;
    }


    //Handles the update case and returns update Id
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mInventoryDbHelper.getWritableDatabase();
        selection = InventoryEntry._ID+"=?";
        selectionArgs = new String [] {String.valueOf(ContentUris.parseId(uri))};
        int updateId = db.update(InventoryEntry.TABLE_NAME,values,selection,selectionArgs);
        //Setting up change notification configuration to notify of changes
        getContext().getContentResolver().notifyChange(uri,null);
        return updateId;
    }
}
