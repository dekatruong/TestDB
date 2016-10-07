package com.dekatruong.testdb.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Deka on 07/10/2016.
 */

abstract public class DaoBase {

    protected FeedReaderDbHelper mDbHelper;

    protected SQLiteDatabase readableDbConnection;
    protected SQLiteDatabase writableDbConnection;

    public DaoBase(Context context) {
        this.mDbHelper = new FeedReaderDbHelper(context);

        //To do: may long-run task
        //this.connectDB();
    }

    public void connectDB() {
        readableDbConnection = mDbHelper.getReadableDatabase();
        writableDbConnection = mDbHelper.getWritableDatabase();
    }

    public void connectReadableDB() {
        readableDbConnection = mDbHelper.getReadableDatabase();
    }

    public void connectWritableDB() {
        readableDbConnection = mDbHelper.getReadableDatabase();
    }
}
