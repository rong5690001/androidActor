package com.example.administrator.myapplication1.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/7/3/003.
 */

public class BookContentProvider extends ContentProvider {

    private static final String AUTHORITY = "com.provider";

    public static final String BOOK_CONTENT_URI = "content://" + AUTHORITY + "/book";
    public static final String USER_CONTENT_URI = "content://" + AUTHORITY + "/user";

    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, "book",0);
        uriMatcher.addURI(AUTHORITY, "user",1);
    }

    private Context context;
    private SQLiteDatabase db;
    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate: " + Thread.currentThread().getName());
        context = getContext();
        initProviderData();
        return false;
    }

    private void initProviderData(){
        db = new DBOpenHelper(context).getWritableDatabase();
        db.execSQL("delete from " + DBOpenHelper.BOOK_TABLE_NAME);
        db.execSQL("delete from " + DBOpenHelper.USER_TABLE_NAME);
        db.execSQL("insert into " + DBOpenHelper.BOOK_TABLE_NAME + " values(3, 'android');");
        db.execSQL("insert into " + DBOpenHelper.BOOK_TABLE_NAME + " values(4, 'ios');");
        db.execSQL("insert into " + DBOpenHelper.BOOK_TABLE_NAME + " values(5, 'windows');");
        db.execSQL("insert into " + DBOpenHelper.USER_TABLE_NAME + " values(1, 'laura', 0);");
        db.execSQL("insert into " + DBOpenHelper.USER_TABLE_NAME + " values(2, 'map', 1);");
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Log.d(TAG, "query: " + Thread.currentThread().getName());
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("UnSupported URI: " + uri);
        }
        return db.query(tableName, strings, s, strings1,null, s1,null);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        Log.d(TAG, "getType: ");
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Log.d(TAG, "insert: ");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("UnSupported URI: " + uri);
        }
        db.insert(tableName, null, contentValues);
        context.getContentResolver().notifyChange(uri, null);
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "delete: ");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("UnSupported URI: " + uri);
        }
        int count = db.delete(tableName, s, strings);
        if (count > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        Log.d(TAG, "update: ");
        String tableName = getTableName(uri);
        if (tableName == null) {
            throw new IllegalArgumentException("UnSupported URI: " + uri);
        }
        int row = db.update(tableName, contentValues,s, strings);
        if (row > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri){
        switch (uriMatcher.match(uri)){
            case BOOK_URI_CODE:
                return DBOpenHelper.BOOK_TABLE_NAME;
            case USER_URI_CODE:
                return DBOpenHelper.USER_TABLE_NAME;
        }
        return null;
    }
}
