package com.chiragawale.inventoryapp;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.chiragawale.inventoryapp.data.InventoryContract.InventoryEntry;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    /*
    Inflates a menu with delete and add dummy data options
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu,menu);
        return true;
    }

    /**
     * Sets Actions to menu items
     * @param item the item which is selected
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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

    public void insertProduct(){
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME,"Sony Vaio");
        values.put(InventoryEntry.COLUMN_PRICE,233);
        values.put(InventoryEntry.COLUMN_SUPPLIER,"Sony");
        values.put(InventoryEntry.COLUMN_QUANTITY_AVAILABLE,45);

        Uri uri =  getContentResolver().insert(InventoryEntry.CONTENT_URI,values);
            if (uri!= null)
            {Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
        }
    }
}
