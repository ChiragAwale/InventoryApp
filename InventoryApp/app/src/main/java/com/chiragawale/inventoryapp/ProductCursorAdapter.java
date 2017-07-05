package com.chiragawale.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.chiragawale.inventoryapp.data.InventoryContract;

/**
 * Created by chira on 7/3/2017.
 */

public class ProductCursorAdapter extends CursorAdapter {
    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    //Creates a new list item if not present
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    //Loads up the list item with data from cursor
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Text views for changing their texts
        TextView nameTextView = (TextView) view.findViewById(R.id.product_name);
        TextView supplierTextView = (TextView) view.findViewById(R.id.product_supplier);
        TextView priceTextView = (TextView) view.findViewById(R.id.product_price);
        TextView quantityTextview = (TextView) view.findViewById(R.id.quantityAvailable);


        //Extracting data from cursor
        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String supplier = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER));
        String price = "$"+ cursor.getDouble(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE));
        String quantity = "Q - "+cursor.getInt(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY_AVAILABLE));

        //Sets data of text views according to data extracted from cursor
        nameTextView.setText(name);
        if(TextUtils.isEmpty(supplier)){
            supplierTextView.setText("Unknown");
        }else {
            supplierTextView.setText(supplier);
        }
        if(TextUtils.isEmpty(price)){
            priceTextView.setText("N/A");
        }else {
            priceTextView.setText(price);
        }
        if(TextUtils.isEmpty(quantity)){
            quantityTextview.setText("N/A");
        }else {
            quantityTextview.setText(quantity);
        }

    }
}
