package com.android.fourthwardcoder.popularmovies.models;

/**
 * Created by chare on 9/3/2015.
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
