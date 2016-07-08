package com.android.fourthwardcoder.movingpictures.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

import com.android.fourthwardcoder.movingpictures.activities.MainActivity;

/**
 * Class FavoritesContract
 * Author:  Chris Hare
 * Created: 9/18/2015.
 * <p/>
 * Contract Provider MovieOld Contract
 */
public class FavoritesContract {

    public static final int COL_ID = 1;
    public static final int COL_POSTER_PATH = 2;
    public static final int COL_MEDIA_TYPE = 3;
    public static final int COL_NAME = 4;

    //Content provider authority for Favorites Movie DB
    public static final String CONTENT_AUTHORITY = "com.android.fourthwardcoder.movingpictures";

    //Base URI for content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //path to movie data in content provider
    public static final String PATH_FAVORITES = "favorites";

    public static class FavoritesEntry implements BaseColumns {

        /*
         * Content Provider Defines
         */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_FAVORITES;

        /*
         * DB MovieOld Table Entries
         */
        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_ID = "favorites_id";
        public static final String COLUMN_POSTER_PATH = "favorites_poster_path";
        public static final String COLUMN_MEDIA_TYPE = "favorites_media_type";
        public static final String COLUMN_NAME = "favorites_name";

        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }

//        public static Uri buildMovieWithIdUri(int id) {
//
//            return ContentUris.withAppendedId(CONTENT_URI, id);
//        }
    }

}
