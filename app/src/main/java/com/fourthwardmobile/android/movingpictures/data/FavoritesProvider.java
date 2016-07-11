package com.fourthwardmobile.android.movingpictures.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Class FavoritesProvider
 * Author: Chris Hare
 * Created: 9/20/2015
 * <p/>
 * Content Provider
 */
public class FavoritesProvider extends ContentProvider {

    /**********************************************************************/
    /*                           Constants                                */
    /**********************************************************************/
    private final static String TAG = FavoritesProvider.class.getSimpleName();

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    /**********************************************************************/
    /*                           Local Data                               */
    /**********************************************************************/
    // The URI Mather used by this content provider
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private FavoritesDbHelper mMovieHelper;

    static UriMatcher buildUriMatcher() {

        UriMatcher sURIMather = new UriMatcher(UriMatcher.NO_MATCH);

        String authority = FavoritesContract.CONTENT_AUTHORITY;

        sURIMather.addURI(authority, FavoritesContract.PATH_FAVORITES, MOVIE);
        sURIMather.addURI(authority, FavoritesContract.PATH_FAVORITES + "/#", MOVIE_WITH_ID);

        return sURIMather;
    }

    @Override
    public boolean onCreate() {
        mMovieHelper = new FavoritesDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        Cursor retCursor;

        switch (sUriMatcher.match(uri)) {

            case MOVIE: {
                retCursor = mMovieHelper.getReadableDatabase().query(
                        FavoritesContract.FavoritesEntry.TABLE_NAME,
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
                return FavoritesContract.FavoritesEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
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
                long _id = db.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, contentValues);
                if (_id > 0)
                    returnUri = FavoritesContract.FavoritesEntry.buildMovieUri();
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
                deletedRows = db.delete(FavoritesContract.FavoritesEntry.TABLE_NAME, selection, selectionArgs);
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
                updatedRows = db.update(FavoritesContract.FavoritesEntry.TABLE_NAME, contentValues, selection, selectionArgs);
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
