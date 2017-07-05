package com.chiragawale.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.Toast;

import com.chiragawale.inventoryapp.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    //For Storing Uri sent by Main activity for editing mode
    Uri currentInventoryUri;

    EditText nameEditText;
    EditText supplierEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Localizing views to variables
        nameEditText= (EditText) findViewById(R.id.product_name);
        supplierEditText = (EditText) findViewById(R.id.product_supplier);

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
        String productName = nameEditText.getText().toString();
        String productSupplier = supplierEditText.getText().toString();
        double productPrice = 23;
        double quantityAvailable = 22;

        //Creates a set of values to be sent to be added or updated
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,productName);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER,productSupplier);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE,productPrice);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY_AVAILABLE,quantityAvailable);

        //Checks if the user is either adding or updating and proceeds accordingly
        if(currentInventoryUri == null){
            Uri uri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI,values);
            if(uri != null){
                Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        }else{
            int updateId =getContentResolver().update(currentInventoryUri,values,null,null);
            if(updateId!=-1){
                finish();
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
            }
        }


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
            //Extracting data from cursor
            String name = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            String supplier = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER));
            //Setting data of the cursor.
            nameEditText.setText(name);
            supplierEditText.setText(supplier);
        }
    }
}
