package com.android.fourthwardcoder.movingpictures.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.fourthwardcoder.movingpictures.data.FavoritesContract.FavoritesEntry;

/**
 * Class FavoritesDbHelper
 * Author: Chris Hare
 * Created: 9/18/2015.
 * <p/>
 * Database helper for the Movies Content Provider.
 */
public class FavoritesDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "favorites.db";

    public FavoritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         * Create MovieOld's Table
         */
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavoritesEntry.TABLE_NAME + " (" +

                FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the movie entry associated with this movie data
                FavoritesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                FavoritesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                FavoritesEntry.COLUMN_MEDIA_TYPE + " TEXT, " +
                FavoritesEntry.COLUMN_NAME + " TEXT, " +


                " UNIQUE (" + FavoritesEntry.COLUMN_ID + ", " +
                FavoritesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //!!! May need to use ALTER TABLE here.
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME);
    }
}
