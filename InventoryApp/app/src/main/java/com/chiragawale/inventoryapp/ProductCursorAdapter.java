package com.chiragawale.inventoryapp;

import android.content.Context;
import android.database.Cursor;
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

        //Extracting data from cursor
        String name = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME));
        String supplier = cursor.getString(cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_SUPPLIER));

        //Sets data of text views according to data extracted from cursor
        nameTextView.setText(name);
        supplierTextView.setText(supplier);
    }
}
