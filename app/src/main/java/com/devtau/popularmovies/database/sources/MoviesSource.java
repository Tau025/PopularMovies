package com.devtau.popularmovies.database.sources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.BaseColumns;
import android.widget.Toast;
import com.devtau.popularmovies.database.MySQLHelper;
import com.devtau.popularmovies.model.Movie;
import com.devtau.popularmovies.util.Logger;
import java.util.ArrayList;
import static com.devtau.popularmovies.database.tables.MoviesTable.*;

public class MoviesSource {
    //represents top level of abstraction from dataBase
    //all work with db layer must be done in this class
    //getReadableDatabase() and getWritableDatabase() must not be called outside this class
    private final String LOG_TAG = MoviesSource.class.getSimpleName();
    private static final String ERROR_TOAST = "db access error";
    private MySQLHelper dbHelper;
    private Context context;

    public MoviesSource(Context context) {
        this.context = context.getApplicationContext();
        this.dbHelper = MySQLHelper.getInstance(this.context);
    }

    public long create(Movie item){
        long id = -1;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = getContentValues(item);
            id = db.insert(TABLE_NAME, null, cv);
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        return id;
    }

    public boolean update(Movie item){
        int result = -1;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = getContentValues(item);
            result = db.update(TABLE_NAME, cv, BaseColumns._ID + " = ?", new String[]{String.valueOf(item.getId())});
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        return result != -1;
    }

    public boolean remove(Movie item){
        int result = -1;
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            result = db.delete(TABLE_NAME, BaseColumns._ID + " = ?", new String[]{String.valueOf(item.getId())});
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        return result != -1;
    }

    //moviesSource.getItemsList() would return non-null ArrayList even if it's empty
    public ArrayList<Movie> getItemsList() {
        ArrayList<Movie> itemsList = new ArrayList<>();
        String sortMethod = "ASC";
        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " ORDER BY " + DATE + " " + sortMethod;
        Logger.d(LOG_TAG, "getItemsList() selectQuery: " + String.valueOf(selectQuery));

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    itemsList.add(new Movie(cursor));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        Logger.d(LOG_TAG, "getItemsList() itemsList.size(): " + String.valueOf(itemsList.size()));
        return itemsList;
    }

    public int getItemsCount() {
        int itemsCount = 0;
        String selectQuery = "SELECT COUNT (*) FROM " + TABLE_NAME;

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                itemsCount = cursor.getInt(0);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        Logger.d(LOG_TAG, "getItemsCount() itemsCount: " + String.valueOf(itemsCount));
        return itemsCount;
    }

    public Movie getItemByID(long itemID) {
        Movie movie = null;
        String selectQuery = "SELECT * FROM " + TABLE_NAME
                + " WHERE " + BaseColumns._ID + "='" + String.valueOf(itemID) + "'";

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                movie = new Movie(cursor);
            }
            cursor.close();
        } catch (SQLiteException e) {
            Toast.makeText(context, ERROR_TOAST, Toast.LENGTH_SHORT).show();
        }
        return movie;
    }
}