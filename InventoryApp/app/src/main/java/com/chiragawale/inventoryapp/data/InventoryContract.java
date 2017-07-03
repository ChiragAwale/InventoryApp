package com.chiragawale.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chira on 7/2/2017.
 */

public class InventoryContract {
    /**
     * To prevent someone from accidentally initializing the class
     */
    private InventoryContract(){}
    public static final String CONTENT_AUTHORITY = "com.chiragawale.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_INVENTORIES = "inventory";


    public static final class InventoryEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH_INVENTORIES);

        public static final String TABLE_NAME = "inventory";

        /*
        Unique number for the inventory id
        Type : INTEGER
         */
        public static final String _ID = BaseColumns._ID;
        /*
        For Product Name
        Type : TEXT
         */
        public static final String COLUMN_PRODUCT_NAME = "productName";
        /*
         Price
         Type: REAL
         */
        public static final String COLUMN_PRICE = "price";
        /*
        Quantity available
        Type : INTEGER
         */
        public static final String COLUMN_QUANTITY_AVAILABLE = "quantity";
        /*
        Supplier information
        Type : TEXT
         */
        public static final String COLUMN_SUPPLIER = "supplier";

    }



}
