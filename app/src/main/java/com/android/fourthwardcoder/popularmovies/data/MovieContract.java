package com.android.fourthwardcoder.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by chare on 9/18/2015.
 */
public class MovieContract  {

    public static class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_MOVIE_ID = "movie_id";
    }
}
