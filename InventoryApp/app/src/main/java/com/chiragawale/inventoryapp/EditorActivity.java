package com.chiragawale.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.chiragawale.inventoryapp.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    //For Storing Uri sent by Main activity for editing mode
    Uri currentInventoryUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        //Stores the sent Uri to a local variable
        currentInventoryUri = getIntent().getData();
        //Setting title and proceeding according to the URI sent if sent
        if(currentInventoryUri==null){
            setTitle("Add a product");
        }else {
            setTitle("Edit Mode");
            //Kick off the loader
            getLoaderManager().initLoader(1, null, this);
        }
        }

    //Inflates the customized menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_activity, menu);
        return true;
    }
    //Sets actions for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_action:
                saveproduct();
                return true;
            case R.id.delete_action:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void saveproduct(){
        /***
         * Next TODO Set up Insert function
         */
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,currentInventoryUri,null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       updateUi(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    finish();
    }
    //Updates the UI after the loading is done
    void updateUi(Cursor data){
        if(data.moveToFirst()) {
            EditText nameEditText = (EditText) findViewById(R.id.product_name);
            EditText supplierEditText = (EditText) findViewById(R.id.product_supplier);

            String name = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            String supplier = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER));

            nameEditText.setText(name);
            supplierEditText.setText(supplier);
        }
    }
}
