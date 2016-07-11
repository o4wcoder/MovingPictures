package com.fourthwardmobile.android.movingpictures.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME + " (" +

                FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the movie entry associated with this movie data
                FavoritesContract.FavoritesEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH + " TEXT, " +
                FavoritesContract.FavoritesEntry.COLUMN_MEDIA_TYPE + " TEXT, " +
                FavoritesContract.FavoritesEntry.COLUMN_NAME + " TEXT, " +


                " UNIQUE (" + FavoritesContract.FavoritesEntry.COLUMN_ID + ", " +
                FavoritesContract.FavoritesEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //!!! May need to use ALTER TABLE here.
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_NAME);
    }
}
