package com.android.fourthwardcoder.popularmovies.models;

import android.content.ContentValues;

/**
 * Class SimpleMovie
 * Author: Chris Hare
 * Created: 9/3/2015.
 *
 * Class to hold simple movie details used for the Filmography list.
 */
public class SimpleMovie {

    int id;
    String posterPath;

    public SimpleMovie(int id, String posterPath) {
        this.id = id;
        this.posterPath = posterPath;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


}
