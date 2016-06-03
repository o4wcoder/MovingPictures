package com.android.fourthwardcoder.movingpictures.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Class MovieProvider
 * Author: Chris Hare
 * Created: 9/20/2015
 * <p/>
 * Content Provider
 */
public class MovieProvider extends ContentProvider {

    /**********************************************************************/
    /*                           Constants                                */
    /**********************************************************************/
    private final static String TAG = MovieProvider.class.getSimpleName();

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    /**********************************************************************/
    /*                           Local Data                               */
    /**********************************************************************/
    // The URI Mather used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mMovieHelper;

    static UriMatcher buildUriMatcher() {

        UriMatcher sURIMather = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = MovieContract.CONTENT_AUTHORITY;

        sURIMather.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        sURIMather.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID);

        return sURIMather;
    }

    @Override
    public boolean onCreate() {
        mMovieHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case MOVIE: {
                retCursor = mMovieHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = MovieContract.MovieEntry.buildMovieUri();
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Notify all register observer of changes
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        //Get writeable database
        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int deletedRows;

        if (selection == null)
            selection = "1";

        switch (match) {

            case MOVIE: {
                deletedRows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknow uri: " + uri);
        }

        //Notify all registered observers of changes
        if (deletedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        //return the rows deleted
        return deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mMovieHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int updatedRows;

        if (selection == null)
            selection = "1";

        switch (match) {
            case MOVIE: {
                updatedRows = db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Notify all registered observers of changes
        if (updatedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return updatedRows;
    }
}
