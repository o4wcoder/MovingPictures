package com.android.fourthwardcoder.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.fourthwardcoder.popularmovies.data.MovieContract.MovieEntry;

/**
 * Class MovieDbHelper
 * Author: Chris Hare
 * Created: 9/18/2015.
 * <p/>
 * Database helper for the Movies Content Provider.
 */
public class MovieDbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /*
         * Create Movie's Table
         */
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                // the ID of the movie entry associated with this movie data
                MovieEntry.COLUMN_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_RUNTIME + " TEXT, " +
                MovieEntry.COLUMN_RATING + " REAL, " +
                MovieEntry.COLUMN_REVENUE + " TEXT, " +
                MovieEntry.COLUMN_GENRE_JSON + " TEXT, " +
                MovieEntry.COLUMN_DIRECTOR_JSON + " TEXT, " +
                MovieEntry.COLUMN_ACTOR_JSON + " TEXT, " +
                MovieEntry.COLUMN_VIDEO_JSON + " TEXT, " +


                " UNIQUE (" + MovieEntry.COLUMN_ID + ", " +
                MovieEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //!!! May need to use ALTER TABLE here.
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
    }
}
