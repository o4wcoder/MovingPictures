package com.android.fourthwardcoder.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by chare on 9/18/2015.
 */
public class MovieContract  {

    public static final String[] MOVIE_COLUMNS = {
            MovieEntry.TABLE_NAME,
            MovieEntry.COLUMN_MOVIE_ID
    };

    public static final int COL_MOVIE = 0;
    public static final int COL_MOVIE_ID = 1;
    public static final int COL_MOVIE_POSTER_PATH = 2;

    //Content provider authority for Movie DB
    public static final String CONTENT_AUTHORITY = "com.android.fourthwardcoder.popularmovies";

    //Base URI for content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path to movie data in content provider
    public static final String PATH_MOVIE = "movie";

    public static class MovieEntry implements BaseColumns {

        /*
         * Content Provider Defines
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        /*
         * DB Movie Table Entries
         */
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_POSTER_PATH = "movie_poster_path";

        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }

        public static Uri buildMovieWithIdUri(int id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
