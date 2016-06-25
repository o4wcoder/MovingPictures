package com.android.fourthwardcoder.movingpictures.interfaces;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Interface Constants
 * Author: Chris Hare
 * Created: 9/2/2015.
 *
 * Interface to hold constant defines shared by all classes.
 */
public interface Constants {

    //Extras for intents
    String EXTRA_ID = "com.android.fourthwardcoder.popularmovies.extra_id";
    String EXTRA_MOVIE = "com.android.fourthwardcoder.popularmovies.extra_movie";
   // String EXTRA_MOVIE_ID = "com.android.fourthwardcoder.popularmovies.extra_movie_id";
    String EXTRA_MOVIE_LIST = "com.android.fourthwardcoder.popularmovies.extra_movie_list";
    String EXTRA_TITLE = "com.android.fourthwardcoder.popularmovies.extra_title";
    String EXTRA_PERSON = "com.android.fourthwardcoder.popularmovies.extra_person";
   // String EXTRA_PERSON_ID = "com.android.fourthwardcoder.popularmovies.extra_person_id";
    String EXTRA_PERSON_NAME = "com.android.fourthwardcoder.popularmovies.extra_person_name";
    String EXTRA_FULL_PHOTO_PATH = "com.android.fourthwardcoder.popularmovies.extra_full_photo_path";
    String EXTRA_PERSON_PHOTO = "com.android.fourthwardcoder.popularmovies.extra_person_photo";
    String EXTRA_PERSON_PHOTO_ID = "com.android.fourthwardcoder.popularmovies.extra_person_photo_id";
    //String EXTRA_TV_ID = "com.android.fourthwardcoder.popularmovies.extra_tv_id";
    String EXTRA_ENT_TYPE = "com.android.fourthwardcoder.popularmovies.extra_ent_type";
    String EXTRA_LIST_TYPE = "com.android.fourthwardcoder.popularmovies.extra_list_type";
    String EXTRA_VIDEO_URI = "com.android.fourthwardcoder.popularmovies.extra_video_uri";
    String EXTRA_REVIEW_LIST = "com.android.fourthwardcoder.popularmovies.extra_review_list";


    //Entertainment type Annotations
    @Retention(RetentionPolicy.SOURCE)
            @IntDef({ENT_TYPE_MOVIE,ENT_TYPE_TV,ENT_TYPE_PERSON})
            @interface EntertainmentType {}
    int ENT_TYPE_MOVIE = 0;
    int ENT_TYPE_TV = 1;
    int ENT_TYPE_PERSON = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({LIST_TYPE_MOVIE,LIST_TYPE_TV,LIST_TYPE_MOVIE_CAST,LIST_TYPE_MOVIE_CREW,
    LIST_TYPE_TV_CAST,LIST_TYPE_TV_CREW})
    @interface ShowAllListType {}
    int LIST_TYPE_MOVIE = 0;
    int LIST_TYPE_TV = 1;
    int LIST_TYPE_MOVIE_CAST = 2;
    int LIST_TYPE_MOVIE_CREW = 3;
    int LIST_TYPE_TV_CAST = 4;
    int LIST_TYPE_TV_CREW = 5;

    public static final int NUM_CAST_DISPLAY = 3;

    //Sort order Annotations
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({SORT_POPULAR,SORT_NOW_PLAYING,SORT_UPCOMING})
    @interface SortType {}
    int SORT_POPULAR = 0;
    int SORT_NOW_PLAYING = 1;
    int SORT_UPCOMING = 2;





}
