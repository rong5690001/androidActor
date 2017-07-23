package com.example.administrator.myapplication1;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.administrator.myapplication1.customview.CustomViewActivity;
import com.example.administrator.myapplication1.socket.SocketActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Uri uri = Uri.parse("content://com.example.administrator.myapplication.book.provider/book");
//        getContentResolver().query(uri, null, null ,null,null);
//        getContentResolver().query(uri, null, null ,null,null);
//        getContentResolver().query(uri, null, null ,null,null);
        Uri bookUri = Uri.parse("content://com.provider/book");
        ContentValues values = new ContentValues();
        values.put("_id", 6);
        values.put("name", "android开发艺术探索");
        getContentResolver().insert(bookUri, values);
        Cursor bookCursor = getContentResolver().query(bookUri, new String[]{"_id", "name"}, null, null, null);
        while(bookCursor.moveToNext()){
            Log.d(TAG, "_id: " + bookCursor.getString(0));
            Log.d(TAG, "name: " + bookCursor.getString(1));
        }
        bookCursor.close();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.socket:
                startActivity(new Intent(this, SocketActivity.class));
                break;
            case R.id.customActivity:
                startActivity(new Intent(this, CustomViewActivity.class));
                break;
        }
    }
}
