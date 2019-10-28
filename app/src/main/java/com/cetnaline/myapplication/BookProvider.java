package com.cetnaline.myapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class BookProvider extends ContentProvider {
    private static final String TAG = "BookProvider";

    public static final String AUTHORITY = "com.cetnaline.myapplication.PROVIDER";

    public static final Uri BOOK_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/book");

    public static final Uri USER_CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/user");

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUriMatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }

    private SQLiteDatabase mDb;

    private String getTableName(Uri uri) {
        String tableName = null;

        switch (sUriMatcher.match(uri)) {
            case 0:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case 1:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
        }

        return tableName;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate, current thread:"+Thread.currentThread().getName());
        mDb = new DbOpenHelper(getContext()).getWritableDatabase();
        mDb.execSQL("delete from "+DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'ANDROID')");
        mDb.execSQL("insert into book values(4,'IOS')");
        mDb.execSQL("insert into book values(5,'HTML')");
        mDb.execSQL("insert into book values(6,'JAVA')");
        mDb.execSQL("insert into user values(1,'Jack',1)");
        mDb.execSQL("insert into user values(2,'Curry',0)");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query, current thread:"+Thread.currentThread().getName());
        String table = getTableName(uri);
        if (table == null) {
            throw  new IllegalArgumentException("Unsupported URI:"+uri);
        }

        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "getType");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(TAG, "insert");
        String table = getTableName(uri);
        if (table == null) {
            throw  new IllegalArgumentException("Unsupported URI:"+uri);
        }
        mDb.insert(table, null,values);
        getContext().getContentResolver().notifyChange(uri,null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.d(TAG, "delete");
        String table = getTableName(uri);
        if (table == null) {
            throw  new IllegalArgumentException("Unsupported URI:"+uri);
        }
        int count = mDb.delete(table,selection,selectionArgs);
        if (count>0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update");
        String table = getTableName(uri);
        if (table == null) {
            throw  new IllegalArgumentException("Unsupported URI:"+uri);
        }

        int row = mDb.update(table, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }
}
