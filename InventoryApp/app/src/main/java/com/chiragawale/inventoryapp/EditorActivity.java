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
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chiragawale.inventoryapp.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    //For Storing Uri sent by Main activity for editing mode
    Uri currentInventoryUri;

    EditText nameEditText;
    EditText supplierEditText;
    EditText priceEditText;
    EditText quantityEditText;

    private boolean modeCheck = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Localizing views to variables
        nameEditText = (EditText) findViewById(R.id.product_name);
        supplierEditText = (EditText) findViewById(R.id.product_supplier);
        quantityEditText = (EditText) findViewById(R.id.product_quantity);
        priceEditText = (EditText) findViewById(R.id.product_price);

        //Stores the sent Uri to a local variable
        currentInventoryUri = getIntent().getData();
        //Setting title and proceeding according to the URI sent if sent
        if (currentInventoryUri == null) {
            setTitle("Add a product");

        } else {
            setTitle("Edit Mode");
            modeCheck=false;
            //Kick off the loader
            getLoaderManager().initLoader(1, null, this);
        }
    }

    //Inflates the customized menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_activity, menu);
        if(modeCheck){
            menu.findItem(R.id.delete_action).setVisible(false);
        }
        return true;
    }

    //Sets actions for menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_action:
                if(checkForInvalidEntry()) {
                    saveproduct();
                }else{
                    Toast.makeText(this, "Please check the fields for valid inputs and try again", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.delete_action:
                deleteProduct();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    boolean checkForInvalidEntry(){
        boolean checker = true;
        //Extracting data from the editable text views
        String productName = nameEditText.getText().toString();
        String productSupplier = supplierEditText.getText().toString();
        double productPrice = Double.parseDouble(priceEditText.getText().toString());
        int quantityAvailable =  Integer.parseInt(quantityEditText.getText().toString());

        if(TextUtils.isEmpty(productName)){
            checker = false;
            Toast.makeText(this, "Null name not allowed", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(productSupplier)){
            checker = false;
            Toast.makeText(this, "Null supplier not allowed", Toast.LENGTH_SHORT).show();
        }
        if(productPrice < 0 || quantityAvailable < 0){
            checker = false;
            Toast.makeText(this, "Negative price or quantity not allowed", Toast.LENGTH_SHORT).show();
        }
        if(checker == true){
            return  true;
        }

        return false;
    }

    void saveproduct() {
        //Extracting data from the editable text views
        String productName = nameEditText.getText().toString();
        String productSupplier = supplierEditText.getText().toString();
        double productPrice = Double.parseDouble(priceEditText.getText().toString());
        int quantityAvailable =  Integer.parseInt(quantityEditText.getText().toString());

        //Creates a set of values to be sent to be added or updated
        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME, productName);
        values.put(InventoryContract.InventoryEntry.COLUMN_SUPPLIER, productSupplier);
        values.put(InventoryContract.InventoryEntry.COLUMN_PRICE, productPrice);
        values.put(InventoryContract.InventoryEntry.COLUMN_QUANTITY_AVAILABLE, quantityAvailable);

        //Checks if the user is either adding or updating and proceeds accordingly
        if (currentInventoryUri == null) {
            Uri uri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
            if (uri != null) {
                Toast.makeText(this, "Insert successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            int updateId = getContentResolver().update(currentInventoryUri, values, null, null);
            if (updateId != -1) {
                finish();
                Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteProduct(){
        //Setting up the delete statement
        String selection = InventoryContract.InventoryEntry._ID+"=?";
        String [] selectionArgs = {String.valueOf(ContentUris.parseId(currentInventoryUri))};
        //Calls the provider for executing the actual statement
        int delId = getContentResolver().delete(currentInventoryUri,selection,selectionArgs);
        if(delId != -1 ){
            Toast.makeText(this, "Delete successful", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Delete Unsuccessful", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, currentInventoryUri, null, null, null, null);
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
    void updateUi(Cursor data) {
        if (data.moveToFirst()) {
            //Extracting data from cursor
            String name = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
            String supplier = data.getString(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER));
            double price = data.getDouble(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
            int quantity = data.getInt(data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY_AVAILABLE));
            //Setting data of the cursor.
            nameEditText.setText(name);
            supplierEditText.setText(supplier);
            quantityEditText.setText(String.valueOf(quantity));
            priceEditText.setText(String.valueOf(price));
        }
    }
}
