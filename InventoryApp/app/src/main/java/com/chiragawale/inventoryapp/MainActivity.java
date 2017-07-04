package com.chiragawale.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.chiragawale.inventoryapp.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private ProductCursorAdapter mProductAdapter;
    private static final int PRODUCTLOADER_ID = 0;
    private static final String projection[] = {
            InventoryEntry._ID,
            InventoryEntry.COLUMN_PRODUCT_NAME,
            InventoryEntry.COLUMN_PRICE,
            InventoryEntry.COLUMN_QUANTITY_AVAILABLE,
            InventoryEntry.COLUMN_SUPPLIER
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //List view for populating data with adapter
        ListView listView = (ListView) findViewById(R.id.list);

        //Empty adapter ro setup listview
        mProductAdapter = new ProductCursorAdapter(this, null);

        listView.setAdapter(mProductAdapter);

        Log.w("Main", "Initializing loader");


        //Kick off the loader
        getLoaderManager().initLoader(PRODUCTLOADER_ID, null, this);
        Log.w("Main", "Initialized loader");

        //Sets up individual click listeners to each item in the list view to forward to edit mode
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI,id);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.setData(currentProductUri);
                startActivity(intent);
            }
        });
    }

    /*
    Inflates a menu with delete and add dummy data options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    /**
     * Sets Actions to menu items
     *
     * @param item the item which is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                Toast.makeText(this, "All items deleted", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.insert_dummy_data:
                insertProduct();
                //Toast.makeText(this, "Dummy data inserted", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insertProduct() {
        //Setting up the insert statement
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "Sony Vaio");
        values.put(InventoryEntry.COLUMN_PRICE, 233);
        values.put(InventoryEntry.COLUMN_SUPPLIER, "Sony");
        values.put(InventoryEntry.COLUMN_QUANTITY_AVAILABLE, 45);

        Uri uri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
        }
    }

    //Calls the query method and returns result
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.w("Loader", "Oncreate loader");
        return new CursorLoader(this, InventoryEntry.CONTENT_URI, projection, null
                , null, null);
    }
    //Loads up the data
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.w("Loader", "On Finished loader");
        mProductAdapter.swapCursor(data);
    }
    //Releases the adapter
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mProductAdapter.swapCursor(null);
    }
}
