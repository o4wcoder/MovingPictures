package com.android.fourthwardcoder.popularmovies.interfaces;

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
    String EXTRA_PERSON_ID = "com.android.fourthwardcoder.popularmovies.extra_person_id";
    String EXTRA_PERSON_NAME = "com.android.fourthwardcoder.popularmovies.extra_person_name";
    String EXTRA_FULL_PHOTO_PATH = "com.android.fourthwardcoder.popularmovies.extra_full_photo_path";
    String EXTRA_PERSON_PHOTO = "com.android.fourthwardcoder.popularmovies.extra_person_photo";
    String EXTRA_PERSON_PHOTO_ID = "com.android.fourthwardcoder.popularmovies.extra_person_photo_id";


    //Names of filmography tabs
    enum FilmographyTabName {
        MOVIES,
        TV
    }

    String EXTRA_FILMOGRAPHY_TAB = "com.android.fourthwardcoder.popularmovies.extra_filmography_tab";
}
