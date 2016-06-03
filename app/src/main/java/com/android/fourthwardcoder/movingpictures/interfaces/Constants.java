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
    String EXTRA_MOVIE = "com.android.fourthwardcoder.popularmovies.extra_movie";
    String EXTRA_MOVIE_ID = "com.android.fourthwardcoder.popularmovies.extra_movie_id";
    String EXTRA_MOVIE_LIST = "com.android.fourthwardcoder.popularmovies.extra_movie_list";
    String EXTRA_TITLE = "com.android.fourthwardcoder.popularmovies.extra_title";
    String EXTRA_PERSON_ID = "com.android.fourthwardcoder.popularmovies.extra_person_id";
    String EXTRA_PERSON_NAME = "com.android.fourthwardcoder.popularmovies.extra_person_name";
    String EXTRA_FULL_PHOTO_PATH = "com.android.fourthwardcoder.popularmovies.extra_full_photo_path";
    String EXTRA_PERSON_PHOTO = "com.android.fourthwardcoder.popularmovies.extra_person_photo";
    String EXTRA_PERSON_PHOTO_ID = "com.android.fourthwardcoder.popularmovies.extra_person_photo_id";
    String EXTRA_TV_ID = "com.android.fourthwardcoder.popularmovies.extra_tv_id";
    String EXTRA_ENT_TYPE = "com.android.fourthwardcoder.popularmovies.extra_ent_type";
    String EXTRA_VIDEO_URI = "com.android.fourthwardcoder.popularmovies.extra_video_uri";


    //Entertainment type Annotations
    @Retention(RetentionPolicy.SOURCE)
            @IntDef({ENT_TYPE_MOVIE,ENT_TYPE_TV})
            @interface EntertainmentType {}
    int ENT_TYPE_MOVIE = 0;
    int ENT_TYPE_TV = 1;





}
