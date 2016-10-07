package com.dekatruong.testdb.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dekatruong.testdb.dao.FeedReaderDbHelper.FeedEntry;
import com.dekatruong.testdb.dto.Feed;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Deka on 06/10/2016.
 */

public class FeedDao extends DaoBase {

    public FeedDao(Context context) {
        super(context);
    }

    /**
     *
     * @param feed
     * @return id new record id
     */
    public long create(Feed feed) {
        // Gets the data repository in write mode
        SQLiteDatabase writableDbConnection = mDbHelper.getWritableDatabase(); //To do: connect once in init

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, feed.title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, feed.subtitle);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = writableDbConnection.insert(FeedEntry.TABLE_NAME, null, values);

        //Release connection
        writableDbConnection.close();

        return newRowId;
    }

    /**
     *
     * @param feed_id
     * @return Feed null if cant find
     */
    public Feed getById(long feed_id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); //To do: connect once in init

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(feed_id)};

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        //Fetch
        Feed feed = null;
        if(null != cursor && !cursor.isAfterLast()) {
            cursor.moveToFirst();
            feed = FeedDao.fetchRowToFeed(cursor);
        }

        return feed;
    }

    public List<Feed> getByTitle(String title) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); //To do: connect once in init

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = {title};

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        //Fetch
        cursor.moveToFirst();
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );

        List<Feed> result_list = new LinkedList();
        while (cursor.isAfterLast() == false) {
            Feed feed = FeedDao.fetchRowToFeed(cursor);

            result_list.add(feed);

            cursor.moveToNext();
        }

        return result_list;
    }

    public List<Feed> getAll() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); //To do: connect once in init

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

//        String limit = "1, 100";

        //Execute query
        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        //Release connection
        db.close();

        //Fetch
        return FeedDao.fetchToFeedList(cursor);
    }

    public Cursor getAllToCursor() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase(); //To do: connect once in init

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.COLUMN_NAME_TITLE,
                FeedEntry.COLUMN_NAME_SUBTITLE
        };

        // How you want the results sorted in the resulting Cursor
//        String sortOrder =
//                FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

//        String limit = "1, 100";

        //Execute query
        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        //Fetch
        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    private static List<Feed> fetchToFeedList(Cursor cursor) {
        cursor.moveToFirst();
        long itemId = cursor.getLong(
                cursor.getColumnIndexOrThrow(FeedEntry._ID)
        );

        List<Feed> result_list = new LinkedList();
        while (cursor.isAfterLast() == false) {
            Feed feed = FeedDao.fetchRowToFeed(cursor);

            result_list.add(feed);

            cursor.moveToNext();
        }

        return result_list;
    }

    private static Feed fetchRowToFeed(Cursor cursor) {
        long rs_id = cursor.getLong(cursor.getColumnIndex(FeedEntry._ID));
        String rs_title = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_TITLE));
        String rs_subtitle = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_SUBTITLE));

        Feed rs = new Feed(rs_title, rs_subtitle);
        rs.setId(rs_id); //to do: work with DB _id

        return rs;
    }

    public int deleteByTitle(String title) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase(); //To do: connect once in init

        // Define 'where' part of query.
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {title};

        // Issue SQL statement.
        int affected_row = db.delete(FeedEntry.TABLE_NAME, selection, selectionArgs);

        return affected_row;
    }

    public int updateTitle(String old_tile, String new_title) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, old_tile);

        // Which row to update, based on the title
        String selection = FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
        String[] selectionArgs = {new_title};

        int affected_row = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return affected_row;
    }

    public int updateById(long id, Feed new_feed) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_NAME_TITLE, new_feed.title);
        values.put(FeedEntry.COLUMN_NAME_SUBTITLE, new_feed.subtitle);

        // Which row to update, based on the title
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        int affected_row = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        //
        db.close();

        return affected_row;
    }


}
