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

    public static final String[] MOVIE_COLUMNS = {
            MovieEntry.TABLE_NAME,
            MovieEntry.COLUMN_ID,

    };

//    public static final int COL_MOVIE = 0;
//    public static final int COL_MOVIE_ID = 1;
//    public static final int COL_MOVIE_TITLE = 2;
//    public static final int COL_MOVIE_OVERVIEW = 3;
//    public static final int COL_MOVIE_POSTER_PATH = 4;
//    public static final int COL_MOVIE_BACKDROP_PATH = 5;
//    public static final int COL_MOVIE_RELEASE_DATE = 6;
//    public static final int COL_MOVIE_RUNTIME = 7;
//    public static final int COL_MOVIE_RATING = 8;
//    public static final int COL_MOVIE_REVENUE = 9;
//    public static final int COL_MOVIE_GENRE_JSON = 10;
//    public static final int COL_MOVIE_DIRECTOR_JSON = 11;
//    public static final int COL_MOVIE_ACTOR_JSON = 12;
//    public static final int COL_MOVIE_VIDEO_JSON = 13;


    //Content provider authority for MovieOld DB
    public static final String CONTENT_AUTHORITY = "com.android.fourthwardcoder.movingpictures";

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
         * DB MovieOld Table Entries
         */
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_ID = "movie_id";
        public static final String COLUMN_TITLE = "movie_title";
        public static final String COLUMN_OVERVIEW = "movie_overview";
        public static final String COLUMN_POSTER_PATH = "movie_poster_path";
        public static final String COLUMN_BACKDROP_PATH = "movie_backdrop_path";
        public static final String COLUMN_RELEASE_DATE = "movie_release_date";
        public static final String COLUMN_RUNTIME = "movie_runtime";
        public static final String COLUMN_RATING = "movie_rating";
        public static final String COLUMN_REVENUE = "movie_revenue";

        //Store Array lists as JSON Strings. No need to create complicated SQL tabels
        //ass these values will never change once the data has been fetched.
        public static final String COLUMN_GENRE_JSON = "movie_genre_json";
        public static final String COLUMN_DIRECTOR_JSON = "movie_director_json";
        public static final String COLUMN_ACTOR_JSON = "movie_actor_json";
        public static final String COLUMN_VIDEO_JSON = "movie_video_json";

        public static Uri buildMovieUri() {
            return CONTENT_URI;
        }

        public static Uri buildMovieWithIdUri(int id) {

            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
